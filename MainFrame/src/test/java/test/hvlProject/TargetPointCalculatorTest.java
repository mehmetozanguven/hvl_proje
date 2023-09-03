package test.hvlProject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.hvlProject.bearing.BearingCalculator;
import test.hvlProject.bearing.BearingInfo;
import test.hvlProject.world.Item;
import test.hvlProject.world.ItemType;

class TargetPointCalculatorTest {
    private TargetPointCalculator targetPointCalculator;

    @BeforeEach
    void setup() {
        targetPointCalculator = new TargetPointCalculator();
    }

    @Test
    void testTargetPointCalculator() {
        int[] sensorXCoordinates = {-5, 5, 3, 100, 987};
        int[] sensorYCoordinates = {1, -1, 2, -50, 542};
        Item target = new Item();
        target.setItemType(ItemType.TARGET);
        target.setCoordinates(-1, 5);

        for (int index = 0; index < sensorXCoordinates.length; index++) {
            int sensorX = sensorXCoordinates[index];
            int sensorY = sensorYCoordinates[index];
            Item sensorItem = new Item();
            sensorItem.setItemType(ItemType.SENSOR);
            sensorItem.setCoordinates(sensorX, sensorY);

            BearingInfo bearingInfo = BearingCalculator.calculateBearing(sensorItem, target);
            targetPointCalculator.addInfoFromSensor(bearingInfo);
        }
        Target foundTarget = targetPointCalculator.tryToFindTargetPoint();
        Assertions.assertTrue(foundTarget.isFound());
        Assertions.assertEquals(target.getxCoordinate(), Math.round(foundTarget.getxCoordinate()));
        Assertions.assertEquals(target.getyCoordinate(), Math.round(foundTarget.getyCoordinate()));
    }
}