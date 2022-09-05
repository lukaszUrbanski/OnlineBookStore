package pl.urbanskilukasz.onlineLibrary.uploads.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Upload {

    @Id
    @GeneratedValue
    private Long id;
    private byte[] file;
    private String fileName;
    private String contentType;
    @CreatedDate
    private LocalDateTime createdAt;

    public Upload(String fileName, String contentType, byte[] file) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.file = file;
    }

}
