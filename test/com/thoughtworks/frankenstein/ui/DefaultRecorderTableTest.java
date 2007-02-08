package com.thoughtworks.frankenstein.ui;

import java.util.ArrayList;
import javax.swing.*;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.thoughtworks.frankenstein.recorders.EventRecorder;

public class DefaultRecorderTableTest extends MockObjectTestCase {
    private DefaultRecorderTable recorderTable;
    private RecorderTableModel recorderTableModel;

    protected void setUp() throws Exception {
        super.setUp();
        Mock eventRecorderMock = mock(EventRecorder.class);
        eventRecorderMock.expects(atLeastOnce()).method("eventList").withNoArguments().will(returnValue(getEventList()));
        eventRecorderMock.expects(atLeastOnce()).method("addChangeListener").withAnyArguments();
        recorderTableModel = new RecorderTableModel((EventRecorder) eventRecorderMock.proxy());
        recorderTable = new DefaultRecorderTable(recorderTableModel);
    }

    private ArrayList getEventList() {
        ArrayList list = new ArrayList();
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        return list;
    }

    public void testIfTableIsUsingTheCorrectTableModel() {
        assertSame("RecorderTable doesn't use the given Table Model", recorderTableModel, recorderTable.getModel());
    }

    public void testIfTableSelectionDefaultsAreCorrect() {
        assertEquals(ListSelectionModel.SINGLE_SELECTION, recorderTable.getSelectionModel().getSelectionMode());
        assertTrue(recorderTable.getRowSelectionAllowed());
        assertFalse(recorderTable.getColumnSelectionAllowed());
    }

    public void testIfTableSelectsRowCorrectly() {
        recorderTable.selectRow(1);
        assertEquals(1, recorderTable.getSelectedRow());
    }
}
