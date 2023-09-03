package test.hvlProject.topic;

public enum KafkaTopicName {
    NEW_ITEM_EVENT("new-item-event"), RADAR_TOPIC("radar-event"), SEND_TARGET_TO_MAINFRAME("send-target-to-mainframe-event");
    public final String topicName;

    KafkaTopicName(String topicName) {
        this.topicName = topicName;
    }
}
