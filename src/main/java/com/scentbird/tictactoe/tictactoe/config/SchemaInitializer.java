package com.scentbird.tictactoe.tictactoe.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Component
public class SchemaInitializer implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        try (Connection connection = dataSource.getConnection();

             Statement statement = connection.createStatement()) {
             statement.execute("CREATE SCHEMA IF NOT EXISTS \"instance-1\"");
        }
    }
}
