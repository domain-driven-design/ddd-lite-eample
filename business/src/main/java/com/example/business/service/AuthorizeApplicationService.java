package com.example.business.service;

import com.example.business.common.UserCriteria;
import com.example.business.usecase.authorize.GetUserProfileCase;
import com.example.business.usecase.authorize.LoginCase;
import com.example.domain.iam.auth.model.Authorize;
import com.example.domain.iam.auth.service.AuthorizeService;
import com.example.domain.iam.user.model.User;
import com.example.domain.iam.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizeApplicationService {
    @Autowired
    private AuthorizeService service;

    @Autowired
    private UserService userService;

    public LoginCase.Response login(LoginCase.Request request) {
        User user = userService.get(UserCriteria.ofEmail(request.getEmail()));

        Authorize authorize = service.create(user, request.getPassword());
        return LoginCase.Response.from(authorize);
    }

    public void logout() {
        Authorize authorize = service.getCurrent();
        service.delete(authorize.getId());
    }

    public GetUserProfileCase.Response getProfile() {
        Authorize authorize = service.getCurrent();
        return GetUserProfileCase.Response.from(authorize);
    }
}
