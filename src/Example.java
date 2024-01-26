import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Example {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter username: ");
        String user = scanner.nextLine();

        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        Properties properties = new Properties();
        properties.setProperty("user", user);
        properties.setProperty("password", password);

        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/soft_uni", properties);

        PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT first_name, last_name FROM employees WHERE salary > ?");

        double salary = Double.parseDouble(scanner.nextLine());

        preparedStatement.setDouble(1, salary);

        ResultSet result = preparedStatement.executeQuery();

        while (result.next()) {
            System.out.printf("%s %s\n", result.getString("first_name"),
                    result.getString("last_name"));
        }

        connection.close();
    }
}
