# HibernateProject1

A practical **Java + Hibernate ORM e-commerce backend project** built with Maven, Jakarta Persistence (JPA), and MySQL.

The project focuses on understanding how Java objects are mapped to relational database tables and how Hibernate handles **entity relationships, persistence, transactions, CRUD operations, cascading, lazy loading, and fetch joins**.

Rather than treating Hibernate as just a database library, this project is structured around a small e-commerce domain to demonstrate how different entities work together in a real application.

---

## Project Overview

**HibernateProject1** models the core data layer of an e-commerce system:

* Users can place multiple orders.
* Orders contain multiple order details.
* Each order detail refers to a product.
* Products belong to categories.
* Hibernate manages the object-relational mapping between Java entities and MySQL tables.

The project contains independent CRUD implementations for each entity, making it useful for learning Hibernate fundamentals as well as practicing relational data modeling.

---

## Tech Stack

| Technology                | Purpose                         |
| ------------------------- | ------------------------------- |
| Java 17                   | Application development         |
| Maven                     | Dependency and build management |
| Hibernate ORM 6.3.1.Final | ORM and persistence             |
| Jakarta Persistence API   | Entity mapping                  |
| MySQL                     | Relational database             |
| MySQL Connector/J         | JDBC connectivity               |
| JUnit                     | Testing                         |

---

## Project Architecture

```text
HibernateProject1/
│
├── pom.xml
│
├── resource/
│   ├── hibernate.cfg.xml
│   └── schema.sql
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/code/HibernateProject1/
│   │           │
│   │           ├── App.java
│   │           ├── HibernateUtil.java
│   │           │
│   │           ├── entity/
│   │           │   ├── Category.java
│   │           │   ├── Product.java
│   │           │   ├── Users.java
│   │           │   ├── Orders.java
│   │           │   └── OrderDetails.java
│   │           │
│   │           └── crud/
│   │               ├── CreateCategory.java
│   │               ├── ReadCategory.java
│   │               ├── UpdateCategory.java
│   │               ├── DeleteCategory.java
│   │               │
│   │               ├── CreateProduct.java
│   │               ├── ReadProduct.java
│   │               ├── UpdateProduct.java
│   │               ├── DeleteProduct.java
│   │               │
│   │               ├── CreateUsers.java
│   │               ├── ReadUsers.java
│   │               ├── UpdateUsers.java
│   │               ├── DeleteUsers.java
│   │               │
│   │               ├── CreateOrders.java
│   │               ├── ReadOrders.java
│   │               ├── UpdateOrders.java
│   │               ├── DeleteOrders.java
│   │               │
│   │               ├── CreateOrderDetails.java
│   │               ├── ReadOrderDetails.java
│   │               ├── UpdateOrderDetails.java
│   │               └── DeleteOrderDetails.java
│   │
│   └── test/
│       └── java/
│           └── com/code/HibernateProject1/
│               └── AppTest.java
│
└── target/
    └── Maven build output
```

---

# Domain Model

The application is built around five main entities.

### 1. Category

Represents a product category.

**Main characteristics:**

* Auto-generated primary key
* Unique and required category name
* Optional description
* One category can contain multiple products
* Cascade operations and orphan removal are used for its products

```text
Category
   │
   └────── * Product
```

---

### 2. Product

Represents an item available for purchase.

**Main characteristics:**

* Auto-generated ID
* Required product name
* `BigDecimal` price
* Stock quantity
* Belongs to one category

```text
Product
   │
   └────── Category
```

Using `BigDecimal` for monetary values avoids the precision problems associated with floating-point types.

---

### 3. Users

Represents customers and administrators of the application.

**Main characteristics:**

* Auto-generated ID
* Unique username
* Unique email
* SHA-256 password hashing
* `Role` enum
* Supported roles:

  * `ADMIN`
  * `CUSTOMER`
* One user can create multiple orders

```text
Users
  │
  └────── * Orders
```

---

### 4. Orders

Represents a customer's purchase.

**Main characteristics:**

