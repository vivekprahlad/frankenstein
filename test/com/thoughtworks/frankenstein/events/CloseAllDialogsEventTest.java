package com.thoughtworks.frankenstein.events;

import org.jmock.Mock;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.common.RobotFactory;


/**
 * Ensures behaviour of CloseAllDialogsEvent
 * @author vivek
 */
public class CloseAllDialogsEventTest extends AbstractEventTestCase{
    public void testEqualsAndHashCode() {
        CloseAllDialogsEvent one = new CloseAllDialogsEvent("");
        CloseAllDialogsEvent two = new CloseAllDialogsEvent("");
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
    }

    public void testToString() {
        assertEquals("CloseAllDialogsEvent", new CloseAllDialogsEvent("").toString());
    }

    public void testAction() {
        assertEquals("CloseAllDialogs", new CloseAllDialogsEvent("").action());
    }

    public void testTarget() {
        assertEquals("", new CloseAllDialogsEvent("").target());
    }

    public void testParameters() {
        assertEquals("", new CloseAllDialogsEvent("").parameters());
    }

    public void testScriptLine() {
        assertEquals("close_all_dialogs", new CloseAllDialogsEvent("").scriptLine());
    }

    public void testPlaysEvent() throws Exception {
        Mock mockComponentFinder = mock(ComponentFinder.class);
        Mock mockContext = mock(WindowContext.class);
        WindowContext context = (WindowContext) mockContext.proxy();
        CloseAllDialogsEvent closeAllDialogs = new CloseAllDialogsEvent("dialog");
        mockContext.expects(once()).method("closeAllDialogs");
        closeAllDialogs.play(context, (ComponentFinder) mockComponentFinder.proxy(), null, RobotFactory.getRobot());
    }

    protected FrankensteinEvent createEvent() {
        return new CloseAllDialogsEvent("");
    }
}
