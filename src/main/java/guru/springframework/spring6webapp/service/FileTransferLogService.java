package guru.springframework.spring6webapp.service;

import guru.springframework.spring6webapp.entity.FileTransferLog;
import guru.springframework.spring6webapp.repository.FileTransferLogRepository;
import guru.springframework.spring6webapp.util.FileChecksumUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileTransferLogService {

    private final FileTransferLogRepository fileTransferLogRepository;
    private final FileChecksumUtil fileChecksumUtil;

    public FileTransferLog createFileTransferLog(FileTransferLog fileTransferLog) {
        try {
            // Calculate checksum if source path is provided
            if (fileTransferLog.getSourcePath() != null && !fileTransferLog.getSourcePath().isEmpty()) {
                String calculatedChecksum = fileChecksumUtil.calculateFileChecksum(fileTransferLog.getSourcePath());
                fileTransferLog.setFileChecksum(calculatedChecksum);
                
                // Check if file with same checksum already exists
                Optional<FileTransferLog> existingFile = fileTransferLogRepository.findExistingByChecksum(calculatedChecksum);
                
                if (existingFile.isPresent()) {
                    fileTransferLog.setStatus("DUPLICATE");
                    fileTransferLog.setErrorMessage("File with same checksum already exists: " + existingFile.get().getFileId());
                    log.warn("Duplicate file detected with checksum: {}", calculatedChecksum);
                } else {
                    fileTransferLog.setStatus("PENDING");
                    log.info("New file transfer log created with checksum: {}", calculatedChecksum);
                }
            } else {
                // If no source path, set status to PENDING
                fileTransferLog.setStatus("PENDING");
                log.warn("No source path provided for file transfer log");
            }
            
            return fileTransferLogRepository.save(fileTransferLog);
            
        } catch (IOException e) {
            fileTransferLog.setStatus("FAILED");
            fileTransferLog.setErrorMessage("Failed to calculate checksum: " + e.getMessage());
            log.error("Error calculating checksum for file: {}", fileTransferLog.getSourcePath(), e);
            return fileTransferLogRepository.save(fileTransferLog);
        } catch (Exception e) {
            fileTransferLog.setStatus("FAILED");
            fileTransferLog.setErrorMessage("Unexpected error: " + e.getMessage());
            log.error("Unexpected error creating file transfer log", e);
            return fileTransferLogRepository.save(fileTransferLog);
        }
    }

    public List<FileTransferLog> getAllFileTransferLogs() {
        return fileTransferLogRepository.findAll();
    }

    public Optional<FileTransferLog> getFileTransferLogById(Long fileId) {
        return fileTransferLogRepository.findById(fileId);
    }

    public List<FileTransferLog> getFileTransferLogsByStatus(String status) {
        return fileTransferLogRepository.findByStatus(status);
    }

    public List<FileTransferLog> getFileTransferLogsByJobId(String jobId) {
        return fileTransferLogRepository.findByJobId(jobId);
    }

    public Optional<FileTransferLog> getFileTransferLogByChecksum(String checksum) {
        return fileTransferLogRepository.findByFileChecksum(checksum);
    }

    public FileTransferLog updateFileTransferLog(Long fileId, FileTransferLog updatedLog) {
        return fileTransferLogRepository.findById(fileId)
                .map(existingLog -> {
                    // Update fields but preserve ID and timestamps
                    existingLog.setDirection(updatedLog.getDirection());
                    existingLog.setSourcePath(updatedLog.getSourcePath());
                    existingLog.setSourceFileName(updatedLog.getSourceFileName());
                    existingLog.setDestinationPath(updatedLog.getDestinationPath());
                    existingLog.setFileChecksum(updatedLog.getFileChecksum());
                    existingLog.setStatus(updatedLog.getStatus());
                    existingLog.setErrorMessage(updatedLog.getErrorMessage());
                    existingLog.setSiteNo(updatedLog.getSiteNo());
                    existingLog.setCountryCode(updatedLog.getCountryCode());
                    existingLog.setJobId(updatedLog.getJobId());
                    existingLog.setMessageId(updatedLog.getMessageId());
                    existingLog.setProcessPrefix(updatedLog.getProcessPrefix());
                    existingLog.setProcessStatus(updatedLog.getProcessStatus());
                    existingLog.setUploadAttempts(updatedLog.getUploadAttempts());
                    existingLog.setProcessControl(updatedLog.getProcessControl());
                    
                    return fileTransferLogRepository.save(existingLog);
                })
                .orElseThrow(() -> new RuntimeException("FileTransferLog not found with id: " + fileId));
    }

    public void deleteFileTransferLog(Long fileId) {
        fileTransferLogRepository.deleteById(fileId);
    }
}