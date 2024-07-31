package edu.escuelaing.arsw.ASE.app.springpacman.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        loginController.resetIndices();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testGetPlayerDataAsThief() throws Exception {
        mockMvc.perform(get("/login/getPlayerData")
                .param("isThief", "true")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.top").value(2))
                .andExpect(jsonPath("$.left").value(2));
    }

    @Test
    void testGetPlayerDataAsPolice() throws Exception {
        mockMvc.perform(get("/login/getPlayerData")
                .param("isThief", "false")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.top").value(22))
                .andExpect(jsonPath("$.left").value(37));
    }

    @Test
    void testGetPlayerDataThiefNoMorePositions() throws Exception {

        for (int i = 0; i < 4; i++) {
            mockMvc.perform(get("/login/getPlayerData")
                    .param("isThief", "true")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }
        mockMvc.perform(get("/login/getPlayerData")
                .param("isThief", "true")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetPlayerDataPoliceNoMorePositions() throws Exception {
        for (int i = 0; i < 4; i++) {
            mockMvc.perform(get("/login/getPlayerData")
                    .param("isThief", "false")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }
        mockMvc.perform(get("/login/getPlayerData")
                .param("isThief", "false")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
