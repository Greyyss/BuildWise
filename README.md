# BuildWise

BuildWise es una aplicación web desarrollada con Spring Boot para la gestión, recomendación y cotización de computadoras personalizadas. El sistema permite administrar componentes, categorías, proveedores, usuarios, solicitudes, cotizaciones, setups y reportes generales del inventario.

El objetivo principal del sistema es ayudar a los usuarios a armar una computadora según su presupuesto, tipo de uso y componentes disponibles.

## Descripción del proyecto

BuildWise funciona como una plataforma para armar y cotizar computadoras. El sistema cuenta con dos tipos de usuarios:

- Administrador
- Usuario normal

El administrador puede gestionar el inventario, proveedores, categorías, usuarios, solicitudes, cotizaciones, setups y reportes.

El usuario normal puede acceder a la página pública, usar el armador automático, armar su propia PC manualmente, consultar componentes disponibles y enviar solicitudes de cotización.

## Objetivo general

Desarrollar una aplicación web que permita administrar componentes de computadoras y generar cotizaciones o recomendaciones de PC según presupuesto, tipo de uso y disponibilidad de inventario.

## Objetivos específicos

- Implementar un sistema de autenticación con roles.
- Gestionar componentes, categorías, proveedores, usuarios, solicitudes, cotizaciones y setups.
- Permitir al usuario generar recomendaciones automáticas de PC según presupuesto.
- Permitir al usuario armar una PC manualmente seleccionando componentes disponibles.
- Calcular el total de una configuración y compararlo contra el presupuesto indicado.
- Permitir el envío de solicitudes públicas de cotización.
- Permitir al administrador atender solicitudes generando cotizaciones.
- Mostrar reportes generales del sistema e inventario.
- Utilizar una base de datos MySQL para la persistencia de información.

## Tecnologías utilizadas

- Java
- Spring Boot
- Spring MVC
- Spring Data JPA
- Hibernate
- Thymeleaf
- MySQL
- Bootstrap
- Bootstrap Icons
- HTML
- CSS
- Maven
- Git
- GitHub
- Visual Studio Code

## Arquitectura del proyecto

El proyecto utiliza una arquitectura basada en MVC, separando responsabilidades en diferentes capas.

```text
Controller  → Maneja las rutas y peticiones HTTP.
Service     → Contiene la lógica de negocio.
Repository  → Acceso a la base de datos.
Model       → Entidades del sistema.
Templates   → Vistas Thymeleaf.
Static      → Archivos CSS y recursos estáticos.

Antes de ejecutar el proyecto se necesita tener instalado:

Java JDK 21 o superior.
MySQL.
Git.
Navegador web.
Visual Studio Code, IntelliJ IDEA o NetBeans.
Cómo ejecutar el proyecto localmente
Clonar el repositorio:
git clone URL_DEL_REPOSITORIO
Entrar a la carpeta del proyecto:
cd buildwise
Crear la base de datos en MySQL:
CREATE DATABASE buildwise_db;
Configurar el archivo application.properties con el usuario y contraseña de MySQL.
Ejecutar el proyecto con Maven Wrapper.

En Windows:
.\mvnw.cmd spring-boot:run

En Linux o Mac:
./mvnw spring-boot:run
Abrir el sistema en el navegador:
http://localhost:8080
Credenciales de prueba
Administrador
Correo: admin@buildwise.com
Contraseña: admin123
Rol: ADMIN
Usuario normal

El usuario normal puede crearse desde la página de registro:
http://localhost:8080/registro
Los usuarios registrados desde esa página reciben automáticamente el rol USUARIO

Funcionalidades implementadas
Registro de usuarios.
Login con sesión.
Cierre de sesión.
Manejo de roles.
Protección de rutas administrativas.
CRUD de componentes.
CRUD de categorías.
CRUD de proveedores.
Gestión de usuarios.
Gestión de setups.
Asociación de componentes a setups.
Cálculo de total real de setups.
Comparación contra presupuesto.
Armador automático según presupuesto y tipo de uso.
Armador manual por selección de componentes.
Consulta de componentes en stock.
Envío de solicitudes públicas.
Atención de solicitudes mediante cotización.
Gestión de cotizaciones.
Dashboard administrativo.
Reportes generales.
Control de stock bajo.
Valor total del inventario.
Validaciones principales

El sistema utiliza validaciones en formularios HTML y controles en la lógica del sistema para evitar datos inválidos.
Ejemplos:
Campos obligatorios en formularios.
Presupuesto mayor a cero.
Precio mayor a cero.
Stock no negativo.
Correo único para usuarios.
Validación de sesión para rutas privadas.
Validación de rol para rutas administrativas.
Estado activo o inactivo para registros administrativos.
Seguridad y control de acceso

El sistema maneja sesiones para controlar el acceso de los usuarios.
El acceso se divide según el rol:

ADMIN   → Acceso completo al sistema administrativo.
USUARIO → Acceso limitado a funciones públicas y de armado de PC.

Las rutas administrativas validan que exista una sesión activa y que el usuario tenga rol ADMIN.
Estado actual del proyecto
El sistema se encuentra funcional y permite cumplir con los objetivos principales del proyecto:

Administración de inventario.
Recomendación de PC.
Cotización de configuraciones.
Solicitudes de clientes.
Gestión por roles.
Reportes administrativos.
Consulta de componentes disponibles.
Armado manual y automático de computadoras.
