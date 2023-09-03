package test.hvlProject.core;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * Handle the incoming messages from kafka
 */
public interface KafkaMessageHandler {
    void processMessage(String topicName, ConsumerRecord<String, String> message);
}
