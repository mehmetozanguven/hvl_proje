package test.hvlProject;

public class Target {
    private final boolean isFound;
    private final double xCoordinate;
    private final double yCoordinate;

    public Target(boolean isFound, double xCoordinate, double yCoordinate) {
        this.isFound = isFound;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public double getxCoordinate() {
        return xCoordinate;
    }

    public double getyCoordinate() {
        return yCoordinate;
    }

    public boolean isFound() {
        return isFound;
    }
}
