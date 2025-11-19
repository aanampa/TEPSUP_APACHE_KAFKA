package com.example.kafkademo.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    @Value("${kafka.topic.name}")
    private String topicName;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        logger.info("Enviando mensaje: {}", message);
        
        CompletableFuture<SendResult<String, String>> future = 
            kafkaTemplate.send(topicName, message);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Mensaje enviado exitosamente: [{}] con offset: [{}]",
                        message, result.getRecordMetadata().offset());
            } else {
                logger.error("Error al enviar mensaje: [{}]", message, ex);
            }
        });
    }

    public void sendMessageWithKey(String key, String message) {
        logger.info("Enviando mensaje con key: {} - mensaje: {}", key, message);
        
        CompletableFuture<SendResult<String, String>> future = 
            kafkaTemplate.send(topicName, key, message);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Mensaje con key enviado exitosamente: [{}] - [{}] con offset: [{}]",
                        key, message, result.getRecordMetadata().offset());
            } else {
                logger.error("Error al enviar mensaje con key: [{}] - [{}]", key, message, ex);
            }
        });
    }
}
