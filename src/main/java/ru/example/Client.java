package ru.example;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.example.config.AppConfig;
import ru.example.dto.EmployeeDTO;
import ru.example.service.EmployeeService;

import java.util.Scanner;
import java.util.Set;

public class Client {

	public static final String EXIT = "exit";

	public static void main(String[] args) {
		final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		final EmployeeService employeeService = context.getBean(EmployeeService.class);

		final Set<EmployeeDTO> structure = employeeService.structure();
		final long maxDepth = employeeService.getMaxDepth(structure);

		System.out.println("Кол-во уровней:" + maxDepth);
		try (final Scanner in = new Scanner(System.in)) {
			while (!in.hasNext(EXIT)) {
				in.next();
				while (in.hasNextInt()) {
					int input = in.nextInt();
					System.out.printf("Пользователь ввёл: \"%s\"\n", input);
					final EmployeeDTO employee = employeeService.findEmployeeById(input);
					System.out.println(employee.printInfo());
				}
			}
		}
		context.close();
	}
}