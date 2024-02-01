package org.knpkid.kms.service.impl;

import lombok.AllArgsConstructor;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.model.AdminResponse;
import org.knpkid.kms.repository.AdminRepository;
import org.knpkid.kms.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return adminRepository.findById(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
    }

    @Override
    public AdminResponse get(Admin admin) {
        return new AdminResponse(admin.getUsername(), admin.getName(), admin.getImage());
    }
}
