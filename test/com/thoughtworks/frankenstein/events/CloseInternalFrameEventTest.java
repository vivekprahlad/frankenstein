package com.thoughtworks.frankenstein.events;

import org.jmock.Mock;
import com.thoughtworks.frankenstein.playback.ComponentFinder;

import javax.swing.*;
import java.beans.PropertyVetoException;

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

    public void testPlay() throws Exception {
        Mock mockComponentFinder = mock(ComponentFinder.class);
        JInternalFrame internalFrame = new JInternalFrame("title") {
            public boolean isShowing() {
                return true;
            }
        };
        mockComponentFinder.expects(once()).method("findInternalFrame").will(returnValue(internalFrame));
        internalFrame.setSelected(false);
        new CloseInternalFrameEvent("title").play(null, (ComponentFinder) mockComponentFinder.proxy(), null, null);
        assertTrue(internalFrame.isClosed());
    }

    public void testPropogatesPropertyVetoExceptionIfFrameIsNotSelectable() throws PropertyVetoException {
        Mock mockComponentFinder = mock(ComponentFinder.class);
        JInternalFrame internalFrame = new JInternalFrame("title") {
            public boolean isShowing() {
                return true;
            }

            public void setClosed(boolean closed) throws PropertyVetoException {
                throw new PropertyVetoException("Vetoing", null);
            }
        };
        mockComponentFinder.expects(once()).method("findInternalFrame").will(returnValue(internalFrame));
        try {
            new CloseInternalFrameEvent("title").play(null, (ComponentFinder) mockComponentFinder.proxy(), null, null);
            fail("Expected closing frame to fail");
        } catch (Exception e) {
        }
    }

    protected FrankensteinEvent createEvent() {
        return new CloseInternalFrameEvent("title");
    }

}
