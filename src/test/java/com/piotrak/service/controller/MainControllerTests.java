package com.piotrak.service.controller;

import com.piotrak.service.element.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = MainController.class)
public class MainControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Map<String, List<Element>> elementsMap;

    @Test
    public void emptyPathTest() throws Exception {
        checkMainView("");
    }

    @Test
    public void mainPathTest() throws Exception {
        checkMainView("/main");
    }

    @Test
    public void homePathTest() throws Exception {
        checkMainView("/home");
    }

    @Test
    public void incorrectPathTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/mainView"))
                .andExpect(status().isNotFound());
    }

    private void checkMainView(String path) throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(path))
                .andExpect(model().size(1))
                .andExpect(model().attribute("elementsMap", elementsMap))
                .andExpect(status().isOk());
    }

}
