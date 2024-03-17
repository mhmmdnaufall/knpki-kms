package org.knpkid.kms.service;

import org.knpkid.kms.entity.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    Image save(MultipartFile image);

    void delete(Image image);

    void deleteAll(Iterable<Image> images);

}
