package com.example.kafkademo.repository;

import com.example.kafkademo.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    
    // Buscar estudiante por DNI
    Optional<Estudiante> findByDni(String dni);
    
    // Buscar estudiante por email
    Optional<Estudiante> findByEmail(String email);
    
    // Verificar si existe un estudiante con ese DNI
    boolean existsByDni(String dni);
    
    // Verificar si existe un estudiante con ese email
    boolean existsByEmail(String email);
    
    // Buscar estudiantes por nombre
    List<Estudiante> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar estudiantes por apellido
    List<Estudiante> findByApellidoContainingIgnoreCase(String apellido);
    
    // Buscar estudiantes activos
    List<Estudiante> findByActivo(Boolean activo);
    
    // Buscar estudiantes por nombre o apellido
    List<Estudiante> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
        String nombre, String apellido);
    
    // Contar estudiantes activos
    @Query("SELECT COUNT(e) FROM Estudiante e WHERE e.activo = true")
    Long countEstudiantesActivos();
    
    // Obtener los últimos N estudiantes registrados
    List<Estudiante> findTop10ByOrderByFechaRegistroDesc();
}
