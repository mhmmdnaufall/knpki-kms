package org.knpkid.kms.validation;

import lombok.Getter;

public enum FileSize {

    KB(1024),
    MB(1_048_576),
    GB(1_073_741_824);

    @Getter
    private final int size;

    FileSize(int size) {
        this.size = size;
    }
}
