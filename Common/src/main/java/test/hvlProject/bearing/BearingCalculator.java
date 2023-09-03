package test.hvlProject.bearing;

import test.hvlProject.world.Item;

public class BearingCalculator {

    public static BearingInfo calculateBearing(Item from, Item to) {
        double deltaX = to.getxCoordinate() - from.getxCoordinate();
        double deltaY = to.getyCoordinate() - from.getyCoordinate();
        double radian = Math.atan2(deltaY, deltaX);
        double bearingResult = 90 - Math.toDegrees(radian);
        if (bearingResult < 0) {
            bearingResult += 360;
        }
        BearingInfo bearingInfo = new BearingInfo(from);
        bearingInfo.setDescription(
                String.format("%s için hedefin kerterizi Y pozitif eksenden saat yönündeki açısı %,.2f", from.getItemName(), bearingResult)
        );
        bearingInfo.setTargetDegree(bearingResult);
        return bearingInfo;
    }
}
