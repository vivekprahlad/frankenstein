package com.thoughtworks.frankenstein.events;

import org.jmock.Mock;
import com.thoughtworks.frankenstein.playback.ComponentFinder;

import javax.swing.*;
import java.beans.PropertyVetoException;
import java.lang.reflect.InvocationTargetException;

/**
 * Ensures behaviour of CloseInternalFrameEvent
 */
public class CloseInternalFrameEventTest extends AbstractEventTestCase {
    public void testEqualsAndHashCode() {
        CloseInternalFrameEvent one = new CloseInternalFrameEvent("title");
        CloseInternalFrameEvent two = new CloseInternalFrameEvent("title");
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
    }

    public void testToString() {
        assertEquals("CloseInternalFrameEvent: title", new CloseInternalFrameEvent("title").toString());
    }

    public void testAction() {
        assertEquals("CloseInternalFrame", new CloseInternalFrameEvent("title").action());
    }

    public void testTarget() {
        assertEquals("title", new CloseInternalFrameEvent("title").target());
    }

    public void testParameters() {
        assertEquals("", new CloseInternalFrameEvent("title").parameters());
    }

    public void testScriptLine() {
        assertEquals("close_internal_frame \"title\"", new CloseInternalFrameEvent("title").scriptLine());
    }

    public void testPlaysEvent() throws Exception {
        Mock mockComponentFinder = mock(ComponentFinder.class);
        JInternalFrame internalFrame = createInternalFrame();
        mockComponentFinder.expects(once()).method("findInternalFrame").will(returnValue(internalFrame));
        internalFrame.setSelected(false);
        new CloseInternalFrameEvent("title").play(null, (ComponentFinder) mockComponentFinder.proxy(), null, null);
        assertTrue(internalFrame.isClosed());
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
        ;
        return internalFrame[0];
    }

    public void testPropogatesPropertyVetoExceptionIfFrameIsNotSelectable() throws PropertyVetoException, InterruptedException, InvocationTargetException {
        Mock mockComponentFinder = mock(ComponentFinder.class);
        JInternalFrame internalFrame = createFrameWithPropertyVeto();
        mockComponentFinder.expects(once()).method("findInternalFrame").will(returnValue(internalFrame));
        try {
            new CloseInternalFrameEvent("title").play(null, (ComponentFinder) mockComponentFinder.proxy(), null, null);
            fail("Expected closing frame to fail");
        } catch (Exception e) {
        }
    }

    private JInternalFrame createFrameWithPropertyVeto() throws InvocationTargetException, InterruptedException {
        final JInternalFrame[] internalFrame = new JInternalFrame[1];
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                internalFrame[0] = new JInternalFrame("title") {
                    public boolean isShowing() {
                        return true;
                    }

                    public void setClosed(boolean closed) throws PropertyVetoException {
                        throw new PropertyVetoException("Vetoing", null);
                    }
                };
            }
        });
        ;
        return internalFrame[0];
    }

    protected FrankensteinEvent createEvent() {
        return new CloseInternalFrameEvent("title");
    }

}
