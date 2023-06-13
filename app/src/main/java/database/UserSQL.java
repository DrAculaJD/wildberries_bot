package database;

import wildberries.TypeOfApi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class UserSQL {
    private final String username;
    private final String password;
    private final String pathToDatabase;
    private static final String STATISTICS_KEY = "statisticsApi";
    private static final String STANDART_KEY = "standartApi";

    public UserSQL(String username, String password, String pathToDatabase) {
        this.username = username;
        this.password = password;
        this.pathToDatabase = pathToDatabase;
    }

    public void setTelegramUser(String chatId, String apiKey, TypeOfApi typeOfApi) {

        try (Connection connection = DriverManager.getConnection(pathToDatabase, username, password)) {
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

        try (PreparedStatement statement = connection.prepareStatement(selectRequest(typeOfApi))) {

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

    public String getApi(String chatId, TypeOfApi typeOfApi) {
        String result = "";

        for (Map.Entry<String, Map<String, String>> map: getFromDatabase(chatId).entrySet()) {
            for (Map.Entry<String, String> key: map.getValue().entrySet()) {
                if (key.getKey().equals(STATISTICS_KEY) && typeOfApi.equals(TypeOfApi.STATISTICS_API)) {
                    result = key.getValue();
                } else if (key.getKey().equals(STANDART_KEY) && typeOfApi.equals(TypeOfApi.STANDART_API)) {
                    result = key.getValue();
                }
            }
        }

        return result;
    }

    private String selectRequest(TypeOfApi typeOfApi) {

        String request = "INSERT INTO users (statistics_api, chat_id) VALUES (?, ?)";

        if (typeOfApi.equals(TypeOfApi.STATISTICS_API)) {
            request = "UPDATE users SET statistics_api = ? WHERE chat_id = ?";
        } else if (typeOfApi.equals(TypeOfApi.STANDART_API)) {
            request = "UPDATE users SET standart_api = ? WHERE chat_id = ?";
        }

        return request;
    }

    //метод для получения ключей АПИ (стандартный и статистика) из базы данных SQL
    private Map<String, Map<String, String>> getFromDatabase(String chatId) {
        Map<String, String> apiKeys = new HashMap<>();
        Map<String, Map<String, String>> result = new HashMap<>();

        try (Connection connection = DriverManager.getConnection(pathToDatabase, username, password)) {
            final String request = "SELECT * FROM users";

            try (Statement statement = connection.createStatement()) {

                try (ResultSet resultSet = statement.executeQuery(request)) {
                    while (resultSet.next()) {

                        if (String.valueOf(resultSet.getInt("chat_id")).equals(chatId)) {

                            apiKeys.put(STATISTICS_KEY, resultSet.getString("statistics_api"));
                            apiKeys.put(STANDART_KEY, resultSet.getString("standart_api"));
                            result.put(chatId, apiKeys);

                        }
                    }
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

}
