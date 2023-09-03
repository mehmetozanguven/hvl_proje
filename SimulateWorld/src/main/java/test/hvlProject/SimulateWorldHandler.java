package test.hvlProject;

import com.fasterxml.jackson.core.type.TypeReference;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.hvlProject.core.KafkaItemProducer;
import test.hvlProject.core.KafkaMessageHandler;
import test.hvlProject.json.Mapper;
import test.hvlProject.topic.KafkaTopicName;
import test.hvlProject.world.Item;

public class SimulateWorldHandler implements KafkaMessageHandler {
    private final Logger log = LoggerFactory.getLogger(SimulateWorldHandler.class);

    private final KafkaItemProducer kafkaItemProducer;
    private final KafkaTopicName kafkaTopicName;

    public SimulateWorldHandler(KafkaItemProducer kafkaItemProducer, KafkaTopicName producerTopicName) {
        this.kafkaItemProducer = kafkaItemProducer;
        this.kafkaTopicName = producerTopicName;
    }

    @Override
    public void processMessage(String topicName, ConsumerRecord<String, String> message) {
        String jsonValue = message.value();
        try {
            Item itemInTheWorld = Mapper.getInstance().readValue(jsonValue, new TypeReference<Item>() {});
            kafkaItemProducer.produce(kafkaTopicName.topicName, Mapper.getInstance().writeValueAsString(itemInTheWorld));
        } catch (Exception ex) {
            log.error("Invalid value, doesn't match with item class, for the following json content : {}", jsonValue);
        }
    }
}
