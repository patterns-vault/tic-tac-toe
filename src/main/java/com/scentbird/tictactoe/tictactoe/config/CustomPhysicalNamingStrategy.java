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
    private String schemaName;

    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment context) {
        // Always return the custom schema name instead of the default one
        return Identifier.toIdentifier(schemaName);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        // Optionally customize the table name (if needed)
        return name; // Default behavior for table names
    }

    @Override
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment context) {
        return name; // Default catalog behavior
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment context) {
        return name; // Default sequence behavior
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        return name; // Default column behavior
    }

    @PostConstruct
    @SneakyThrows
    public void initSchema() {
        try (Connection connection = dataSource.getConnection()) {
            String sql = String.format("CREATE SCHEMA IF NOT EXISTS %s", schemaName);

            Statement statement = connection.createStatement();
            statement.execute(sql);
        }
    }
}
