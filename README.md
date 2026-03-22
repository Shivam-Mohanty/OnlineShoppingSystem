# University Online Store Management System

## Project Overview
This application is a full-stack desktop software developed to demonstrate the integration of a JavaFX graphical user interface with a PostgreSQL relational database. The system provides a real-time e-commerce environment including dynamic product searching, shopping cart management, and an administrative dashboard for inventory control.

## Technical Specifications
* **Programming Language:** Java 21
* **GUI Framework:** JavaFX 21.0.2
* **Database Management System:** PostgreSQL 17
* **Database Connectivity:** JDBC
* **Build Tool:** Apache Maven
* **Styling:** Custom CSS (Dark Mode)

---

## Key Features
* **Inventory Management:** Tracks stock levels in real-time. The database automatically decrements stock counts upon successful checkout.
* **Dynamic Search Filtering:** Implements a search bar that updates the product list instantly as the user types.
* **Admin Dashboard:** Allows authorized users to add new products directly to the PostgreSQL database through the GUI.
* **Detailed Checkout:** Generates a formatted order confirmation showing all purchased items, individual prices, and the final total.
* **Modern User Interface:** Features a professional dark mode palette with custom-aligned product rows for improved readability.

---

## Database Schema
The application requires a PostgreSQL database named `university_store`. Use the following SQL commands to set up the necessary table:

```sql
CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    stock_count INTEGER DEFAULT 10
);

-- Seed data for testing
INSERT INTO products (name, price, stock_count) VALUES
('Gaming Laptop Z1', 1250.00, 15),
('Mechanical Keyboard', 85.50, 20),
('Wireless Mouse Pro', 45.00, 30),
('Java Programming Guide', 55.00, 10);
```

---

## Installation and Setup

### 1. Database Configuration
Ensure PostgreSQL is installed and the `products` table has been created. Update the following credentials in `ProductDAO.java` to match your local environment:
* **URL:** `jdbc:postgresql://localhost:5432/university_store`
* **Username:** Your PostgreSQL username
* **Password:** Your PostgreSQL password

### 2. Project Execution
Navigate to the project root directory and run the application using Maven:
```bash
mvn javafx:run
```

---

## Project Structure
* **OnlineStoreGUI.java:** Manages the primary stage, UI layouts, and event handling logic.
* **Product.java:** The data model class representing store entities.
* **ProductDAO.java:** Handles all SQL operations including data retrieval and updates.
* **style.css:** Contains the CSS rules for the application's dark mode theme.

---

## Author
**Shivam Kumar Mohanty** Computer Science and Engineering  
KIIT University  
4th Semester, 2026

Would you like me to help you create a **test case document** to go along with this, showing exactly how to verify each feature works?
