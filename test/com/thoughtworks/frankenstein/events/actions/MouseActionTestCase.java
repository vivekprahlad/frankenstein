package com.thoughtworks.frankenstein.events.actions;

import org.jmock.MockObjectTestCase;
import org.jmock.Mock;

import com.thoughtworks.frankenstein.common.WaitForIdle;
import com.thoughtworks.frankenstein.playback.WindowContext;

import javax.swing.*;
import java.awt.*;

/**

 */
public abstract class MouseActionTestCase extends MockObjectTestCase {

    protected Mock mockWindowContext;
    protected JButton button;
    private JFrame frame;

    protected void setUp() throws Exception {
        super.setUp();
        mockWindowContext = mock(WindowContext.class);
        button = new JButton();
        frame = new JFrame();
        frame.getContentPane().add(button);
        frame.pack();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        frame.dispose();
    }

    protected void waitForIdle() {
        new WaitForIdle().waitForIdle();
    }

    protected void expectAddAndRemoveWindowContextListener(Action action) {
        mockWindowContext.expects(once()).method("addWindowContextListener").with(same(action));
        mockWindowContext.expects(once()).method("removeWindowContextListener").with(same(action));
    }

    protected Point center(JComponent component) {
        return new Point(component.getWidth() / 2, component.getHeight()/2);
    }


}
