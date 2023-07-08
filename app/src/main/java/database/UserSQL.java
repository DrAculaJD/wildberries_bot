package database;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import telegram.Data;
import wildberries.TypeOfApi;
import wildberries.typeOfOperations.TypeOfOperations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Класс для взаимодействия с базой данных PostrgeSQL, конкретно служит для выполнения следующих действий:<br>
 * - запись в БД данных новых пользователей<br>
 * - обновление данных пользователей<br>
 * - получение API ключей пользователей из БД.<br><br>
 */

public class UserSQL {
    /** Имя учетной записи, которой принадлежит БД, с которой работает бот. */
    private static String username;
    /** Пароль к учетной записи с которой работает бот. */
    private static String password;
    /** Путь к локальной БД. */
    private static String pathToDatabase;
    /** Список хранит ID чаты пользователей, которые выбрали команду "Удалить данные". */
    public static Set<String> theyWantToDeleteData = new HashSet<>();
    private static final String STATISTICS_KEY = "statisticsApi";
    private static final String STANDART_KEY = "standartApi";

    public UserSQL(String username, String password, String pathToDatabase) {
        UserSQL.username = username;
        UserSQL.password = password;
        UserSQL.pathToDatabase = pathToDatabase;
    }

    /**
     * Метод для добавления API ключей пользователя в базу данных PostrgeSQL. Его задача открыть соединение с БД
     * и передать данные пользователя в следующий метод, который определит тип запроса к БД и отправит этот запрос.
     * <br> Метод запускается при желании пользователя начать использовать бот или при необходимости
     * обновить API ключи.
     * @param chatId ID Telegram чата пользователя
     * @param apiKey API ключ, который требуется добавить в БД
     * @param typeOfApi тип API ключа, который требуется добавить в БД
     * @see wildberries.TypeOfApi
     */
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

