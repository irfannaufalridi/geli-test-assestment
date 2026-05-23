## Design Decisions & Architecture

Below are the key architectural and design decisions made to ensure the system is robust, scalable, and maintainable:

### 1. Dual-Foreign Key Strategy for Transactions
**Decision:** The `TransactionDetail` entity implements a flexible relationship containing both `item_id` and `variant_id` (both nullable), rather than forcing every item to have a dummy/default variant.
**Why:** The business context requires selling both simple items (no variants) and complex items (with variants). This dual-key approach normalizes the database, prevents redundant "default variant" rows, and makes querying straightforward depending on the product type being sold.

### 2. Strict Separation of Concerns (DTO Pattern)
**Decision:** Implementation of Data Transfer Objects (DTOs) for all incoming requests and outgoing responses. Database Entities are never exposed directly to the REST controllers.
**Why:** - **Security:** Prevents over-posting attacks and hides internal database structures.
- **Stability:** Eliminates `Circular Reference` looping issues during JSON serialization (especially critical in bidirectional `@OneToMany` relationships like Items and Variants).
- **Flexibility:** Allows the API response to be structured specifically for the client's needs, independently of how data is stored.

### 3. ACID Compliance & Stock Integrity
**Decision:** Stock validation and deduction logic are encapsulated within a single `@Transactional` method in the `TransactionService`. 
**Why:** To strictly satisfy the requirement of "preventing the sale of out-of-stock items." If a user attempts to buy 5 different items and the 5th item has insufficient stock, the `@Transactional` annotation guarantees a complete rollback. This ensures the database is never left in a compromised or partially-updated state, maintaining 100% data integrity.

### 4. Immutable Historical Pricing (Snapshot)
**Decision:** During a transaction, the current `sellingPrice` from the Item or Variant is copied and persisted directly into the `TransactionDetail` table.
**Why:** Prices in the master catalog change over time. By storing a snapshot of the price at the exact moment of the transaction, historical sales data and financial calculations remain accurate and are unaffected by future price adjustments in the master data.

### 5. Layered Architecture (MVC Pattern)
**Decision:** The application strictly follows a Layered Architecture pattern, separating the codebase into distinct layers: `controller`, `service`, `repository`, and `model`.
**Why:** This enforces the "Separation of Concerns" principle. Controllers only handle HTTP requests and responses, Services contain pure business logic, and Repositories handle database interactions. This modular approach makes the code highly readable, easier to unit-test, and scalable for future feature additions.

## Assumptions Made

### 1. Open API for Assessment Purposes (No Security Layer)
It is assumed that the primary goal of this assessment is to evaluate core Java, Spring Boot, and database architecture skills. Therefore, Spring Security (JWT, Role-Based Access Control) has been intentionally omitted to allow the reviewers to test the endpoints easily via Postman without needing to handle authentication tokens.

### 2. Items Without Variants
It is assumed that the shop sells a mix of complex products (e.g., T-shirts with sizes and colors) and simple products (e.g., a standard mug). The system allows an `Item` to exist and be sold without requiring a "default" dummy variant, fetching the stock and price directly from the main `Item` table when a variant is not present.

### 3. Flat Pricing Model
For the current scope, it is assumed that the transaction amount is purely based on `quantity * price`. Complex promotional logic, such as dynamic percentage discounts, bundle deals, or varying tax rates, are considered out of scope for this baseline version.

## API Endpoint Examples

All API requests and responses use the `application/json` content type. The responses are wrapped in a standard unified structure: `BaseResponse<T>` containing `data`, `message`, and `status`.

### 1. Item & Variant Management

#### Create Item (With or Without Variants)
* **Endpoint:** `POST` `/api/item/addItem`
* **Request Body Example (With Variants):**
* Request if have variants
```json
{
	"itemName": "Basketball",
    "price": 0,
    "stock": 100,
    "description": "",
    "variants": [
        {
            "variantName": "Basketball size 6",
            "price": 5000000,
            "stock": 50
        },
        {
            "variantName": "Basketball size 7",
            "price": 750000,
            "stock": 50
        }
    ]
}
```
* Request if have no variants
```json
{
	"itemName": "Basketball",
    "price": 0,
    "stock": 100,
    "description": "",
    "variants": []
}
```
* **Response Success
```json
{
    "data": null,
    "message": "Item created successfully",
    "status": 200
}
```
### Get All Transactions
* **Endpoint:** `POST` `/api/transaction/getAllTransactions`
* **Request Body Example (With Variants):**
* **Response Success
```json
{
    "data": [
        {
            "invoiceNumber": "INV-1779440131955-6A60",
            "totalAmount": 26500000.00,
            "transactionDate": "2026-05-22T15:55:32.0241",
            "details": [
                {
                    "id": 1,
                    "itemName": "Basketball - Basketball size 6",
                    "quantity": 5,
                    "sellingPrice": 5000000.00,
                    "subTotal": 25000000.00
                },
                {
                    "id": 2,
                    "itemName": "Baju Kaoss - Warna Biru",
                    "quantity": 10,
                    "sellingPrice": 150000.00,
                    "subTotal": 1500000.00
                }
            ]
        }
    ],
    "message": "Transactions retrieved successfully",
    "statusCode": 200
}
```

## How to Run the Application

Follow these steps to set up and run the application locally on your machine.

### 📋 Prerequisites
Before running the application, ensure you have the following installed:
- **Java Development Kit (JDK):** Version 17
- **Apache Maven:** Version 3.5.14
- **Database:** PostgreSQL or MySQL running locally

---


#### 1. Clone or Extract the Project
#### 2. Create PostgreSQL database
#### 3, Configure Database Credentials in application.properties at resource folder in the project
#### 4. Build and run the application using `mvn clean install` and run with `mvn spring-boot:run`





























