package pl.urbanskilukasz.onlineLibrary.uploads.application.port;

import lombok.AllArgsConstructor;
import lombok.Value;
import pl.urbanskilukasz.onlineLibrary.uploads.domain.Upload;

import java.util.Optional;

public interface UploadUseCase {

    Upload save (SaveUploadCommand command);

    Optional<Upload> getById(String id);

    void removeById(String coverId);

    @Value
    @AllArgsConstructor
    class SaveUploadCommand {
        String fileName;
        byte[] file;
        String contentType;
    }
}
