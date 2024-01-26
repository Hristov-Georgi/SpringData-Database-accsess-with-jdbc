package databaseAppsIntroduction;

import DBconnection.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Ex_03_GetMinionNames {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        Connection connection = DatabaseConnection.connectTo(scanner, "minions_db");

        PreparedStatement villainStatement = connection.prepareStatement(
                "SELECT name FROM villains WHERE id = ?;");

        int villain_id = Integer.parseInt(scanner.nextLine());
        villainStatement.setInt(1, villain_id);

        ResultSet resultSetVillainName = villainStatement.executeQuery();

        if (!resultSetVillainName.next()) {
            System.out.printf("No villain with ID %d exists in the database.\n", villain_id);
            return;
        }

        PreparedStatement minionsStatement = connection.prepareStatement(
                "SELECT m.name, m.age FROM minions AS m" +
                " JOIN minions_villains AS mv ON m.id = mv.minion_id" +
                " WHERE mv.villain_id = ?;");

        minionsStatement.setInt(1, villain_id);

        ResultSet minionsSet = minionsStatement.executeQuery();

        System.out.printf("Villain: %s\n", resultSetVillainName.getString("name"));

        int count = 1;
        while (minionsSet.next()) {
            System.out.printf("%d. %s %d\n", count,
                    minionsSet.getString("m.name"),
                    minionsSet.getInt("m.age"));
            count++;
        }

        connection.close();
    }
}