    /**
     * Метод, который добавляет данные пользователя в БД в зависимости от того есть ли уже в ней данные
     * этого пользователя.
     * Если это новый пользователь, то будет отправлен запрос на добавление данных, если пользователь
     * уже пользовался ботом ранее, то запись будет обновлена.
     * @param connection соединение с базой данных
     * @param chatId ID Telegram чата пользователя
     * @param apiKey API ключ, который требуется добавить в БД
     * @param typeOfApi тип API ключа, который требуется добавить в БД
     */
    private void setUserToSql(Connection connection, String chatId, String apiKey,
                              TypeOfApi typeOfApi) throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement(selectRequest(typeOfApi, chatId))) {

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

    /**
     * Метод для выбора и получения API ключа.
     * @param chatId ID Telegram чата пользователя
     * @param typeOfApi тип API ключа, который требуется добавить в БД
     * @return Возвращает значение API ключа, тип переменной <b>String</b>
     */
    public static String getApi(String chatId, TypeOfApi typeOfApi) {
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

    /** Метод выбора запроса для выполнения разных действий с базой данных.
     * @return Возвращает один из запросов типа <b>String</b>:<br>
     * - запрос для добавления ключа "статистика" и id чата в БД при первом запуске бота;<br>
     * - обновление ключа "статистика" в БД<br>
     * - обновление ключа "стандартный" в БД
     * @param typeOfApi тип API ключа, который требуется добавить в БД
     * @param chatId ID Telegram чата пользователя
     */
    private String selectRequest(TypeOfApi typeOfApi, String chatId) {
        // запрос для добавления ключа "статистика" и id чата в БД при первом запуске бота
        String request = "INSERT INTO users (statistics_api, chat_id) VALUES (?, ?)";

        if (getFromDatabase(chatId).isEmpty()) {
            return request;
        }

        if (typeOfApi.equals(TypeOfApi.STATISTICS_API)) {
            request = "UPDATE users SET statistics_api = ? WHERE chat_id = ?"; // обновление ключа "статистика" в бд
        } else if (typeOfApi.equals(TypeOfApi.STANDART_API)) {
            request = "UPDATE users SET standart_api = ? WHERE chat_id = ?"; // обновление ключа "стандартный" в бд
        }

        return request;
    }

    /**
     * Метод для получения API ключей (стандартный и статистика) из базы данных SQL по ID Telegram чата.
     * @param chatId ID Telegram чата пользователя
     * @return Возвращается объект Map, в котором хранится ID Telegram чата пользователя
     * в качестве ключа типа <b>String</b>. В качестве соответсвующего ключу значения
     * хранится <b>Map</b><<b>String, String</b>>, где:<br>
     * key - тип ключа <br>
     * value - значение ключа
     */
    private static Map<String, Map<String, String>> getFromDatabase(String chatId) {
        Map<String, String> apiKeys = new HashMap<>();
        Map<String, Map<String, String>> result = new HashMap<>();

        try (Connection connection = DriverManager.getConnection(pathToDatabase, username, password)) {
            final String request = "SELECT * FROM users";

            try (Statement statement = connection.createStatement()) {
                // выбираем все записи в БД
                try (ResultSet resultSet = statement.executeQuery(request)) {
                    while (resultSet.next()) {
                        // если id чата соответсвует переданному в метод значению, сохраняем данные пользователяв map
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
        // возвращаются данные пользователя
        return result;
    }

    /**
     * Отправляет запрос в БД на удаление данных по ID чата пользователя.
     * Удаляет ID чата пользователя из списка <b>theyWantToDeleteData</b>.
     * @param chatId ID Telegram чата пользователя
     * @return объект <b>SendMessage</b>, который хранит сообщение об успешном/неуспешном удалении данных пользователя.
     */
    public static SendMessage deleteUser(String chatId) throws SQLException {
        final String request = "DELETE FROM users WHERE chat_id = ?";
        final SendMessage outputMessage = new SendMessage();
        outputMessage.setChatId(chatId);

        try (Connection connection = DriverManager.getConnection(pathToDatabase, username, password)) {
            try (PreparedStatement statement = connection.prepareStatement(request)) {

                statement.setLong(1, Integer.parseInt(chatId));

                int rowsDeleted = statement.executeUpdate();

                if (rowsDeleted > 0) {
                    outputMessage.setText("""
                            Ваши данные успешно удалены!
                            Команды бота больше не будут работать.

                            Благодарим вас за пользование ботом!""");
                }
            } catch (SQLException ex) {
                outputMessage.setText("Удаление завершено с ошибкой, "
                        + "пожалуйста, оставьте заявку на удаление данных в этом чате: https://t.me/wbotfeedback");
            }

            theyWantToDeleteData.remove(chatId);
        }

        return outputMessage;
    }

    /**
     * Формирует сообщение, в котором пользователю предлагается подтвердить удаление данных.
     * Добавляет ID чата с пользователем в список <b>theyWantToDeleteData</b>.
     * @param chatId ID Telegram чата пользователя
     * @return объект <b>SendMessage</b>, который хранит сообщение в котором пользователю
     * предлагается подтвердить удаление данных.
     */
    public static SendMessage areYouSureMessage(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        try {
            Data.getTodayData(chatId, TypeOfOperations.ORDER, TypeOfApi.STATISTICS_API);
            final String yesOrNoMessage = """
                Если вы действительно желаете удалить ваши данные из этого бота, введите в ответ "Да"
                Если вы решили не удалаять данные, тогда введите "Нет\"""";

            theyWantToDeleteData.add(chatId);
            message.setText(yesOrNoMessage);
        } catch (Exception e) {
            message.setText("""
                    Ваших данных нет в базе данных бота.\s
                    Для начала работы перезапустите бот или выберите команду "Обновить ключи\"""");
        }

        return message;
    }

    /**
     * Удаляет ID чата пользователя из списка <b>theyWantToDeleteData</b>.
     * @param chatId ID Telegram чата пользователя
     * @return объект <b>SendMessage</b>, который хранит сообщение "Ваши данные не удалены из бота."
     */
    public static SendMessage undoDataDeletion(String chatId) {
        theyWantToDeleteData.remove(chatId);
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Ваши данные не удалены из бота.");

        return message;
    }

}
