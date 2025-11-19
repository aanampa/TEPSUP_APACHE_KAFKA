package com.example.kafkademo.controller;

import com.example.kafkademo.dto.EstudianteDTO;
import com.example.kafkademo.entity.Estudiante;
import com.example.kafkademo.service.EstudianteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {
    
    private final EstudianteService estudianteService;
    
    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }
    
    /**
     * Registrar un nuevo estudiante
     * POST /api/estudiantes
     */
    @PostMapping
    public ResponseEntity<?> registrarEstudiante(@RequestBody EstudianteDTO estudianteDTO) {
        try {
            // Validaciones básicas
            if (estudianteDTO.getDni() == null || estudianteDTO.getDni().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(crearRespuestaError("El DNI es obligatorio"));
            }
            
            if (estudianteDTO.getNombre() == null || estudianteDTO.getNombre().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(crearRespuestaError("El nombre es obligatorio"));
            }
            
            if (estudianteDTO.getApellido() == null || estudianteDTO.getApellido().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(crearRespuestaError("El apellido es obligatorio"));
            }
            
            if (estudianteDTO.getEmail() == null || estudianteDTO.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(crearRespuestaError("El email es obligatorio"));
            }
            
            if (estudianteDTO.getFechaNacimiento() == null) {
                return ResponseEntity.badRequest()
                    .body(crearRespuestaError("La fecha de nacimiento es obligatoria"));
            }
            
            Estudiante estudiante = estudianteService.registrarEstudiante(estudianteDTO);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Estudiante registrado exitosamente");
            response.put("data", estudiante);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error al registrar estudiante: " + e.getMessage()));
        }
    }
    
    /**
     * Obtener todos los estudiantes
     * GET /api/estudiantes
     */
    @GetMapping
    public ResponseEntity<List<Estudiante>> obtenerTodosLosEstudiantes() {
        return ResponseEntity.ok(estudianteService.obtenerTodosLosEstudiantes());
    }
    
    /**
     * Obtener estudiantes activos
     * GET /api/estudiantes/activos
     */
    @GetMapping("/activos")
    public ResponseEntity<List<Estudiante>> obtenerEstudiantesActivos() {
        return ResponseEntity.ok(estudianteService.obtenerEstudiantesActivos());
    }
    
    /**
     * Obtener estudiantes inactivos
     * GET /api/estudiantes/inactivos
     */
    @GetMapping("/inactivos")
    public ResponseEntity<List<Estudiante>> obtenerEstudiantesInactivos() {
        return ResponseEntity.ok(estudianteService.obtenerEstudiantesInactivos());
    }
    
    /**
     * Obtener estudiantes recientes
     * GET /api/estudiantes/recientes
     */
    @GetMapping("/recientes")
    public ResponseEntity<List<Estudiante>> obtenerEstudiantesRecientes() {
        return ResponseEntity.ok(estudianteService.obtenerEstudiantesRecientes());
    }
    
    /**
     * Buscar estudiante por DNI
     * GET /api/estudiantes/dni/{dni}
     */
    @GetMapping("/dni/{dni}")
    public ResponseEntity<?> buscarPorDni(@PathVariable String dni) {
        return estudianteService.buscarPorDni(dni)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Buscar estudiante por email
     * GET /api/estudiantes/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<?> buscarPorEmail(@PathVariable String email) {
        return estudianteService.buscarPorEmail(email)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Buscar estudiantes por nombre
     * GET /api/estudiantes/buscar/nombre?q=Juan
     */
    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<Estudiante>> buscarPorNombre(@RequestParam String q) {
        return ResponseEntity.ok(estudianteService.buscarPorNombre(q));
    }
    
    /**
     * Buscar estudiantes por apellido
     * GET /api/estudiantes/buscar/apellido?q=Perez
     */
    @GetMapping("/buscar/apellido")
    public ResponseEntity<List<Estudiante>> buscarPorApellido(@RequestParam String q) {
        return ResponseEntity.ok(estudianteService.buscarPorApellido(q));
    }
    
    /**
     * Buscar estudiantes por nombre o apellido
     * GET /api/estudiantes/buscar?q=Juan
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Estudiante>> buscarPorNombreOApellido(@RequestParam String q) {
        return ResponseEntity.ok(estudianteService.buscarPorNombreOApellido(q));
    }
    
    /**
     * Actualizar un estudiante
     * PUT /api/estudiantes/{dni}
     */
    @PutMapping("/{dni}")
    public ResponseEntity<?> actualizarEstudiante(
            @PathVariable String dni,
            @RequestBody EstudianteDTO estudianteDTO) {
        try {
            Estudiante estudianteActualizado = estudianteService.actualizarEstudiante(dni, estudianteDTO);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Estudiante actualizado exitosamente");
            response.put("data", estudianteActualizado);
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error al actualizar estudiante: " + e.getMessage()));
        }
    }
    
    /**
     * Eliminar un estudiante
     * DELETE /api/estudiantes/{dni}
     */
    @DeleteMapping("/{dni}")
    public ResponseEntity<?> eliminarEstudiante(@PathVariable String dni) {
        try {
            estudianteService.eliminarEstudiante(dni);
            
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Estudiante eliminado exitosamente");
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error al eliminar estudiante: " + e.getMessage()));
        }
    }
    
    /**
     * Desactivar un estudiante
     * PATCH /api/estudiantes/{dni}/desactivar
     */
    @PatchMapping("/{dni}/desactivar")
    public ResponseEntity<?> desactivarEstudiante(@PathVariable String dni) {
        try {
            Estudiante estudiante = estudianteService.desactivarEstudiante(dni);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Estudiante desactivado exitosamente");
            response.put("data", estudiante);
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error al desactivar estudiante: " + e.getMessage()));
        }
    }
    
    /**
     * Activar un estudiante
     * PATCH /api/estudiantes/{dni}/activar
     */
    @PatchMapping("/{dni}/activar")
    public ResponseEntity<?> activarEstudiante(@PathVariable String dni) {
        try {
            Estudiante estudiante = estudianteService.activarEstudiante(dni);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Estudiante activado exitosamente");
            response.put("data", estudiante);
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error al activar estudiante: " + e.getMessage()));
        }
    }
    
    /**
     * Obtener estadísticas
     * GET /api/estudiantes/estadisticas
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalEstudiantes", estudianteService.obtenerTodosLosEstudiantes().size());
        stats.put("estudiantesActivos", estudianteService.contarEstudiantesActivos());
        stats.put("estudiantesInactivos", 
            estudianteService.obtenerTodosLosEstudiantes().size() - estudianteService.contarEstudiantesActivos());
        
        return ResponseEntity.ok(stats);
    }
    
    // Método auxiliar para crear respuestas de error
    private Map<String, String> crearRespuestaError(String mensaje) {
        Map<String, String> error = new HashMap<>();
        error.put("status", "error");
        error.put("message", mensaje);
        return error;
    }
}
