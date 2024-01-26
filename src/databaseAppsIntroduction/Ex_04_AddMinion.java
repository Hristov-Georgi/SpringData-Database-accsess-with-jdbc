package databaseAppsIntroduction;

import DBconnection.DatabaseConnection;

import java.sql.*;
import java.util.Scanner;

public class Ex_04_AddMinion {
    public static void main(String[] args) throws SQLException{
        Scanner scanner = new Scanner(System.in);

        Connection connection = DatabaseConnection.connectTo(scanner, "minions_db");

        String[] minionInputData = scanner.nextLine().split("\\s+");
        String minionName = minionInputData[1];
        int age = Integer.parseInt(minionInputData[2]);
        String minionTown = minionInputData[3];

        String villainName = scanner.nextLine().split("\\s+")[1];

        PreparedStatement townsStatement = connection.prepareStatement(
                "SELECT id FROM towns WHERE name = ?;");
        townsStatement.setString(1, minionTown);

        ResultSet townsSet = townsStatement.executeQuery();

        int townId = 0;
        if(townsSet.next()) {
            townId = townsSet.getInt("id");
        } else {
            String query = "INSERT INTO towns (name) VALUES (?);";
           insertIntoTable(connection, query, minionTown);

            System.out.printf("Town %s was added to the database.\n", minionTown);

            ResultSet newTownsSet = townsStatement.executeQuery();
            newTownsSet.next();
            townId = newTownsSet.getInt("id");
        }

        PreparedStatement villainStatement = connection.prepareStatement("SELECT id FROM villains WHERE name = ?;");
        villainStatement.setString(1, villainName);

        ResultSet villainSet = villainStatement.executeQuery();

        int villainId = 0;
        if(villainSet.next()) {
            villainId = villainSet.getInt("id");
        } else {
            String query = "INSERT INTO villains(name, evilness_factor) VALUES (?, 'evil');";
            insertIntoTable(connection, query, villainName);

            System.out.printf("Villain %s was added to the database.\n", villainName);

            ResultSet newVillainSet = villainStatement.executeQuery();
            newVillainSet.next();
            villainId = newVillainSet.getInt("id");
        }

        PreparedStatement minionStatement = connection.prepareStatement(
                "SELECT id FROM minions WHERE name = ?;");
        minionStatement.setString(1, minionName);
        ResultSet minionSet = minionStatement.executeQuery();

        int minionId = 0;
        if (minionSet.next()) {
            minionId = minionSet.getInt("id");

        } else {
            String query = "INSERT INTO minions (name, age, town_id) VALUES (?, ?, ?);";
            insertIntoTable(connection, query, minionName, age, townId);

            ResultSet newMinionSet = minionStatement.executeQuery();
            newMinionSet.next();
            minionId = newMinionSet.getInt("id");
        }

        PreparedStatement connectMinionVillain = connection.prepareStatement(
                "INSERT INTO minions_villains(minion_id, villain_id) VALUES (?, ?);");

        connectMinionVillain.setInt(1, minionId);
        connectMinionVillain.setInt(2, villainId);

        System.out.printf("Successfully added %s to be minion of %s.\n", minionName, villainName);

        connection.close();
    }

    private static void insertIntoTable(Connection con, String query, String name) throws SQLException {
        PreparedStatement insertVillain = con.prepareStatement(query);
        insertVillain.setString(1, name);
        insertVillain.executeUpdate();
    }

    private static void insertIntoTable(Connection con, String query, String name, int age, int townId) throws SQLException {
        PreparedStatement insertVillain = con.prepareStatement(query);
        insertVillain.setString(1, name);
        insertVillain.setInt(2, age);
        insertVillain.setInt(3, townId);
        insertVillain.executeUpdate();
    }

}
