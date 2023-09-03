package test.hvlProject.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class KafkaItem {
    private static final Logger log = LoggerFactory.getLogger(KafkaItem.class);

    /**
     * Add shutdown hook to close kafka consumer gracefully
     * @throws Exception
     */
    public KafkaItem() throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    shutdown();
                    log.info("Shutdown hook has been called");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public abstract void shutdown() throws Exception;

    public abstract void consumeAlways(String topicName, KafkaMessageHandler callback) throws Exception;

    public abstract void consumeAndProcessCallbacks(String topicName, List<KafkaMessageHandler> callbacks) throws Exception;

    public abstract void produce(String topicName, String message) throws Exception;
}
