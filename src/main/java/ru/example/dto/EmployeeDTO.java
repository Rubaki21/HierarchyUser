package ru.example.dto;

import ru.example.model.Employee;

import java.util.HashSet;
import java.util.Set;

public class EmployeeDTO {

    private final Long id;

    private final String fullName;

    private final Set<Employee> supervisor = new HashSet<>();

    private long depthSubordinates = 1;

    private final Set<EmployeeDTO> subordinates = new HashSet<>();

    public EmployeeDTO(Long id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public Set<Employee> getSupervisor() {
        return supervisor;
    }

    public String getFullName() {
        return fullName;
    }

    public Set<EmployeeDTO> getSubordinates() {
        return subordinates;
    }

    public long getDepthSubordinates() {
        return depthSubordinates;
    }

    public void setDepthSubordinates(long depthSubordinates) {
        this.depthSubordinates = depthSubordinates;
    }

    @Override
    public String toString() {
        return "EmployeeDTO{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", supervisor=" + supervisor +
                ", depthSubordinates=" + depthSubordinates +
                ", subordinates=" + subordinates +
                '}';
    }

    public String printInfo() {
        StringBuilder builder = new StringBuilder();
        builder.append("Начальник: ");
        supervisor.forEach(e -> builder.append("\"").append(e.getFullName()).append("\" "));
        builder.append("\n");
        builder.append("Подчинённые: ");
        subordinates.forEach(e -> builder.append("\"").append(e.getFullName()).append("\" "));

        return builder.toString();
    }
}