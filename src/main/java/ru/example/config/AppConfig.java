package ru.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
@ComponentScan("ru.example")
@PropertySource("classpath:application.properties")
public class AppConfig {

	final Environment environment;

	public AppConfig(Environment environment) {
		this.environment = environment;
	}

	@Bean
	public DataSource dataSource(
			@Value("${datasource.dbname}") String dbname,
			@Value("${datasource.script}") String script) {

		return new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.setName(dbname)
				.addScript(script)
				.build();
	}
}