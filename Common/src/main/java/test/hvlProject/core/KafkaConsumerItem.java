package test.hvlProject.core;


import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class KafkaConsumerItem extends KafkaItem {
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerItem.class);

    private final Consumer<String, String> kafkaConsumer;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    /**
     * Add shutdown hook to close kafka consumer gracefully
     *
     * @throws Exception
     */
    public KafkaConsumerItem(Consumer<String, String> kafkaConsumer) throws Exception {
        this.kafkaConsumer = kafkaConsumer;
    }

    public Consumer<String, String> getKafkaConsumer() {
        return kafkaConsumer;
    }

    @Override
    public void shutdown() {
        closed.set(true);
        log.info("Shutting down consumer");
        getKafkaConsumer().wakeup();
    }

    @Override
    public void produce(String topicName, String message) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void consumeAndProcessCallbacks(String topicName, List<KafkaMessageHandler> callbacks) throws Exception {
        try {
            getKafkaConsumer().subscribe(List.of(topicName));
            while (!closed.get()) {
                ConsumerRecords<String, String> records =
                        getKafkaConsumer().poll(Duration.ofMillis(5000));
                for (ConsumerRecord<String, String> record : records) {
                    callbacks.forEach((callback) -> {
                        callback.processMessage(record.topic(), record);
                    });
                }
            }
        } catch (WakeupException e) {
            // Ignore exception if closing
            if (!closed.get()) throw e;
        }
    }

    @Override
    public void consumeAlways(String topicName, KafkaMessageHandler callback) throws Exception {
        try {
            getKafkaConsumer().subscribe(List.of(topicName));
            while (!closed.get()) {
                ConsumerRecords<String, String> records =
                        getKafkaConsumer().poll(Duration.ofMillis(5000));
                for (ConsumerRecord<String, String> record : records) {
                    callback.processMessage(record.topic(), record);
                }
            }
        } catch (WakeupException e) {
            // Ignore exception if closing
            if (!closed.get()) throw e;
        }
    }
}
