# Guía de Uso: Sistema de Perfiles de Usuario

## Resumen de la Implementación

Se ha implementado un sistema de **Roles + Perfiles específicos** donde:
- **Roles**: Se usan para autenticación y autorización (permisos)
- **Perfiles**: Se usan para almacenar datos específicos de cada tipo de usuario

## Estructura de Base de Datos

### Tablas creadas:
- `users` - Datos comunes de todos los usuarios
- `roles` - Roles para permisos
- `user_roles` - Relación muchos a muchos entre usuarios y roles
- `student_profiles` - Datos específicos de estudiantes
- `professor_profiles` - Datos específicos de profesores
- `administrator_profiles` - Datos específicos de administradores
- `assistant_profiles` - Datos específicos de asistentes

## Endpoints Disponibles

### 1. Crear Estudiante
```http
POST /api/students/create
Content-Type: application/json

{
  "username": "juan.perez",
  "password": "password123",
  "email": "juan.perez@universidad.edu",
  "phone": "555-0001",
  "roleIds": [1],
  "studentCode": "EST-2024-001",
  "semester": 5,
  "major": "Derecho",
  "enrollmentDate": "2022-01-15",
  "university": "Universidad Nacional",
  "academicStatus": "ACTIVE"
}
```

**Respuesta:**
```json
{
  "id": 1,
  "username": "juan.perez",
  "email": "juan.perez@universidad.edu",
  "phone": "555-0001",
  "roles": [
    {
      "id": 1,
      "name": "ROLE_STUDENT",
      "spanishName": "Estudiante"
    }
  ],
  "studentProfile": {
    "id": 1,
    "studentCode": "EST-2024-001",
    "semester": 5,
    "major": "Derecho",
    "enrollmentDate": "2022-01-15",
    "university": "Universidad Nacional",
    "academicStatus": "ACTIVE"
  },
  "professorProfile": null,
  "administratorProfile": null,
  "assistantProfile": null
}
```

### 2. Crear Profesor
```http
POST /api/professors/create
Content-Type: application/json

{
  "username": "maria.lopez",
  "password": "password123",
  "email": "maria.lopez@universidad.edu",
  "phone": "555-0002",
  "roleIds": [2],
  "department": "Derecho Penal",
  "specialization": "Derecho Penal y Criminología",
  "officeNumber": "301-A",
  "hireDate": "2015-03-10",
  "title": "Dra.",
  "researchArea": "Derecho Penal Comparado",
  "employmentType": "FULL_TIME"
}
```

### 3. Obtener Estudiante por ID
```http
GET /api/students/{userId}
```

### 4. Obtener Profesor por ID
```http
GET /api/professors/{userId}
```

### 5. Actualizar Estudiante
```http
PUT /api/students/{userId}
Content-Type: application/json

{
  "username": "juan.perez",
  "email": "juan.perez@universidad.edu",
  "phone": "555-0001",
  "roleIds": [1],
  "studentCode": "EST-2024-001",
  "semester": 6,
  "major": "Derecho",
  "enrollmentDate": "2022-01-15",
  "university": "Universidad Nacional",
  "academicStatus": "ACTIVE"
}
```

### 6. Actualizar Profesor
```http
PUT /api/professors/{userId}
```

### 7. Endpoints Generales (UserController - sin perfil específico)
```http
POST /api/users/create      # Crear usuario básico sin perfil
GET /api/users/all           # Listar todos los usuarios (incluye perfiles)
GET /api/users/{id}          # Obtener usuario por ID (incluye perfiles)
PUT /api/users/{id}          # Actualizar usuario básico
DELETE /api/users/{id}       # Eliminar usuario (elimina perfil automáticamente)
```

## Cómo funciona

### 1. Separación de Responsabilidades
- **Roles** → Determinan QUÉ puede hacer el usuario (permisos)
- **Perfiles** → Almacenan DATOS específicos del tipo de usuario

### 2. Relaciones
- Un `User` puede tener múltiples `Roles` (ManyToMany)
- Un `User` puede tener máximo UN perfil de cada tipo (OneToOne)
- Los perfiles se eliminan automáticamente cuando se elimina el usuario (CASCADE)

### 3. Consultas Optimizadas
- Cuando consultas un estudiante via `/api/students/{id}`, obtienes el usuario + perfil de estudiante
- Cuando consultas todos los usuarios via `/api/users/all`, obtienes todos los usuarios con sus perfiles correspondientes

### 4. Ventajas
✅ **Sin campos NULL** - Cada perfil solo tiene datos relevantes
✅ **Flexible** - Un usuario puede ser profesor Y administrador (múltiples roles + perfiles)
✅ **Escalable** - Fácil agregar nuevos tipos de perfil
✅ **Integridad** - Las FK garantizan consistencia de datos

## Casos de Uso Avanzados

### Usuario con múltiples roles
```json
{
  "username": "carlos.admin",
  "password": "password123",
  "email": "carlos@universidad.edu",
  "phone": "555-0003",
  "roleIds": [2, 3],  // PROFESOR + ADMIN
  "department": "Derecho Civil",
  "specialization": "Derecho Civil y Comercial",
  "title": "Dr.",
  "employmentType": "FULL_TIME"
}
```

Este usuario puede:
- Crear su perfil de profesor en `/api/professors/create`
- Luego agregar perfil de administrador usando el UserService directamente
- Tener acceso a funcionalidades de ambos roles

## Próximos Pasos

Para completar la implementación, necesitas crear servicios y controladores para:
- `AdministratorService` / `AdministratorController`
- `AssistantService` / `AssistantController`

Ambos siguen el mismo patrón que `StudentService` y `ProfessorService`.

## Seguridad con Spring Security

Puedes usar `@PreAuthorize` para proteger endpoints:

```java
@PreAuthorize("hasRole('STUDENT')")
@GetMapping("/my-courses")
public ResponseEntity<?> getMyCourses(Authentication auth) {
    // Solo estudiantes pueden acceder
}

@PreAuthorize("hasRole('PROFESSOR')")
@PostMapping("/grade-assignment")
public ResponseEntity<?> gradeAssignment() {
    // Solo profesores pueden acceder
}

@PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR')")
@GetMapping("/reports")
public ResponseEntity<?> getReports() {
    // Admins y profesores pueden acceder
}
```
