package com.thoughtworks.frankenstein.events;

import java.awt.*;
import javax.swing.*;

import com.thoughtworks.frankenstein.events.actions.Action;

/**
 * Base class for all compound events - these events are created with an action.
 */
public abstract class AbstractCompoundEvent extends AbstractFrankensteinEvent {
    protected Action action;

    protected AbstractCompoundEvent(Action action) {
        this.action = action;
        executeInPlayerThread();
    }

    public String action() {
        return action.name() + super.action();
    }

    protected Point center(JComponent component) {
        return new Point(component.getWidth() / 2, component.getHeight()/2);
    }
}
