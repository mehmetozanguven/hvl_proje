package test.hvlProject;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.hvlProject.bearing.BearingInfo;
import test.hvlProject.core.KafkaMessageHandler;
import test.hvlProject.json.Mapper;

public class MainFrameImplementation implements KafkaMessageHandler {
    private final Logger log = LoggerFactory.getLogger(MainFrameImplementation.class);
    private final TargetPointCalculator targetPointCalculator;

    public MainFrameImplementation() {
        targetPointCalculator = new TargetPointCalculator();

    }

    @Override
    public void processMessage(String topicName, ConsumerRecord<String, String> message) {
        String jsonValue = message.value();
        try {
            BearingInfo bearingInfo = Mapper.getInstance().readValue(jsonValue, new TypeReference<BearingInfo>() {});
            log.info("{}", bearingInfo);
            targetPointCalculator.addInfoFromSensor(bearingInfo);
            Target target = targetPointCalculator.tryToFindTargetPoint();
            if (target.isFound()) {
                log.info("Hedefin tahmini noktası ( {}, {} )", target.getxCoordinate(), target.getyCoordinate());
            }
        } catch (Exception ex) {
            log.error("Invalid value, doesn't match with bearing class, for the following json content : {}", jsonValue);
        }
    }
}
