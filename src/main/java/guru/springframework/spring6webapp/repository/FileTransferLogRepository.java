package guru.springframework.spring6webapp.repository;

import guru.springframework.spring6webapp.entity.FileTransferLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileTransferLogRepository extends JpaRepository<FileTransferLog, Long> {
    
    Optional<FileTransferLog> findByFileChecksum(String fileChecksum);
    
    List<FileTransferLog> findBySourceFileName(String sourceFileName);
    
    @Query("SELECT f FROM FileTransferLog f WHERE f.fileChecksum = :checksum AND f.status != 'FAILED'")
    Optional<FileTransferLog> findExistingByChecksum(@Param("checksum") String checksum);
    
    List<FileTransferLog> findByStatus(String status);
    
    List<FileTransferLog> findByJobId(String jobId);
}