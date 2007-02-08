package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.*;

import com.thoughtworks.frankenstein.common.ComponentName;
import com.thoughtworks.frankenstein.naming.NamingStrategy;

/**
 * Base class for all assertion recorders.
 *
 * @author Vivek Prahlad
 */
public abstract class AbstractCheckRecorder implements ComponentRecorder, AWTEventListener {
    protected EventRecorder recorder;
    protected NamingStrategy namingStrategy;
    private Class componentClass;

    public AbstractCheckRecorder(EventRecorder recorder, NamingStrategy namingStrategy, Class componentClass) {
        this.recorder = recorder;
        this.namingStrategy = namingStrategy;
        this.componentClass = componentClass;
    }

    public void register() {
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.MOUSE_EVENT_MASK);
    }

    public void unregister() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(this);
    }

    public void eventDispatched(AWTEvent event) {
        MouseEvent mouseEvent = (MouseEvent) event;
        if (isControlRightClick(mouseEvent) && matchesTarget(event.getSource())) {
            Component source = (Component) event.getSource();
            highlightComponent((JComponent) source, 1000);
            check(source);
        }
    }

    protected abstract void check(Component component);

    protected boolean isControlRightClick(MouseEvent mouseEvent) {
        return isControlPopup(mouseEvent, MouseEvent.MOUSE_PRESSED)
                || isControlPopup(mouseEvent, MouseEvent.MOUSE_RELEASED);
    }

    private boolean isControlPopup(MouseEvent mouseEvent, int id) {
        return mouseEvent.getID() == id
                && isControlPopup(mouseEvent);
    }

    private boolean isControlPopup(MouseEvent event) {
        return event.isPopupTrigger() && event.isControlDown();
    }

    protected void highlightComponent(final JComponent component, int delay) {
        final Color background = component.getBackground();
        component.setBackground(Color.RED);
        Timer timer = new Timer(delay, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                component.setBackground(background);
            }
        });
        timer.start();
    }

    protected String componentName(Component component) {
        return new ComponentName().componentName(component, namingStrategy);
    }

    protected boolean matchesTarget(Object source) {
        return componentClass.isAssignableFrom(source.getClass());
    }
}
