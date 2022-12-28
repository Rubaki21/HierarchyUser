package ru.example.service;

import org.springframework.stereotype.Service;
import ru.example.dao.EmployeeDAO;
import ru.example.dto.EmployeeDTO;
import ru.example.model.Employee;

import java.util.*;
import java.util.stream.Collectors;

import static ru.example.model.EmployeeToEmployeeDTOMapper.convert;

@Service
public class EmployeeService {

    private static final int SUPERVISOR_ID = 0;
    private static final int SUPERVISOR_LEVEL = 0;
    private static final int DEFAULT_LEVEL_FOR_SEARCH_EMPLOYEE = 0;

    private final EmployeeDAO employeeDAO;

    public EmployeeService(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    public List<Employee> getAllEmployee() {
        return employeeDAO.getAllEmployee();
    }

    public Set<EmployeeDTO> structure() {
        final List<Employee> allEmployments = getAllEmployee();
        final Set<Employee> supervisors = findSupervisors(allEmployments);

        return linkEmployees(allEmployments, supervisors);
    }

    public EmployeeDTO findEmployeeById(long employeeId) {
        final List<Employee> allEmployments = getAllEmployee();
        final Optional<Employee> current = findEmployeeById(allEmployments, employeeId);
        if (current.isPresent()) {
            final EmployeeDTO employee = convert(current.get());
            addSupervisors(allEmployments, employee);
            addSubordinates(allEmployments, employee, DEFAULT_LEVEL_FOR_SEARCH_EMPLOYEE);
            return employee;
        } else {
            throw new IllegalArgumentException("Пользователь с id \"" + employeeId + "\" не найден");
        }
    }

    private Set<EmployeeDTO> linkEmployees(final List<Employee> allEmployments, final Set<Employee> supervisors) {
        final Set<EmployeeDTO> employeeDTOS = new HashSet<>();
        for (final Employee supervisor : supervisors) {
            final EmployeeDTO employeeDTO = setSubordinates(allEmployments, supervisor, SUPERVISOR_LEVEL);
            long depth = getIncrementDepth(employeeDTO);
            employeeDTO.setDepthSubordinates(depth);
            employeeDTOS.add(employeeDTO);
        }

        return employeeDTOS;
    }

    private EmployeeDTO setSubordinates(final List<Employee> allEmployments, final Employee employee, int level) {
        final EmployeeDTO employeeDTO = convert(employee);
        addSupervisors(allEmployments, employeeDTO);
        addSubordinates(allEmployments, employeeDTO, level);

        return employeeDTO;
    }

    private void addSupervisors(final List<Employee> allEmployments, final EmployeeDTO employeeDTO) {
        final Set<Employee> supervisors = findSupervisorEmploymentsByEmployeeId(allEmployments, employeeDTO.getId());
        final Set<Employee> supervisorsDTO = employeeDTO.getSupervisor();
        supervisorsDTO.addAll(supervisors);
    }

    private void addSubordinates(final List<Employee> allEmployments, final EmployeeDTO employeeDTO, int level) {
        final Set<EmployeeDTO> innerSubordinate = setInnerSubordinates(allEmployments, employeeDTO, level);
        final Set<EmployeeDTO> subordinates = employeeDTO.getSubordinates();
        subordinates.addAll(innerSubordinate);
    }

    private Set<EmployeeDTO> setInnerSubordinates(final List<Employee> allEmployments,
                                                  final EmployeeDTO employee,
                                                  int level) {
        final Set<EmployeeDTO> subordinateDTOs = new HashSet<>();
        final Set<Employee> subordinates = findSubordinatesByEmployeeId(allEmployments, employee.getId());
        if (subordinates.isEmpty()) {
            return Set.of();
        } else {
            for (final Employee subordinate : subordinates) {
                final EmployeeDTO subordinateDTO = setSubordinates(allEmployments, subordinate, level++);
                long depth = getIncrementDepth(subordinateDTO);
                subordinateDTO.setDepthSubordinates(depth);
                subordinateDTOs.add(subordinateDTO);
            }
        }
        return subordinateDTOs;
    }

    private long getIncrementDepth(final EmployeeDTO employee) {
        return getMaxDepth(employee.getSubordinates()) + employee.getDepthSubordinates();
    }

    public long getMaxDepth(final Set<EmployeeDTO> employeeDTOS) {
        return employeeDTOS
                .stream()
                .max(Comparator.comparing(EmployeeDTO::getDepthSubordinates))
                .map(EmployeeDTO::getDepthSubordinates)
                .orElse(0L);
    }

    private Optional<Employee> findEmployeeById(final List<Employee> employees, long employeeId) {
        return employees
                .stream()
                .filter(e -> e.getId() == employeeId)
                .findAny();
    }

    private Set<Employee> findSubordinatesByEmployeeId(final List<Employee> employees, long employeeId) {
        return employees
                .stream()
                .filter(e -> e.getSupervisorId() == employeeId)
                .collect(Collectors.toSet());
    }

    private Set<Employee> findSupervisorEmploymentsByEmployeeId(final List<Employee> employees, long employeeId) {
        final Set<Long> supervisorEmployeeIdsForEmployee = findSupervisorEmployeeIdsForEmployee(employees, employeeId);

        return employees
                .stream()
                .filter(e -> supervisorEmployeeIdsForEmployee.contains(e.getId()))
                .collect(Collectors.toSet());
    }

    private Set<Long> findSupervisorEmployeeIdsForEmployee(final List<Employee> employees, long employeeId) {
        return employees
                .stream()
                .filter(e -> e.getId() == employeeId)
                .map(Employee::getSupervisorId)
                .collect(Collectors.toSet());
    }

    private Set<Employee> findSupervisors(final List<Employee> employees) {
        return employees
                .stream()
                .filter(e -> e.getSupervisorId() == SUPERVISOR_ID)
                .collect(Collectors.toSet());
    }
}