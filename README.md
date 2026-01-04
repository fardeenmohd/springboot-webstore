# Webstore Project

This is a webstore project built with Java and Spring Boot. It provides a RESTful API for managing products and
categories, with user authentication and authorization.

## Key Features:

* **RESTful API:** Exposes endpoints for creating, reading, updating, and deleting products and categories.
* **JPA Persistence:** Uses Spring Data JPA and Hibernate to interact with the database.
* **In-Memory Database:** Utilizes an H2 in-memory database for development and testing purposes.
* **User Authentication:** Secures the API with JWT-based authentication and Spring Security.
* **Input Validation:** Ensures data integrity with Spring Validation.

## Technologies Used:

* **Spring Boot:** The core framework for building the application.
    * `spring-boot-starter-webmvc`: For building web applications and RESTful APIs.
    * `spring-boot-starter-data-jpa`: For data persistence using JPA and Hibernate.
    * `spring-boot-starter-security`: For authentication and authorization.
    * `spring-boot-starter-validation`: For data validation.
* **H2 Database:** An in-memory database for development and testing.
* **Lombok:** A library to reduce boilerplate code for model and data objects.
* **ModelMapper:** A library for object mapping between DTOs and entities.
* **JSON Web Token (JJWT):** For creating and validating JWTs for user authentication.

## Getting Started

### Prerequisites

* Java 25
* Maven

### Running the Application

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/webstore.git
   ```
2. Navigate to the project directory:
   ```bash
   cd webstore
   ```
3. Run the application using Maven:
   ```bash
   mvn spring-boot:run
   ```

The application will be available at `http://localhost:8080`.

## API Reference

### Categories

#### Get All Categories

* **URL:** `/api/public/categories`
* **Method:** `GET`
* **Query Parameters:**
    * `pageNumber` (optional, default: 0)
    * `pageSize` (optional, default: 10)
    * `sortBy` (optional, default: "categoryId")
    * `sortOrder` (optional, default: "asc")
* **Success Response:**
    * **Code:** 200 OK
    * **Content:** `CategoryResponse`

#### Create Category

* **URL:** `/api/public/categories`
* **Method:** `POST`
* **Request Body:** `CategoryDTO`
* **Success Response:**
    * **Code:** 200 OK
    * **Content:** `CategoryDTO`

#### Delete Category

* **URL:** `/api/admin/categories/{categoryId}`
* **Method:** `DELETE`
* **URL Parameters:**
    * `categoryId` (required)
* **Success Response:**
    * **Code:** 200 OK
    * **Content:** `CategoryDTO`

#### Update Category

* **URL:** `/api/admin/categories`
* **Method:** `PUT`
* **Request Body:** `CategoryDTO`
* **Success Response:**
    * **Code:** 200 OK
    * **Content:** `CategoryDTO`

### Products

#### Add Product

* **URL:** `/api/admin/categories/{categoryId}/product`
* **Method:** `POST`
* **URL Parameters:**
    * `categoryId` (required)
* **Request Body:** `ProductDTO`
* **Success Response:**
    * **Code:** 201 Created
    * **Content:** `ProductDTO`

#### Get All Products

* **URL:** `/api/admin/products`
* **Method:** `GET`
* **Query Parameters:**
    * `pageNumber` (optional, default: 0)
    * `pageSize` (optional, default: 10)
    * `sortBy` (optional, default: "productId")
    * `sortOrder` (optional, default: "asc")
* **Success Response:**
    * **Code:** 200 OK
    * **Content:** `ProductResponse`

#### Get Products by Category

* **URL:** `/api/admin/categories/{categoryId}/products`
* **Method:** `GET`
* **URL Parameters:**
    * `categoryId` (required)
* **Query Parameters:**
    * `pageNumber` (optional, default: 0)
    * `pageSize` (optional, default: 10)
    * `sortBy` (optional, default: "productId")
    * `sortOrder` (optional, default: "asc")
* **Success Response:**
    * **Code:** 200 OK
    * **Content:** `ProductResponse`

#### Get Products by Name

* **URL:** `/api/admin/products/keyword/{name}`
* **Method:** `GET`
* **URL Parameters:**
    * `name` (required)
* **Query Parameters:**
    * `pageNumber` (optional, default: 0)
    * `pageSize` (optional, default: 10)
    * `sortBy` (optional, default: "productId")
    * `sortOrder` (optional, default: "asc")
* **Success Response:**
    * **Code:** 200 OK
    * **Content:** `ProductResponse`

#### Update Product

* **URL:** `/api/admin/products`
* **Method:** `PUT`
* **Request Body:** `ProductDTO`
* **Success Response:**
    * **Code:** 200 OK
    * **Content:** `ProductDTO`

#### Delete Product

* **URL:** `/api/admin/products/{productId}`
* **Method:** `DELETE`
* **URL Parameters:**
    * `productId` (required)
* **Success Response:**
    * **Code:** 200 OK
    * **Content:** `ProductDTO`

#### Update Product Image

* **URL:** `/api/products/{productId}/image`
* **Method:** `PUT`
* **URL Parameters:**
    * `productId` (required)
* **Request Parameters:**
    * `Image` (MultipartFile, required)
* **Success Response:**
    * **Code:** 200 OK
    * **Content:** `ProductDTO`
