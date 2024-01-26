package databaseAppsIntroduction;

import DBconnection.DatabaseConnection;

import java.sql.*;
import java.util.Scanner;

public class Ex_09_IncreaseAgeStoredProcedure {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        int minion_id = Integer.parseInt(scanner.nextLine());

        Connection connection = DatabaseConnection.connectTo(scanner, "minions_db");

        CallableStatement statement = connection.prepareCall("CALL usp_get_older(?)");
        statement.setInt(1, minion_id);
        statement.executeQuery();

        PreparedStatement selectMinionById = connection.prepareStatement("SELECT name, age FROM minions" +
                " WHERE id = ?;");
        selectMinionById.setInt(1, minion_id);
        selectMinionById.executeQuery();

        ResultSet getResult = selectMinionById.getResultSet();

        if(getResult.next()) {
            System.out.printf("%s %d", getResult.getString("name"), getResult.getInt("age"));
        }

        connection.close();
    }
}
