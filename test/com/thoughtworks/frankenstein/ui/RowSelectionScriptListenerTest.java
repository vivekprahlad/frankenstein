package com.thoughtworks.frankenstein.ui;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

public class RowSelectionScriptListenerTest extends MockObjectTestCase {
    private Mock recorderTableMock;
    private RowSelectionScriptListener listener;


    protected void setUp() throws Exception {
        super.setUp();
        recorderTableMock = mock(RecorderTable.class);
        listener = new RowSelectionScriptListener((RecorderTable) recorderTableMock.proxy());
    }

    public void testScriptSteptStartedSelectsCorrectRowInRecorderTable() {
        int index = 0;
        recorderTableMock.expects(once()).method("selectRow").with(eq(index));
        listener.scriptStepStarted(index);
    }

    public void testScriptCompletedClearsSelection() {
        recorderTableMock.expects(once()).method("clearSelection");
        listener.scriptCompleted(true);
    }
}
