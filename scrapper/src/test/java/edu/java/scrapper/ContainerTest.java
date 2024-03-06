package edu.java.scrapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ContainerTest extends IntegrationTest {

    @Test
    public void shouldSuccessfulRunningContainer() {
        try {
            Connection connection = DriverManager.getConnection(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT 1");
            if (resultSet.next()) {
                int result = resultSet.getInt(1);
                Assertions.assertEquals(result, 1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertTrue(POSTGRES.isRunning());
    }
}

