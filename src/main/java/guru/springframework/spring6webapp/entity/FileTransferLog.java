package guru.springframework.spring6webapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "file_transfer_queue")
@Data
public class FileTransferLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @PrePersist
    protected void onCreate() {
        createdOn = LocalDateTime.now();
        updatedOn = LocalDateTime.now();
        if (uploadAttempts == null) {
            uploadAttempts = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedOn = LocalDateTime.now();
    }
}