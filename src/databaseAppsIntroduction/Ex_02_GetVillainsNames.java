package databaseAppsIntroduction;

import DBconnection.DatabaseConnection;
import java.sql.*;
import java.util.Scanner;


public class Ex_02_GetVillainsNames {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        Connection connection = DatabaseConnection.connectTo(scanner, "minions_db");

        PreparedStatement statement = connection.prepareStatement(
                "SELECT v.name, COUNT(DISTINCT minion_id) AS minions_count" +
                " FROM villains AS v" +
                " JOIN minions_villains AS mv ON mv.villain_id = v.id" +
                " GROUP BY v.id" +
                " HAVING minions_count > 15" +
                " ORDER BY minions_count DESC;");

        ResultSet result = statement.executeQuery();

        while (result.next()) {
            System.out.printf("%s %d%n",
                    result.getString("name"),
                    result.getInt("minions_count"));
        }

        connection.close();
    }
}
