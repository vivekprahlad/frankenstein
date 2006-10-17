package com.thoughtworks.frankenstein.ui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelListener;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.thoughtworks.frankenstein.events.SwitchTabEvent;
import com.thoughtworks.frankenstein.recorders.Recorder;
import com.thoughtworks.frankenstein.common.WaitForIdle;

/**
 * Ensures behaviour of the recorder table model.
 */
public class RecorderTableModelTest extends MockObjectTestCase {
    private List eventList;
    private RecorderTableModel model;
    private Mock mockRecorder;

    protected void setUp() throws Exception {
        super.setUp();
        eventList = new ArrayList();
        eventList.add(new SwitchTabEvent("tab", "tabOne"));
        mockRecorder = mock(Recorder.class);
        mockRecorder.expects(once()).method("eventList").will(returnValue(eventList));
        mockRecorder.expects(once()).method("addChangeListener");
        model = new RecorderTableModel((Recorder) mockRecorder.proxy());
    }

    public void testReturnsRowCountEqualToTheNumberOfEvents() {
        assertEquals(1, model.getRowCount());
    }

    public void testReturnColumnCountAsThree() {
        assertEquals(3, model.getColumnCount());
    }

    public void testGetActionColumn() {
        assertEquals("SwitchTab", model.getValueAt(0, 0));
    }

    public void testGetTargetColumn() {
        assertEquals("tab", model.getValueAt(0, 1));
    }

    public void testGetParametersColumn() {
        assertEquals("tabOne", model.getValueAt(0, 2));
    }

    public void testGettingInvalidColumnFails() {
        try {
            model.getValueAt(0, 3);
            fail();
        } catch (Exception e) {
        }
    }

    public void testFiresTableUpdateWhenRecorderChanges() {
        Mock mockTableModelListener = mock(TableModelListener.class);
        model.addTableModelListener((TableModelListener) mockTableModelListener.proxy());
        mockTableModelListener.expects(once()).method("tableChanged");
        mockRecorder.expects(once()).method("eventList");
        model.stateChanged(new ChangeEvent(this));
        new WaitForIdle().waitForIdle();
    }

    public void testReturnsActionAsColumnNameForFirstColumn() {
        assertEquals("Action", model.getColumnName(0));
    }

    public void testReturnsTargetAsColumnNameForFirstColumn() {
        assertEquals("Target", model.getColumnName(1));
    }

    public void testReturnsParametersAsColumnNameForFirstColumn() {
        assertEquals("Parameters", model.getColumnName(2));
    }

    public void testThrowsExceptionForUnknownColumn() {
        try {
            model.getColumnName(4);
            fail("Should not have been able to get a column name for the 4th column");
        } catch(Exception e) {
            //Expected.
        }
    }
}
