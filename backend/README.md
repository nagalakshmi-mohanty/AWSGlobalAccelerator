# Reaction Challenge Backend — Spring Boot API

A lightweight, hackathon-ready Spring Boot backend for storing reaction times, computing analytics, and serving multi-region AWS EC2 infrastructure metadata.

---

## 🛠 Prerequisites

- **Java Development Kit (JDK 17 or higher)**
- **Apache Maven (3.8+)**
- **PostgreSQL Database Server (13+)**

---

## 🚀 Local Database Setup (PostgreSQL)

1. Start your local PostgreSQL server:
   ```bash
   # On macOS (Homebrew)
   brew services start postgresql
   
   # On Linux / Windows
   sudo service postgresql start
   ```

2. Open PostgreSQL prompt and create the database:
   ```sql
   CREATE DATABASE reaction_challenge;
   ```

3. Default credentials expected by the backend:
   - **Database**: `reaction_challenge`
   - **User**: `postgres`
   - **Password**: `postgres`
   - **URL**: `jdbc:postgresql://localhost:5432/reaction_challenge`

*(Note: Table creation is automated by Spring Data JPA via `spring.jpa.hibernate.ddl-auto=update`.)*

---

## ⚙️ Environment Variables

The backend uses environment variables with sensible defaults for local development:

| Variable | Default Value | Description |
| :--- | :--- | :--- |
| `SERVER_PORT` | `8080` | Port where Spring Boot runs |
| `DB_URL` | `jdbc:postgresql://localhost:5432/reaction_challenge` | PostgreSQL JDBC connection URL |
| `DB_USERNAME` | `postgres` | Database username |
| `DB_PASSWORD` | `postgres` | Database password |
| `SERVER_REGION` | `local` | AWS Region code (e.g. `ap-south-1` or `us-east-1`) |
| `SERVER_ID` | `local-server` | Instance identifier (e.g. `mumbai-01` or `usa-01`) |
| `GLOBAL_ACCELERATOR_ENABLED` | `false` | Set `true` when running behind AWS Global Accelerator |
| `CORS_ORIGINS` | `http://localhost:5173` | Allowed frontend origin URLs (comma-separated) |

---

## 💻 Running the Backend Locally

### Option A: Using Maven
```bash
cd backend
mvn spring-boot:run
```

### Option B: Package & Run Executable JAR
```bash
cd backend
mvn clean package
java -jar target/reaction-challenge-backend-0.0.1-SNAPSHOT.jar
```

---

## 🧪 Testing Endpoints Locally

### 1. Health Check
```bash
curl http://localhost:8080/api/health
```
**Response:**
```json
{
  "status": "UP",
  "region": "local",
  "server": "local-server",
  "timestamp": "2026-10-03T17:30:00Z"
}
```

### 2. Record Reaction Attempt
```bash
curl -X POST http://localhost:8080/api/reactions \
  -H "Content-Type: application/json" \
  -d '{
    "sessionId": "sess-demo123",
    "roundNumber": 1,
    "reactionTime": 142,
    "targetShownAt": "2026-10-03T17:30:00.000Z",
    "clickedAt": "2026-10-03T17:30:00.142Z"
  }'
```

### 3. Get Analytics
```bash
curl http://localhost:8080/api/analytics
```

### 4. Lightweight Network Test
```bash
curl http://localhost:8080/api/network-test
```

### 5. Recent Reactions (Debug Log)
```bash
curl http://localhost:8080/api/reactions/recent
```

---

## ☁️ AWS EC2 & Global Accelerator Deployment

### 1. Build Executable JAR
On your build server or locally:
```bash
mvn clean package -DskipTests
```
The output file is generated at `target/reaction-challenge-backend-0.0.1-SNAPSHOT.jar`.

### 2. Deploy to EC2 Instances

#### **Mumbai Region (`ap-south-1`)**:
```bash
export SERVER_PORT=8080
export DB_URL=jdbc:postgresql://your-rds-endpoint.rds.amazonaws.com:5432/reaction_challenge
export DB_USERNAME=dbmaster
export DB_PASSWORD=yourpassword
export SERVER_REGION=ap-south-1
export SERVER_ID=mumbai-01
export GLOBAL_ACCELERATOR_ENABLED=true
export CORS_ORIGINS=http://your-frontend-domain.com

java -jar reaction-challenge-backend-0.0.1-SNAPSHOT.jar
```

#### **USA Region (`us-east-1`)**:
```bash
export SERVER_PORT=8080
export DB_URL=jdbc:postgresql://your-rds-endpoint.rds.amazonaws.com:5432/reaction_challenge
export DB_USERNAME=dbmaster
export DB_PASSWORD=yourpassword
export SERVER_REGION=us-east-1
export SERVER_ID=usa-01
export GLOBAL_ACCELERATOR_ENABLED=true
export CORS_ORIGINS=http://your-frontend-domain.com

java -jar reaction-challenge-backend-0.0.1-SNAPSHOT.jar
```

### 3. AWS Architecture Verification
1. Configure **Application Load Balancer (ALB)** target groups pointing to `/api/health` port `8080`.
2. Connect **AWS Global Accelerator** to the Mumbai ALB and USA ALB endpoints.
3. Verify failover by stopping the Mumbai EC2 instance: AWS Global Accelerator automatically redirects inbound traffic to `us-east-1` (USA)!
