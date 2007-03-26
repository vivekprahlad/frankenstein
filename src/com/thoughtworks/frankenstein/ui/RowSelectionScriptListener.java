package com.thoughtworks.frankenstein.ui;

import com.thoughtworks.frankenstein.recorders.ScriptListener;

/**
 * Understands rendering row selection after a script step is completed.
 *
 * @author Pavan
 * @author Prakash
 */
public class RowSelectionScriptListener implements ScriptListener {
    private RecorderTable recorderTable;

    public RowSelectionScriptListener(RecorderTable recorderTable) {
        this.recorderTable = recorderTable;
    }

    public void scriptCompleted(boolean passed) {
        recorderTable.clearSelection();
    }

    public void scriptStepStarted(int eventIndex) {
        recorderTable.selectRow(eventIndex);
    }
}
