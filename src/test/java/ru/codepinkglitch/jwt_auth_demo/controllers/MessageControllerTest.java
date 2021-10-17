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
import ru.codepinkglitch.jwt_auth_demo.dtos.in.AuthenticationRequest;
import ru.codepinkglitch.jwt_auth_demo.dtos.in.MessageIn;
import ru.codepinkglitch.jwt_auth_demo.dtos.out.AuthenticationResponse;
import ru.codepinkglitch.jwt_auth_demo.services.AuthenticationService;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@Sql({"classpath:schema.sql", "classpath:test_data.sql", "classpath:test_data_messages.sql"})
public class MessageControllerTest {

    private static final String URI = "/message";
    private static AuthenticationResponse testUserToken;

    @Rule
    public JUnitRestDocumentation restDocumentation =
            new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AuthenticationService authenticationService;

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

        if (testUserToken == null) {
            AuthenticationRequest authenticationRequest = new AuthenticationRequest();
            authenticationRequest.setUsername(testUserUsername);
            authenticationRequest.setPassword(testUserPassword);
            testUserToken = authenticationService.authenticate(authenticationRequest);
        }
    }

    @Test
    public void sendMessageTest() throws Exception {
        MessageIn messageIn = new MessageIn();
        String messageName = testUserUsername;
        String message = "some_message";
        messageIn.setName(messageName);
        messageIn.setMessage(message);


        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageIn))
                        .header("Authorization", "Bearer " + testUserToken.getToken()))
                .andDo(document(URI.replace("/", "\\")))
                .andExpect(jsonPath("$.name").value(messageName))
                .andExpect(jsonPath("$.message").value(message))
                .andExpect(status().isOk());
    }

    @Test
    public void getHistoryTest() throws Exception {
        MessageIn messageIn = new MessageIn();
        messageIn.setName(testUserUsername);
        Integer numOfMessages = 10;
        messageIn.setMessage("history " + numOfMessages);

        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageIn))
                        .header("Authorization", "Bearer " + testUserToken.getToken()))
                .andDo(document(URI.replace("/", "\\") + " (requesting history)"))
                .andExpect(jsonPath("$.length()").value(numOfMessages))
                .andExpect(status().isOk());
    }

    @Test
    public void sendMessageUnauthorized() throws Exception {
        MessageIn messageIn = new MessageIn();
        messageIn.setName("unauthorized_name");
        messageIn.setMessage("unauthorized_message");

        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageIn)))
                .andDo(document(URI.replace("/", "\\") + " (unauthorized access)"))
                .andExpect(status().isForbidden());
    }

}
