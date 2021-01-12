package com.babayan.babe.cafe.app.configuration.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Hikari Connection Pool configuration.
 */
@SuppressWarnings("ALL")
@Configuration
public class DSConfig {
	private static final String HIKARICP_CLOSE_METHOD = "close";

	@Primary
	@Bean(name = "hikariConnection", destroyMethod = HIKARICP_CLOSE_METHOD)
	DataSource dataSource(Environment env) {
		// initializing CP
		HikariConfig dataSourceConfig = new HikariConfig();

		// Applying configuration properties
		dataSourceConfig.setDriverClassName(
			env.getRequiredProperty("spring.datasource.driver-class-name"));
		dataSourceConfig.setJdbcUrl(
			env.getRequiredProperty("spring.datasource.url"));
		dataSourceConfig.setUsername(
			env.getRequiredProperty("spring.datasource.username"));
		dataSourceConfig.setPassword(
			env.getRequiredProperty("spring.datasource.password"));
		dataSourceConfig.setConnectionTestQuery(
				env.getRequiredProperty("spring.datasource.validation-query"));
		dataSourceConfig.setConnectionTimeout(
				Long.valueOf(env.getRequiredProperty("spring.datasource.hikari.connection-timeout")));
		dataSourceConfig.setMinimumIdle(
				Integer.valueOf(env.getRequiredProperty("spring.datasource.hikari.minimum-idle")));
		dataSourceConfig.setMaximumPoolSize(
			Integer.valueOf(env.getRequiredProperty("spring.datasource.hikari.maximum-pool-size")));
		dataSourceConfig.setIdleTimeout(
				Long.valueOf(env.getRequiredProperty("spring.datasource.hikari.idle-timeout")));
		dataSourceConfig.setMaxLifetime(
				Long.valueOf(env.getRequiredProperty("spring.datasource.hikari.max-lifetime")));
		dataSourceConfig.setAutoCommit(
				Boolean.valueOf(env.getRequiredProperty("spring.datasource.hikari.auto-commit")));

		// returning instantiated bean
		return new HikariDataSource(dataSourceConfig);
	}

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate jdbcTemplate(Environment env) {
        return new JdbcTemplate(dataSource(env));
    }

}
