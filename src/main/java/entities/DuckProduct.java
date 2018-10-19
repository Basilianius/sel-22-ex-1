package entities;

import org.openqa.selenium.WebElement;

import java.io.File;

/**
 * описывает продукт Уточки
 */
public class DuckProduct {
    private WebElement link;
    private String name;
    private String regularPrice;
    private String regularPriceColor;
    private String regularPriceSize;
    private String campaignPrice;
    private String campaignPriceColor;
    private String campaignPriceSize;

    private int quantity;
    private String filePath = new File("newDuck.jpg").getAbsolutePath();

    public DuckProduct(String name, String regularPrice, int quantity) {
        this.name = name;
        this.regularPrice = regularPrice;
        this.quantity = quantity;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public DuckProduct(WebElement link, String name, String regularPrice, String regularPriceColor, String regularPriceSize, String campaignPrice, String campaignPriceColor, String campaignPriceSize) {
        this.link = link;
        this.name = name;
        this.regularPrice = regularPrice;
        this.regularPriceColor = regularPriceColor;
        this.regularPriceSize = regularPriceSize;
        this.campaignPrice = campaignPrice;
        this.campaignPriceColor = campaignPriceColor;
        this.campaignPriceSize = campaignPriceSize;
    }

    public String getRegularPriceSize() {
        return regularPriceSize;
    }

    public void setRegularPriceSize(String regularPriceSize) {
        this.regularPriceSize = regularPriceSize;
    }

    public String getCampaignPriceSize() {
        return campaignPriceSize;
    }

    public void setCampaignPriceSize(String campaignPriceSize) {
        this.campaignPriceSize = campaignPriceSize;
    }

    public WebElement getLink() {
        return link;
    }

    public void setLink(WebElement link) {
        this.link = link;
    }

    public DuckProduct() {
    }

    public DuckProduct(WebElement link, String name, String regularPrice, String regularPriceColor, String campaignPrice, String campaignPriceColor) {
        this.link = link;
        this.name = name;
        this.regularPrice = regularPrice;
        this.regularPriceColor = regularPriceColor;
        this.campaignPrice = campaignPrice;
        this.campaignPriceColor = campaignPriceColor;
    }

    public DuckProduct(String name, String regularPrice, String regularPriceColor, String campaignPrice, String campaignPriceColor) {
        this.name = name;
        this.regularPrice = regularPrice;
        this.regularPriceColor = regularPriceColor;
        this.campaignPrice = campaignPrice;
        this.campaignPriceColor = campaignPriceColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(String regularPrice) {
        this.regularPrice = regularPrice;
    }

    public String getRegularPriceColor() {
        return regularPriceColor;
    }

    public void setRegularPriceColor(String regularPriceColor) {
        this.regularPriceColor = regularPriceColor;
    }

    public String getCampaignPrice() {
        return campaignPrice;
    }

    public void setCampaignPrice(String campaignPrice) {
        this.campaignPrice = campaignPrice;
    }

    public String getCampaignPriceColor() {
        return campaignPriceColor;
    }

    public void setCampaignPriceColor(String campaignPriceColor) {
        this.campaignPriceColor = campaignPriceColor;
    }


    @Override
    public String toString() {
        return "DuckProduct{" +
                "link=" + link +
                ", name='" + name + '\'' +
                ", regularPrice='" + regularPrice + '\'' +
                ", regularPriceColor='" + regularPriceColor + '\'' +
                ", regularPriceSize='" + regularPriceSize + '\'' +
                ", campaignPrice='" + campaignPrice + '\'' +
                ", campaignPriceColor='" + campaignPriceColor + '\'' +
                ", campaignPriceSize='" + campaignPriceSize + '\'' +
                ", quantity=" + quantity +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