* Auto-generated ID
* Order creation timestamp
* Total order amount
* Associated user
* Multiple order details

```text
Users
  │
  └────── * Orders
              │
              └────── * OrderDetails
```

`LocalDateTime` is used to represent when the order was created, while `BigDecimal` represents the monetary total.

---

### 5. OrderDetails

Represents an individual product line inside an order.

**Main characteristics:**

* Auto-generated ID
* Quantity
* Unit price
* Associated order
* Associated product

```text
Orders
  │
  └────── * OrderDetails
               │
               └────── Product
```

This separates order-level information from individual purchased items.

---

# Entity Relationship Model

```text
                    ┌──────────────┐
                    │   Category   │
                    └──────┬───────┘
                           │
                           │ 1 : N
                           ▼
                    ┌──────────────┐
                    │   Product    │
                    └──────┬───────┘
                           │
                           │ 1 : N
                           ▼
                    ┌──────────────┐
                    │OrderDetails  │
                    └──────┬───────┘
                           │
                           │ N : 1
                           ▼
                    ┌──────────────┐
                    │    Orders    │
                    └──────┬───────┘
                           │
                           │ N : 1
                           ▼
                    ┌──────────────┐
                    │    Users     │
                    └──────────────┘
```

### Relationship Summary

| Relationship           | Mapping     |
| ---------------------- | ----------- |
| Category → Product     | One-to-Many |
| Product → Category     | Many-to-One |
| User → Orders          | One-to-Many |
| Orders → User          | Many-to-One |
| Orders → OrderDetails  | One-to-Many |
| OrderDetails → Orders  | Many-to-One |
| Product → OrderDetails | One-to-Many |
| OrderDetails → Product | Many-to-One |

---

# CRUD Implementation

Each entity has its own CRUD classes.

| Entity       | Create | Read | Update | Delete |
| ------------ | :----: | :--: | :----: | :----: |
| Category     |    ✓   |   ✓  |    ✓   |    ✓   |
| Product      |    ✓   |   ✓  |    ✓   |    ✓   |
| Users        |    ✓   |   ✓  |    ✓   |    ✓   |
| Orders       |    ✓   |   ✓  |    ✓   |    ✓   |
| OrderDetails |    ✓   |   ✓  |    ✓   |    ✓   |

This structure keeps database operations separated instead of putting every operation into a single large class.

---

# Hibernate Concepts Demonstrated

This project goes beyond basic CRUD and demonstrates several important Hibernate concepts.

### JPA Entity Mapping

The entity classes use annotations such as:

```java
@Entity
@Table
@Id
@GeneratedValue
@Column
@ManyToOne
@OneToMany
```

These annotations connect Java classes and fields with database tables and relationships.

### Lazy Loading

Entity collections are configured to load lazily where appropriate.

This means related records do not necessarily have to be loaded immediately when the parent entity is retrieved.

### Cascading

Cascade operations allow changes to be propagated between related entities where the relationship requires it.

### Orphan Removal

Orphan removal is used where child entities should no longer exist after being removed from their parent relationship.

### Fetch Join

`ReadOrders` uses a Hibernate fetch-join query to retrieve an order together with its related user, order details, and products.

Conceptually:

```text
Order
 ├── User
 └── OrderDetails
      └── Product
```

This demonstrates how Hibernate can retrieve an object graph instead of repeatedly loading individual records.

---

# Hibernate Configuration

The main Hibernate configuration is located at:

```text
resource/hibernate.cfg.xml
```

It contains configuration for:

* MySQL JDBC connection
* Hibernate MySQL dialect
* Automatic schema updates
* SQL logging
* SQL formatting
* Current-session handling
* Entity mappings

The project currently uses:

```properties
hibernate.hbm2ddl.auto=update
```

This allows Hibernate to update the database schema based on the entity mappings during development.

For production systems, schema migration tools such as Flyway or Liquibase are generally preferable.

---

# SessionFactory

`HibernateUtil.java` is responsible for creating and exposing the application's shared `SessionFactory`.

Conceptually:

