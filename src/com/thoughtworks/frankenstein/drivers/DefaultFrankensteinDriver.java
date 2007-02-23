package com.thoughtworks.frankenstein.drivers;

import com.thoughtworks.frankenstein.application.RegexWorkerThreadMonitor;
import com.thoughtworks.frankenstein.application.WorkerThreadMonitor;
import com.thoughtworks.frankenstein.events.*;
import com.thoughtworks.frankenstein.events.actions.ClickAction;
import com.thoughtworks.frankenstein.events.actions.DoubleClickAction;
import com.thoughtworks.frankenstein.events.actions.RightClickAction;
import com.thoughtworks.frankenstein.events.assertions.AssertEvent;
import com.thoughtworks.frankenstein.naming.DefaultNamingStrategy;
import com.thoughtworks.frankenstein.playback.DefaultComponentFinder;
import com.thoughtworks.frankenstein.playback.DefaultWindowContext;
import com.thoughtworks.frankenstein.recorders.DefaultScriptContext;
import com.thoughtworks.frankenstein.recorders.ScriptContext;
import com.thoughtworks.frankenstein.script.CompositeReporter;
import com.thoughtworks.frankenstein.script.TestReporter;

/**
 * Default FrankensteinDriver implementation.
 *
 * @author Pavan
 * @author Prakash
 */
public class DefaultFrankensteinDriver implements FrankensteinDriver {
    private ScriptContext scriptContext;
    private TestReporter testReporter;

    public DefaultFrankensteinDriver(TestReporter testReporter) {
        this(testReporter, "test");
    }

    public DefaultFrankensteinDriver(TestReporter testReporter, String testName) {
        this(testReporter, new RegexWorkerThreadMonitor("UIWorker"), testName);
    }

    public DefaultFrankensteinDriver(TestReporter testReporter, WorkerThreadMonitor threadMonitor, String testName) {
        this.testReporter = createCompositeReporter(testReporter);
        setScriptContext(createScriptContext(this.testReporter, threadMonitor));
        startTest(testName);
    }

    private ScriptContext createScriptContext(TestReporter testReporter, WorkerThreadMonitor threadMonitor) {
        DefaultComponentFinder componentFinder = new DefaultComponentFinder(new DefaultNamingStrategy());
        return new DefaultScriptContext(testReporter, threadMonitor, new DefaultWindowContext(), componentFinder);
    }

    private CompositeReporter createCompositeReporter(TestReporter testReporter) {
        CompositeReporter compositeReporter = new CompositeReporter();
        compositeReporter.addTestReporter(testReporter);
        compositeReporter.addTestReporter(new FailFastReporter());
        return compositeReporter;
    }

    void setScriptContext(ScriptContext scriptContext) {
        this.scriptContext = scriptContext;
        this.scriptContext.startMonitor();
    }

    TestReporter getTestReporter() {
        return testReporter;
    }

    private void startTest(String testName) {
        testReporter.startTest(testName);
    }

    public void activateDialog(String dialogTitle) {
        ActivateDialogEvent activateDialogEvent = new ActivateDialogEvent(dialogTitle);
        scriptContext.play(activateDialogEvent);
    }

    public void activateWindow(String windowTitle) {
        ActivateWindowEvent activateWindowEvent = new ActivateWindowEvent(windowTitle);
        scriptContext.play(activateWindowEvent);
    }

    public void activateInternalFrame(String internalFrameTitle) {
        ActivateInternalFrameEvent activateInternalFrameEvent = new ActivateInternalFrameEvent(internalFrameTitle);
        scriptContext.play(activateInternalFrameEvent);
    }

    public void assertValue(String componentName, String ognlExpression, String expectedValue) {
        AssertEvent assertEvent = new AssertEvent(componentName, ognlExpression, expectedValue);
        scriptContext.play(assertEvent);
    }

    public void assertNumberOfTableRows(String tableName, int expectedNumberOfRows) {
        String numberOfRowAsString = String.valueOf(expectedNumberOfRows);
        assertValue(tableName, "rowCount", numberOfRowAsString);
    }

