package localDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static telegram.MyBot.SHOP;

public class UserSQL {
    private final String username;
    private final String password;
    private final String pathToDatabase;
    private String request;

    public UserSQL(String username, String password, String pathToDatabase) {
        this.username = username;
        this.password = password;
        this.pathToDatabase = pathToDatabase;
    }

    public void setTelegramUser() {

        try (Connection connection = DriverManager.getConnection(pathToDatabase, username, password)) {
            request = "INSERT INTO users (statistics_api, chat_id) VALUES (?, ?)";
            Class.forName("org.postgresql.Driver");

            try  {
                setUserToSql(connection);
            } catch (SQLException ex) {
                System.out.println("Ошибка при работе с базой данных: " + ex.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при закрытии соединения: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private void setUserToSql(Connection connection) throws SQLException {

        if (getChats()) {
            request = "UPDATE users SET statistics_api = ? WHERE chat_id = ?";
        }

        try (PreparedStatement statement = connection.prepareStatement(request)) {

            statement.setString(1, SHOP.getStatisticsApi());
            statement.setLong(2, Integer.parseInt(SHOP.getChatId()));

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Данные успешно добавлены в таблицу!");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public boolean getChats() {

        boolean result = false;

        try (Connection connection = DriverManager.getConnection(pathToDatabase, username, password)) {
            final String request = "SELECT * FROM users";

            try (Statement statement = connection.createStatement()) {

                try (ResultSet resultSet = statement.executeQuery(request)) {
                    while (resultSet.next()) {

                        if (String.valueOf(resultSet.getInt("chat_id")).equals(SHOP.getChatId())) {
                            result = true;
                        }

//                        System.out.println(resultSet.getInt("chat_id"));
//                        System.out.println(resultSet.getString("statistics_api"));
//                        System.out.println();
                    }
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
