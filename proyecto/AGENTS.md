# AGENTS.md - Proyecto BlackCode+Ticket

## Build, Lint & Test Commands

### Compilación y Ejecución
```bash
# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run

# Compilar sin ejecutar tests
mvn clean compile
```

### Tests
```bash
# Ejecutar todos los tests
mvn test

# Ejecutar un solo test
mvn test -Dtest=ProyectoApplicationTests

# Ejecutar tests con verbose
mvn test -X
```

---

## Code Style Guidelines

### Estructura del Proyecto
```
src/main/java/com/example/demo/
├── controller/      # REST Controllers
├── model/
│   ├── entidad/    # Entidades JPA
│   ├── repository/ # Repositorios
│   └── service/
│       ├── Interface/ # Interfaces de servicio
│       └── Class/     # Implementaciones
└── config/         # Configuración
```

### Convenciones de Código

#### Naming Conventions
- **Clases**: PascalCase (ej: `UsuarioController`, `ProductoService`)
- **Interfaces**: Prefijo "I" + PascalCase (ej: `IUsuarioService`)
- **Variables**: camelCase (ej: `idUsuario`, `passwordHash`)
- **Constantes**: UPPER_SNAKE_CASE
- **Paquetes**: lowercase (ej: `com.example.demo.model.entidad`)

#### Imports
- Ordenar alfabéticamente o usar "Organize Imports" del IDE
- Imports de Java antes que Spring
- Imports de Lombok al final

#### Anotaciones JPA
- Usar `@Entity`, `@Table(name = "NombreTabla")`
- Relaciones: `@ManyToOne`, `@OneToMany`, `@JoinColumn`
- Siempre especificar `nullable = false` cuando sea requerido

#### Servicios
- Implementar interfaz separada en paquete `Interface/`
- Usar `@Service` y `@RequiredArgsConstructor` de Lombok
- Métodos de lectura: `@Transactional(readOnly = true)`
- Métodos de escritura: `@Transactional`

#### Controladores REST
- Usar `@RestController` con `@RequestMapping("/api/recurso")`
- Métodos HTTP: `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`
- Usar `@Valid` en `@RequestBody` para validación
- Retornar `ResponseEntity<T>` con códigos HTTP apropiados:
  - `200 OK` - Operaciones exitosas
  - `201 Created` - Creación exitosa
  - `204 No Content` - Eliminación exitosa
  - `400 Bad Request` - Validación fallida
  - `404 Not Found` - Recurso no encontrado

#### Validación
- Usar anotaciones de `jakarta.validation.constraints`
- `@NotBlank`, `@NotNull`, `@Size`, `@Email`, `@Min`, `@Max`, `@Pattern`
- Mensajes en español: `message = "El campo es obligatorio"`

#### Entidades
- Usar Lombok: `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`
- Campos con valores por defecto inicializar en declaración
- Usar enums para estados definidos: `@Enumerated(EnumType.STRING)`

---

## Errores Conocidos a Evitar

### 1. CORS Duplicado
- **Problema**: CORS configurado en `application.properties` Y en `CorsConfig.java`
- **Solución**: Usar solo uno (recomendado: `application.properties`)

### 2. Relaciones Inconsistentes
- **Problema**: `Carrito.java` y `DetalleCarrito.java` usan `Long idUsuario`/`Long idProducto` en lugar de `@ManyToOne`
- **Solución**: Cambiar a relaciones JPA consistentes con el resto del proyecto

### 3. Manejo de Errores
- **Problema**: Servicios retornan `null` en lugar de lanzar excepciones
- **Solución**: Implementar manejo de excepciones con `@ControllerAdvice`

### 4. Eliminar sin Verificación
- **Problema**: `eliminar(id)` puede fallar si el ID no existe
- **Solución**: Verificar existencia antes de eliminar o capturar `DataIntegrityViolationException`

### 5. Seguridad
- **Problema**: `passwordHash` en texto plano
- **Solución**: Implementar cifrado con `BCryptPasswordEncoder`

### 6. Paginación
- **Problema**: Endpoints `listar()` retornan todos los registros
- **Solución**: Usar `Pageable` y `Page<T>` para grandes volúmenes

---

## Buenas Prácticas

### Transacciones
```java
@Service
@RequiredArgsConstructor
public class ProductoService implements IProductoService {
    private final IProductoRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listar() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public Producto guardar(Producto producto) {
        return repository.save(producto);
    }
}
```

### Controladores
```java
@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {
    private final IProductoService service;

    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @PostMapping
    public ResponseEntity<Producto> guardar(@Valid @RequestBody Producto producto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(producto));
    }
}
```

---

## Notas Adicionales

- Java 17 con Spring Boot 3.5.7
- MySQL 8 como base de datos
- Lombok para reducción de boilerplate
- Validación con Jakarta Bean Validation
