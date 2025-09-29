# GestorProducto

GestorProducto es una aplicación web desarrollada en **Spring Boot** con soporte reactivo usando **R2DBC** y base de datos **H2** en memoria. El proyecto permite gestionar productos de una tienda, consultar su stock y precio, y ofrece integración con una API externa de tipo de cambio.

---

## Características

- Gestión de productos: listar, crear, actualizar y eliminar.
- Base de datos reactiva en memoria (H2) para pruebas rápidas.
- Exposición de endpoints REST reactivos con `Mono` y `Flux`.
- Integración con API externa de cambio de moneda (`https://api.fastforex.io`).
- Inicialización automática de datos mediante `schema.sql` y `data.sql`.
- Consola H2 habilitada para desarrollo y pruebas (`/h2-console`).

---

## Tecnologías

- **Backend:** Java 17, Spring Boot, Spring WebFlux
- **Base de datos:** H2 (reactiva con R2DBC)
- **Lombok** para generación automática de getters, setters y constructores
- **Build Tool:** Maven
- **Control de versiones:** Git, GitHub

---

## Requisitos

- Java 17 o superior
- Maven 3.6+
- Git

---

## Configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/MathRandox/Product-Project.git
cd Product-Project
```

## Configuración de propiedades

El archivo `application.yml` contiene la configuración de la aplicación:

```yaml
spring:
  application:
    name: gestor-producto
  r2dbc:
    url: r2dbc:h2:mem:///gestorproducto;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    username: sa
    password:
  sql:
    init:
      mode: always
      schema-locations: classpath:schema.sql
      data-locations: classpath:data.sql
  h2:
    console:
      enabled: true
      path: /h2-console

server:
  port: 8080

external:
  currency:
    url: https://api.fastforex.io
    base: PEN
    api-key: f181db87c0-41e70636cc-t3bgfk
```
## Inicialización de la base de datos

- `schema.sql` crea la tabla `products`:

```sql
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(19,2) NOT NULL,
    stock INT NOT NULL
);
```
- `data.sql` crea la tabla `products`:
  ```sql
  INSERT INTO products (name, price, stock) VALUES ('Piano Digital', 1200.00, 2);
  INSERT INTO products (name, price, stock) VALUES ('Violin Clasico', 750.00, 5);
  INSERT INTO products (name, price, stock) VALUES ('Guitarra Acustica', 500.00, 10);
  -- ... (continúa con todos los demás productos)
  INSERT INTO products (name, price, stock) VALUES ('Mango Kent 1kg', 16.00, 35);
  ```
## Endpoints principales

| Método | URL                        | Descripción                       |
|--------|----------------------------|-----------------------------------|
| GET    | `/products`               | Listar todos los productos       |
| GET    | `/products/{id}`          | Obtener producto por ID          |
| POST   | `/products`               | Crear un nuevo producto          |
| PUT    | `/products/{id}`          | Actualizar producto existente    |
| DELETE | `/products/{id}`          | Eliminar producto                |
| GET    | `/products/currency`      | Convertir precios a otra moneda |

## Ejemplo de uso con cURL

### Listar todos los productos
```bash
curl -X GET http://localhost:8080/products
```

## Obtener un producto por ID
```bash
curl -X GET http://localhost:8080/products/1
```

### Crear un nuevo producto
```bash
curl -X POST http://localhost:8080/products \
-H "Content-Type: application/json" \
-d '{
  "name": "Nuevo Producto",
  "price": 99.99,
  "stock": 10
}'
```

### Actualizar un producto existente
```bash
curl -X PUT http://localhost:8080/products/1 \
-H "Content-Type: application/json" \
-d '{
  "name": "Producto Actualizado",
  "price": 120.00,
  "stock": 15
}'
```

### Eliminar un producto
```bash
curl -X DELETE http://localhost:8080/products/1
```

### Convertir precios a otra moneda
```bash
curl -X GET http://localhost:8080/products/currency?to=USD
```

## Flujo Reactivo

Este proyecto utiliza Spring WebFlux para exponer los endpoints de manera **reactiva**, utilizando `Mono` y `Flux` para el manejo asíncrono de los datos. Esto permite un procesamiento más eficiente y escalable, especialmente bajo cargas altas.

- **Mono**: Representa 0 o 1 elemento.
- **Flux**: Representa 0 o N elementos.
- Todos los endpoints están construidos para devolver datos de forma reactiva, por ejemplo:
```java
Mono<ResponseEntity<ApiResponseDTO<ProductResponseDTO>>> getProductById(Long id);
Flux<ProductResponseDTO> getAllProducts();
```

El flujo de datos reactivo permite:

- Mejor uso de recursos del servidor.
- Respuestas más rápidas en escenarios de I/O intensivo.
- Manejo eficiente de múltiples solicitudes simultáneas.

## Contribuciones
## Contribuciones

¡Contribuciones son bienvenidas! Si deseas mejorar el proyecto, sigue estos pasos:

1. Haz un fork del repositorio.
2. Crea una rama con tu mejora: `git checkout -b feature/nueva-funcionalidad`.
3. Realiza tus cambios y haz commits claros: `git commit -m "Agrega nueva funcionalidad"`.
4. Empuja tu rama a tu fork: `git push origin feature/nueva-funcionalidad`.
5. Abre un Pull Request describiendo tus cambios y mejoras implementadas.

## Licencia

Este proyecto está bajo la licencia **MIT**.  

Se permite el uso, copia, modificación, fusión, publicación, distribución, sublicencia y/o venta de copias del software, siempre que se incluya el aviso de copyright y esta declaración de permiso en todas las copias o partes sustanciales del software.

EL SOFTWARE SE PROPORCIONA "TAL CUAL", SIN GARANTÍA DE NINGÚN TIPO, EXPRESA O IMPLÍCITA, INCLUYENDO PERO NO LIMITADO A GARANTÍAS DE COMERCIALIZACIÓN, IDONEIDAD PARA UN PROPÓSITO PARTICULAR Y NO INFRACCIÓN. EN NINGÚN CASO LOS AUTORES O TITULARES DEL COPYRIGHT SERÁN RESPONSABLES DE NINGUNA RECLAMACIÓN, DAÑO O OTRA RESPONSABILIDAD, YA SEA EN UNA ACCIÓN DE CONTRATO, AGRAVIO O CUALQUIER OTRA FORMA, QUE SURJA DE, FUERA DE O EN CONEXIÓN CON EL SOFTWARE O EL USO U OTROS TRATOS EN EL SOFTWARE.