    public void assertTableCell(String tableName, int row, int column, String expectedCellValue) {
        assertValue(tableName, "getValueAt(" + row + "," + column + ")", expectedCellValue);
    }

    public void assertTableRow(String tableName, int row, String[] expectedCellValues) {
        for (int column = 0; column < expectedCellValues.length; column++) {
            assertTableCell(tableName, row, column, expectedCellValues[column]);
        }
    }

    public void assertText(String textComponentName, String expectedText) {
        assertValue(textComponentName, "text", expectedText);
    }

    public void assertTrue(String componentName, String ognlExpression) {
        assertValue(componentName, ognlExpression, "true");
    }

    public void assertFalse(String componentName, String ognlExpression) {
        assertValue(componentName, ognlExpression, "false");
    }

    public void assertEnabled(String componentName) {
        assertTrue(componentName, "enabled");
    }

    public void assertDisabled(String componentName) {
        assertFalse(componentName, "enabled");
    }

    public void assertToggleButtonSelected(String toggleButtonName) {
        assertTrue(toggleButtonName, "selected");
    }

    public void assertToggleButtonNotSelected(String toggleButtonName) {
        assertFalse(toggleButtonName, "selected");
    }

    public void assertCheckBoxSelected(String checkBoxName) {
        assertToggleButtonSelected(checkBoxName);
    }

    public void assertCheckBoxNotSelected(String checkBoxName) {
        assertToggleButtonNotSelected(checkBoxName);
    }

    public void assertRadioButtonSelected(String radioButtonName) {
        assertToggleButtonSelected(radioButtonName);
    }

    public void assertRadioButtonNotSelected(String radioButtonName) {
        assertToggleButtonNotSelected(radioButtonName);
    }

    public void assertLabel(String expectedText) {
        AssertLabelEvent assertLabelEvent = new AssertLabelEvent(expectedText);
        scriptContext.play(assertLabelEvent);
    }

    public void cancelTableEdit(String tableName) {
        CancelTableEditEvent cancelTableEditEvent = new CancelTableEditEvent(tableName);
        scriptContext.play(cancelTableEditEvent);
    }

    public void clickButton(String buttonName) {
        ButtonEvent buttonEvent = new ButtonEvent(buttonName, new ClickAction());
        scriptContext.play(buttonEvent);
    }

    public void clickCheckBox(String checkBoxName, boolean isChecked) {
        CheckboxEvent checkBoxEvent = new CheckboxEvent(checkBoxName, isChecked, new ClickAction());
        scriptContext.play(checkBoxEvent);
    }

    public void clickRadioButton(String radioButtonName) {
        RadioButtonEvent radioButtonEvent = new RadioButtonEvent(radioButtonName, new ClickAction());
        scriptContext.play(radioButtonEvent);
    }

    public void clickTableHeader(String tableHeaderName, String tableColumnName) {
        TableHeaderEvent tableHeaderEvent = new TableHeaderEvent(tableHeaderName, tableColumnName, new ClickAction());
        scriptContext.play(tableHeaderEvent);
    }

    public void closeInternalFrame(String internalFrameTitle) {
        CloseInternalFrameEvent closeInternalFrameEvent = new CloseInternalFrameEvent(internalFrameTitle);
        scriptContext.play(closeInternalFrameEvent);
    }

    public void closeAllDialogs() {
        CloseAllDialogsEvent closeAllDialogsEvent = new CloseAllDialogsEvent(null);
        scriptContext.play(closeAllDialogsEvent);
    }

    public void delay(int milliseconds) {
        DelayEvent delayEvent = new DelayEvent(String.valueOf(milliseconds));
        scriptContext.play(delayEvent);
    }

    public void dialogClosed(String dialogTitle) {
        DialogClosedEvent dialogClosedEvent = new DialogClosedEvent(dialogTitle);
        scriptContext.play(dialogClosedEvent);
    }

    public void dialogShown(String dialogTitle) {
        DialogShownEvent dialogShownEvent = new DialogShownEvent(dialogTitle);
        scriptContext.play(dialogShownEvent);
    }

