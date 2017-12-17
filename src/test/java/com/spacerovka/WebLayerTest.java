package com.spacerovka;

import com.spacerovka.service.GreetingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Spring Boot is only instantiating the web layer, not the whole context, no server
 * need to mock all services for context to start
 */

@RunWith(SpringRunner.class)
@WebMvcTest
public class WebLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GreetingService service;

    @Test
    public void testHomeUrl() throws Exception {
        when(service.greet()).thenReturn("Hi, Marla");
        this.mockMvc
                .perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hi, Marla")));
        // also has .andReturn() for direct access to results
    }

    @Test
    public void testJson() throws Exception {
        this.mockMvc
                .perform(get("/api/greet?name=Marla"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.content").value("Hello, Marla"));
    }

    @Test
    public void testModel() throws Exception {
        mockMvc.perform(get("/ui/greet?name=Marla"))
                .andExpect(status().isOk())
                .andExpect(model().size(1))
                .andExpect(model().attributeExists("name"));
    }

    @Test
    public void testRedirect() throws Exception {
        mockMvc.perform(get("/ui/redirect"))
                .andExpect(redirectedUrl("ui/guest/greet"))
                .andExpect(flash().attributeCount(1))
                .andExpect(flash().attribute("name", "Guest"));
    }
}
