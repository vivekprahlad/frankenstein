package com.thoughtworks.frankenstein.events;

import org.jmock.Mock;
import com.thoughtworks.frankenstein.playback.ComponentFinder;

import javax.swing.*;
import java.beans.PropertyVetoException;

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

    public void testPlay() throws Exception {
        Mock mockComponentFinder = mock(ComponentFinder.class);
        JInternalFrame internalFrame = new JInternalFrame("title") {
            public boolean isShowing() {
                return true;
            }
        };
        mockComponentFinder.expects(once()).method("findInternalFrame").will(returnValue(internalFrame));
        internalFrame.setSelected(false);
        new ActivateInternalFrameEvent("title").play(null, (ComponentFinder) mockComponentFinder.proxy(), null, null);
        assertTrue(internalFrame.isSelected());
    }

    public void testPropogatesPropertyVetoExceptionIfFrameIsNotSelectable() throws PropertyVetoException {
        Mock mockComponentFinder = mock(ComponentFinder.class);
        JInternalFrame internalFrame = new JInternalFrame("title") {
            public boolean isShowing() {
                return true;
            }

            public void setSelected(boolean selected) throws PropertyVetoException {
                throw new PropertyVetoException("Vetoing", null);
            }
        };
        mockComponentFinder.expects(once()).method("findInternalFrame").will(returnValue(internalFrame));
        try {
            new ActivateInternalFrameEvent("title").play(null, (ComponentFinder) mockComponentFinder.proxy(), null, null);
            fail("Expected selection to fail");
        } catch (Exception e) {
        }
    }
}
