package org.knpkid.kms.service;

import org.knpkid.kms.model.LoginAdminRequest;

public interface AuthService {

    String getLoginToken(LoginAdminRequest request);

}
