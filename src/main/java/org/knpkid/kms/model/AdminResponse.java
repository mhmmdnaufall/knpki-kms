package org.knpkid.kms.model;

import java.util.Arrays;
import java.util.Objects;

public record AdminResponse(String username, String name, byte[] image) {

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        AdminResponse that = (AdminResponse) object;
        return Objects.equals(username, that.username) && Objects.equals(name, that.name) && Arrays.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(username, name);
        result = 31 * result + Arrays.hashCode(image);
        return result;
    }

    @Override
    public String toString() {
        return "AdminResponse{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
