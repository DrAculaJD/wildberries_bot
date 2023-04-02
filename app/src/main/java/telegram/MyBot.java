package telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import wildberries.Parsing;
import wildberries.Shop;
import wildberries.WBdata;

import java.util.ArrayList;
import java.util.List;

public class MyBot extends TelegramLongPollingBot {

    private boolean firstStart = true;
    private boolean statisticsApiMessage = true;
    private final Shop shop = new Shop();

    private String chatId;

    @Override
    public String getBotUsername() {
        return "polezhaevTestBot";
    }

    @Override
    public String getBotToken() {
        return "5699977622:AAFVfx9eVU6_-1lHGD1DB0VGCbbjtDc1878";
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.getMessage() != null) {
            String inputMessage = update.getMessage().getText();

            if (inputMessage.equals("/start") && firstStart) {
                startAction(update);
            } else if (statisticsApiMessage && inputMessage.length() == 149) {
                statisticsApi(update);
            }
        } else if (update.getCallbackQuery().getData().equals("Заказы сегодня")) {
            getTodayOrders();
        }

        if (!statisticsApiMessage) {
            getButtons(update);
        }

    }

    private void startAction(Update update) {
        firstStart = false;
        chatId = update.getMessage().getChatId().toString();

        SendMessage otputMessageFirst = new SendMessage();
        otputMessageFirst.setChatId(chatId);
        otputMessageFirst.setText("❗ Сообщаем вам, что все данные, которые передаются в этом боте, не защищены, " +
                "так как это открытый проект. \nМы не несем ответственность за сохранность этих данных. ❗");

        SendMessage otputMessageNext = new SendMessage();
        otputMessageNext.setChatId(update.getMessage().getChatId().toString());
        otputMessageNext.setText("Введите ваш ключ API \"Статистика\"");

        try {
            execute(otputMessageFirst);
            execute(otputMessageNext);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void statisticsApi(Update update) {
        String inputMessage = update.getMessage().getText().trim();
        shop.setStatisticsApi(inputMessage);
        final String ordersToday = WBdata.getOrdersForTheDay(shop.getStatisticsApi());

        SendMessage otputMessage = new SendMessage();
        otputMessage.setChatId(update.getMessage().getChatId().toString());

        try {
            Parsing.dataToString(ordersToday);

            statisticsApiMessage = false;
            otputMessage.setText("Отлично, можем приступать к работе! ✨");
            otputMessage.setReplyMarkup(setButtons());
        } catch (Exception e) {
            otputMessage.setText("К сожалению, этот ключ не работает, проверьте, правильно ли скопирован ключ "
                    + "и попробуйте еще раз \uD83D\uDE80");
        }

        try {
            execute(otputMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private InlineKeyboardMarkup setButtons() {

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        InlineKeyboardButton ordersToday = new InlineKeyboardButton();
        ordersToday.setText("Заказы сегодня");
        ordersToday.setCallbackData("Заказы сегодня");

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(ordersToday);

        rowsInline.add(rowInline);
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

        SendMessage otputMessage = new SendMessage();

        if (inputData.equals("Заказы сегодня")) {
            otputMessage.setText("Выберите действие");
        }

        otputMessage.setReplyMarkup(setButtons());
        otputMessage.setChatId(chatId);

        try {
            execute(otputMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void getTodayOrders() {
        final String ordersToday = WBdata.getOrdersForTheDay(shop.getStatisticsApi());

        SendMessage otputMessage = new SendMessage();
        otputMessage.setChatId(chatId);
        otputMessage.setText(Parsing.dataToString(ordersToday));

        try {
            execute(otputMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
