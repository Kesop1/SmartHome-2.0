package com.piotrak.service.technology.time;

import com.piotrak.service.technology.Command;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Command that can be scheduled for a specific time
 */
public class ScheduledCommand extends Command {

    /**
     * Job's identifier
     */
    private String id;

    /**
     * Time offset
     */
    private Date date;

    /**
     * Element name
     */
    private String element;

    public ScheduledCommand(@NotNull Date date, @NotEmpty String element, @NotEmpty String value) {
        super(value);
        this.date = date;
        this.element = element;
    }

    public Date getDate() {
        return date;
    }

    public String getElement() {
        return element;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ScheduledCommand{" +
                "id='" + getId() + '\'' +
                "date='" + getDate() + '\'' +
                "element='" + getElement() + '\'' +
                "value='" + getValue() + '\'' +
                '}';
    }
}
