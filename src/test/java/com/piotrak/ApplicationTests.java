package com.piotrak;

import com.piotrak.service.controller.MainControllerTests;
import com.piotrak.service.controller.element.SwitchControllerTests;
import com.piotrak.service.element.SwitchElementTests;
import com.piotrak.service.elementservice.TvElementServiceTests;
import com.piotrak.service.technology.mqtt.MQTTConnectionServiceTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Suite.class)
@SpringBootTest
@Suite.SuiteClasses({MainControllerTests.class, MQTTConnectionServiceTests.class, SwitchControllerTests.class, SwitchElementTests.class,
		TvElementServiceTests.class})
public class ApplicationTests {

}
