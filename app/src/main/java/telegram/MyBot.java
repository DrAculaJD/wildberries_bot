package telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import telegram.data.Statistics;
import wildberries.Shop;
import wildberries.typesOfOperations.TypeOfOperations;

import java.util.ArrayList;
import java.util.List;

import static main.Main.USER_SQL;

public class MyBot extends TelegramLongPollingBot {
    private final String botUsername;
    private final String botToken;
    public static final Shop SHOP = new Shop();

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

        if (update.getMessage() != null) {
            firstStart(update);
        } else if (update.getCallbackQuery().getData().equals("Заказы сегодня")) {
            sendMessage(Statistics.getTodayData(TypeOfOperations.ORDER));
        } else if (update.getCallbackQuery().getData().equals("Продажи сегодня")) {
            sendMessage(Statistics.getTodayData(TypeOfOperations.SALE));
        }

        if (!SHOP.isStatisticsApiMessage()) {
            getButtons(update);
        }

    }

    private void firstStart(Update update) {
        String inputMessage = update.getMessage().getText();
        final int apiLength = 149;

        if (inputMessage.equals("/start")) {
            startAction(update);
        } else if (SHOP.isStatisticsApiMessage() && inputMessage.length() == apiLength) {
            sendMessage(Statistics.setStatisticsApi(update));
        }
    }

    private void startAction(Update update) {
        SHOP.setStatisticsApiMessage(true);
        SHOP.setChatId(update.getMessage().getChatId().toString());
        USER_SQL.setTelegramUser();

        SendMessage outputMessageFirst = new SendMessage();
        outputMessageFirst.setChatId(SHOP.getChatId());
        outputMessageFirst.setText("❗❗❗ Сообщаем вам, что все данные, которые передаются в этом боте, не защищены, "
                + "так как это открытый проект.\nМы не несем ответственность за сохранность этих данных.");

        SendMessage outputMessageNext = new SendMessage();
        outputMessageNext.setChatId(SHOP.getChatId());
        outputMessageNext.setText("Введите ваш ключ API \"Статистика\"");

        try {
            execute(outputMessageFirst);
            execute(outputMessageNext);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static InlineKeyboardMarkup setButtons() {

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        InlineKeyboardButton ordersToday = new InlineKeyboardButton();
        ordersToday.setText("Заказы сегодня");
        ordersToday.setCallbackData("Заказы сегодня");

        InlineKeyboardButton salesToday = new InlineKeyboardButton();
        salesToday.setText("Продажи сегодня");
        salesToday.setCallbackData("Продажи сегодня");

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        //List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        rowInline1.add(ordersToday);
        rowInline1.add(salesToday);

        rowsInline.add(rowInline1);
        //rowsInline.add(rowInline2);
        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

    private void getButtons(Update update) {

        String inputData;

        if (update.getCallbackQuery() == null) {
            inputData = "";
        } else {
            inputData = update.getCallbackQuery().getData();
        }

        SendMessage outputMessage = new SendMessage();

        if (!inputData.isEmpty()) {
            outputMessage.setText("Выберите действие");
        }

        outputMessage.setReplyMarkup(setButtons());
        outputMessage.setChatId(SHOP.getChatId());
        outputMessage.setParseMode(ParseMode.HTML);
        sendMessage(outputMessage);
    }

    public void sendMessage(SendMessage message) {

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println("Message without text.");
        }
    }
}
