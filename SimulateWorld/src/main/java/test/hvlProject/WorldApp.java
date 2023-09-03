package test.hvlProject;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.hvlProject.core.KafkaConstant;
import test.hvlProject.core.KafkaConsumerItem;
import test.hvlProject.core.KafkaItemProducer;
import test.hvlProject.json.Mapper;
import test.hvlProject.topic.KafkaTopicName;
import test.hvlProject.world.Item;
import test.hvlProject.world.ItemType;

import java.util.Properties;
import java.util.UUID;

/**
 * Hello world!
 *
 */
public class WorldApp
{
    private static final Logger log = LoggerFactory.getLogger(WorldApp.class);

    public static void main( String[] args ) throws Exception {
        final String groupId = "SimulateWord";

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
        KafkaItemProducer produceNewItem = new KafkaItemProducer(new KafkaProducer<>(producerProperties));

        kafkaConsumerItem.consumeAlways(KafkaTopicName.NEW_ITEM_EVENT.topicName,  new SimulateWorldHandler(produceNewItem, KafkaTopicName.RADAR_TOPIC));
    }
}
