package telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import org.telegram.telegrambots.meta.exceptions.TelegramApiValidationException;
import telegram.data.Statistics;
import wildberries.Shop;

import java.util.ArrayList;
import java.util.List;

public class MyBot extends TelegramLongPollingBot {

    public static final Shop shop = new Shop();
    public final int API_LENGTH = 149;

    @Override
    public String getBotUsername() {
        return "testing_wb_bot";
    }

    @Override
    public String getBotToken() {
        return "5751010883:AAFEAHtopRPUq8-U06bI1zfknRhAldzJR3s";
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.getMessage() != null) {
            firstStart(update);
        } else if (update.getCallbackQuery().getData().equals("Заказы сегодня")) {
            sendMessage(Statistics.getTodayOrders());
        } else if (update.getCallbackQuery().getData().equals("Продажи сегодня")) {
            sendMessage(Statistics.getTodaySales());
        }

        if (!shop.isStatisticsApiMessage()) {
            getButtons(update);
        }

    }

    private void firstStart(Update update) {
        String inputMessage = update.getMessage().getText();
        if (inputMessage.equals("/start")) {
            startAction(update);
        } else if (shop.isStatisticsApiMessage() && inputMessage.length() == API_LENGTH) {
            sendMessage(Statistics.setStatisticsApi(update));
        }
    }

    private void startAction(Update update) {
        shop.setStatisticsApiMessage(true);
        shop.setChatId(update.getMessage().getChatId().toString());

        SendMessage outputMessageFirst = new SendMessage();
        outputMessageFirst.setChatId(shop.getChatId());
        outputMessageFirst.setText("❗ Сообщаем вам, что все данные, которые передаются в этом боте, не защищены, " +
                "так как это открытый проект. \nМы не несем ответственность за сохранность этих данных. ❗");

        SendMessage outputMessageNext = new SendMessage();
        outputMessageNext.setChatId(shop.getChatId());
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
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        rowInline1.add(ordersToday);
        rowInline2.add(salesToday);

        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);
        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

    private void getButtons (Update update) {

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
        outputMessage.setChatId(shop.getChatId());

        sendMessage(outputMessage);
    }

    public void sendMessage (SendMessage message) {

        try {
            execute(message);
//        } catch (TelegramApiValidationException e) {
//            e.printStackTrace();
        } catch (TelegramApiException e) {
            System.out.println("Message without text.");
        }
    }
}
