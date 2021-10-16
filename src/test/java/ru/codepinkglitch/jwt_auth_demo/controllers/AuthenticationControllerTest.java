package ru.codepinkglitch.jwt_auth_demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;
import ru.codepinkglitch.jwt_auth_demo.models.AuthenticationRequest;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@Sql({"classpath:test_schema.sql", "classpath:test_data.sql"})
public class AuthenticationControllerTest {

    private static final String URI = "/authentication";

    @Rule
    public JUnitRestDocumentation restDocumentation =
            new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @Value("${tests_variables.test_username}")
    private String testUserUsername;

    @Value("${tests_variables.test_password}")
    private String testUserPassword;

    @Before
    public void setup() {
        ConfigurableMockMvcBuilder builder =
                MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                        .apply(documentationConfiguration(this.restDocumentation))
                        .apply(springSecurity());

        this.mockMvc = builder.build();
    }

    @Test
    public void acquireTokenTest() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername(testUserUsername);
        authenticationRequest.setPassword(testUserPassword);

        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andDo(document(URI.replace("/", "\\")))
                .andExpect(jsonPath("$.token").exists())
                .andExpect(status().isOk());
    }

    @Test(expected = NestedServletException.class)
    public void acquireTokenWithWrongUserTest() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("wrong_name");
        authenticationRequest.setPassword("wrong_password");

        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andDo(document(URI.replace("/", "\\")))
                .andExpect(status().isInternalServerError());
    }
}
