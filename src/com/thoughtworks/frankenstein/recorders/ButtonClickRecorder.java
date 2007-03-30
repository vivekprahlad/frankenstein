package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import com.thoughtworks.frankenstein.common.RootPaneContainerFinder;
import com.thoughtworks.frankenstein.events.ButtonEvent;
import com.thoughtworks.frankenstein.events.actions.ClickAction;
import com.thoughtworks.frankenstein.naming.ComponentHierarchyWalker;
import com.thoughtworks.frankenstein.naming.NamingStrategy;
import com.thoughtworks.frankenstein.ui.RecorderPane;

/**
 * Understands recording button clicks.
 *
 * @author Vivek Prahlad
 */
public class ButtonClickRecorder extends AbstractComponentRecorder implements ActionListener {

    public ButtonClickRecorder(EventRecorder recorder, NamingStrategy namingStrategy) {
        super(recorder, namingStrategy, AbstractButton.class);
    }

    void componentShown(Component component) {
        button(component).addActionListener(this);
    }

    private AbstractButton button(Component event) {
        return (AbstractButton) event;
    }

    void componentHidden(Component component) {
        button(component).removeActionListener(this);
    }

    protected boolean matchesComponentType(AWTEvent event) {
        Object source = event.getSource();
        return (source instanceof JButton
                && !source.getClass().getName().matches(".*Combo.*|javax\\.swing\\.plaf.*|FilePane.*"))
                || source.getClass() == JToggleButton.class;
    }

    public void actionPerformed(ActionEvent e) {
        AbstractButton button = (AbstractButton) e.getSource();
        if (hasNoRecorderPane(button) && !isButtonOnJavaConsole(button)) {
            recorder.record(new ButtonEvent(componentName(button), new ClickAction()));
        }
    }

    protected boolean hasNoRecorderPane(Container component) {
        return new ComponentHierarchyWalker().hasNoMatches(rootPaneContainer(component), RecorderPane.class);
    }

    private boolean isButtonOnJavaConsole(AbstractButton abstractButton) {
        JFrame frame = getParentFrame(abstractButton.getParent());
        return frame != null && frame.getTitle().equalsIgnoreCase(WindowActivationRecorder.JAVA_CONSOLE);
    }

    private JFrame getParentFrame(Container container) {
        while(container.getParent() != null && !(container instanceof JFrame)) {
            container = container.getParent();
        }
        if(container instanceof JFrame) {
            return (JFrame)container;
        }
        return null;
    }

    private Container rootPaneContainer(Container component) {
        return (Container) new RootPaneContainerFinder().findRootPane(component);
    }
}
