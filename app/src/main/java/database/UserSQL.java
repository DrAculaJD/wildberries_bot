package database;

import wildberries.TypeOfApi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserSQL {
    private final String username;
    private final String password;
    private final String pathToDatabase;
    private String request;
    private String actualStatApiKey;
    private String actualStandartApiKey;

    public UserSQL(String username, String password, String pathToDatabase) {
        this.username = username;
        this.password = password;
        this.pathToDatabase = pathToDatabase;
    }

    public void setTelegramUser(String chatId, String apiKey, TypeOfApi typeOfApi) {

        try (Connection connection = DriverManager.getConnection(pathToDatabase, username, password)) {
            request = "INSERT INTO users (statistics_api, chat_id) VALUES (?, ?)";
            Class.forName("org.postgresql.Driver");

            try  {
                setUserToSql(connection, chatId, apiKey, typeOfApi);
            } catch (SQLException ex) {
                System.out.println("Ошибка при работе с базой данных: " + ex.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при закрытии соединения: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private void setUserToSql(Connection connection, String chatId, String apiKey,
                              TypeOfApi typeOfApi) throws SQLException {

        if (baseContainsId(chatId)) {
            selectRequest(typeOfApi);
        }

        try (PreparedStatement statement = connection.prepareStatement(request)) {

            statement.setString(1, apiKey);
            statement.setLong(2, Integer.parseInt(chatId));

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Данные успешно добавлены в таблицу!");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public boolean baseContainsId(String chatId) {

        boolean result = false;

        try (Connection connection = DriverManager.getConnection(pathToDatabase, username, password)) {
            request = "SELECT * FROM users";

            try (Statement statement = connection.createStatement()) {

                try (ResultSet resultSet = statement.executeQuery(request)) {
                    while (resultSet.next()) {

                        if (String.valueOf(resultSet.getInt("chat_id")).equals(chatId)) {
                            result = true;
                            actualStatApiKey = resultSet.getString("statistics_api");
                            actualStandartApiKey = resultSet.getString("standart_api");
                        }
                    }
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public String getStatisticsApi(String chatId) {
        baseContainsId(chatId);

        return actualStatApiKey;
    }

    public String getStandartApi(String chatId) {
        baseContainsId(chatId);

        return actualStandartApiKey;
    }

    private void selectRequest(TypeOfApi typeOfApi) {

        if (typeOfApi.equals(TypeOfApi.STATISTICS_API)) {
            request = "UPDATE users SET statistics_api = ? WHERE chat_id = ?";
        } else if (typeOfApi.equals(TypeOfApi.STANDART_API)) {
            request = "UPDATE users SET standart_api = ? WHERE chat_id = ?";
        }
    }
}
