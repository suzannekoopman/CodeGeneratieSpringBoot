package io.swagger.model;

import io.swagger.model.MachineTransfer;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.web.context.WebApplicationContext;

import static junit.framework.TestCase.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MachineTransferModelTest {
    @Autowired
    private WebApplicationContext webApplicationContext;



    @Before
    public void setUp() {

    }

    @Test
    public void createMachineTransferShouldNotBeNull() throws Exception
    {
        MachineTransfer machineTransfer = new MachineTransfer(50, "deposit");
        assertNotNull(machineTransfer);
    }
}
