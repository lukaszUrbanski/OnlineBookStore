package pl.urbanskilukasz.onlineLibrary.uploads.application.port;

import lombok.AllArgsConstructor;
import lombok.Value;
import pl.urbanskilukasz.onlineLibrary.uploads.domain.Upload;

public interface UploadUseCase {

    Upload save (SaveUploadCommand command);

    @Value
    @AllArgsConstructor
    class SaveUploadCommand {
        String fileName;
        byte[] file;
        String contentType;
    }
}
