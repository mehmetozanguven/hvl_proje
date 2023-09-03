package test.hvlProject.core;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class KafkaItemProducer extends KafkaItem {
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final Producer<String, String> kafkaProducer;
    private final Logger log = LoggerFactory.getLogger(KafkaItemProducer.class);

    public KafkaItemProducer(Producer<String, String> producer) throws Exception {
        this.kafkaProducer = producer;
    }

    public Producer<String, String> getKafkaProducer() {
        return this.kafkaProducer;
    }

    @Override
    public void shutdown() throws Exception {
        closed.set(true);
        log.info("Shutting down producer {}", Thread.currentThread().getName());
        getKafkaProducer().close();
    }

    @Override
    public void consumeAlways(String topicName, KafkaMessageHandler callback) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void consumeAndProcessCallbacks(String topicName, List<KafkaMessageHandler> callbacks) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void produce(String topicName, String message) throws Exception {
        String key = UUID.randomUUID().toString();
        this.send(topicName, key, message);
    }

    protected void send(String topicName, String key, String message) throws Exception {
        ProducerRecord<String, String> producerRecord =
                new ProducerRecord<>(topicName, key, message);
        getKafkaProducer().send(producerRecord);
    }
}
