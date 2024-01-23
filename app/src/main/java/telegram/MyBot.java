package telegram;

import database.UserSQL;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import wildberries.TypeOfApi;
import wildberries.typeOfOperations.TypeOfOperations;

import java.sql.SQLException;

import static telegram.Data.getWaitMessage;

/**
 * Класс содержит логику взаимодествия бота с программой.
 */
public class MyBot extends TelegramLongPollingBot {
    private final String botUsername;
    private final String botToken;

    public MyBot(String botUsername, String botToken) {
        this.botUsername = botUsername;
        this.botToken = botToken;
    }
    @Override
    public String getBotUsername() {
        return this.botUsername;
    }
    @Override
    public String getBotToken() {
        return this.botToken;
    }
    @Override
    public void onUpdateReceived(Update update) {
        final String chatId = update.getMessage().getChatId().toString();
        final String inputMessage = update.getMessage().getText();
        //System.out.println(update.getMessage());

        if (inputMessage != null) {
            startForNewUser(update);
            try {
                selectCommand(inputMessage, chatId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * Метод обрабатывает команды, которые пользователь отправляет в чат при помощи меню.
     * @param inputMessage текст сообщения пользователя
     * @param chatId ID Telegram чата пользователя
     */
    private void selectCommand(String inputMessage, String chatId) throws Exception {


        // отправка ответа на вопрос/отзыв, если клиент ранее выбрал соответсвующую команду
        if (Answers.answerers.containsKey(chatId)) {
            sendMessage(Answers.sendAnswer(chatId, inputMessage));
        } else if (UserSQL.theyWantToDeleteData.contains(chatId)) {
            sendMessage(deleteUser(chatId, inputMessage));
        }

        if ("/orders".equals(inputMessage)) {
            // отправка списка заказов
            sendMessage(Data.getTodayData(chatId, TypeOfOperations.ORDER, TypeOfApi.STATISTICS_API));
        } else if ("/sales".equals(inputMessage)) {
            // отправка списка продаж
            sendMessage(Data.getTodayData(chatId, TypeOfOperations.SALE, TypeOfApi.STATISTICS_API));
        } else if ("/questions".equals(inputMessage)) {
            // отправка списка отзывов, на которые нужно ответить
            sendMessage(Data.getTodayData(chatId, TypeOfOperations.QUESTIONS, TypeOfApi.STANDART_API));
        } else if ("/feedbacks".equals(inputMessage)) {
            // отправка списка вопросов, на которые нужно ответить
            sendMessage(Data.getTodayData(chatId, TypeOfOperations.FEEDBACKS, TypeOfApi.STANDART_API));
        } else if ("/feedback_answer".equals(inputMessage)) {
            // отправка сообщения с предложением ввести ответ на первый отзыв
            sendMessage(Answers.getFirstMessage(chatId, TypeOfOperations.FEEDBACKS, TypeOfApi.STANDART_API));
        } else if ("/question_answer".equals(inputMessage)) {
            // отправка сообщения с предложением ввести ответ на первый вопрос
            sendMessage(Answers.getFirstMessage(chatId, TypeOfOperations.QUESTIONS, TypeOfApi.STANDART_API));
        } else if ("/more_orders".equals(inputMessage)) {
            sendMessage(getWaitMessage(chatId));
            sendDocument(Data.getDataForSeveralMonths(chatId, TypeOfOperations.ORDER));
            sendMessage(Data.deleteExcel(chatId, TypeOfOperations.ORDER));
        } else if ("/more_sales".equals(inputMessage)) {
            sendMessage(getWaitMessage(chatId));
            sendDocument(Data.getDataForSeveralMonths(chatId, TypeOfOperations.SALE));
            sendMessage(Data.deleteExcel(chatId, TypeOfOperations.SALE));
        } else if ("/delete_user".equals(inputMessage)) {
            sendMessage(UserSQL.areYouSureMessage(chatId));
        }

    }

    /**
     * Метод обрабатывает сообщение пользователя, если он впервые запустил бот или решил обновить API ключи в боте.
     * @param update сообщение из чата, которое отправил пользователь
     * @see org.telegram.telegrambots.meta.api.objects.Update
     */
    private void startForNewUser(Update update) {
        final String inputMessage = update.getMessage().getText().trim();
        final String chatId = update.getMessage().getChatId().toString();

        // длина API ключа, используется для его идентификации,
        // так как совпадения иного сообщения по количеству символов маловероятно
        //final int longApiLength = 381;
        //final int shortApiLength = 372;
        final String checkTokenMessage = "eyjh";
        final String firstSymFromInput = inputMessage.substring(0,4).trim().toLowerCase();

        if (inputMessage.equals("/start")) {
            startAction(update);
        } else if (firstSymFromInput.equals(checkTokenMessage)) {
            if (Data.isStatisticsKey(chatId, inputMessage, TypeOfOperations.SALE)) {

                sendMessage(Data.setApiKey(update, TypeOfApi.STATISTICS_API));
                sendMessage(getStandartApiKey(update));

            } else if (Data.isStandartKey(chatId, inputMessage, TypeOfOperations.QUESTIONS)) {

                sendMessage(Data.setApiKey(update, TypeOfApi.STANDART_API));

            }
        }
    }

    /**
     * Метод обрабатывает команду <b>/start</b> от пользователя: выводит информацию о боте.
     * и предлагает пользователю ввести API ключ "Статистика"
     * @param update сообщение из чата, которое отправил пользователь
     * @see org.telegram.telegrambots.meta.api.objects.Update
     */
    private void startAction(Update update) {
        final String chatId = update.getMessage().getChatId().toString();

        SendMessage outputMessageFirst = new SendMessage();
        outputMessageFirst.setChatId(chatId);
        outputMessageFirst.setText("❗❗❗ Сообщаем вам, что все данные, которые передаются в этом боте, не защищены, "
                + "так как это открытый проект.\nМы не несем ответственность за сохранность этих данных.");

        SendMessage outputMessageNext = new SendMessage();
        outputMessageNext.setChatId(chatId);
        outputMessageNext.setText("Если вы используете этот бот впервые, пожалуйста, введите ваш "
                + "ключ API \"Статистика\"");

        SendMessage outputMessageThird = new SendMessage();
        outputMessageThird.setChatId(chatId);
        outputMessageThird.setText("Если вы уже пользовались этим ботом, тогда можете сразу приступать "
                + "к использованию команд из меню.");

        try {
            execute(outputMessageFirst);
            execute(outputMessageNext);
            execute(outputMessageThird);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Отправляет сообщение для пользователя в бот. Метод создан для сокращения дублированых частей в коде.
     * @param message объект <b>SendMessage</b>, в котором содержится сообщение для пользователя
     * @see org.telegram.telegrambots.meta.api.methods.send.SendMessage
     */
    public void sendMessage(SendMessage message) {

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println("Message without text.");
        }
    }

    public void sendDocument(SendDocument document) {

        try {
            execute(document);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Формирует сообщение с просьбой пользователю ввести API ключ "Стандартный".
     * @param update сообщение из чата, которое отправил пользователь
     * @return Cообщение с просьбой пользователю ввести API ключ "Стандартный"
     * @see org.telegram.telegrambots.meta.api.objects.Update
     */
    private SendMessage getStandartApiKey(Update update) {
        SendMessage message = new SendMessage();

        message.setChatId(update.getMessage().getChatId());
        message.setText("Теперь, пожалуйста, введите ваш ключ API \"Вопросы и отзывы\"");

        return message;
    }

    /**
     * Метод проверяет какой ответ отправил пользователь для подтверждения удаления данных.
     * В зависимости от этого ответа формурует новое сообщение для пользователя.
     * @param inputMessage текст сообщения пользователя
     * @param chatId ID Telegram чата пользователя
     * @return объект <b>SendMessage</b>, который хранит сообщение об успешном/неуспешном удалении данных пользователя.
     */
    private SendMessage deleteUser(String chatId, String inputMessage) throws SQLException {

        final String formatMessage = inputMessage.trim().toLowerCase();

        if (formatMessage.equals("да")) {
            return UserSQL.deleteUser(chatId);
        } else if (formatMessage.equals("нет")) {
            return UserSQL.undoDataDeletion(chatId);
        }

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Команда не распознана, пожалуйста, введите \"Да\" или \"Нет\"");

        return message;
    }
}
