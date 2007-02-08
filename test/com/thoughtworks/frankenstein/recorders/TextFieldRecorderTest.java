package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.HierarchyEvent;
import java.lang.reflect.InvocationTargetException;
import javax.swing.*;
import javax.swing.text.AbstractDocument;

import org.jmock.Mock;

import com.thoughtworks.frankenstein.events.EnterTextEvent;
import com.thoughtworks.frankenstein.naming.DefaultNamingStrategy;

/**
 * Ensures behaviour of the switch recorder.
 */
public class TextFieldRecorderTest extends AbstractRecorderTestCase {
    private JTextField textField;
    private TextFieldRecorder recorder;
    private Mock mockVisibility;

    protected void setUp() throws Exception {
        super.setUp();
        textField = new JTextField();
        textField.setName("testTextField");
        mockVisibility = mock(ComponentVisibility.class);
        recorder = new TextFieldRecorder((Recorder) mockRecorder.proxy(), new DefaultNamingStrategy(), (ComponentVisibility) mockVisibility.proxy());
    }

    public void testAddsListenerToTabbedPaneWhenTabbedPaneIsShown() {
        int listenerCount = documentListenerCount();
        recorder.componentShown(textField);
        assertTrue(documentListenerCount() == listenerCount + 1);
    }

    private int documentListenerCount() {
        return ((AbstractDocument) textField.getDocument()).getDocumentListeners().length;
    }

    public void testRemovesListenerWhenTabbedPaneIsHidden() {
        int listenerCount = documentListenerCount();
        recorder.componentShown(textField);
        recorder.componentHidden(textField);
        assertEquals(listenerCount, documentListenerCount());
    }

    public void testAddsAwtEventListenerOnRegister() {
        int awtListenerCount = awtListenerCount();
        recorder.register();
        assertEquals(awtListenerCount(), awtListenerCount + 1);
    }

    public void testRemovesAwtEventListenerOnUnRegister() {
        int awtListenerCount = awtListenerCount();
        recorder.register();
        recorder.unregister();
        assertEquals(awtListenerCount(), awtListenerCount);
    }

    private int awtListenerCount() {
        return Toolkit.getDefaultToolkit().getAWTEventListeners().length;
    }

    public void testEnteringTextPostsEnterEvent() {
        mockRecorder.expects(once()).method("record").with(eq(new EnterTextEvent("testTextField", "abc")));
        recorder.componentShown(textField);
        mockVisibility.stubs().method("isShowingAndHasFocus").will(returnValue(true));
        textField.setText("abc");
        mockRecorder.expects(once()).method("record").with(eq(new EnterTextEvent("testTextField", "")));
        mockRecorder.expects(once()).method("record").with(eq(new EnterTextEvent("testTextField", "def")));
        textField.setText("def");
    }

    public void testAutomaticallyNamesTextFieldWithoutName() {
        textField.setName(null);
        new JFrame().getContentPane().add(textField);
        mockRecorder.expects(once()).method("record").with(eq(new EnterTextEvent("JTextField_1", "abc")));
        recorder.componentShown(textField);
        mockVisibility.stubs().method("isShowingAndHasFocus").will(returnValue(true));
        textField.setText("abc");
    }

    public void testMatchesComponentType() {
        assertTrue(recorder.matchesComponentType(new ComponentEvent(new JTextField(), ComponentEvent.COMPONENT_SHOWN)));
    }

    public void testEventDispatchedWithEventHidden() {
        int listenerCount = documentListenerCount();
        recorder.componentShown(textField);
        HierarchyEvent event = new HierarchyEvent(textField, HierarchyEvent.SHOWING_CHANGED, textField, new JPanel(), HierarchyEvent.DISPLAYABILITY_CHANGED);
        recorder.eventDispatched(event);
        assertEquals(listenerCount, documentListenerCount());
    }

    public void testEventDispatchedWithComponentShown() throws InterruptedException, InvocationTargetException {
        int listenerCount = documentListenerCount();
        JTextField textField = textField();
        HierarchyEvent event = new HierarchyEvent(textField, HierarchyEvent.DISPLAYABILITY_CHANGED, textField, new JPanel(), HierarchyEvent.SHOWING_CHANGED);
        recorder.eventDispatched(event);
        recorder.componentHidden(textField);
        assertEquals(listenerCount, documentListenerCount());
    }

    private FakeComponent textField() throws InvocationTargetException, InterruptedException {
        final FakeComponent[] fakeComponent = new FakeComponent[1];
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                fakeComponent[0] = new FakeComponent();
            }
        });
        return fakeComponent[0];
    }

    private class FakeComponent extends JTextField {
        public boolean isShowing() {
            return true;
        }
    }
}
