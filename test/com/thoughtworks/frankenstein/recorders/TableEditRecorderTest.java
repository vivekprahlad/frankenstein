package com.thoughtworks.frankenstein.recorders;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeListener;

import com.thoughtworks.frankenstein.events.CancelTableEditEvent;
import com.thoughtworks.frankenstein.events.EditTableCellEvent;
import com.thoughtworks.frankenstein.events.FrankensteinEvent;
import com.thoughtworks.frankenstein.naming.DefaultNamingStrategy;

/**
 * Ensures behaviour of the switch recorder.
 */
public class TableEditRecorderTest extends AbstractRecorderTestCase {
    private JTable table;
    private TableEditRecorder recorder;
    private TableEditRecorderTest.MockRecorder mockTableRecorder;

    protected void setUp() throws Exception {
        super.setUp();
        waitForIdle();
        table = new JTable(new TestTableModel());
        table.setName("testTable");
        mockTableRecorder = new MockRecorder();
        recorder = new TableEditRecorder(mockTableRecorder, new DefaultNamingStrategy());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        waitForIdle();
    }

    public void testAddsListenerToTableWhenTableIsShown() {
        int listenerCount = propertyChangeListenerCount();
        recorder.componentShown(table);
        assertTrue(propertyChangeListenerCount() == listenerCount + 1);
    }

    private int propertyChangeListenerCount() {
        return table.getPropertyChangeListeners().length;
    }

    public void testRemovesListenerWhenTableIsHidden() {
        int listenerCount = propertyChangeListenerCount();
        recorder.componentShown(table);
        recorder.componentHidden(table);
        assertEquals(listenerCount,  propertyChangeListenerCount());
    }

    public void testEditingTablePostsEditTableEvent() throws InterruptedException, InvocationTargetException {
        recorder.componentShown(table);
        editCell();
        assertTrue("Event: " + mockTableRecorder.event, mockTableRecorder.wasCalledWith(new EditTableCellEvent("testTable", 0, 0)));
    }

    public void testStoppingEditDoesNotRecordEventIfValueDoesNotChange() throws InterruptedException, InvocationTargetException {
        editCell();
        recorder.componentShown(table);
        stopCellEditing();
        assertTrue("Event: " + mockTableRecorder.event, mockTableRecorder.wasCalledWith(new CancelTableEditEvent("testTable")));
    }

    public void testStoppingEditRecordsEventIfValueChanges() throws InterruptedException, InvocationTargetException {
        recorder.componentShown(table);
        editCell();
        editorComponent().setText("abc");
        stopCellEditing();
        assertTrue("Event: " + mockTableRecorder.event, mockTableRecorder.wasCalledWith(new CancelTableEditEvent("testTable")));
    }

    public void testCancellingEditRecordsEvent() throws InterruptedException, InvocationTargetException {
        recorder.componentShown(table);
        editCell();
        editorComponent().setText("abc");
        cancelEditing();
        assertTrue("Event: " + mockTableRecorder.event, mockTableRecorder.wasCalledWith(new CancelTableEditEvent("testTable")));
    }

    private JTextField editorComponent() {
        DefaultCellEditor editor = (DefaultCellEditor) table.getCellEditor();
        return (JTextField) editor.getComponent();
    }

    //Ensure that all operations on the table happen on the swing thread.
    private void cancelEditing() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                table.getCellEditor().cancelCellEditing();
            }
        });
        waitForIdle();
    }

    //Ensure that all operations on the table happen on the swing thread.
    private void stopCellEditing() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                table.getCellEditor().stopCellEditing();
            }
        });
        waitForIdle();
    }

    //Ensure that all operations on the table happen on the swing thread.
    private void editCell() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                table.editCellAt(0,0);
            }
        });
        waitForIdle();
    }

    private class MockRecorder implements Recorder {
        private FrankensteinEvent event;

        public void record(FrankensteinEvent event) {
            this.event = event;
        }

        public void addChangeListener(ChangeListener listener) {
        }

        public void removeChangeListener() {
        }

        public void start() {
        }

        public void stop() {
        }

        public void play() {
        }

        public void reset() {
        }

        public List eventList() {
            return null;
        }

        public void setEventList(List events) {
        }

        private boolean wasCalledWith(FrankensteinEvent event) {
            if (this.event == null && event == null) return true;
            return event.equals(this.event);
        }
    }
}
