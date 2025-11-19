package com.example.kafkademo.service;

import com.example.kafkademo.entity.LogProceso;
import com.example.kafkademo.repository.LogProcesoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogProcesoService {
    
    private static final Logger logger = LoggerFactory.getLogger(LogProcesoService.class);
    
    private final LogProcesoRepository logProcesoRepository;
    
    public LogProcesoService(LogProcesoRepository logProcesoRepository) {
        this.logProcesoRepository = logProcesoRepository;
    }
    
    @Transactional
    public LogProceso guardarLog(String contenido) {
        LogProceso log = new LogProceso();
        log.setContenido(contenido);
        
        LogProceso logGuardado = logProcesoRepository.save(log);
        logger.info("Log guardado en BD con ID: {}", logGuardado.getIdLogProceso());
        
        return logGuardado;
    }
    
    public List<LogProceso> obtenerTodosLosLogs() {
        return logProcesoRepository.findAllByOrderByFechaRegistroDesc();
    }
    
    public List<LogProceso> obtenerLogsRecientes() {
        return logProcesoRepository.findTop10ByOrderByFechaRegistroDesc();
    }
    
    public List<LogProceso> obtenerLogsRecientes(int cantidad) {
        Pageable pageable = PageRequest.of(0, cantidad, Sort.by(Sort.Direction.DESC, "fechaRegistro"));
        return logProcesoRepository.findAll(pageable).getContent();
    }
    
    public List<LogProceso> buscarLogs(String keyword) {
        return logProcesoRepository.findByContenidoContainingIgnoreCase(keyword);
    }
    
    public Long contarTotalLogs() {
        return logProcesoRepository.countTotalLogs();
    }
    
    public List<LogProceso> obtenerLogsPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return logProcesoRepository.findByFechaRegistroBetween(inicio, fin);
    }
}
