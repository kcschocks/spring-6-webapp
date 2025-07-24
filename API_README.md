# File Transfer Log API

This Spring Boot application provides a REST API for managing file transfer logs with automatic checksum validation and duplicate detection.

## Features

- **File Transfer Log Management**: Create, read, update, and delete file transfer logs
- **Automatic Checksum Calculation**: SHA-256 checksum calculation for files
- **Duplicate Detection**: Automatically detects duplicate files based on checksum
- **Status Management**: Tracks file transfer status (PENDING, DUPLICATE, FAILED, etc.)
- **H2 Database**: In-memory database for development and testing

## Prerequisites

- Java 21
- Maven 3.6+

## Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## API Endpoints

### Base URL
```
http://localhost:8080/api/file-transfer-logs
```

### 1. Create File Transfer Log
**POST** `/api/file-transfer-logs`

Creates a new file transfer log entry. The system will:
- Calculate SHA-256 checksum of the file at `sourcePath`
- Check for existing files with the same checksum
- Set status to `PENDING` (new file) or `DUPLICATE` (existing file)

**Request Body:**
```json
{
  "direction": "UPLOAD",
  "sourcePath": "/path/to/source/file.txt",
  "sourceFileName": "file.txt",
  "destinationPath": "/path/to/destination/",
  "siteNo": "SITE001",
  "countryCode": "US",
  "jobId": "JOB123",
  "messageId": "MSG456",
  "processPrefix": "PROC",
  "processStatus": "INITIATED",
  "processControl": "CONTROL001"
}
```

**Response:**
```json
{
  "fileId": 1,
  "direction": "UPLOAD",
  "sourcePath": "/path/to/source/file.txt",
  "sourceFileName": "file.txt",
  "destinationPath": "/path/to/destination/",
  "fileChecksum": "a1b2c3d4e5f6...",
  "status": "PENDING",
  "createdOn": "2024-01-15T10:30:00",
  "updatedOn": "2024-01-15T10:30:00",
  "errorMessage": null,
  "siteNo": "SITE001",
  "countryCode": "US",
  "jobId": "JOB123",
  "messageId": "MSG456",
  "processPrefix": "PROC",
  "processStatus": "INITIATED",
  "uploadAttempts": 0,
  "processControl": "CONTROL001",
  "isDuplicate": false,
  "message": "File transfer log created successfully"
}
```

### 2. Get All File Transfer Logs
**GET** `/api/file-transfer-logs`

Returns all file transfer logs.

### 3. Get File Transfer Log by ID
**GET** `/api/file-transfer-logs/{fileId}`

Returns a specific file transfer log by its ID.

### 4. Get File Transfer Logs by Status
**GET** `/api/file-transfer-logs/status/{status}`

Returns all file transfer logs with the specified status (e.g., PENDING, DUPLICATE, FAILED).

### 5. Get File Transfer Logs by Job ID
**GET** `/api/file-transfer-logs/job/{jobId}`

Returns all file transfer logs for a specific job.

### 6. Get File Transfer Log by Checksum
**GET** `/api/file-transfer-logs/checksum/{checksum}`

Returns a file transfer log by its checksum.

### 7. Update File Transfer Log
**PUT** `/api/file-transfer-logs/{fileId}`

Updates an existing file transfer log.

### 8. Delete File Transfer Log
**DELETE** `/api/file-transfer-logs/{fileId}`

Deletes a file transfer log.

### 9. Health Check
**GET** `/api/file-transfer-logs/health`

Returns the health status of the API.

## Status Values

- **PENDING**: New file transfer log created successfully
- **DUPLICATE**: File with same checksum already exists
- **FAILED**: Error occurred during processing
- **COMPLETED**: File transfer completed successfully
- **IN_PROGRESS**: File transfer in progress

## Database

The application uses H2 in-memory database for development. You can access the H2 console at:
```
http://localhost:8080/h2-console
```

**Database Configuration:**
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## Error Handling

The API includes comprehensive error handling:
- File not found errors when calculating checksums
- Database constraint violations
- Invalid input data
- General runtime exceptions

All errors are logged and appropriate HTTP status codes are returned.

## Example Usage with curl

```bash
# Create a new file transfer log
curl -X POST http://localhost:8080/api/file-transfer-logs \
  -H 'Content-Type: application/json' \
  -d '{
    "direction": "UPLOAD",
    "sourcePath": "/path/to/file.txt",
    "sourceFileName": "file.txt",
    "destinationPath": "/destination/",
    "siteNo": "SITE001",
    "countryCode": "US",
    "jobId": "JOB123"
  }'

# Get all logs
curl -X GET http://localhost:8080/api/file-transfer-logs

# Get logs by status
curl -X GET http://localhost:8080/api/file-transfer-logs/status/PENDING

# Get log by ID
curl -X GET http://localhost:8080/api/file-transfer-logs/1
```

## Project Structure

```
src/main/java/guru/springframework/spring6webapp/
├── controller/
│   └── FileTransferLogController.java
├── service/
│   └── FileTransferLogService.java
├── repository/
│   └── FileTransferLogRepository.java
├── entity/
│   └── FileTransferLog.java
├── dto/
│   ├── FileTransferLogRequest.java
│   └── FileTransferLogResponse.java
├── util/
│   └── FileChecksumUtil.java
└── Spring6WebappApplication.java
```

## Dependencies

- Spring Boot 3.3.0-RC1
- Spring Data JPA
- Spring Web
- H2 Database
- Lombok
- Java 21