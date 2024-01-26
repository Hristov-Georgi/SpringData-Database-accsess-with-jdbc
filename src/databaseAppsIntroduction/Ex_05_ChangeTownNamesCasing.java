package databaseAppsIntroduction;

import DBconnection.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Ex_05_ChangeTownNamesCasing {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        Connection connection = DatabaseConnection.connectTo(scanner, "minions_db");

        String country = scanner.nextLine();

        PreparedStatement updateStatement = connection.prepareStatement(
                "UPDATE towns" +
                " SET name = UPPER(name)" +
                " WHERE country = ?;");
        updateStatement.setString(1, country);
        int updatedTowns = updateStatement.executeUpdate();

        if (updatedTowns == 0) {
            System.out.println("No town names were affected.");
            return;
        }

        PreparedStatement selectTowns = connection.prepareStatement("SELECT name FROM towns WHERE country = ?;");

        selectTowns.setString(1, country);
        selectTowns.executeQuery();

        ResultSet townsSet = selectTowns.getResultSet();

        List<String> townsList = new ArrayList<>();

        System.out.printf("%d town names were affected.\n", updatedTowns);
        while (townsSet.next()) {
            townsList.add(townsSet.getString("name"));
        }

        System.out.println(townsList);

        connection.close();
    }
}
