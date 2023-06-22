package telegram;

import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import wildberries.JsonToExcelConverter;
import wildberries.Parsing;
import wildberries.TypeOfApi;
import wildberries.WBdata;
import wildberries.typeOfOperations.TypeOfOperations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static main.Main.userSQL;

/**
 * Класс служит прослойкой между методами, которые выполняют обработку данных,
 * поступающих из Telegram бота и методами, которые взаимодействуют с другими сервисами (Wildberries, SQL).
 * Содержит методы, которые обращаются к серверу Wildberries и базе данных, форматируют и передают эти данные
 * в методы класса MyBot.
 */

public class Data {

    /**
     * Получает сообщение из бота, которое должно быть API ключем пользователя, и передает в класс <b>userSQL</b>
     * для записи в базу данных.
     * @param update сообщение пользователя из Telegram
     * @param typeOfApi тип API ключа, который требуется добавить в БД
     * @return объект типа <b>SendMessage</b>, в котором содержится сообщение
     * об успешном/неуспешном добавлении API ключа в БД
     * @see org.telegram.telegrambots.meta.api.objects.Update
     * @see wildberries.TypeOfApi
     * @see org.telegram.telegrambots.meta.api.methods.send.SendMessage
     */
    public static SendMessage setApiKey(Update update, TypeOfApi typeOfApi) {
        // получение текста сообщения
        final String apiKey = update.getMessage().getText().trim();
        // получение ID Telegram чата
        final String chatId = update.getMessage().getChatId().toString();

        SendMessage outputMessage = new SendMessage();
        outputMessage.setChatId(chatId);

        try {
            userSQL.setTelegramUser(chatId, apiKey, typeOfApi);

            outputMessage.setText("Отлично, ключ работает! ✨");
        } catch (Exception e) {
            outputMessage.setText("К сожалению, этот ключ не работает, проверьте, правильно ли он скопирован "
                    + "и попробуйте еще раз \uD83D\uDE80");
        }

        return outputMessage;
    }

    /**
     * Метод получает данные от сервера Wildberries и форматирует их для отправки пользователю.
     * @param chatId ID Telegram чата пользователя
     * @param typeOfOperations тип объекта, с которым работает метод
     * @param typeOfApi тип API ключа, который требуется добавить в БД
     * @return объект <b>SendMessage</b>, который содержит сообщение для пользователя
     * @see wildberries.TypeOfApi
     * @see wildberries.typeOfOperations.TypeOfOperations
     * @see org.telegram.telegrambots.meta.api.methods.send.SendMessage
     */
    public static SendMessage getTodayData(String chatId, TypeOfOperations typeOfOperations, TypeOfApi typeOfApi) {
        final SendMessage outputMessage = new SendMessage();
        final String api = userSQL.getApi(chatId, typeOfApi);

        final String ordersToday = WBdata.getData(api, typeOfOperations, typeOfApi, false);

        outputMessage.setChatId(chatId);
        outputMessage.setText(Parsing.dataToString(chatId, ordersToday, typeOfOperations));

        return outputMessage;
    }

