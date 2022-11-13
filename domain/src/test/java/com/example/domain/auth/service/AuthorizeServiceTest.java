package com.example.domain.auth.service;

import com.example.domain.iam.auth.AuthorizeContextHolder;
import com.example.domain.iam.auth.exception.AuthorizeException;
import com.example.domain.iam.auth.model.Authorize;
import com.example.domain.common.BaseException;
import com.example.domain.iam.auth.service.AuthorizeService;
import com.example.domain.iam.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class AuthorizeServiceTest {

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private AuthorizeService authorizeService;

    @Test
    void should_create_authorize_failed_when_password_is_wrong() {
        //given
        User user = User.build("any", "any", "password");
        String password = "wrongTestPassword";
        Mockito.when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(false);

        BaseException exception = assertThrows(AuthorizeException.class, () -> {
            //when
            authorizeService.create(user, password);
        });

        assertEquals("invalid_credential", exception.getMessage());
        assertEquals(BaseException.Type.UNAUTHORIZED, exception.getType());
    }

    @Test
    void should_fetch_authorize_when_authorize_exist() {
        //given
        Authorize authorize = Authorize.build("test-user-id", User.Role.USER);
        AuthorizeContextHolder.setContext(authorize);
        //when
        Authorize current = authorizeService.getCurrent();
        //then
        assertEquals(authorize.getId(), current.getId());
        AuthorizeContextHolder.setContext(null);
    }

    @Test
    void should_fetch_authorize_failed_when_authorize_is_not_exist() {
        //when
        BaseException exception = assertThrows(AuthorizeException.class, () -> {
            //when
            authorizeService.getOperator();
        });

        assertEquals("unauthorized", exception.getMessage());
        assertEquals(BaseException.Type.UNAUTHORIZED, exception.getType());
    }

}
