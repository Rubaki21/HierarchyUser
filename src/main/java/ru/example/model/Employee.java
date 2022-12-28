package ru.example.model;

public class Employee {

    private final Long id;

    private final Long supervisorId;

    private final String fullName;

    public Employee(Long id, Long supervisorId, String fullName) {
        this.id = id;
        this.supervisorId = supervisorId;
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public Long getSupervisorId() {
        return supervisorId;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", supervisorId=" + supervisorId +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}