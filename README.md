# 🛍️ ShopSphere Backend API

This is the backend repository for the **ShopSphere** e-commerce application. It is a robust Spring Boot API designed to handle all core e-commerce functionalities, including user management, product catalog, shopping cart, payment processing (Razorpay), order management, and administrative business reporting.

---

## ✨ Features and Endpoints

The API is built using Java Spring Boot and provides secure, role-based access control (RBAC) via JWT tokens stored in HttpOnly cookies.

### 🔒 Authentication & Users (`/api/auth`, `/api/users`)
| Endpoint | Method | Description | Access |
| :--- | :--- | :--- | :--- |
| `/api/users/register` | `POST` | Registers a new user. | Public |
| `/api/auth/login` | `POST` | Authenticates a user and sets a JWT cookie. | Public |
| `/api/auth/logout` | `POST` | Clears the JWT cookie to log out the user. | Public |

### 📦 Product Catalog (`/api/products`)
| Endpoint | Method | Description | Access |
| :--- | :--- | :--- | :--- |
| `/api/products` | `GET` | Fetches all products, optionally filtered by `category` query parameter. | Customer |

### 🛒 Shopping Cart (`/api/cart`)
| Endpoint | Method | Description | Access |
| :--- | :--- | :--- | :--- |
| `/api/cart/items/count` | `GET` | Returns the total count of items in the user's cart. | Customer |
| `/api/cart/items` | `GET` | Retrieves all products in the user's cart with details. | Customer |
| `/api/cart/add` | `POST` | Adds a product to the cart (or increments quantity). | Customer |
| `/api/cart/update` | `PUT` | Updates the quantity of a specific cart item. | Customer |
| `/api/cart/delete` | `DELETE` | Removes a product from the cart. | Customer |

### 💰 Payment & Orders (`/api/payment`, `/api/orders`)
| Endpoint | Method | Description | Access |
| :--- | :--- | :--- | :--- |
| `/api/payment/create` | `POST` | Initiates a payment process by creating a Razorpay Order ID. | Customer |
| `/api/payment/verify` | `POST` | Verifies the Razorpay payment signature, updates order status to `SUCCESS`, saves `OrderItems`, and clears the cart. | Customer |
| `/api/orders` | `GET` | Fetches all successful orders for the authenticated user. | Customer |

### 👑 Admin Management (`/admin`)
Requires `ADMIN` role.

| Endpoint | Method | Description |
| :--- | :--- | :--- |
| `/admin/products/add` | `POST` | Creates a new product and associates an image URL. |
| `/admin/products/delete` | `DELETE` | Deletes a product by ID and its associated images. |
| `/admin/user/modify` | `PUT` | Updates a user's details (username, email, role) and invalidates their JWT token. |
| `/admin/user/getbyid` | `GET` | Fetches a user's details by ID. |
| `/admin/business/monthly` | `GET` | Reports total successful business and category sales for a given month and year. |
| `/admin/business/daily` | `GET` | Reports total successful business and category sales for a given date (YYYY-MM-DD format). |
| `/admin/business/yearly` | `GET` | Reports total successful business and category sales for a given year. |
| `/admin/business/overall` | `GET` | Calculates and reports the overall total business since inception. |

---

## 💻 Tech Stack

* **Language:** Java
* **Framework:** Spring Boot (using Maven for dependency management).
* **Database:** MySQL (via Spring Data JPA).
* **Security:** JWT (JJWT), BCrypt for password hashing.
* **Payment Gateway:** Razorpay integration.

---

## ⚙️ Prerequisites

* **Java 17** or newer.
* **MySQL Database** instance running.
* **Maven** (optional, included as a wrapper).

---

## 🛠️ Installation and Setup

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/Abhayaj247/Shop_Sphere_Backend.git]
    cd shop_sphere_backend
    ```

2.  **Database Configuration:**

    * Create a new database for the application (e.g., `your_project_name`).
    * Rename `src/main/resources/application-example.properties` to `src/main/resources/application.properties` (this file is gitignored).
    * Update the following properties with your database credentials and application keys:

    ```properties
    # Replace placeholders with your values
    spring.datasource.url=jdbc:mysql://localhost:3306/your_project_name
    spring.datasource.username=yours_db_username
    spring.datasource.password=yours_db_password
    
    # Must be 64 bytes long for HS512 (min. 86 characters Base64 encoded)
    jwt.secret=yours_jwt_secret_key 
    
    # Razorpay Credentials
    razorpay.key_id=yours_razorpay_key_id
    razorpay.key_secret=yours_razorpay_key_secret
    
    # CORS Configuration for Frontend
    spring.web.cors.allowed-origin-patterns=http://localhost:5173
    ```

3.  **Run the application:**

    Use the Maven wrapper to run the Spring Boot application:

    ```bash
    # On Linux/macOS
    ./mvnw spring-boot:run

    # On Windows
    .\mvnw.cmd spring-boot:run
    ```

The API will start running, typically on `http://localhost:8080`. The application uses JPA to automatically create the necessary database schema on startup (`spring.jpa.hibernate.ddl-auto=update`).

---

## 🛠️ Project Structure and Conventions

### Filter Logic
The core security logic resides in `AuthenticationFilter.java`.
* It intercepts all requests to `/api/*` and `/admin/*`.
* It validates the JWT token found in the `authToken` cookie.
* **Role Enforcement:**
    * Requests to `/admin/*` require the `ADMIN` role.
    * Requests to `/api/*` require the `CUSTOMER` role (except for public paths like `/api/auth/login`, `/api/users/register`, and `/api/auth/logout`).
* On successful authentication, the `User` object is attached to the request attribute `"authenticatedUser"` for controllers to access.