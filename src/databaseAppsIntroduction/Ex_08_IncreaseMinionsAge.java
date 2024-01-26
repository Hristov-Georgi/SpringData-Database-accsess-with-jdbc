package databaseAppsIntroduction;

import DBconnection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

public class Ex_08_IncreaseMinionsAge {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        Connection connection = DatabaseConnection.connectTo(scanner, "minions_db");

        int[] minionIds = Arrays.stream(scanner.nextLine().split("\\s+"))
                        .mapToInt(Integer::parseInt)
                        .toArray();

        connection.setAutoCommit(false);
        try {
            for (int i = 0; i < minionIds.length; i++) {
                PreparedStatement increaseAgeAndLowerName = connection.prepareStatement(
                        "UPDATE minions" +
                                " SET age = age + 1, name = LOWER(name)" +
                                " WHERE id = ?;");
                increaseAgeAndLowerName.setInt(1, minionIds[i]);
                increaseAgeAndLowerName.executeUpdate();

            }

            connection.commit();

        } catch (SQLException exception) {
            connection.rollback();
        }

        PreparedStatement selectAllMinions = connection.prepareStatement("SELECT name, age FROM minions;");
        selectAllMinions.executeQuery();

        ResultSet minions = selectAllMinions.getResultSet();

        while (minions.next()) {
            System.out.println(minions.getString("name") + " " + minions.getInt("age"));
        }

        connection.close();
    }
}