    public void doubleClickTableRow(String tableName, int rowIndex) {
        TableRowEvent tableRowEvent = new TableRowEvent(tableName, rowIndex, new DoubleClickAction());
        scriptContext.play(tableRowEvent);
    }

    public void doubleClickList(String listName, int itemIndex) {
        ListEvent listEvent = new ListEvent(listName, itemIndex, new DoubleClickAction());
        scriptContext.play(listEvent);
    }

    public void doubleClickTree(String treeName, String[] pathElements) {
        TreeEvent treeEvent = new TreeEvent(treeName, pathElements, new DoubleClickAction());
        scriptContext.play(treeEvent);
    }

    public void enterText(String textFieldname, String text) {
        EnterTextEvent enterTextEvent = new EnterTextEvent(textFieldname, text);
        scriptContext.play(enterTextEvent);
    }

    public void editTableCell(String tableName, int row, int column) {
        EditTableCellEvent editTableCellEvent = new EditTableCellEvent(tableName, row, column);
        scriptContext.play(editTableCellEvent);
    }

    public void internalFrameShown(String internalFrameTitle) {
        InternalFrameShownEvent internalFrameShownEvent = new InternalFrameShownEvent(internalFrameTitle);
        scriptContext.play(internalFrameShownEvent);
    }

    public void keyStroke(String keyModifierAndKeyCodeText) {
        KeyStrokeEvent keyStrokeEvent = new KeyStrokeEvent(keyModifierAndKeyCodeText);
        scriptContext.play(keyStrokeEvent);
    }

    public void navigate(String pathString) {
        NavigateEvent navigateEvent = new NavigateEvent(pathString);
        scriptContext.play(navigateEvent);
    }

    public void rightClickTree(String treeName, String[] pathElements) {
        TreeEvent treeEvent = new TreeEvent(treeName, pathElements, new RightClickAction());
        scriptContext.play(treeEvent);
    }

    public void rightClickList(String listName, int itemIndex) {
        ListEvent listEvent = new ListEvent(listName, itemIndex, new RightClickAction());
        scriptContext.play(listEvent);
    }

    public void rightClickTableRow(String tableName, int rowIndex) {
        TableRowEvent tableRowEvent = new TableRowEvent(tableName, rowIndex, new RightClickAction());
        scriptContext.play(tableRowEvent);
    }

    public void selectDropDown(String comboBoxName, String value) {
        SelectDropDownEvent selectDropDownEvent = new SelectDropDownEvent(comboBoxName, value);
        scriptContext.play(selectDropDownEvent);
    }

    public void selectFile(String filePath) {
        SelectFileEvent selectFileEvent = new SelectFileEvent(filePath);
        scriptContext.play(selectFileEvent);
    }

    public void selectFiles(String[] filePaths) {
        SelectFilesEvent selectFilesEvent = new SelectFilesEvent(filePaths);
        scriptContext.play(selectFilesEvent);
    }

    public void selectList(String listName, String[] listElements) {
        SelectListEvent selectListEvent = new SelectListEvent(listName, listElements);
        scriptContext.play(selectListEvent);
    }

    public void selectTableRow(String tableName, int[] rows) {
        SelectTableRowEvent selectTableRowEvent = new SelectTableRowEvent(tableName, rows);
        scriptContext.play(selectTableRowEvent);
    }

    public void selectTree(String treeName, String[] pathElements) {
        SelectTreeEvent selectTreeEvent = new SelectTreeEvent(treeName, pathElements);
        scriptContext.play(selectTreeEvent);
    }

    public void stopTableEdit(String tableName) {
        StopTableEditEvent stopTableEditEvent = new StopTableEditEvent(tableName);
        scriptContext.play(stopTableEditEvent);
    }

    public void switchTab(String tabPaneTitle, String tabTitle) {
        SwitchTabEvent switchTabEvent = new SwitchTabEvent(tabPaneTitle, tabTitle);
        scriptContext.play(switchTabEvent);
    }

    public void moveSlider(String sliderName, int position) {
        MoveSliderEvent moveSliderEvent = new MoveSliderEvent(sliderName, position);
        scriptContext.play(moveSliderEvent);
    }

    public void finishTest() {
        testReporter.finishTest();
    }
}