```text
Application
     │
     ▼
HibernateUtil
     │
     ▼
SessionFactory
     │
     ▼
Hibernate Session
     │
     ▼
MySQL Database
```

The `SessionFactory` acts as the central entry point for creating Hibernate sessions.

---

# Database Schema

The project also contains:

```text
resource/schema.sql
```

The SQL file defines the relational structure for:

* Users
* Categories
* Products
* Orders
* Order Details

Foreign keys maintain the relationships between the tables.

---

# Order Creation Flow

One of the important parts of the project is creating an order containing multiple products.

The flow can be represented as:

```text
User
 │
 ▼
Create Order
 │
 ├── Product A → Quantity → Unit Price
 │
 ├── Product B → Quantity → Unit Price
 │
 └── Product C → Quantity → Unit Price
 │
 ▼
Order Total
```

This gives the project a more realistic e-commerce workflow instead of treating every entity as an isolated CRUD exercise.

---

# Assignment Coverage

| Requirement                | Implementation       |
| -------------------------- | -------------------- |
| Maven project              | `pom.xml`            |
| MySQL configuration        | `hibernate.cfg.xml`  |
| Category entity            | `Category.java`      |
| Product entity             | `Product.java`       |
| User entity                | `Users.java`         |
| Order entity               | `Orders.java`        |
| Order details              | `OrderDetails.java`  |
| JPA annotations            | Entity package       |
| Shared SessionFactory      | `HibernateUtil.java` |
| Category CRUD              | `crud` package       |
| Product CRUD               | `crud` package       |
| User CRUD                  | `crud` package       |
| Order CRUD                 | `crud` package       |
| OrderDetail CRUD           | `crud` package       |
| Multiple order details     | `CreateOrders.java`  |
| Associated order retrieval | `ReadOrders.java`    |
| Database schema            | `schema.sql`         |
| Test structure             | `AppTest.java`       |

---

# Running the Project

### 1. Configure MySQL

Create the required database in MySQL.

Update the database credentials in:

```text
resource/hibernate.cfg.xml
```

### 2. Build with Maven

```bash
mvn clean install
```

### 3. Run the application

Run:

```text
App.java
```

The application can be used to execute the available CRUD demonstrations.

---

# What This Project Helped Me Understand

This project was built to understand Hibernate from the database side as well as the Java side.

Key concepts practiced:

* Object-Relational Mapping
* JPA annotations
* Entity relationships
* Primary and foreign keys
* CRUD operations
* Hibernate Sessions
* SessionFactory
* Transactions
* Lazy loading
* Cascading
* Orphan removal
* Fetch joins
* JPQL/Hibernate queries
* MySQL integration
* Maven project structure
* Mapping Java data types to SQL types

---

# Possible Next Improvements

The current project focuses mainly on Hibernate and persistence fundamentals. A natural next step would be to evolve it into a complete backend application by adding:

```text
Hibernate
   │
   ▼
Repository / DAO Layer
   │
   ▼
Service Layer
   │
   ▼
REST API
   │
   ▼
Spring Boot
   │
   ▼
Frontend
```

Potential improvements include:

* Spring Boot integration
* REST APIs
* DTOs
* Service and repository layers
* Bean Validation
* Proper exception handling
* Authentication and authorization
* BCrypt/Argon2 password hashing
* Pagination
* Sorting and filtering
* Database migrations with Flyway/Liquibase
* Unit and integration tests
* Dockerized MySQL
* API documentation with OpenAPI/Swagger

---

## Project Purpose

**HibernateProject1 is not intended to be a production-ready e-commerce platform.**

It is a learning project designed to understand how a Java application communicates with a relational database through Hibernate and how real-world relationships can be represented using JPA.

The main goal was to move beyond writing SQL manually and understand the complete flow:

```text
Java Object
     ↓
JPA Mapping
     ↓
Hibernate ORM
     ↓
SQL
     ↓
MySQL
```

That foundation can later be carried into larger applications built with **Spring Boot, REST APIs, and modern backend architectures**.
