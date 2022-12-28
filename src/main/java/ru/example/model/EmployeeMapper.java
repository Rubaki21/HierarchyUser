package ru.example.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeMapper implements RowMapper<Employee> {

    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {

        final Long id = rs.getLong("employee_id");
        final Long supervisorId = rs.getLong("supervisor_id");
        final String fullName = rs.getString("full_name");

        return new Employee(id, supervisorId, fullName);
    }
}