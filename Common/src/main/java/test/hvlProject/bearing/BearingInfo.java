package test.hvlProject.bearing;

import test.hvlProject.world.Item;

public class BearingInfo {
    private Item sensor;
    private String description;
    private double targetDegree;

    public BearingInfo() {
    }

    public BearingInfo(Item sensor) {
        this.sensor = sensor;
    }

    public Item getSensor() {
        return sensor;
    }

    public void setSensor(Item sensor) {
        this.sensor = sensor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTargetDegree() {
        return targetDegree;
    }

    public void setTargetDegree(double targetDegree) {
        this.targetDegree = targetDegree;
    }

    @Override
    public String toString() {
        return "BearingInfo{" +
                "sensor=[" + sensor.getxCoordinate() + ", " + sensor.getyCoordinate() + "]"  +
                ", description='" + description + '\'' +
                ", targetDegree=" + targetDegree +
                '}';
    }
}
