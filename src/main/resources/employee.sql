CREATE TABLE EMPLOYEE (
      employee_id integer not null PRIMARY KEY,
      supervisor_id integer not null,
      full_name VARCHAR(200) not null
) AS
SELECT *
FROM CSVREAD('classpath:/employee_multi_roots.csv');