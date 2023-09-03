package test.hvlProject;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.hvlProject.bearing.BearingInfo;
import test.hvlProject.core.KafkaConsumerItem;
import test.hvlProject.core.KafkaItemProducer;
import test.hvlProject.json.Mapper;
import test.hvlProject.topic.KafkaTopicName;
import test.hvlProject.world.Item;
import test.hvlProject.world.ItemType;

import java.util.*;

public class SensorTest {

    private MockConsumer<String, String> mockConsumer;
    private MockProducer<String, String> mockProducer;

    @BeforeEach
    void setup() {
        mockProducer = new MockProducer<>(true, new StringSerializer(), new StringSerializer());
        mockConsumer = new MockConsumer<>(OffsetResetStrategy.EARLIEST);
    }

    @Test
    void processMessage_ShouldProduceRecord_WhenReceivedTargetItem() throws Exception {
        Item sensorItem = new Item();
        sensorItem.setItemName("Sensör1");
        sensorItem.setItemType(ItemType.SENSOR);
        sensorItem.setCoordinates(-5, 1);

        String targetName = UUID.randomUUID().toString();
        Item targetItem = new Item();
        targetItem.setItemName(targetName);
        targetItem.setItemType(ItemType.TARGET);
        targetItem.setCoordinates(-1, 5);

        String topic = KafkaTopicName.RADAR_TOPIC.topicName;

        ConsumerRecord<String, String> targetRecord = new ConsumerRecord<>(
                topic,
                0,
                0L,
                "key",
                Mapper.getInstance().writeValueAsString(targetItem)
        );
        KafkaConsumerItem consumerItem = new KafkaConsumerItem(mockConsumer);

        TopicPartition topicPartition = new TopicPartition(topic, 0);

        mockConsumer.schedulePollTask(() -> {
            setPartition(mockConsumer, topicPartition);
            mockConsumer.addRecord(targetRecord);
        });
        mockConsumer.schedulePollTask(consumerItem::shutdown);

        KafkaItemProducer sendToMainFrame = new KafkaItemProducer(mockProducer);

        consumerItem.consumeAlways(topic, new SensorImplementation(sensorItem, sendToMainFrame, KafkaTopicName.RADAR_TOPIC));
        List<ProducerRecord<String, String>> history = mockProducer.history();
        Assertions.assertEquals(1, history.size());

        ProducerRecord<String, String> record = history.get(0);
        BearingInfo recordVal = Mapper.getInstance().readValue(record.value(), BearingInfo.class);
        Assertions.assertEquals("Sensör1", recordVal.getSensor().getItemName());
        Assertions.assertEquals(45, recordVal.getTargetDegree());
    }

    private void setPartition(MockConsumer<String, String> mockConsumer, TopicPartition topicPartition) {
        final Map<TopicPartition, Long> beginningOffsets = new HashMap<>();
        beginningOffsets.put(topicPartition, 0L);
        mockConsumer.rebalance(Collections.singletonList(topicPartition));
        mockConsumer.updateBeginningOffsets(beginningOffsets);
    }

}
