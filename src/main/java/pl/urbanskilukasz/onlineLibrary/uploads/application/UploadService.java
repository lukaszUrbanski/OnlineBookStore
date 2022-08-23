package pl.urbanskilukasz.onlineLibrary.uploads.application;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import pl.urbanskilukasz.onlineLibrary.uploads.application.port.UploadUseCase;
import pl.urbanskilukasz.onlineLibrary.uploads.domain.Upload;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UploadService implements UploadUseCase {

    private final Map<String, Upload> storage = new ConcurrentHashMap<>();
    @Override
    public Upload save(SaveUploadCommand command) {
        String newId = RandomStringUtils.randomAlphanumeric(8);
        Upload upload = new Upload(
                newId,
                command.getFile(),
                command.getFileName(),
                command.getContentType(),
                LocalDateTime.now()
        );
        storage.put(upload.getId(), upload);
        System.out.println("Upload saved: " + upload.getFileName() + " with id: " + upload.getId());
        return upload;
    }
}
