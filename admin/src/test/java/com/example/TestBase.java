package com.example;

import com.example.admin.common.AdminCriteria;
import com.example.config.ResetDbListener;
import com.example.domain.iam.auth.model.Authorize;
import com.example.domain.iam.auth.service.AuthorizeService;
import com.example.domain.iam.user.model.User;
import com.example.domain.iam.user.repository.UserRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = {AdminTestApplication.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        ResetDbListener.class,
        SqlScriptsTestExecutionListener.class,
})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class TestBase {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorizeService authorizeService;

    @BeforeEach
    public void setUp() {
        System.out.println("port:" + port);

        RestAssured.port = port;
        RestAssured.basePath = "/";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    public Authorize prepareAuthorize() {
        User admin = userRepository.findOne(AdminCriteria.ofName("admin"))
                .get();
        return authorizeService.create(admin, "password");
    }

}
