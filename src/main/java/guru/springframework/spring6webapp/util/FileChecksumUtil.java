package guru.springframework.spring6webapp.util;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class FileChecksumUtil {

    public String calculateFileChecksum(String filePath) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            Path path = Path.of(filePath);
            
            if (!Files.exists(path)) {
                throw new IOException("File not found: " + filePath);
            }
            
            try (InputStream is = Files.newInputStream(path)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                
                while ((bytesRead = is.read(buffer)) != -1) {
                    digest.update(buffer, 0, bytesRead);
                }
            }
            
            byte[] hashBytes = digest.digest();
            StringBuilder hexString = new StringBuilder();
            
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
    
    public String calculateFileChecksum(Path filePath) throws IOException {
        return calculateFileChecksum(filePath.toString());
    }
}