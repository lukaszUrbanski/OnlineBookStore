package pl.urbanskilukasz.onlineLibrary.uploads.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Upload {

    String id;
    byte[] file;
    String fileName;
    String contentType;
    LocalDateTime createdAt;
}
