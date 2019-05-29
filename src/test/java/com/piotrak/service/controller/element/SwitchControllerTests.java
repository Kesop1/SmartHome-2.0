package com.piotrak.service.controller.element;

import com.piotrak.service.element.Element;
import com.piotrak.service.elementservice.TvElementService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = TvController.class)
public class SwitchControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Map<String, Map<String, Element>> elementsMap;

    @MockBean
    private TvElementService tvElementService;

    @Test
    public void correctPathNoArguments() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/tv"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void correctPathWithArguments() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/tv?cmd=ON"))
                .andExpect(model().size(1))
                .andExpect(model().attribute("elementsMap", elementsMap))
                .andExpect(status().isOk());
    }

    @Test
    public void incorrectPath() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/t"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void correctPathWrongArguments() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/tv?name=TV"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void incorrectRequestMethod() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/tv?cmd=ON"))
                .andExpect(status().is4xxClientError());
    }
}
