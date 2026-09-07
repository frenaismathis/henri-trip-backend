package com.hws.henritrip.controller;

import com.hws.henritrip.service.GuideService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * No SecurityConfig is imported here on purpose: the web slice falls back to Spring Boot's
 * default security auto-configuration, which still requires authentication for every request.
 * That is enough to prove an unauthenticated call is rejected, without pulling in JwtTokenProvider
 * and its JWT signing configuration just to exercise this one behaviour.
 */
@WebMvcTest(GuideController.class)
class GuideControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GuideService guideService;

    @Test
    void getAllGuides_withoutAuthentication_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/admin/guides"))
                .andExpect(status().isUnauthorized());
    }
}
