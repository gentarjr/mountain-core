package com.mountain.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@RequiredArgsConstructor
public class PersistenceConfig {
    private final Environment env;

    @Bean
    public DataSource dataSource() {
        HikariConfig dbConfig = new HikariConfig();
        dbConfig.setJdbcUrl(env.getProperty("spring.datasource.url"));
        dbConfig.setUsername(env.getProperty("spring.datasource.username"));
        dbConfig.setPassword(env.getProperty("spring.datasource.password"));

        return new HikariDataSource(dbConfig);
    }
}
