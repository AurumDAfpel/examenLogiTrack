# LogiTrack S.A. - Sistema Backend & Frontend de Gestión Logística

Sistema integral para el control de inventarios, bodegas, movimientos y auditoría desarrollado para **LogiTrack S.A.** con Spring Boot 3, Java 17, PostgreSQL y seguridad mediante JWT.

---

## Descripción del Proyecto

LogiTrack es una API RESTful diseñada para gestionar la cadena logística de una empresa. Permite:
* Autenticación segura mediante usuarios y roles (ADMIN, USER) con Tokens JWT.
* Gestión CRUD de Productos y Bodegas.
* Registro transaccional de Movimientos (Entradas, Salidas y Transferencias entre bodegas).
* Auditoría automática de cambios en el sistema.
* Control de stock mínimo con alertas automáticas.

---

## Requisitos Previos

* **Java Development Kit (JDK):** Version 17 o superior.
* **Apache Maven:** 3.8+
* **PostgreSQL:** 12+
* **IDE Recomendado:** VS Code, IntelliJ IDEA o Eclipse.

---

## Instalación y Ejecución

### 1. Clonar o descargar el repositorio
```bash
git clone <URL_DEL_REPOSITO>
cd logitrack
# examenLogiTrack
