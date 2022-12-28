package ru.example.model;

import ru.example.dto.EmployeeDTO;

public class EmployeeToEmployeeDTOMapper {

    public static EmployeeDTO convert(final Employee employee) {
        return new EmployeeDTO(employee.getId(), employee.getFullName());
    }
}