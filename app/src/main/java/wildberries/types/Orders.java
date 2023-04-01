package wildberries.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Orders {
    @JsonIgnore
    private String date;
    @JsonIgnore
    private String lastChangeDate;
    private String supplierArticle;
    @JsonIgnore
    private String techSize;
    @JsonIgnore
    private String barcode;
    private double totalPrice;
    private int discountPercent;
    private String warehouseName;
    private String oblast;
    @JsonIgnore
    private int incomeID;
    @JsonIgnore
    private long odid;
    @JsonIgnore
    private long nmId;
    @JsonIgnore
    private String subject;
    @JsonIgnore
    private String category;
    private String brand;
    @JsonProperty("isCancel")
    private boolean isCancel;
    @JsonIgnore
    private String cancel_dt;
    @JsonIgnore
    private String gNumber;
    @JsonIgnore
    private String sticker;
    @JsonIgnore
    private String srid;

    public void setDate(String date) {
        this.date = date;
    }

    public void setLastChangeDate(String lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
    }

    public void setSupplierArticle(String supplierArticle) {
        this.supplierArticle = supplierArticle;
    }

    public void setTechSize(String techSize) {
        this.techSize = techSize;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public void setOblast(String oblast) {
        this.oblast = oblast;
    }

    public void setIncomeID(int incomeID) {
        this.incomeID = incomeID;
    }

    public void setOdid(long odid) {
        this.odid = odid;
    }

    public void setNmId(long nmId) {
        this.nmId = nmId;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

    public void setCancel_dt(String cancelDt) {
        this.cancel_dt = cancelDt;
    }

    public void setgNumber(String gNumber) {
        this.gNumber = gNumber;
    }

    public void setSticker(String sticker) {
        this.sticker = sticker;
    }

    public void setSrid(String srid) {
        this.srid = srid;
    }

    @Override
    public String toString() {
        String cancel = "нет";

        double priceWithDiscount = totalPrice * (1 - (double) discountPercent / 100);
        BigDecimal bd = new BigDecimal(priceWithDiscount);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        priceWithDiscount = bd.doubleValue();

        if (isCancel) {
            cancel = "да";
        }

        return "Бренд: " + brand + '\n'
                + "Артикул продавца: " + supplierArticle + '\n'
                + "Стоимость: " + priceWithDiscount + " руб." + '\n'
                + "Склад продажи: " + warehouseName + '\n'
                + "Куда заказан: " + oblast + '\n'
                + "Отменен: " + cancel + '\n';
    }
}
