package com.example.kafkademo.consumer;

import com.example.kafkademo.service.LogProcesoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
    
    private final LogProcesoService logProcesoService;
    
    public KafkaConsumerService(LogProcesoService logProcesoService) {
        this.logProcesoService = logProcesoService;
    }
    
    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message) {
        try {
            logger.info("📨 Mensaje recibido de Kafka: {}", message);
            
            // Guardar el mensaje en la tabla log_proceso
            logProcesoService.guardarLog(message);
            
            logger.info("✅ Mensaje procesado y guardado exitosamente en log_proceso");
            
        } catch (Exception e) {
            logger.error("❌ Error procesando mensaje: {}", message, e);
        }
    }
}
