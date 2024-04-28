package org.knpkid.kms.service;

import org.knpkid.kms.entity.Archive;
import org.springframework.web.multipart.MultipartFile;

public interface ArchiveService {

    Archive save(MultipartFile archive);

    void delete(Archive archive);

}
