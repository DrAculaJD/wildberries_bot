package telegram;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import wildberries.TypeOfApi;
import wildberries.typesOfOperations.TypeOfOperations;

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
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }

    }

    /**
     * Метод обрабатывает команды, которые пользователь отправляет в чат при помощи меню.
     * @param inputMessage текст сообщения пользователя
     * @param chatId ID Telegram чата пользователя
     */
    private void selectCommand(String inputMessage, String chatId) throws JsonProcessingException {

        // отправка ответа на вопрос/отзыв, если клиент ранее выбрал соответсвующую команду
        if (Answers.answerers.containsKey(chatId)) {
            sendMessage(Answers.sendAnswer(chatId, inputMessage));
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
        } else if ("/feedbackanswer".equals(inputMessage)) {
            // отправка сообщения с предложением ввести ответ на первый отзыв
            sendMessage(Answers.getFirstMessage(chatId, TypeOfOperations.FEEDBACKS, TypeOfApi.STANDART_API));
        } else if ("/questionanswer".equals(inputMessage)) {
            // отправка сообщения с предложением ввести ответ на первый вопрос
            sendMessage(Answers.getFirstMessage(chatId, TypeOfOperations.QUESTIONS, TypeOfApi.STANDART_API));
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
        final int apiLength = 149;

        if (inputMessage.equals("/start")) {
            startAction(update);
        } else if (inputMessage.length() == apiLength) {
            if (Data.isStatisticsKey(chatId, inputMessage, TypeOfOperations.SALE)) {

                sendMessage(Data.setApiKey(update, TypeOfApi.STATISTICS_API));
                sendMessage(getStandartApiKey(update));

            } else if (Data.isStandartKey(chatId, inputMessage, TypeOfOperations.QUESTIONS)) {

                sendMessage(Data.setApiKey(update, TypeOfApi.STANDART_API));

            }
        }
    }

    /**
     * Метод обрабатывает команду <b>/start</b> от пользователя: выводит информацию о боте
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

    /**
     * Формирует сообщение с просьбой пользователю ввести API ключ "Стандартный".
     * @param update сообщение из чата, которое отправил пользователь
     * @see org.telegram.telegrambots.meta.api.objects.Update
     */
    private SendMessage getStandartApiKey(Update update) {
        SendMessage message = new SendMessage();

        message.setChatId(update.getMessage().getChatId());
        message.setText("Теперь, пожалуйста, введите ваш ключ API \"Стандартный\"");

        return message;
    }
}
