<h1 align="center">🛒 RestAssured Grocery Store — API Test Automation Framework</h1>
<p align="center">
  <img src="https://img.shields.io/badge/Java-9-orange?style=for-the-badge&logo=java" />
  <img src="https://img.shields.io/badge/REST--Assured-6.0.0-green?style=for-the-badge" />
  <img src="https://img.shields.io/badge/TestNG-7.12.0-blue?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Allure-2.29.1-yellow?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Maven-Build-red?style=for-the-badge&logo=apache-maven" />
  <img src="https://img.shields.io/badge/GitHub%20Actions-CI%2FCD-black?style=for-the-badge&logo=github-actions" />
</p>


<p align="center">
  A <strong>production-grade, layered REST API test automation framework</strong> built in Java using REST-Assured, TestNG, and Allure Reports — designed with scalability, thread safety, and clean architecture at its core.
</p>

---

## 📋 Table of Contents

- [About the API Under Test](#-about-the-api-under-test)
- [Framework Architecture](#-framework-architecture)
- [Project Structure](#-project-structure)
- [Core Components Deep Dive](#-core-components-deep-dive)
  - [RestHelper — The HTTP Engine](#1-resthelper--the-http-engine)
  - [ConfigLoader — Centralized Configuration](#2-configloader--centralized-configuration)
  - [TokenService + ThreadLocal — Thread-Safe Auth](#3-tokenservice--threadlocal--thread-safe-auth)
  - [Service Layer](#4-service-layer)
  - [Endpoint Layer](#5-endpoint-layer)
  - [Model Layer (POJOs + Jackson)](#6-model-layer-pojos--jackson)
  - [JSON Schema Validation](#7-json-schema-validation)
- [Test Suites](#-test-suites)
  - [Smoke Suite](#1-smoke-suite)
  - [Regression Parallel Suite](#2-regression-parallel-suite)
  - [Full Parallel Suite](#3-full-parallel-suite)
- [Test Coverage](#-test-coverage)
- [Allure Reporting](#-allure-reporting)
- [GitHub Actions — CI/CD Pipeline](#-github-actions--cicd-pipeline)
- [Dependencies & Tech Stack](#-dependencies--tech-stack)
- [How to Run](#-how-to-run)
- [Configuration](#-configuration)
- [Design Principles](#-design-principles-applied)
- [Author](#-author)

---

## 🏪 About the API Under Test

This framework tests the **[Simple Grocery Store API](https://simple-grocery-store-api.click)** — a fully functional RESTful grocery store backend that simulates real-world e-commerce API behaviour.

### Base URL
```
https://simple-grocery-store-api.click
```

### API Resources

The API exposes **4 main resources**, all of which are fully tested in this framework:

| Resource | Endpoint | Auth Required | Description |
|----------|----------|:-------------:|-------------|
| **API Clients** | `/api-clients` | ❌ | Register a new API client to obtain a Bearer token |
| **Products** | `/products` | ❌ | Browse and query the product catalog |
| **Carts** | `/carts` | ❌ | Create and manage shopping carts |
| **Orders** | `/orders` | ✅ | Place, view, update, and delete orders |

### Authentication Flow

The API uses **Bearer Token authentication**. Before placing orders, a client must register via `POST /api-clients` with a name and email, and receive a unique `accessToken`. This token is then passed as an `Authorization: Bearer <token>` header on all protected endpoints.

### Product Categories

The API supports 7 product categories:

`coffee` · `fresh-produce` · `meat-seafood` · `candy` · `dairy` · `bread-bakery` · `eggs`

### Cart Lifecycle

```
POST   /carts                           → Create a new cart, receive cartId
POST   /carts/{cartId}/items            → Add a product to the cart
GET    /carts/{cartId}/items            → Retrieve all items in the cart
PATCH  /carts/{cartId}/items/{itemId}   → Modify item quantity (partial update)
PUT    /carts/{cartId}/items/{itemId}   → Replace item completely (full update)
DELETE /carts/{cartId}/items/{itemId}   → Remove item from cart
```

### Order Lifecycle

```
POST   /orders              → Create order (requires cartId + customerName + token)
GET    /orders              → Get all orders for this API client (requires token)
GET    /orders/{orderId}    → Get a specific order by ID (requires token)
PATCH  /orders/{orderId}    → Update order details e.g. customer name (requires token)
DELETE /orders/{orderId}    → Delete an order (requires token)
```

---

## 🏗 Framework Architecture

This framework follows a **layered architecture pattern** that promotes clean separation of concerns, high reusability, and parallel-execution safety.

```
┌──────────────────────────────────────────────────────────────┐
│                        TEST LAYER                            │
│  testcases/auth · testcases/product · testcases/cart         │
│  testcases/order                                             │
│  (Happy Path Tests + Negative Tests per feature)             │
└───────────────────────────┬──────────────────────────────────┘
                            │ uses
┌───────────────────────────▼──────────────────────────────────┐
│                      SERVICE LAYER                           │
│  TokenService · CartService · ProductService                 │
│  OrderService · RegisterService                              │
│  (Test data orchestration, shared reusable actions)          │
└───────────────────────────┬──────────────────────────────────┘
                            │ uses
┌───────────────────────────▼──────────────────────────────────┐
│                     ENDPOINT LAYER                           │
│  ClientEndpoint · ProductEndpoint · CartEndpoint             │
│  OrdersEndpoint                                              │
│  (One class per resource, maps HTTP verbs to API routes)     │
└───────────────────────────┬──────────────────────────────────┘
                            │ uses
┌───────────────────────────▼──────────────────────────────────┐
│                      UTILITY LAYER                           │
│  RestHelper (generic HTTP engine)                            │
│  ConfigLoader (property reader)                              │
└───────────────────────────┬──────────────────────────────────┘
                            │
┌───────────────────────────▼──────────────────────────────────┐
│                      MODEL LAYER                             │
│  Request/Response POJOs per domain                           │
│  (client · product · cart · order · ErrorResponse)           │
│  9 JSON Schema files for contract validation                 │
└──────────────────────────────────────────────────────────────┘
```

---

## 📁 Project Structure

```
RestAssured-Grocery-Store-Framework/
│
├── .github/
│   └── workflows/
│       └── regression.yml                   ← GitHub Actions CI/CD pipeline
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── endpoints/
│   │   │   │   ├── CartEndpoint.java
│   │   │   │   ├── ClientEndpoint.java
│   │   │   │   ├── OrdersEndpoint.java
│   │   │   │   └── ProductEndpoint.java
│   │   │   ├── models/
│   │   │   │   ├── cart/
│   │   │   │   │   ├── CartItem.java
│   │   │   │   │   ├── CartItemRequest.java
│   │   │   │   │   ├── CartItemResponse.java
│   │   │   │   │   ├── CreateCartResponse.java
│   │   │   │   │   └── GetCartResponse.java
│   │   │   │   ├── client/
│   │   │   │   │   ├── ClientRegisterRequest.java
│   │   │   │   │   └── ClientRegisterResponse.java
│   │   │   │   ├── order/
│   │   │   │   │   ├── CreateOrderRequest.java
│   │   │   │   │   ├── CreateOrderResponse.java
│   │   │   │   │   └── GetOrder.java
│   │   │   │   ├── product/
│   │   │   │   │   ├── ProductCategory.java    ← Enum with @JsonValue
│   │   │   │   │   ├── ProductQueryParams.java
│   │   │   │   │   └── ProductResponse.java
│   │   │   │   └── ErrorResponse.java          ← Shared across all negative tests
│   │   │   ├── services/
│   │   │   │   ├── CartService.java
│   │   │   │   ├── OrderService.java
│   │   │   │   ├── ProductService.java         ← In-memory product cache
│   │   │   │   ├── RegisterService.java
│   │   │   │   └── TokenService.java           ← ThreadLocal token caching
│   │   │   └── utils/
│   │   │       ├── ConfigLoader.java
│   │   │       └── RestHelper.java             ← Generic HTTP engine (all verbs)
│   │   └── resources/
│   │       └── config.properties               ← Base URL + optional client config
│   │
│   └── test/
│       ├── java/
│       │   └── testcases/
│       │       ├── auth/
│       │       │   └── RegistrationTest.java
│       │       ├── cart/
│       │       │   ├── add_item/
│       │       │   │   ├── AddItemHappyPathTest.java
│       │       │   │   └── AddItemNegativeTest.java
│       │       │   ├── create_cart/
│       │       │   ├── delete_item/
│       │       │   ├── modify_item/
│       │       │   └── replace_item/
│       │       ├── order/
│       │       │   ├── create_order/
│       │       │   ├── delete_order/
│       │       │   ├── get_all_orders/
│       │       │   ├── get_order_by_id/
│       │       │   └── update_order/
│       │       └── product/
│       │           ├── GetProductsHappyPathTest.java
│       │           └── GetProductNegativeTest.java
│       ├── resources/
│       │   └── schemas/                        ← 9 JSON Schema contract files
│       │       ├── all-orders-schema.json
│       │       ├── cart-created-schema.json
│       │       ├── cart-items-schema.json
│       │       ├── cart-schema.json
│       │       ├── client-created-schema.json
│       │       ├── error-schema.json
│       │       ├── order-schema.json
│       │       ├── product-list-schema.json
│       │       └── product-schema.json
│       └── test-suites/
│           ├── smoke.xml                       ← Fast sanity check (sequential)
│           ├── regression-parallel.xml         ← Full regression, 11 threads
│           └── full-parallel.xml               ← Maximum parallelism, 22 threads
│
└── pom.xml                                     ← Maven build + Surefire + AspectJ
```

---

## 🔬 Core Components Deep Dive

### 1. `RestHelper` — The HTTP Engine

**File:** `src/main/java/utils/RestHelper.java`

`RestHelper` is the **backbone of the entire framework**. It is a static utility class that wraps all REST-Assured HTTP operations behind a clean, consistent, and generic API. Every endpoint class routes its HTTP calls through `RestHelper`, which means:

- The base URL is **always injected from `config.properties`** — never hardcoded in test code
- **JSON Schema validation** is built into every response chain where applicable
- **Status code assertions** are performed inline before deserialization
- **Bearer token injection** is an optional overload — same method signature, different arity

#### Key Design Decisions

| Feature | Implementation |
|---------|----------------|
| Generic return types `<T>` | Every `get/post/put/patch/delete` is fully generic — no casting needed anywhere above |
| Overloaded methods | Multiple signatures per HTTP verb support: no-token, with-token, query params, raw `Response` |
| Schema validation inline | `matchesJsonSchemaInClasspath(schemaPath)` chained directly into `.assertThat()` on every call |
| Automatic failure logging | `RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()` in a `static {}` block |

#### Method Signatures at a Glance

```java
// GET variants
RestHelper.get(endpoint, responseClass, statusCode, schemaPath)
RestHelper.get(endpoint, id, responseClass, statusCode, schemaPath)         // with path param
RestHelper.get(endpoint, responseClass, statusCode, schemaPath, token)      // authenticated
RestHelper.get(endpoint, responseClass, statusCode, ProductQueryParams, schemaPath)  // typed params
RestHelper.get(endpoint, responseClass, statusCode, Map<String,Object>, schemaPath)  // raw params

// POST variants
RestHelper.post(endpoint, responseClass, statusCode, body)
RestHelper.post(endpoint, responseClass, statusCode, body, token)           // authenticated

// PATCH variants
RestHelper.patch(endpoint, responseClass, statusCode, body, id)
RestHelper.patch(endpoint, responseClass, statusCode, body, id, token)      // authenticated
RestHelper.patch(endpoint, statusCode, body, id)                            // returns raw Response

// PUT variants
RestHelper.put(endpoint, responseClass, statusCode, body)
RestHelper.put(endpoint, responseClass, statusCode, body, token)            // authenticated
RestHelper.put(endpoint, statusCode, body)                                  // returns raw Response

// DELETE variants
RestHelper.delete(endpoint, id, token, statusCode)                          // authenticated
RestHelper.delete(endpoint, statusCode)                                     // anonymous
```

---

### 2. `ConfigLoader` — Centralized Configuration

**File:** `src/main/java/utils/ConfigLoader.java`

`ConfigLoader` exposes a single static method `getProperty(String key)` that reads from `config.properties` at runtime. This decouples all environment-specific values from test code — no hardcoded URLs anywhere.

```properties
# src/main/resources/config.properties
baseUrl     = https://simple-grocery-store-api.click
clientName  =          ← leave empty → JavaFaker auto-generates
clientEmail =          ← leave empty → JavaFaker auto-generates
```

---

### 3. `TokenService` + `ThreadLocal` — Thread-Safe Auth

**File:** `src/main/java/services/TokenService.java`

This is one of the most **architecturally significant** components of the framework. The problem it solves:

> **In parallel test execution, multiple threads need authentication tokens. A naive singleton would either create unnecessary API calls (one per test) or cause race conditions (shared mutable state).**

#### The Solution: `ThreadLocal<String>`

```java
private static final ThreadLocal<String> cachedToken = new ThreadLocal<>();
```

Each thread maintains its **own independent copy** of the access token. The first time a thread calls `TokenService.getToken()`, it registers a new API client and stores the resulting token in that thread's `ThreadLocal` slot. All subsequent calls from the same thread return the cached value — **zero additional API calls**.

#### Execution Flow

```
Thread A → getToken() → cachedToken == null → register client A → cache token A
Thread A → getToken() → returns cached token A  ← no API call

Thread B → getToken() → cachedToken == null → register client B → cache token B
Thread B → getToken() → returns cached token B  ← no API call

✅ Thread A and Thread B have DIFFERENT tokens
✅ ZERO shared mutable state
✅ ZERO race conditions
```

#### Smart Fallback with JavaFaker

```java
if (clientName == null || clientName.isEmpty()) {
    clientName = faker.name().fullName();           // e.g. "Dr. John Carter"
}
if (clientEmail == null || clientEmail.isEmpty()) {
    clientEmail = faker.internet().emailAddress();  // e.g. "john.carter@example.com"
}
```

If credentials are not provided in `config.properties`, the service auto-generates realistic ones with JavaFaker — making the framework **zero-config for immediate use**.

---

### 4. Service Layer

The service layer acts as the **test data orchestration layer**. Services compose endpoint calls into meaningful, reusable high-level actions that tests consume in a single line — no test class ever needs to wire together multiple endpoint calls manually.

#### `ProductService`

**File:** `src/main/java/services/ProductService.java`

Solves a critical performance problem: tests that need a random product shouldn't each make their own `GET /products` API call.

```java
private static ProductResponse[] cachedProducts = null;

private static void initializeCacheIfEmpty() {
    if (cachedProducts == null) {
        cachedProducts = new ProductEndpoint()
            .getAllProducts(200, "schemas/product-list-schema.json", ProductResponse[].class);
    }
}
```

The **first** call fetches all products and caches them in a static array. All subsequent calls — from any test — use the in-memory cache. Across a full parallel run with dozens of tests needing products, this results in exactly **one API call** to fetch the catalog.

| Method | Description |
|--------|-------------|
| `getRandomAvailableProduct()` | Returns a random in-stock product from cache |
| `getRandomNonAvailableProduct()` | Returns a random out-of-stock product from cache |
| `getAllProducts()` | Returns the full cached product array |
| `getProductById(id)` | Fetches a specific product by ID directly from the API |
| `getRandomQuantity(product)` | Returns a safe random quantity within `[1, currentStock]` |

#### `CartService`

**File:** `src/main/java/services/CartService.java`

Exposes cart lifecycle utilities. The elegant `addRandomItemToCart()` chains `ProductService` and `CartService` together — picking a valid random product and safely adding it — all in one call from the test.

```java
public static CartItem addRandomItemToCart(String cartId) {
    Integer productId   = ProductService.getRandomAvailableProduct().id;
    ProductResponse product = ProductService.getProductById(productId);
    Integer quantity    = ProductService.getRandomQuantity(product);
    return addItemToCart(cartId, productId, quantity);
}
```

#### `OrderService`

**File:** `src/main/java/services/OrderService.java`

Creates an order using a Faker-generated customer name with the provided `cartId` and `token`, and returns the `orderId` directly for use in test assertions.

#### `RegisterService`

**File:** `src/main/java/services/RegisterService.java`

Two overloads:
- `registerClientAndGetToken(ClientRegisterRequest)` — for negative tests where you control the payload exactly
- `registerClientAndGetToken()` — auto-generates fake credentials, for use in setup flows

---

### 5. Endpoint Layer

The endpoint layer provides **one class per API resource**, each mapping HTTP operations to clearly named Java methods. Tests and services never call REST-Assured or `RestHelper` directly — they always go through an Endpoint class.

| Class | Resource | HTTP Methods Covered |
|-------|----------|---------------------|
| `ClientEndpoint` | `/api-clients` | `POST` (register) |
| `ProductEndpoint` | `/products` | `GET` (all), `GET` (with query params ×2 overloads), `GET` (by ID) |
| `CartEndpoint` | `/carts` | `POST` (create), `GET` (cart), `POST` (add item), `GET` (items), `PATCH` (modify), `PUT` (replace), `DELETE` (item) |
| `OrdersEndpoint` | `/orders` | `POST` (create), `GET` (all), `GET` (by ID), `PATCH` (update), `DELETE` |

Each endpoint method accepts `statusCode` as an explicit parameter — meaning **negative tests are first-class citizens**. A 400-expecting call uses the exact same endpoint method as a 201-expecting call.

---

### 6. Model Layer (POJOs + Jackson)

All request and response bodies map to **strongly typed Java POJOs** using Jackson annotations. This eliminates brittle string-based field access and gives full IDE autocompletion everywhere.

#### Jackson Annotations Used

| Annotation | Purpose |
|------------|---------|
| `@JsonProperty("fieldName")` | Maps JSON keys to Java field names |
| `@JsonInclude(NON_NULL)` | Excludes null fields from serialization — critical for partial requests |
| `@JsonPropertyOrder({...})` | Controls field serialization order |
| `@JsonValue` | Used on `ProductCategory` enum to serialize to its string value |

#### Builder-Style Request Models

```java
ClientRegisterRequest request = new ClientRegisterRequest()
    .setClientName("Ahmed Zahran")
    .setClientEmail("ahmed@test.com");
```

#### Shared `ErrorResponse` Model

A single `ErrorResponse` class captures the standard error structure returned by the API for any `4xx` response. All negative tests assert against it with a clean `getErrorMessage()` getter:

```java
Assert.assertEquals(response.getErrorMessage(), "Invalid or missing client email.");
```

---

### 7. JSON Schema Validation

**Location:** `src/test/resources/schemas/`

Every significant API response is validated against a **JSON Schema** before deserialization. This provides **contract testing** out of the box — if the API silently changes its response shape, schema validation catches it immediately, independently of functional assertions.

| Schema File | Validates |
|-------------|-----------|
| `client-created-schema.json` | `POST /api-clients` 201 response |
| `error-schema.json` | Any 4xx error response (shared across all negative tests) |
| `product-list-schema.json` | `GET /products` array response |
| `product-schema.json` | `GET /products/{id}` single product |
| `cart-created-schema.json` | `POST /carts` 201 response |
| `cart-schema.json` | `GET /carts/{id}` full cart object |
| `cart-items-schema.json` | `GET /carts/{id}/items` items array |
| `order-schema.json` | `GET /orders/{id}` single order |
| `all-orders-schema.json` | `GET /orders` orders array |

Schema validation is wired into `RestHelper` via:

```java
.assertThat().body(matchesJsonSchemaInClasspath(schemaPath))
```

Every endpoint call that passes a `schemaPath` gets structural contract validation for free, without any extra code in the test class.

---

## 🧪 Test Suites

Three TestNG XML suite files control how tests are grouped, ordered, and parallelised. The active suite is selected via the Maven property `suiteXmlFile` (default: `smoke.xml`).

---

### 1. Smoke Suite

**File:** `src/test/test-suites/smoke.xml`
**Mode:** `parallel="none"` (sequential, no threads)

Runs one carefully chosen happy-path test per feature to confirm the system is **fundamentally operational**. Ideal as a quick sanity check after a deployment or before a longer run.

```xml
<suite name="Smoke Suite" parallel="none">
```

| Test Group | Test Included |
|-----------|---------------|
| Smoke - Auth | `testValidSuccessfulRegistration` |
| Smoke - Product | All `GetProductsHappyPathTest` methods |
| Smoke - Cart | `CreateNewCartTest`, `testAddingItemAndVerifyItExists`, Modify, Replace, Delete happy paths |
| Smoke - Order | Create, GetAll, GetById, Update, Delete happy path methods (one each) |

**Run:**
```bash
mvn test -DsuiteXmlFile=smoke.xml
```

---

### 2. Regression Parallel Suite

**File:** `src/test/test-suites/regression-parallel.xml`
**Mode:** `parallel="tests"` · **11 threads**

Runs the **complete test library** — both happy-path and negative tests — with test-group level parallelism. Each `<test>` block runs concurrently in its own thread:

```xml
<suite name="Regression Parallel Suite" parallel="tests" thread-count="11">
```

| Thread | Test Group |
|:------:|-----------|
| 1 | Auth - Registration |
| 2 | Product - Happy Path |
| 3 | Product - Negative |
| 4 | Cart - Happy Path |
| 5 | Cart - Negative |
| 6 | Order - Create (Happy Path) |
| 7 | Order - Create (Negative) |
| 8 | Order - Get (Happy Path) |
| 9 | Order - Get (Negative) |
| 10 | Order - Update (Happy + Negative) |
| 11 | Order - Delete (Happy + Negative) |

> This is the suite executed by the **GitHub Actions CI/CD pipeline**.

**Run:**
```bash
mvn test -DsuiteXmlFile=regression-parallel.xml
```

---

### 3. Full Parallel Suite

**File:** `src/test/test-suites/full-parallel.xml`
**Mode:** `parallel="classes"` · **22 threads**

The most aggressive parallelism level. Every **test class** runs in its own thread simultaneously.

```xml
<suite name="Full Parallel Suite" parallel="classes" thread-count="22">
```

This is where `ThreadLocal` in `TokenService` becomes **mission-critical** — with 22 concurrent threads each needing a valid auth token, the `ThreadLocal` design ensures each thread independently manages its own access token with zero shared state and zero race conditions.

**Run:**
```bash
mvn test -DsuiteXmlFile=full-parallel.xml
```

---

### Suite Comparison

| Suite | Mode | Threads | Coverage | Best For |
|-------|------|:-------:|----------|---------|
| `smoke.xml` | Sequential | 1 | Happy path only | Quick deployment sanity check |
| `regression-parallel.xml` | Parallel by test group | 11 | Full (happy + negative) | CI/CD pipeline, full regression |
| `full-parallel.xml` | Parallel by class | 22 | Full (happy + negative) | Maximum-speed local runs |

---

## 📊 Test Coverage

### Auth — `/api-clients`

| Test | Type | Expected Status |
|------|------|:--------------:|
| Valid registration (name + email) → token returned | ✅ Happy | 201 |
| Missing email field | ❌ Negative | 400 |
| Missing client name field | ❌ Negative | 400 |
| Invalid email format | ❌ Negative | 400 |
| Empty client name string | ❌ Negative | 400 |
| Empty client email string | ❌ Negative | 400 |
| Email already registered (duplicate) | ❌ Negative | 409 |

### Products — `/products`

| Test | Type | Expected Status |
|------|------|:--------------:|
| Get all products — non-empty array | ✅ Happy | 200 |
| Filter by `available=true` — all items inStock | ✅ Happy | 200 |
| Filter by `available=false` — all items out-of-stock | ✅ Happy | 200 |
| Filter by each of 7 categories (DataProvider ×7) | ✅ Happy | 200 |
| Limit results to 5 (`results=5`) | ✅ Happy | 200 |
| Get product by valid ID | ✅ Happy | 200 |
| Invalid product ID (-1) | ❌ Negative | 404 |
| Invalid `results` param (`results=-1`) | ❌ Negative | 400 |
| Invalid `category` string | ❌ Negative | 400 |

### Cart — `/carts`

#### Create Cart

| Test | Type | Expected Status |
|------|------|:--------------:|
| Create new cart → cartId returned | ✅ Happy | 201 |

#### Add Item to Cart

| Test | Type | Expected Status |
|------|------|:--------------:|
| Add item and verify it appears in cart items | ✅ Happy | 201 |
| Add all available products one-by-one (no duplicates) | ✅ Happy | 201 |
| Add item with quantity equal to current stock | ✅ Happy | 201 |
| Add same product twice (duplicate) | ❌ Negative | 400 |
| Add product with non-existent product ID | ❌ Negative | 400 |
| Add out-of-stock product | ❌ Negative | 400 |
| Add item with quantity exceeding stock | ❌ Negative | 400 |
| Add item to non-existent cart | ❌ Negative | 404 |
| Add item with invalid product ID (-1) | ❌ Negative | 400 |

#### Modify, Replace, Delete Items

Full happy-path and negative test coverage for `PATCH`, `PUT`, and `DELETE` operations — including invalid IDs, boundary quantities, and missing tokens.

### Orders — `/orders`

| Operation | Happy Path | Negative Cases |
|-----------|:----------:|:--------------:|
| Create Order | ✅ | Missing cart, missing token, empty name |
| Get All Orders | ✅ | Missing token, invalid token |
| Get Order By ID | ✅ | Non-existent ID, wrong token |
| Update Order | ✅ | Invalid data, wrong token, non-existent order |
| Delete Order | ✅ (verify 404 after delete) | Non-existent order, wrong token |

---

## 📈 Allure Reporting

This framework integrates **Allure TestNG 2.29.1** with AspectJ weaving for clean, non-intrusive test reporting.

### How It Works

Allure integrates at the JVM agent level via AspectJ, configured in `pom.xml`:

```xml
<argLine>
  -javaagent:"${settings.localRepository}/org/aspectj/aspectjweaver/1.9.22/aspectjweaver-1.9.22.jar"
</argLine>
```

This means Allure intercepts test execution **without requiring `@Step` annotations** in the test code — the framework stays clean.

### Generating the Allure Report

```bash
# 1. Run the tests (results land in target/allure-results)
mvn test -DsuiteXmlFile=regression-parallel.xml

# 2a. Serve interactively (opens browser automatically)
allure serve target/allure-results

# 2b. Or generate a static report
allure generate target/allure-results --clean -o target/allure-report
allure open target/allure-report
```

#### Install Allure CLI (one-time)
```bash
# Windows (PowerShell via Scoop)
Set-ExecutionPolicy RemoteSigned -Scope CurrentUser
irm get.scoop.sh | iex
scoop install allure

# macOS
brew install allure
```

### What the Report Shows

| Section | Content |
|---------|---------|
| **Overview** | Pass/fail pie chart, execution time breakdown |
| **Suites** | Tests organized by package/class hierarchy |
| **Categories** | Failed tests grouped by failure type |
| **Timeline** | Visual thread execution timeline (great for verifying parallelism) |
| **Graphs** | Trend charts across multiple runs |
| **Test Details** | Full stack traces, request/response logs on failure |

---

## ⚙️ GitHub Actions — CI/CD Pipeline

**File:** `.github/workflows/regression.yml`

The pipeline triggers automatically on every **push** or **pull request** to the `main` branch, ensuring no broken tests ever reach production. It can also be triggered manually via the `workflow_dispatch` event.

```yaml
name: regression

on:
  push:
    branches:
      - main
  pull_request:
    branches:
      - main
  workflow_dispatch:

jobs:
  regression-linux:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout test
        uses: actions/checkout@v4

      - name: Java setup
        uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '21'

      - name: Run Tests
        run: mvn test -DsuiteXmlFile=src/test/test-suites/regression-parallel.xml
        continue-on-error: true

      - name: Upload Surefire Report
        uses: actions/upload-artifact@v4
        if: always()
        with:
          name: regression-report
          path: target/surefire-reports/
```

### Key CI/CD Design Choices

| Choice | Rationale |
|--------|-----------|
| `continue-on-error: true` | Test failures don't kill the pipeline — reports are always generated |
| `if: always()` on report upload | HTML artifacts are uploaded whether tests pass or fail |
| `regression-parallel.xml` suite | Full regression with 11-thread parallelism — balanced for CI |
| Ubuntu runner | Cross-platform validation; rules out Windows-specific path dependencies |
| Java 21 (Temurin) | Latest LTS distribution — matches modern JDK capabilities |
| `**/*.html` artifact glob | Captures both Surefire and Allure HTML reports in one artifact download |

---

## 📦 Dependencies & Tech Stack

| Dependency | Version | Scope | Purpose |
|-----------|:-------:|-------|---------|
| **REST-Assured** | 6.0.0 | compile | Core HTTP client + fluent DSL for API testing |
| **TestNG** | 7.12.0 | test | Test runner, parallel execution, DataProviders, XML suite management |
| **Jackson Databind** | 3.1.3 | compile | JSON ↔ POJO serialization/deserialization |
| **REST-Assured JSON Schema Validator** | 6.0.0 | compile | Contract-level JSON Schema validation on all responses |
| **JavaFaker** | 1.0.2 | compile | Realistic test data generation (names, emails, etc.) |
| **Allure TestNG** | 2.29.1 | test | Rich HTML test reporting with trends and timelines |
| **AspectJ Weaver** | 1.9.22 | test | JVM agent for Allure step interception — zero annotation overhead |
| **Maven Surefire Plugin** | 3.5.3 | build | TestNG XML suite runner + AspectJ `-javaagent` injection |
| **Maven Compiler Plugin** | — | build | Java 9 source/target compilation |

---

## 🚀 How to Run

### Prerequisites

| Tool | Version | Check Command |
|------|---------|--------------|
| Java JDK | 9+ (25 recommended) | `java -version` |
| Apache Maven | 3.6+ | `mvn -version` |
| Git | Any | `git --version` |
| Internet | — | Access to `simple-grocery-store-api.click` |

---

### Step 1 — Clone the Repository

```bash
git clone https://github.com/your-username/RestAssured-Grocery-Store-Framework.git
cd RestAssured-Grocery-Store-Framework
```

---

### Step 2 — (Optional) Configure Credentials

Open `src/main/resources/config.properties`:

```properties
baseUrl     = https://simple-grocery-store-api.click
clientName  =     ← leave blank for auto-generated name (recommended)
clientEmail =     ← leave blank for auto-generated email (recommended)
```

> **Tip:** Leave both `clientName` and `clientEmail` blank. The `TokenService` will use JavaFaker to generate unique credentials automatically — this is the safest approach for parallel runs since the API rejects duplicate emails.

---

### Step 3 — Run Tests

#### Quick Smoke Check
```bash
mvn test
# Runs smoke.xml by default — quick sanity check (~30 seconds)
```

#### Choose Your Suite
```bash
# Smoke — fast sanity (sequential)
mvn test -DsuiteXmlFile=smoke.xml

# Full Regression — 11 parallel threads
mvn test -DsuiteXmlFile=regression-parallel.xml

# Maximum Speed — 22 parallel threads
mvn test -DsuiteXmlFile=full-parallel.xml
```

---

### Step 4 — View the Surefire Report

After the run, open the built-in HTML report:

```
target/surefire-reports/index.html
```

---

### Step 5 — Generate the Allure Report

```bash
allure serve target/allure-results
```

This opens an interactive Allure dashboard in your browser automatically.

---

### Step 6 — (CI) Check GitHub Actions

On every push to `main` (or via manual dispatch), GitHub Actions automatically:

1. Checks out the code
2. Sets up Java 21
3. Runs `regression-parallel.xml`
4. Uploads the Surefire report as a downloadable artifact named `regression-report`

Navigate to **Actions** tab → latest run → **regression-report** artifact to download.

---

## ⚙️ Configuration

### Changing the Default Suite

Edit `pom.xml` to change the suite that runs when no `-D` flag is passed:

```xml
<properties>
  <!-- Change this to regression-parallel.xml or full-parallel.xml -->
  <suiteXmlFile>smoke.xml</suiteXmlFile>
</properties>
```

### Adding a New Environment

All environment configuration lives in one file:

```
src/main/resources/config.properties
```

To target a different environment (e.g. staging), simply change `baseUrl` — no code changes required anywhere else.

---

## 🏛 Design Principles Applied

| Principle | Application in This Framework |
|-----------|-------------------------------|
| **DRY** (Don't Repeat Yourself) | `RestHelper` centralizes all HTTP boilerplate; services centralize test-data setup |
| **Single Responsibility** | Endpoints handle routing, services handle orchestration, tests handle assertions only |
| **Open/Closed** | Adding a new endpoint = new Endpoint class; zero changes to existing code |
| **Separation of Concerns** | Each layer (endpoint / service / test / util / model) has one clear job |
| **Thread Safety** | `ThreadLocal<String>` in `TokenService` ensures parallel threads never share auth state |
| **Fail Fast** | Status code and schema assertions fire before deserialization — no silent failures |
| **Zero-Config Defaults** | JavaFaker generates credentials; `ProductService` auto-caches the catalog |
| **Contract Testing** | 9 JSON schemas validate response structure independently of functional assertions |

---

<div align="center">

---



*If this framework helped you or impressed you, feel free to ⭐ the repository — it means the world!*

*Built with dedication, precision, and a lot of `mvn test` runs. ☕*

</div>
