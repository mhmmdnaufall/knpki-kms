package org.knpkid.kms.service;

import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.model.AdminResponse;
import org.knpkid.kms.model.RegisterAdminRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AdminService extends UserDetailsService {

    AdminResponse get(Admin admin);

    void register(RegisterAdminRequest request);

}
