package test.hvlProject;

import com.fasterxml.jackson.core.type.TypeReference;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.hvlProject.bearing.BearingCalculator;
import test.hvlProject.bearing.BearingInfo;
import test.hvlProject.core.KafkaItemProducer;
import test.hvlProject.core.KafkaMessageHandler;
import test.hvlProject.json.Mapper;
import test.hvlProject.topic.KafkaTopicName;
import test.hvlProject.world.Item;
import test.hvlProject.world.ItemType;

public class SensorImplementation implements KafkaMessageHandler {
    private final Logger log = LoggerFactory.getLogger(SensorImplementation.class);
    private final Item sensorItem;
    private final KafkaItemProducer kafkaItemProducer;
    private final KafkaTopicName kafkaTopicName;

    public SensorImplementation(Item sensorItem, KafkaItemProducer kafkaItemProducer, KafkaTopicName kafkaTopicName) {
        this.sensorItem = sensorItem;
        this.kafkaItemProducer = kafkaItemProducer;
        this.kafkaTopicName = kafkaTopicName;
    }

    @Override
    public void processMessage(String topicName, ConsumerRecord<String, String> message) {
        String jsonValue = message.value();
        try {
            Item itemInTheWorld = Mapper.getInstance().readValue(jsonValue, new TypeReference<Item>() {});
            if (ItemType.SENSOR.equals(itemInTheWorld.getItemType())) {
                log.info("{} -- Don't need to calculate bearing between two sensors", sensorItem.getItemName());
                return;
            }
            BearingInfo bearingInfo = BearingCalculator.calculateBearing(sensorItem, itemInTheWorld);
            kafkaItemProducer.produce(kafkaTopicName.topicName, Mapper.getInstance().writeValueAsString(bearingInfo));
        } catch (Exception ex) {
            log.error("{} -- Invalid value, doesn't match with item class, for the following json content : {}", sensorItem.getItemName(), jsonValue);
        }
    }
}
