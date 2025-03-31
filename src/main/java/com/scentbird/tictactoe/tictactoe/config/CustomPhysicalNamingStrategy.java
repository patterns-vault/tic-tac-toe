package com.scentbird.tictactoe.tictactoe.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Component
@RequiredArgsConstructor
public class CustomPhysicalNamingStrategy implements PhysicalNamingStrategy {

    private final DataSource dataSource;

    @Value("${spring.application.name}")
    private String instanceName; // DB schema name for every instance is the same as instance name

    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment context) {
        return Identifier.toIdentifier(instanceName);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        return name;
    }

    @Override
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment context) {
        return name;
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment context) {
        return name;
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        return name;
    }

    @PostConstruct
    @SneakyThrows
    public void initSchema() {
        try (Connection connection = dataSource.getConnection()) {
            String sql = String.format("CREATE SCHEMA IF NOT EXISTS %s", instanceName);
            Statement statement = connection.createStatement();
            statement.execute(sql);
        }
    }
}
