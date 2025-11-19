package com.example.kafkademo.repository;

import com.example.kafkademo.entity.LogProceso;
//import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogProcesoRepository extends JpaRepository<LogProceso, Long> {
    
    // Buscar logs en un rango de fechas
    List<LogProceso> findByFechaRegistroBetween(LocalDateTime start, LocalDateTime end);
    
    // Buscar logs que contengan un texto
    List<LogProceso> findByContenidoContainingIgnoreCase(String contenido);
    
    // Obtener los últimos 10 logs
    List<LogProceso> findTop10ByOrderByFechaRegistroDesc();
    
    // Contar total de logs
    @Query("SELECT COUNT(l) FROM LogProceso l")
    Long countTotalLogs();
    
    // Obtener todos los logs ordenados por fecha descendente
    List<LogProceso> findAllByOrderByFechaRegistroDesc();
}
