package de.ait.javalessons.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ait.javalessons.model.AuthRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JwtIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testLoginWithCredentialsReturnJwt() throws Exception{
        AuthRequest request = new AuthRequest();
        request.setUsername("admin");
        request.setPassword("admin");

        String response = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response).isNotNull();
        assertThat(response).isNotBlank();
        assertThat(response).startsWith("ey");
    }

    @Test
    void testLoginWithWrongCredentialsReturnUnathorized() throws Exception{
        AuthRequest request = new AuthRequest();
        request.setUsername("admin");
        request.setPassword("fakepassword");
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAccessSecureEndpointWithoutToken() throws Exception {
        mockMvc.perform(get("/api/secure"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAccessEndpointWithValidToken() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername("admin");
        request.setPassword("admin");

        String token = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(token).isNotNull();

        mockMvc.perform(get("/api/secure")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("Вы успешно аутентифицированы с JWT"));
    }
}
