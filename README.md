# TEPSUP_APACHE_KAFKA
Demostración de uso de Apache Kafka

## Instalación

```bash
# Clonar el repositorio
git clone https://github.com/aanampa/TEPSUP_APACHE_KAFKA.git

# Navegar al directorio del repositorio
cd TEPSUP_APACHE_KAFKA

# Iniciar servicios
docker-compose up -d

# Verificar que PostgreSQL esté listo
docker logs postgres-db

# Compilar API
mvn clean install

# Ejecutar la aplicación API
mvn spring-boot:run

```
## Estructura del proyecto


```
proyecto/
├── src/
├── tests/
├── docs/
└── README.md
```
## Ejemplo de Uso
```
# Listar estudiantes
curl http://localhost:8081/api/estudiantes

# Registrar estudiantes
curl -X POST http://localhost:8081/api/estudiantes \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Carlos",
    "apellido": "Pérez García",
    "dni": "12345678B",
    "email": "carlos.perez@test.com",
    "telefono": "666111222",
    "fechaNacimiento": "2000-05-15",
    "direccion": "Calle Test 123",
    "activo": true
  }'

# Listar mensajes
curl http://localhost:8081/api/logs
  ```
