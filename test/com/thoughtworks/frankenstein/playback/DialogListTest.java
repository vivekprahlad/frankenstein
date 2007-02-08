package com.thoughtworks.frankenstein.playback;

import java.awt.*;
import java.awt.event.WindowEvent;
import javax.swing.*;

import com.thoughtworks.frankenstein.common.WaitForIdle;
import junit.framework.TestCase;

/**
 * Ensures
 * @author vivek
 */
public class DialogListTest extends TestCase {

    public void testRecordsDialogWhenDialogIsOpened() {
        DialogList list = new DialogList();
        JDialog dialog = dialog("title");
        list.eventDispatched(new WindowEvent(dialog("anothertitle"), WindowEvent.WINDOW_OPENED));
        list.eventDispatched(new WindowEvent(dialog, WindowEvent.WINDOW_OPENED));
        assertSame(dialog, list.findDialog("title"));
    }

    public void testFindsDialogWithRegularExpression() {
        DialogList list = new DialogList();
        JDialog dialog = dialog("title");
        list.eventDispatched(new WindowEvent(dialog, WindowEvent.WINDOW_OPENED));
        assertSame(dialog, list.findDialog("regex:t.*"));
    }

    private JDialog dialog(String title) {
        return new JDialog((Frame) null, title);
    }

    public void testDoesNotFindDialogOnceDialogIsClosed() {
        DialogList list = new DialogList();
        JDialog dialog = dialog("title");
        list.eventDispatched(new WindowEvent(dialog, WindowEvent.WINDOW_OPENED));
        dialog.dispose();
        new WaitForIdle().waitForIdle();
        try {
            list.findDialog("title");
            fail("Should not have been able to find dialog");
        } catch (Exception e) {
            //Expected
        }
    }
}
