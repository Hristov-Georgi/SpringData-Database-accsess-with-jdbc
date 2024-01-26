package databaseAppsIntroduction;

import DBconnection.DatabaseConnection;

import java.sql.*;
import java.util.Scanner;

public class Lab_02 {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        Connection connection = DatabaseConnection.connectTo(scanner, "diablo");

        PreparedStatement statement = connection
                .prepareStatement("SELECT u.first_name, u.last_name, count(ug.game_id) AS games_count " +
                " FROM users AS u" +
                " JOIN users_games AS ug ON u.id = ug.user_id" +
                " WHERE u.user_name = ?" +
                " GROUP BY ug.user_id;");

        String userName = scanner.nextLine();

        statement.setString(1, userName);

        ResultSet resultSet = statement.executeQuery();

        if(!resultSet.next()) {
            System.out.println("No such user exists");
            return;
        }

        System.out.printf("User: %s\n%s %s has played %d games\n",
                userName, resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getInt("games_count"));

        connection.close();
    }
}
