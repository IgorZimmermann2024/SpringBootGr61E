package de.ait.javalessons.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testWennAdminAccessOk() throws Exception {
        mockMvc.perform(get("/api/private"))
                .andExpect(status().isOk())
                .andExpect(content().string("Это защищенная страница, доступна только после входа"));
    }

    @Test
    void testWennFakeUserAccessForbidden() throws Exception {
        mockMvc.perform(get("/api/private"))
                .andExpect(status().is3xxRedirection());
    }


}
