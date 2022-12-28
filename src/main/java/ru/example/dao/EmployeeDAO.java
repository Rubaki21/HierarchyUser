package ru.example.dao;

import ru.example.model.Employee;

import java.util.List;

public interface EmployeeDAO {

    List<Employee> getAllEmployee();
}