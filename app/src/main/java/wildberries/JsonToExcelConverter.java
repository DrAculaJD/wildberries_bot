package wildberries;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import wildberries.typeOfOperations.TypeOfOperations;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Класс содержит методы для создания Excel файла на основе предоставленных данных в формате JSON.
 */
public class JsonToExcelConverter {

    /**
     * Метод заполняет Excel файл с заказами или продажами поставщика.
     * @param chatId ID Telegram чата пользователя
     * @param jsonData данные в формате JSON для записи в Excel файл
     * @param typeOfOperations тип объекта, с которым работает метод
     * @return Путь к готовому файлу.
     * @see wildberries.typeOfOperations.TypeOfOperations
     */
    public static String createExcel(String chatId,
                                   String jsonData,
                                   TypeOfOperations typeOfOperations) throws IOException {
        final String fileName;

        if (typeOfOperations.equals(TypeOfOperations.ORDER)) {
            fileName = chatId.substring(0, 6) + "_orders";
        } else {
            fileName = chatId.substring(0, 6) + "_sales";
        }

        // Создание рабочей книги Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(fileName);

        // Преобразование JSON в объект JsonNode
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonData);

        // Запись данных в Excel
        writeOrdersJsonToExcel(rootNode, sheet, typeOfOperations);

        // Сохранение Excel-файла
        String excelFilePath = fileName + ".xlsx";
        FileOutputStream outputStream = new FileOutputStream(excelFilePath);
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

        return excelFilePath;
    }

    /**
     * Метод перезаписывает данные из переданного JSON  в Excel файл.
     * @param node объект JsonNode, в котором хранятся данные длля перезаписывания
     * @param sheet Excel таблица, с которой работает метод
     * @param typeOfOperations тип объекта, с которым работает метод
     * @see wildberries.typeOfOperations.TypeOfOperations
     */
    private static void writeOrdersJsonToExcel(JsonNode node, Sheet sheet, TypeOfOperations typeOfOperations) {
        // переменная для хранения индекса строки в Excel файле
        int rowNum = 0;
        createHeaderRowOrders(sheet, typeOfOperations);

        for (JsonNode dataNode : node) {
            // создается новая строка в Excel файле
            Row row = sheet.createRow(++rowNum);
            // переменная для хранения индекса поля
            int cellNum = 0;

            // Извлечение значений из JSON и запись в ячейки Excel
            String value1 = dataNode.path("date").asText();
            row.createCell(cellNum++).setCellValue(value1);

            String value2 = dataNode.path("brand").asText();
            row.createCell(cellNum++).setCellValue(value2);

            String value3 = dataNode.path("subject").asText();
            row.createCell(cellNum++).setCellValue(value3);

            String value4 = dataNode.path("barcode").asText();
            row.createCell(cellNum++).setCellValue(value4);

            String value5 = dataNode.path("nmId").asText();
            row.createCell(cellNum++).setCellValue(value5);

            String value6 = dataNode.path("supplierArticle").asText();
            row.createCell(cellNum++).setCellValue(value6);

            // Вайлдберриз передает цену без скидок, то есть не реальую цену заказа товара
            // поэтому ее необходимо пересчитать методом getPriceWithDiscount
            // В случае с продажами сумма просто округляется до двух знаков после запятой
            String value7 = getPriceWithDiscount(dataNode, typeOfOperations);
            row.createCell(cellNum++).setCellValue(value7);

            // если Excel файл содержиз заказы, тогда в нем создается 2 дополнительных поля
            if (typeOfOperations.equals(TypeOfOperations.ORDER)) {

                String value8 = dataNode.path("warehouseName").asText();
                row.createCell(cellNum++).setCellValue(value8);

                String value9 = dataNode.path("oblast").asText();
                row.createCell(cellNum++).setCellValue(value9);
            }

            // количество всегда равно единице: одна строка - один заказ/продажа
            row.createCell(cellNum).setCellValue("1");
        }
    }

    /**
     * Создает строку заголовков столбцов в Excel файле и задает их значения.
     * @param sheet Excel таблица, с которой работает метод
     * @param typeOfOperations тип объекта, с которым работает метод
     * @see wildberries.typeOfOperations.TypeOfOperations
     */
    private static void createHeaderRowOrders(Sheet sheet, TypeOfOperations typeOfOperations) {
        // Создается переменная с заголовками столбцов будущей таблицы
        Row headerRow = sheet.createRow(0);
        int cellNum = 0;

        // Задаются заголовки столбцов в соответствии со структурой данных
        headerRow.createCell(cellNum++).setCellValue("Дата выкупа");
        headerRow.createCell(cellNum++).setCellValue("Бренд");
        headerRow.createCell(cellNum++).setCellValue("Тип товара");
        headerRow.createCell(cellNum++).setCellValue("Баркод");
        headerRow.createCell(cellNum++).setCellValue("Артикул ВБ");
        headerRow.createCell(cellNum++).setCellValue("Артикул поставщика");
        headerRow.createCell(cellNum++).setCellValue("Стоимость");

        // если необходимо создать таблицу для заказов, то изменяется наименование первого поля
        // и добавляются еще 2 поля: "Склад продажи" и "Куда заказан"
        if (typeOfOperations.equals(TypeOfOperations.ORDER)) {
            headerRow.createCell(0).setCellValue("Дата заказа");
            headerRow.createCell(cellNum++).setCellValue("Склад продажи");
            headerRow.createCell(cellNum++).setCellValue("Куда заказан");
        }

        headerRow.createCell(cellNum).setCellValue("Количество");

    }

    /**
     * Преобразует цену товара без скидок к той цене, по которой заказан или продан товар.
     * @param node объект JsonNode, в котором ведется поиск значений
     * @param typeOfOperations тип объекта, с которым работает метод
     * @return цена, по которой клиент заказал или купил товар
     * @see com.fasterxml.jackson.databind.JsonNode
     * @see wildberries.typeOfOperations.TypeOfOperations
     */
    private static String getPriceWithDiscount(JsonNode node, TypeOfOperations typeOfOperations) {
        double result;

        if (typeOfOperations.equals(TypeOfOperations.ORDER)) {
            result = node.path("totalPrice").doubleValue()
                    * (1 - node.path("discountPercent").doubleValue() / 100);
        } else {
            result = node.path("forPay").doubleValue();
        }

        BigDecimal bd = new BigDecimal(result);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        result = bd.doubleValue();

        return String.valueOf(result);
    }
}
