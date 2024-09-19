import java.sql.*;
import java.util.Scanner;

public class DatabaseOperations {

    private static final String URL = "jdbc:mysql://10.10.13.97/te31332_db";
    private static final String USER = "te31332"; // Change if your MySQL username is different
    private static final String PASSWORD = "te31332"; // Change to your MySQL password

    public static void main(String[] args) throws ClassNotFoundException {
    	Scanner scanner = new Scanner(System.in);
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            while (true) {
                System.out.println("Choose an operation:");
                System.out.println("1. Add User");
                System.out.println("2. Delete User");
                System.out.println("3. Edit User");
                System.out.println("4. View Users");
                System.out.println("5. Exit");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Enter name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter email: ");
                        String email = scanner.nextLine();
                        addUser(connection, name, email);
                        break;
                    case 2:
                        System.out.print("Enter user ID to delete: ");
                        int deleteId = scanner.nextInt();
                        deleteUser(connection, deleteId);
                        break;
                    case 3:
                        System.out.print("Enter user ID to edit: ");
                        int editId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter new name: ");
                        String newName = scanner.nextLine();
                        System.out.print("Enter new email: ");
                        String newEmail = scanner.nextLine();
                        editUser(connection, editId, newName, newEmail);
                        break;
                    case 4:
                        viewUsers(connection);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void addUser(Connection connection, String name, String email) {
        String query = "INSERT INTO users (name, email) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.executeUpdate();
            System.out.println("User added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteUser(Connection connection, int id) {
        String query = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User deleted successfully!");
            } else {
                System.out.println("User not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void editUser(Connection connection, int id, String name, String email) {
        String query = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setInt(3, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User updated successfully!");
            } else {
                System.out.println("User not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewUsers(Connection connection) {
        String query = "SELECT * FROM users";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            System.out.println("Users:");
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") + ", Name: " + resultSet.getString("name") + ", Email: " + resultSet.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}