package telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import wildberries.TypeOfApi;
import wildberries.typesOfOperations.TypeOfOperations;

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

            if (inputMessage.equals("/orders")) {
                sendMessage(Data.getTodayData(chatId, TypeOfOperations.ORDER, TypeOfApi.STATISTICS_API));
            } else if (inputMessage.equals("/sales")) {
                sendMessage(Data.getTodayData(chatId, TypeOfOperations.SALE, TypeOfApi.STATISTICS_API));
            } else if (inputMessage.equals("/questions")) {
                sendMessage(Data.getTodayData(chatId, TypeOfOperations.QUESTIONS, TypeOfApi.STANDART_API));
            } else if (inputMessage.equals("/feedbacks")) {
                sendMessage(Data.getTodayData(chatId, TypeOfOperations.FEEDBACKS, TypeOfApi.STANDART_API));
            }

        }

    }

    private void startForNewUser(Update update) {
        String inputMessage = update.getMessage().getText().trim();
        final int apiLength = 149;

        if (inputMessage.equals("/start")) {
            startAction(update);
        } else if (inputMessage.length() == apiLength) {
            if (Data.isStatisticsKey(inputMessage, TypeOfOperations.SALE)) {

                sendMessage(Data.setApiKey(update, TypeOfApi.STATISTICS_API));
                sendMessage(getStandartApiKey(update));

            } else if (Data.isStandartKey(inputMessage, TypeOfOperations.QUESTIONS)) {

                sendMessage(Data.setApiKey(update, TypeOfApi.STANDART_API));

            }
        }
    }

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
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(SendMessage message) {

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println("Message without text.");
        }
    }

    private SendMessage getStandartApiKey(Update update) {
        SendMessage message = new SendMessage();

        message.setChatId(update.getMessage().getChatId());
        message.setText("Теперь, пожалуйста, введите ваш ключ API \"Стандартный\"");

        return message;
    }
}
