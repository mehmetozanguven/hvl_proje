package test.hvlProject;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import test.hvlProject.core.KafkaConstant;
import test.hvlProject.core.KafkaConsumerItem;
import test.hvlProject.core.KafkaItemProducer;
import test.hvlProject.core.KafkaMessageHandler;
import test.hvlProject.topic.KafkaTopicName;
import test.hvlProject.world.Item;
import test.hvlProject.world.ItemType;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SensorApp {

    public static void main( String[] args ) throws Exception {
        final String groupId = "Sensor";
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstant.BOOTSTRAP_SERVERS);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        KafkaConsumerItem kafkaConsumerItem = new KafkaConsumerItem(new KafkaConsumer<>(properties));

        Properties producerProperties = new Properties();
        producerProperties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstant.BOOTSTRAP_SERVERS);
        producerProperties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProperties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProperties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "1");
        KafkaItemProducer sendToMainFrame = new KafkaItemProducer(new KafkaProducer<>(producerProperties));

        Item sensor1 = new Item();
        sensor1.setCoordinates(-5, 1);
        sensor1.setItemType(ItemType.SENSOR);
        sensor1.setItemName("Sensör1");

        Item sensor2= new Item();
        sensor2.setCoordinates(5, -1);
        sensor2.setItemType(ItemType.SENSOR);
        sensor2.setItemName("Sensör2");

        List<Item> sensorInfoList = List.of(sensor1, sensor2);
        List<KafkaMessageHandler> allSensors = new ArrayList<>();

        sensorInfoList.forEach(sensor ->  allSensors.add(new SensorImplementation(sensor, sendToMainFrame, KafkaTopicName.SEND_TARGET_TO_MAINFRAME)));

        kafkaConsumerItem.consumeAndProcessCallbacks(KafkaTopicName.RADAR_TOPIC.topicName,  allSensors);
    }
}
