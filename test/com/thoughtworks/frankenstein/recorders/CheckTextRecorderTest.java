package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.*;

import org.jmock.Mock;

import com.thoughtworks.frankenstein.events.assertions.AssertEvent;
import com.thoughtworks.frankenstein.naming.DefaultNamingStrategy;

/**
 * Ensures behaviour of check text recorder
 */
public class CheckTextRecorderTest extends AbstractRecorderTestCase{
    private CheckTextRecorder checkTextRecorder;

    protected void setUp() throws Exception {
        super.setUp();
        checkTextRecorder = new CheckTextRecorder(null, new DefaultNamingStrategy());
    }

    public void testAddsListenerOnRegister() {
        int awtListenerCount = awtListenerCount();
        checkTextRecorder.register();
        assertEquals(awtListenerCount(), awtListenerCount + 1);
    }

    public void testRemovesListenerOnRegister() {
        int awtListenerCount = awtListenerCount();
        checkTextRecorder.register();
        checkTextRecorder.unregister();
        assertEquals(awtListenerCount(), awtListenerCount);
    }

    private int awtListenerCount() {
        return Toolkit.getDefaultToolkit().getAWTEventListeners().length;
    }

    public void testHighlightChangesComponentBackgroundToRed() {
        JTextField component = new JTextField();
        checkTextRecorder.highlightComponent(component, 1000);
        assertEquals(Color.RED, component.getBackground());
    }

    public void testHighlightRestoresComponentBackground() throws InterruptedException {
        JTextField component = new JTextField();
        Color componentBackground = component.getBackground();
        checkTextRecorder.highlightComponent(component, 0);
        Thread.sleep(50);//Need this hacky wait because there's no way to access/control what the timer thread is up to.
        waitForIdle();
        assertEquals(componentBackground, component.getBackground());
    }

    public void testCheck() {
        Mock mockEventRecorder = mock(EventRecorder.class);
        checkTextRecorder = new CheckTextRecorder((EventRecorder) mockEventRecorder.proxy(), new DefaultNamingStrategy());
        JTextField source = createTextField();
        expectCheckEvent(mockEventRecorder);
        checkTextRecorder.check(source);
    }

    private void expectCheckEvent(Mock mockEventRecorder) {
        mockEventRecorder.expects(once()).method("record").with(eq(new AssertEvent("textField", "text", "abc")));
    }

    private JTextField createTextField() {
        JTextField source = new JTextField();
        source.setText("abc");
        source.setName("textField");
        return source;
    }

    public void testMatchesTextFieldTarget() {
        assertTrue(checkTextRecorder.matchesTarget(new JTextField()));
        assertFalse(checkTextRecorder.matchesTarget(new JComboBox()));
    }

    public void testEventDispatched() {
        Mock mockEventRecorder = mock(EventRecorder.class);
        CheckTextRecorder recorder = new CheckTextRecorder((EventRecorder) mockEventRecorder.proxy(), new DefaultNamingStrategy());
        MouseEvent event = new MouseEvent(createTextField(), MouseEvent.MOUSE_PRESSED, 3, MouseEvent.CTRL_DOWN_MASK, 1, 2, 1, true);
        expectCheckEvent(mockEventRecorder);
        recorder.eventDispatched(event);
    }
}
