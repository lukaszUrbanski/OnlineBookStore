package pl.urbanskilukasz.onlineLibrary.uploads.db;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.urbanskilukasz.onlineLibrary.uploads.domain.Upload;

public interface UploadJpaRepository extends JpaRepository<Upload, Long> {
}
