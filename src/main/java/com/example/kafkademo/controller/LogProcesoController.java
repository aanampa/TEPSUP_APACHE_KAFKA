package com.example.kafkademo.controller;

import com.example.kafkademo.entity.LogProceso;
import com.example.kafkademo.producer.KafkaProducerService;
import com.example.kafkademo.service.LogProcesoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/logs")
public class LogProcesoController {
    
    private final KafkaProducerService producerService;
    private final LogProcesoService logProcesoService;
    
    public LogProcesoController(KafkaProducerService producerService, 
                                LogProcesoService logProcesoService) {
        this.producerService = producerService;
        this.logProcesoService = logProcesoService;
    }
    
    /**
     * Enviar mensaje a Kafka
     * POST /api/logs/send?mensaje=texto
     */
    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendMessage(@RequestParam String mensaje) {
        producerService.sendMessage(mensaje);
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Mensaje enviado a Kafka: " + mensaje);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtener todos los logs
     * GET /api/logs
     */
    @GetMapping
    public ResponseEntity<List<LogProceso>> getAllLogs() {
        return ResponseEntity.ok(logProcesoService.obtenerTodosLosLogs());
    }
    
    /**
     * Obtener logs recientes
     * GET /api/logs/recientes
     */
    @GetMapping("/recientes")
    public ResponseEntity<List<LogProceso>> getRecentLogs() {
        return ResponseEntity.ok(logProcesoService.obtenerLogsRecientes());
    }
    
    /**
     * Buscar logs
     * GET /api/logs/buscar?keyword=texto
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<LogProceso>> searchLogs(@RequestParam String keyword) {
        return ResponseEntity.ok(logProcesoService.buscarLogs(keyword));
    }
    
    /**
     * Obtener estadísticas
     * GET /api/logs/estadisticas
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalLogs", logProcesoService.contarTotalLogs());
        
        return ResponseEntity.ok(stats);
    }
}
