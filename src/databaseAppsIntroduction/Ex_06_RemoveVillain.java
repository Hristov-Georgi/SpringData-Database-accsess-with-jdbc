package databaseAppsIntroduction;

import DBconnection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Ex_06_RemoveVillain {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        Connection connection  = DatabaseConnection.connectTo(scanner, "minions_db");

        int villain_id = Integer.parseInt(scanner.nextLine());

        PreparedStatement villainStatement = connection.prepareStatement("SELECT name FROM villains WHERE id = ?");
        villainStatement.setInt(1, villain_id);
        villainStatement.executeQuery();

        ResultSet isVillainExist = villainStatement.getResultSet();

        if(!isVillainExist.next()) {
            System.out.println("No such villain was found");
            return;
        }

        connection.setAutoCommit(false);
        try {
            PreparedStatement releaseMinions = connection.prepareStatement(
                    "DELETE FROM minions_villains WHERE villain_id = ?;");

            releaseMinions.setInt(1, villain_id);
            int minionsCount = releaseMinions.executeUpdate();

            PreparedStatement deleteVillains = connection.prepareStatement("DELETE FROM villains WHERE id = ?;");
            deleteVillains.setInt(1, villain_id);
            deleteVillains.executeUpdate();

            connection.commit();

            System.out.printf("%s was deleted\n%d minions released\n",
                    isVillainExist.getString("name"),
                    minionsCount);
        } catch (SQLException ex){
            connection.rollback();
        }

        connection.close();
    }
}
