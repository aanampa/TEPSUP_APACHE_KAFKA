package com.example.kafkademo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudianteDTO {
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String telefono;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;
    
    private String direccion;
    private Boolean activo;
}
