package wildberries;

public class Shop {
    private String statisticsApi;
    private String standartApi;
    private String advertisingApi;
    private boolean statisticsApiMessage = true;
    private String chatId;

    public String getStatisticsApi() {
        return statisticsApi;
    }

    public void setStatisticsApi(String statisticsApi) {
        this.statisticsApi = statisticsApi;
    }

    public String getStandartApi() {
        return standartApi;
    }

    public void setStandartApi(String standartApi) {
        this.standartApi = standartApi;
    }

    public String getAdvertisingApi() {
        return advertisingApi;
    }

    public void setAdvertisingApi(String advertisingApi) {
        this.advertisingApi = advertisingApi;
    }

    public boolean isStatisticsApiMessage() {
        return statisticsApiMessage;
    }

    public void setStatisticsApiMessage(boolean statisticsApiMessage) {
        this.statisticsApiMessage = statisticsApiMessage;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}