    /**
     * Подготавливает данные с сервера Wildberries с начала года для их отправки в чат пользователю.
     * @param chatId ID Telegram чата пользователя
     * @param typeOfOperations тип объекта, с которым работает метод
     * @return объект типа <b>SendDocument</b> в котором хранится Excel файл для отправки пользователю
     * @see wildberries.typeOfOperations.TypeOfOperations
     * @see org.telegram.telegrambots.meta.api.methods.send.SendDocument
     */
    public static SendDocument getDataForSeveralMonths(String chatId,
                                                       TypeOfOperations typeOfOperations) {
        try {
            // получение API ключа для отправки запроса на сервер
            final String api = userSQL.getApi(chatId, TypeOfApi.STATISTICS_API);
            final String data = WBdata.getData(api, typeOfOperations, TypeOfApi.STATISTICS_API, true);
            // создается объект с Excel файлом для его передачи в sendDocument
            final File file = new File(JsonToExcelConverter.createExcel(chatId, data, typeOfOperations));

            // file передается в понятный для пакета org.telegram.telegrambots объект InputFile
            final InputFile inputFile = new InputFile(file);
            // создается объект SendDocument для передачи Excel файла в чат с пользователем бота
            final SendDocument sendDocument = new SendDocument();
            sendDocument.setChatId(chatId);
            sendDocument.setDocument(inputFile);
            return sendDocument;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Удаляет созданный да диске Excel файл, чтобы не засорять систему.
     * @param chatId ID Telegram чата пользователя
     * @param typeOfOperations тип объекта, с которым работает метод
     * @return объект <b>SendMessage</b> в котором хранится сообщения для пользователя
     * об успешной или неуспешной отправке файла
     * @see wildberries.typeOfOperations.TypeOfOperations
     * @see org.telegram.telegrambots.meta.api.methods.send.SendDocument
     */
    public static SendMessage deleteExcel(String chatId, TypeOfOperations typeOfOperations) {
        // инициализируется уникально имя файла, который будет удален, оно состоит из шести первых символов
        // ID чата пользователя и наименования типа данных в файле (продажи или заказы)
        final String fileName;
        if (typeOfOperations.equals(TypeOfOperations.ORDER)) {
            fileName = chatId.substring(0, 6) + "_orders";
        } else {
            fileName = chatId.substring(0, 6) + "_sales";
        }

        // инициализируется переменная для хранения пути к файлу
        final Path path = Paths.get(fileName + ".xlsx");

        final SendMessage outputMessage = new SendMessage();
        outputMessage.setChatId(chatId);

        try {
            // осуществляется попытка удалить файл, если файла не оказалось по указанному пути,
            // то пользователю отправляется сообщение об ошибке
            if (Files.deleteIfExists(path)) {
                outputMessage.setText("Нажмите на файл, чтобы скачать его ☝");
            } else {
                outputMessage.setText("Не удалось сформировать файл, попробуйте позже еще раз \uD83E\uDD14");
            }

            return outputMessage;
        } catch (IOException e) {
            // если возникла ошибка при удалении файла, то пользователю отправляется сообщение об ошибке
            outputMessage.setText("Не удалось сформировать файл, попробуйте позже еще раз \uD83E\uDD14");
            throw new RuntimeException(e);
        }

    }

    /**
     * Метод проверяет, соответсвует ли тип переданного API ключа типу "Статистика".
     * @param chatId ID Telegram чата пользователя
     * @param apiKey API ключ, который ввел пользователь
     * @param typeOfOperations тип объекта, с которым работает метод
     * @return <b>true</b> - если тип переданного API ключа соответсвует типу "Статистика"<br>
     * <b>false</b> - если тип переданного API ключа не соответсвует типу "Статистика"
     * @see wildberries.typeOfOperations.TypeOfOperations
     */
    public static boolean isStatisticsKey(String chatId, String apiKey, TypeOfOperations typeOfOperations) {

        try {
            final String dataToday = WBdata.getData(apiKey, typeOfOperations, TypeOfApi.STATISTICS_API, false);
            Parsing.dataToString(chatId, dataToday, typeOfOperations);
        } catch (Exception e) {
            return false;
        }

        return true;

    }

    /**
     * Метод проверяет, соответсвует ли тип переданного API ключа типу "Стандартный".
     * @param chatId ID Telegram чата пользователя
     * @param apiKey API ключ, который ввел пользователь
     * @param typeOfOperations тип объекта, с которым работает метод
     * @return <b>true</b> - если тип переданного API ключа соответсвует типу "Стандартный"<br>
     * <b>false</b> - если тип переданного API ключа не соответсвует типу "Стандартный"
     * @see wildberries.typeOfOperations.TypeOfOperations
     */
    public static boolean isStandartKey(String chatId, String apiKey, TypeOfOperations typeOfOperations) {

        try {
            final String dataToday = WBdata.getData(apiKey, typeOfOperations, TypeOfApi.STANDART_API, false);
            Parsing.dataToString(chatId, dataToday, typeOfOperations);
        } catch (Exception e) {
            return false;
        }

        return true;

    }
}
