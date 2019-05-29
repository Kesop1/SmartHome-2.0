package com.piotrak.service.element;

import com.piotrak.service.technology.web.WebCommand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.naming.OperationNotSupportedException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class SwitchElementTests {

    @Test
    public void validSwitchCommandTest() throws OperationNotSupportedException {
        SwitchElement element = getElement();
        element.actOnCommand(new WebCommand("ON"));
        assertTrue(element.isOn());
    }

    @Test(expected = OperationNotSupportedException.class)
    public void invalidSwitchCommandTest() throws OperationNotSupportedException {
        SwitchElement element = getElement();
        element.actOnCommand(new WebCommand("TRUE"));
    }

    @Test
    public void repeatedSwitchCommandTest() throws OperationNotSupportedException {
        SwitchElement element = getElement();
        element.actOnCommand(new WebCommand("OFF"));
        assertFalse(element.isOn());
    }

    private SwitchElement getElement(){
        SwitchElement element = new SwitchElement("TV");
        element.setOn(false);
        return element;
    }

}
