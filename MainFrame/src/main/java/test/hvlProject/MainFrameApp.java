package test.hvlProject;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import test.hvlProject.core.KafkaConstant;
import test.hvlProject.core.KafkaConsumerItem;
import test.hvlProject.topic.KafkaTopicName;

import java.util.Properties;

public class MainFrameApp {
    public static void main( String[] args ) throws Exception {
        final String groupId = "MainFrame";
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstant.BOOTSTRAP_SERVERS);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        KafkaConsumerItem kafkaConsumerItem = new KafkaConsumerItem(new KafkaConsumer<>(properties));

        kafkaConsumerItem.consumeAlways(KafkaTopicName.SEND_TARGET_TO_MAINFRAME.topicName, new MainFrameImplementation());
    }
}
