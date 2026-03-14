# 🚀 Swagger Setup - Testing Guide

## **Step 1: Build the Project**
```bash
cd "D:\A SpringBootProject\phase 1\SpringBoot-JWT"
mvnw clean package -DskipTests
```

## **Step 2: Run the Application**
```bash
mvnw spring-boot:run
```

Or run the JAR:
```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

## **Step 3: Access Swagger UI**

### **URL:**
```
http://localhost:8081/api/v1.0/swagger-ui.html
```

You should see the Swagger interface with all your API endpoints documented.

---

## **Step 4: Test Endpoints in Swagger**

### **1. Register User (No Auth Required)**
- Endpoint: `POST /register`
- Body:
```json
{
  "email": "user@example.com",
  "password": "password123",
  "name": "John Doe"
}
```

### **2. Login (No Auth Required)**
- Endpoint: `POST /login`
- Body:
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```
- Response will include JWT token

### **3. Get Profile (Auth Required)**
1. Copy the JWT token from login response
2. Click "Authorize" button in Swagger UI
3. Enter: `Bearer <your_jwt_token>`
4. Call `GET /profile` endpoint

---

## **Troubleshooting**

### Still Getting 404?
- ✅ Check URL: `http://localhost:8081/api/v1.0/swagger-ui.html` (with context path)
- ✅ Rebuild project: `mvnw clean package -DskipTests`
- ✅ Check console for errors
- ✅ Verify SwaggerConfig has `@Bean` annotation

### Swagger loads but no endpoints?
- ✅ Check that controllers have `@Tag` and `@Operation` annotations
- ✅ Rebuild project

### Authentication not working in Swagger?
- ✅ Click "Authorize" button
- ✅ Enter: `Bearer <jwt_token_here>`
- ✅ Make sure token is valid

---

## **Swagger UI Features**

Once loaded, you can:
- 📚 View all API endpoints
- 🧪 Test endpoints directly from the UI
- 🔒 Add JWT authentication via "Authorize" button
- 📖 See request/response examples
- 🏷️ Filter endpoints by tags (Authentication, Profile, etc.)

---

## **Expected Result**

You should see all these endpoint categories:
- **Authentication** (POST /login, POST /register, POST /reset-password, etc.)
- **Profile** (GET /profile, POST /profile, etc.)

All endpoints should be properly documented with descriptions.

