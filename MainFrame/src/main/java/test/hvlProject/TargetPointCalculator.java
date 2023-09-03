package test.hvlProject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.hvlProject.bearing.BearingInfo;
import test.hvlProject.world.Item;
import test.hvlProject.world.ItemType;

import java.util.Arrays;
import java.util.Objects;

public class TargetPointCalculator {
    private final Logger log = LoggerFactory.getLogger(TargetPointCalculator.class);

    private final BearingInfo[] bearingInfoArray;

    public TargetPointCalculator() {
        this.bearingInfoArray = new BearingInfo[2];
    }

    public void addInfoFromSensor(BearingInfo bearingInfo) {
        Item sensor = bearingInfo.getSensor();
        if (!ItemType.SENSOR.equals(sensor.getItemType())) {
            log.warn("Events from unknown sources");
            return;
        }

        bearingInfoArray[(sensor.hashCode()) % 2] = bearingInfo;
    }

    public Target tryToFindTargetPoint() {
        double nullCount = Arrays.stream(bearingInfoArray).filter(Objects::isNull).count();
        if (nullCount > 0) {
            return new Target(false, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        }

        // Calculate slopes (cotangents of angles)
        double[] slopes = new double[2];
        int index = 0;
        for (BearingInfo each : bearingInfoArray) {
            if (Objects.nonNull(each)) {
                slopes[index] = 1 / Math.tan(Math.toRadians(each.getTargetDegree()));
                index ++;
            }
        }

        // Calculate line equations (y = mx + b)
        double[] intercepts = new double[2];
        index = 0;
        for (BearingInfo each : bearingInfoArray) {
            if (Objects.nonNull(each)) {
                slopes[index] = 1 / Math.tan(Math.toRadians(each.getTargetDegree()));
                index ++;
            }
        }

        index = 0;
        for (BearingInfo each : bearingInfoArray) {
            if (Objects.nonNull(each)) {
                intercepts[index] = each.getSensor().getyCoordinate() - slopes[index] * each.getSensor().getxCoordinate();
                index ++;
            }
        }

        // Find the intersection point
        double xIntersection = (intercepts[1] - intercepts[0]) / (slopes[0] - slopes[1]);
        double yIntersection = slopes[0] * xIntersection + intercepts[0];
        if (Double.isNaN(xIntersection) || Double.isNaN(yIntersection)) {
            log.info("Target is not a number");
            return new Target(false, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        }
        return new Target(true, xIntersection, yIntersection);
    }
}
