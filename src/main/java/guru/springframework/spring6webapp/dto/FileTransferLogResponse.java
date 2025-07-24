package guru.springframework.spring6webapp.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FileTransferLogResponse {
    private Long fileId;
    private String direction;
    private String sourcePath;
    private String sourceFileName;
    private String destinationPath;
    private String fileChecksum;
    private String status;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private String errorMessage;
    private String siteNo;
    private String countryCode;
    private String jobId;
    private String messageId;
    private String processPrefix;
    private String processStatus;
    private Integer uploadAttempts;
    private String processControl;
    private boolean isDuplicate;
    private String message;
}