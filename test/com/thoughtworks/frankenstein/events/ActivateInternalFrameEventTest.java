package com.thoughtworks.frankenstein.events;

import java.beans.PropertyVetoException;
import java.lang.reflect.InvocationTargetException;
import javax.swing.*;

import org.jmock.Mock;

import com.thoughtworks.frankenstein.playback.ComponentFinder;

/**
 * Ensures behaviour of ActivateInternalFrameEvent
 */
public class ActivateInternalFrameEventTest extends AbstractEventTestCase {
    public void testEqualsAndHashCode() {
        ActivateInternalFrameEvent one = new ActivateInternalFrameEvent("title");
        ActivateInternalFrameEvent two = new ActivateInternalFrameEvent("title");
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
    }

    public void testToString() {
        assertEquals("ActivateInternalFrameEvent: title", new ActivateInternalFrameEvent("title").toString());
    }

    public void testAction() {
        assertEquals("ActivateInternalFrame", new ActivateInternalFrameEvent("title").action());
    }

    public void testTarget() {
        assertEquals("title", new ActivateInternalFrameEvent("title").target());
    }

    public void testParameters() {
        assertEquals("", new ActivateInternalFrameEvent("title").parameters());
    }

    public void testScriptLine() {
        assertEquals("activate_internal_frame \"title\"", new ActivateInternalFrameEvent("title").scriptLine());
    }

    public void testScriptLineInJava() {
        assertEquals("activateInternalFrame(\"title\")", new ActivateInternalFrameEvent("title").scriptLine(new JavaScriptStrategy()));
    }

    public void testPlaysEvent() throws Exception {
        Mock mockComponentFinder = mock(ComponentFinder.class);
        JInternalFrame internalFrame = createInternalFrame();
        mockComponentFinder.expects(once()).method("findInternalFrame").will(returnValue(internalFrame));
        internalFrame.setSelected(false);
        new ActivateInternalFrameEvent("title").play(null, (ComponentFinder) mockComponentFinder.proxy(), null, null);
        assertTrue(internalFrame.isSelected());
    }

    private JInternalFrame createInternalFrame() throws InvocationTargetException, InterruptedException {
        final JInternalFrame[] internalFrame = new JInternalFrame[1];
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                internalFrame[0] = new JInternalFrame("title") {
                    public boolean isShowing() {
                        return true;
                    }
                };
            }
        });
        return internalFrame[0];
    }

    protected FrankensteinEvent createEvent() {
        return new ActivateWindowEvent("title");
    }

    public void testPropogatesPropertyVetoExceptionIfFrameIsNotSelectable() throws PropertyVetoException, InterruptedException, InvocationTargetException {
        Mock mockComponentFinder = mock(ComponentFinder.class);
        JInternalFrame internalFrame = createInternalFrameWithPropertyVetoException();
        mockComponentFinder.expects(once()).method("findInternalFrame").will(returnValue(internalFrame));
        try {
            new ActivateInternalFrameEvent("title").play(null, (ComponentFinder) mockComponentFinder.proxy(), null, null);
            fail("Expected selection to fail");
        } catch (Exception e) {
        }
    }

    private JInternalFrame createInternalFrameWithPropertyVetoException() throws InvocationTargetException, InterruptedException {
        final JInternalFrame[] internalFrame = new JInternalFrame[1];
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                internalFrame[0] = new JInternalFrame("title") {
                    public boolean isShowing() {
                        return true;
                    }

                    public void setSelected(boolean selected) throws PropertyVetoException {
                        throw new PropertyVetoException("Vetoing", null);
                    }
                };
            }
        });
        return internalFrame[0];
    }
}
