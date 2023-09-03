package test.hvlProject.world;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    private String itemName;
    private ItemType itemType;
    private int xCoordinate;
    private int yCoordinate;

    public Item() {
    }

    public void setCoordinates(int xCoordinate, int yCoordinate) {
        setxCoordinate(xCoordinate);
        setyCoordinate(yCoordinate);
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(int xCoordinate) {
        this.xCoordinate = Math.min(xCoordinate, 1000);
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = Math.min(yCoordinate, 1000);
    }
}
