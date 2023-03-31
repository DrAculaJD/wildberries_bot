package main;

import wildberries.Parsing;
import wildberries.WBdata;

public class Main {
    public static void main(String[] args) {

        String result = WBdata.getOrdersForTheDay("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2Nlc3NJRCI6I"
                + "jQ0YTNiZGY2LThkMzItNDg0Zi1iODA0LTQxNzY2M2IwYWFmZSJ9.5WG20bkJyl7D80DsqICp8n29b5GgD-IR7wXJN18kvzk");

        Parsing.dataToString(result);
    }
}
