# Swagger UI Access Guide

## Problem
Getting 404 when accessing `http://localhost:8081/swagger-ui/index.html`

## Root Cause
Your application has `server.servlet.context-path=/api/v1.0` configured, which means:
- All endpoints are prefixed with `/api/v1.0`
- The Swagger UI is served under this context path

## Solution

### **Correct Swagger UI URL:**
```
http://localhost:8081/api/v1.0/swagger-ui.html
```

### Alternative URLs:
- `http://localhost:8081/api/v1.0/swagger-ui/index.html` (may also work)
- `http://localhost:8081/api/v1.0/swagger-ui/` (redirect to index.html)

### API Docs URL:
```
http://localhost:8081/api/v1.0/v3/api-docs
```

## SecurityConfig Changes Required
Yes, you need to configure the following in SecurityConfig to allow unauthenticated access:

```java
.requestMatchers(
    // ... existing endpoints ...
    // Swagger/OpenAPI endpoints
    "/v3/api-docs",
    "/v3/api-docs.yaml",
    "/swagger-ui.html",
    "/swagger-ui/**",
    "/webjars/**"
).permitAll()
```

These are already configured in your SecurityConfig ✅

## Steps Taken
1. ✅ Added SpringDoc OpenAPI dependency to pom.xml
2. ✅ Created SwaggerConfig.java with OpenAPI bean configuration
3. ✅ Added Swagger annotations to controllers (AuthController, ProfileController)
4. ✅ Updated SecurityConfig to permit Swagger endpoints
5. ✅ Added Swagger properties to application.properties

## Next Steps
1. Rebuild the project: `mvn clean package`
2. Run the application
3. Access Swagger UI at: **http://localhost:8081/api/v1.0/swagger-ui.html**

