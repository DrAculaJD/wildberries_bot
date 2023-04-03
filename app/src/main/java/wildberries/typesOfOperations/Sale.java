package wildberries.typesOfOperations;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Sale {
    @JsonIgnore
    private String date;
    @JsonIgnore
    private String lastChangeDate;

    private String supplierArticle;
    @JsonIgnore
    private String techSize;
    @JsonIgnore
    private String barcode;
    @JsonIgnore
    private int totalPrice;
    @JsonIgnore
    private int discountPercent;
    @JsonIgnore
    private boolean isSupply;
    @JsonIgnore
    private boolean isRealization;
    @JsonIgnore
    private int promoCodeDiscount;

    private String warehouseName;
    @JsonIgnore
    private String countryName;
    @JsonIgnore
    private String oblastOkrugName;
    @JsonIgnore
    private String regionName;
    @JsonIgnore
    private int incomeID;
    @JsonIgnore
    private String saleID;
    @JsonIgnore
    private long odid;
    @JsonIgnore
    private int spp;

    private int forPay;
    @JsonIgnore
    private int finishedPrice;
    @JsonIgnore
    private int priceWithDisc;
    @JsonIgnore
    private int nmId;
    @JsonIgnore
    private String subject;
    @JsonIgnore
    private String category;

    private String brand;

    private int isStorno;
    @JsonIgnore
    private String gNumber;
    @JsonIgnore
    private String sticker;
    @JsonIgnore
    public void setDate(String date) {
        this.date = date;
    }
    @JsonIgnore
    public void setLastChangeDate(String lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
    }

    public void setSupplierArticle(String supplierArticle) {
        this.supplierArticle = supplierArticle;
    }
    @JsonIgnore
    public void setTechSize(String techSize) {
        this.techSize = techSize;
    }
    @JsonIgnore
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
    @JsonIgnore
    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
    @JsonIgnore
    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }
    @JsonIgnore
    public void setSupply(boolean supply) {
        isSupply = supply;
    }
    @JsonIgnore
    public void setRealization(boolean realization) {
        isRealization = realization;
    }
    @JsonIgnore
    public void setPromoCodeDiscount(int promoCodeDiscount) {
        this.promoCodeDiscount = promoCodeDiscount;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }
    @JsonIgnore
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    @JsonIgnore
    public void setOblastOkrugName(String oblastOkrugName) {
        this.oblastOkrugName = oblastOkrugName;
    }
    @JsonIgnore
    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
    @JsonIgnore
    public void setIncomeID(int incomeID) {
        this.incomeID = incomeID;
    }
    @JsonIgnore
    public void setSaleID(String saleID) {
        this.saleID = saleID;
    }
    @JsonIgnore
    public void setOdid(long odid) {
        this.odid = odid;
    }
    @JsonIgnore
    public void setSpp(int spp) {
        this.spp = spp;
    }

    public void setForPay(int forPay) {
        this.forPay = forPay;
    }
    @JsonIgnore
    public void setFinishedPrice(int finishedPrice) {
        this.finishedPrice = finishedPrice;
    }
    @JsonIgnore
    public void setPriceWithDisc(int priceWithDisc) {
        this.priceWithDisc = priceWithDisc;
    }
    @JsonIgnore
    public void setNmId(int nmId) {
        this.nmId = nmId;
    }
    @JsonIgnore
    public void setSubject(String subject) {
        this.subject = subject;
    }
    @JsonIgnore
    public void setCategory(String category) {
        this.category = category;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setIsStorno(int isStorno) {
        this.isStorno = isStorno;
    }
    @JsonIgnore
    public void setgNumber(String gNumber) {
        this.gNumber = gNumber;
    }
    @JsonIgnore
    public void setSticker(String sticker) {
        this.sticker = sticker;
    }

    @Override
    public String toString() {

        String cancel = "нет";
        if (isStorno == 1) {
            cancel = "да ❌";
        }

        return "Продажа"
                + "Артикул продавца: '" + supplierArticle + '\n'
                + "Бренд товара: " + brand + '\n'
                + "Склад продажи: '" + warehouseName + '\n'
                + "К перечислению поставщику: " + forPay + '\n'
                + "Возврат: " + cancel + '\n';
    }
}
