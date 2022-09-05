package pl.urbanskilukasz.onlineLibrary.uploads.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.urbanskilukasz.onlineLibrary.uploads.application.port.UploadUseCase;
import pl.urbanskilukasz.onlineLibrary.uploads.db.UploadJpaRepository;
import pl.urbanskilukasz.onlineLibrary.uploads.domain.Upload;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UploadService implements UploadUseCase {

    private final UploadJpaRepository repository;
    @Override
    public Upload save(SaveUploadCommand command) {
        Upload upload = new Upload(
                command.getFileName(),
                command.getContentType(),
                command.getFile()
                );
        repository.save(upload);
        System.out.println("Upload saved: " + upload.getFileName() + " with id: " + upload.getId());
        return upload;
    }

    @Override
    public Optional<Upload> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void removeById(Long id) {
        repository.deleteById(id);
    }
}
