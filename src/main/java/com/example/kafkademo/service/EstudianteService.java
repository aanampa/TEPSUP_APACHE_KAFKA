package com.example.kafkademo.service;

import com.example.kafkademo.dto.EstudianteDTO;
import com.example.kafkademo.entity.Estudiante;
import com.example.kafkademo.producer.KafkaProducerService;
import com.example.kafkademo.repository.EstudianteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EstudianteService {
    
    private static final Logger logger = LoggerFactory.getLogger(EstudianteService.class);
    
    private final EstudianteRepository estudianteRepository;
    private final KafkaProducerService kafkaProducerService;
    
    public EstudianteService(EstudianteRepository estudianteRepository, 
                            KafkaProducerService kafkaProducerService) {
        this.estudianteRepository = estudianteRepository;
        this.kafkaProducerService = kafkaProducerService;
    }
    
    @Transactional
    public Estudiante registrarEstudiante(EstudianteDTO estudianteDTO) {
        logger.info("Iniciando registro de estudiante con DNI: {}", estudianteDTO.getDni());
        
        // Validar que no exista el DNI
        if (estudianteRepository.existsByDni(estudianteDTO.getDni())) {
            logger.error("El DNI {} ya está registrado", estudianteDTO.getDni());
            throw new RuntimeException("El DNI " + estudianteDTO.getDni() + " ya está registrado");
        }
        
        // Validar que no exista el email
        if (estudianteRepository.existsByEmail(estudianteDTO.getEmail())) {
            logger.error("El email {} ya está registrado", estudianteDTO.getEmail());
            throw new RuntimeException("El email " + estudianteDTO.getEmail() + " ya está registrado");
        }
        
        // Crear entidad Estudiante
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre(estudianteDTO.getNombre());
        estudiante.setApellido(estudianteDTO.getApellido());
        estudiante.setDni(estudianteDTO.getDni());
        estudiante.setEmail(estudianteDTO.getEmail());
        estudiante.setTelefono(estudianteDTO.getTelefono());
        estudiante.setFechaNacimiento(estudianteDTO.getFechaNacimiento());
        estudiante.setDireccion(estudianteDTO.getDireccion());
        estudiante.setActivo(estudianteDTO.getActivo() != null ? estudianteDTO.getActivo() : true);
        
        // Guardar en base de datos
        Estudiante estudianteGuardado = estudianteRepository.save(estudiante);
        logger.info("Estudiante guardado en BD con ID: {}", estudianteGuardado.getEstudianteId());
        
        // Enviar mensaje a Kafka
        String mensaje = String.format("Estudiante %s registrado", estudianteDTO.getDni());
        kafkaProducerService.sendMessage(mensaje);
        logger.info("Mensaje enviado a Kafka: {}", mensaje);
        
        return estudianteGuardado;
    }
    
    public List<Estudiante> obtenerTodosLosEstudiantes() {
        return estudianteRepository.findAll();
    }
    
    public List<Estudiante> obtenerEstudiantesActivos() {
        return estudianteRepository.findByActivo(true);
    }
    
    public List<Estudiante> obtenerEstudiantesInactivos() {
        return estudianteRepository.findByActivo(false);
    }
    
    public Optional<Estudiante> buscarPorDni(String dni) {
        return estudianteRepository.findByDni(dni);
    }
    
    public Optional<Estudiante> buscarPorEmail(String email) {
        return estudianteRepository.findByEmail(email);
    }
    
    public List<Estudiante> buscarPorNombre(String nombre) {
        return estudianteRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    public List<Estudiante> buscarPorApellido(String apellido) {
        return estudianteRepository.findByApellidoContainingIgnoreCase(apellido);
    }
    
    public List<Estudiante> buscarPorNombreOApellido(String termino) {
        return estudianteRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
            termino, termino);
    }
    
    public List<Estudiante> obtenerEstudiantesRecientes() {
        return estudianteRepository.findTop10ByOrderByFechaRegistroDesc();
    }
    
    @Transactional
    public Estudiante actualizarEstudiante(String dni, EstudianteDTO estudianteDTO) {
        Estudiante estudiante = estudianteRepository.findByDni(dni)
            .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con DNI: " + dni));
        
        // Validar email si cambió
        if (!estudiante.getEmail().equals(estudianteDTO.getEmail()) && 
            estudianteRepository.existsByEmail(estudianteDTO.getEmail())) {
            throw new RuntimeException("El email " + estudianteDTO.getEmail() + " ya está registrado");
        }
        
        estudiante.setNombre(estudianteDTO.getNombre());
        estudiante.setApellido(estudianteDTO.getApellido());
        estudiante.setEmail(estudianteDTO.getEmail());
        estudiante.setTelefono(estudianteDTO.getTelefono());
        estudiante.setFechaNacimiento(estudianteDTO.getFechaNacimiento());
        estudiante.setDireccion(estudianteDTO.getDireccion());
        if (estudianteDTO.getActivo() != null) {
            estudiante.setActivo(estudianteDTO.getActivo());
        }
        
        Estudiante estudianteActualizado = estudianteRepository.save(estudiante);
        
        // Enviar mensaje a Kafka
        String mensaje = String.format("Estudiante %s actualizado", dni);
        kafkaProducerService.sendMessage(mensaje);
        
        return estudianteActualizado;
    }
    
    @Transactional
    public void eliminarEstudiante(String dni) {
        Estudiante estudiante = estudianteRepository.findByDni(dni)
            .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con DNI: " + dni));
        
        estudianteRepository.delete(estudiante);
        
        // Enviar mensaje a Kafka
        String mensaje = String.format("Estudiante %s eliminado", dni);
        kafkaProducerService.sendMessage(mensaje);
        
        logger.info("Estudiante con DNI {} eliminado", dni);
    }
    
    @Transactional
    public Estudiante desactivarEstudiante(String dni) {
        Estudiante estudiante = estudianteRepository.findByDni(dni)
            .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con DNI: " + dni));
        
        estudiante.setActivo(false);
        Estudiante estudianteDesactivado = estudianteRepository.save(estudiante);
        
        // Enviar mensaje a Kafka
        String mensaje = String.format("Estudiante %s desactivado", dni);
        kafkaProducerService.sendMessage(mensaje);
        
        logger.info("Estudiante con DNI {} desactivado", dni);
        return estudianteDesactivado;
    }
    
    @Transactional
    public Estudiante activarEstudiante(String dni) {
        Estudiante estudiante = estudianteRepository.findByDni(dni)
            .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con DNI: " + dni));
        
        estudiante.setActivo(true);
        Estudiante estudianteActivado = estudianteRepository.save(estudiante);
        
        // Enviar mensaje a Kafka
        String mensaje = String.format("Estudiante %s activado", dni);
        kafkaProducerService.sendMessage(mensaje);
        
        logger.info("Estudiante con DNI {} activado", dni);
        return estudianteActivado;
    }
    
    public Long contarEstudiantesActivos() {
        return estudianteRepository.countEstudiantesActivos();
    }
}
