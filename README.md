**Flatmate** is a society/township management system. This repository contains the **backend services**, currently focused on the **Admin view**.

The backend is built with **Spring Boot**, following an **MVC architecture**, using **Hibernate** for ORM and **Supabase** as the SQL database. Email notifications to residents are handled through a **DTO → service → NodeMailer pipeline**.

> ⚡ This project is actively under development.
---

## Overview

Flatmate simplifies admin operations for residential communities. Admin features include:

* Resident, apartment, and society management (CRUD)
* Announcement and notice dispatch via email
* Email pipeline integrated with NodeMailer
* Clean, modular architecture to support future user modules

The system is designed to scale with upcoming features like **user dashboards, payments, and notifications**.

---

## Tech Stack & Architecture

* **Backend:** Spring Boot (Java 17+)
* **Architecture:** MVC
* **ORM:** Hibernate
* **Database:** Supabase (PostgreSQL)
* **Email:** NodeMailer (via DTO-service integration)

### MVC Flow

```
Admin Dashboard → Controller → Service → Repository → Supabase
```

* **Controller:** Receives HTTP requests from frontend
* **Service:** Business logic, email pipelines
* **Repository:** Hibernate-powered database access
* **DTOs:** Transfer clean data between layers, especially for notifications

---

## Admin Features (Implemented)

| Feature                   | Status                 |
| ------------------------- | ---------------------- |
| Resident management       | ✅ CRUD                 |
| Apartment management      | ✅ CRUD                 |
| Society management        | ✅ CRUD                 |
| Announcement / notices    | ✅ Email via NodeMailer |

---

## How to Run

1. Clone the repo
2. Configure environment variables:

| Variable       | Purpose              |
| -------------- | -------------------- |
| `SUPABASE_URL` | Supabase project URL |
| `SUPABASE_KEY` | API key              |
| `MAIL_HOST`    | SMTP host            |
| `MAIL_PORT`    | SMTP port            |
| `MAIL_USER`    | SMTP username        |
| `MAIL_PASS`    | SMTP password        |

3. Build and run:

```bash
mvn clean install
mvn spring-boot:run
```

4. Admin APIs are available at `http://localhost:8080/admin`

---

## API Endpoints (Admin)

| Endpoint                | Method | Description              |
| ----------------------- | ------ | ------------------------ |
| `/admin/residents`      | GET    | List all residents       |
| `/admin/residents`      | POST   | Add a resident           |
| `/admin/residents/{id}` | PUT    | Update resident          |
| `/admin/residents/{id}` | DELETE | Delete resident          |
| `/admin/announcement`   | POST   | Send announcement emails |


