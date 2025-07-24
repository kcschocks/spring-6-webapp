package guru.springframework.spring6webapp.dto;

import lombok.Data;

@Data
public class FileTransferLogRequest {
    private String direction;
    private String sourcePath;
    private String sourceFileName;
    private String destinationPath;
    private String siteNo;
    private String countryCode;
    private String jobId;
    private String messageId;
    private String processPrefix;
    private String processStatus;
    private String processControl;
}