package ru.example.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.example.model.Employee;
import ru.example.model.EmployeeMapper;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class EmployeeDAOImpl implements EmployeeDAO {

    final JdbcTemplate jdbcTemplate;

    private final String SQL_GET_ALL = "SELECT EMPLOYEE_ID, SUPERVISOR_ID, FULL_NAME FROM EMPLOYEE";

    @Autowired
    public EmployeeDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Employee> getAllEmployee() {
        return jdbcTemplate.query(SQL_GET_ALL, new EmployeeMapper());
    }
}