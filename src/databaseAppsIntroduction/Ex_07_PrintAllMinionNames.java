package databaseAppsIntroduction;

import DBconnection.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Ex_07_PrintAllMinionNames {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        Connection connection = DatabaseConnection.connectTo(scanner, "minions_db");

        PreparedStatement statement = connection.prepareStatement("SELECT name FROM minions;");
        statement.executeQuery();

        ResultSet totalMinions = statement.getResultSet();

        List<String> minions = new ArrayList<>();
        while (totalMinions.next()) {
            minions.add(totalMinions.getString("name"));
        }

        for (int i = 0; i < minions.size() / 2; i++) {
            System.out.println(minions.get(i));
            System.out.println(minions.get(minions.size() - i - 1));
        }

        if(minions.size() % 2 != 0) {
            System.out.println(minions.get((minions.size() / 2)));
        }

        connection.close();
    }
}
