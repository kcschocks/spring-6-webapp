package guru.springframework.spring6webapp.controller;

import guru.springframework.spring6webapp.dto.FileTransferLogRequest;
import guru.springframework.spring6webapp.dto.FileTransferLogResponse;
import guru.springframework.spring6webapp.entity.FileTransferLog;
import guru.springframework.spring6webapp.service.FileTransferLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/file-transfer-logs")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class FileTransferLogController {

    private final FileTransferLogService fileTransferLogService;

    @PostMapping
    public ResponseEntity<FileTransferLogResponse> createFileTransferLog(@RequestBody FileTransferLogRequest request) {
        try {
            log.info("Creating new file transfer log for file: {}", request.getSourceFileName());
            
            // Convert request to entity
            FileTransferLog fileTransferLog = new FileTransferLog();
            fileTransferLog.setDirection(request.getDirection());
            fileTransferLog.setSourcePath(request.getSourcePath());
            fileTransferLog.setSourceFileName(request.getSourceFileName());
            fileTransferLog.setDestinationPath(request.getDestinationPath());
            fileTransferLog.setSiteNo(request.getSiteNo());
            fileTransferLog.setCountryCode(request.getCountryCode());
            fileTransferLog.setJobId(request.getJobId());
            fileTransferLog.setMessageId(request.getMessageId());
            fileTransferLog.setProcessPrefix(request.getProcessPrefix());
            fileTransferLog.setProcessStatus(request.getProcessStatus());
            fileTransferLog.setProcessControl(request.getProcessControl());
            
            FileTransferLog createdLog = fileTransferLogService.createFileTransferLog(fileTransferLog);
            
            // Convert to response
            FileTransferLogResponse response = convertToResponse(createdLog);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error creating file transfer log", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<FileTransferLog>> getAllFileTransferLogs() {
        try {
            List<FileTransferLog> logs = fileTransferLogService.getAllFileTransferLogs();
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            log.error("Error retrieving file transfer logs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<FileTransferLog> getFileTransferLogById(@PathVariable Long fileId) {
        try {
            Optional<FileTransferLog> log = fileTransferLogService.getFileTransferLogById(fileId);
            return log.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error retrieving file transfer log with id: {}", fileId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<FileTransferLog>> getFileTransferLogsByStatus(@PathVariable String status) {
        try {
            List<FileTransferLog> logs = fileTransferLogService.getFileTransferLogsByStatus(status);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            log.error("Error retrieving file transfer logs by status: {}", status, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<FileTransferLog>> getFileTransferLogsByJobId(@PathVariable String jobId) {
        try {
            List<FileTransferLog> logs = fileTransferLogService.getFileTransferLogsByJobId(jobId);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            log.error("Error retrieving file transfer logs by job id: {}", jobId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/checksum/{checksum}")
    public ResponseEntity<FileTransferLog> getFileTransferLogByChecksum(@PathVariable String checksum) {
        try {
            Optional<FileTransferLog> log = fileTransferLogService.getFileTransferLogByChecksum(checksum);
            return log.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error retrieving file transfer log by checksum: {}", checksum, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{fileId}")
    public ResponseEntity<FileTransferLog> updateFileTransferLog(@PathVariable Long fileId, 
                                                               @RequestBody FileTransferLog updatedLog) {
        try {
            FileTransferLog updated = fileTransferLogService.updateFileTransferLog(fileId, updatedLog);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            log.error("File transfer log not found with id: {}", fileId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating file transfer log with id: {}", fileId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFileTransferLog(@PathVariable Long fileId) {
        try {
            fileTransferLogService.deleteFileTransferLog(fileId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting file transfer log with id: {}", fileId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("File Transfer Log API is running");
    }

    private FileTransferLogResponse convertToResponse(FileTransferLog entity) {
        FileTransferLogResponse response = new FileTransferLogResponse();
        response.setFileId(entity.getFileId());
        response.setDirection(entity.getDirection());
        response.setSourcePath(entity.getSourcePath());
        response.setSourceFileName(entity.getSourceFileName());
        response.setDestinationPath(entity.getDestinationPath());
        response.setFileChecksum(entity.getFileChecksum());
        response.setStatus(entity.getStatus());
        response.setCreatedOn(entity.getCreatedOn());
        response.setUpdatedOn(entity.getUpdatedOn());
        response.setErrorMessage(entity.getErrorMessage());
        response.setSiteNo(entity.getSiteNo());
        response.setCountryCode(entity.getCountryCode());
        response.setJobId(entity.getJobId());
        response.setMessageId(entity.getMessageId());
        response.setProcessPrefix(entity.getProcessPrefix());
        response.setProcessStatus(entity.getProcessStatus());
        response.setUploadAttempts(entity.getUploadAttempts());
        response.setProcessControl(entity.getProcessControl());
        
        // Set additional response fields
        response.setDuplicate("DUPLICATE".equals(entity.getStatus()));
        if ("DUPLICATE".equals(entity.getStatus())) {
            response.setMessage("File with same checksum already exists");
        } else if ("PENDING".equals(entity.getStatus())) {
            response.setMessage("File transfer log created successfully");
        } else if ("FAILED".equals(entity.getStatus())) {
            response.setMessage("Failed to process file transfer log");
        }
        
        return response;
    }
}