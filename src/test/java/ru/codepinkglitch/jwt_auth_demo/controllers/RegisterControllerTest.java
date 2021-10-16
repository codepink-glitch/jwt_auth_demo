package ru.codepinkglitch.jwt_auth_demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.codepinkglitch.jwt_auth_demo.dtos.in.UserDetailsIn;
import ru.codepinkglitch.jwt_auth_demo.repositories.UserDetailsRepository;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RegisterControllerTest {

    private static final String URI = "/registration";
    private static final String TEST_USER_USERNAME = "randomUsername";
    private static final String TEST_USER_PASSWORD = "randomPassword";

    @Rule
    public JUnitRestDocumentation restDocumentation =
            new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserDetailsRepository userDetailsRepository;

    MockMvc mockMvc;

    @Before
    public void setup() {
        ConfigurableMockMvcBuilder builder =
                MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                        .apply(documentationConfiguration(this.restDocumentation))
                        .apply(springSecurity());

        this.mockMvc = builder.build();
    }

    @Test
    public void registerTest() throws Exception {
        UserDetailsIn userDetailsIn = new UserDetailsIn();
        userDetailsIn.setUsername(TEST_USER_USERNAME);
        userDetailsIn.setPassword(TEST_USER_PASSWORD);

        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDetailsIn)))
                .andDo(document(URI.replace("/", "\\")))
                .andExpect(jsonPath("$.username").value(TEST_USER_USERNAME))
                .andExpect(status().isOk());

        Assert.assertTrue(userDetailsRepository.existsMyUserDetailsByUsername(TEST_USER_USERNAME));
    }

}
