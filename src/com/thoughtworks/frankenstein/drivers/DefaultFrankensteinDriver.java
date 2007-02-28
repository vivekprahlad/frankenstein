package com.thoughtworks.frankenstein.drivers;

import com.thoughtworks.frankenstein.application.Application;
import com.thoughtworks.frankenstein.application.CommandLineApplication;
import com.thoughtworks.frankenstein.application.RegexWorkerThreadMonitor;
import com.thoughtworks.frankenstein.application.WorkerThreadMonitor;
import com.thoughtworks.frankenstein.events.*;
import com.thoughtworks.frankenstein.events.actions.ClickAction;
import com.thoughtworks.frankenstein.events.actions.DoubleClickAction;
import com.thoughtworks.frankenstein.events.actions.RightClickAction;
import com.thoughtworks.frankenstein.events.assertions.AssertEvent;
import com.thoughtworks.frankenstein.naming.DefaultNamingStrategy;
import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.DefaultComponentFinder;
import com.thoughtworks.frankenstein.playback.DefaultWindowContext;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.recorders.DefaultScriptContext;
import com.thoughtworks.frankenstein.recorders.ScriptContext;
import com.thoughtworks.frankenstein.script.CompositeReporter;
import com.thoughtworks.frankenstein.script.HtmlTestReporter;
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
    private Application application;

    public DefaultFrankensteinDriver(Class mainClass, String[] args) {
        this(mainClass, args, new HtmlTestReporter());
    }

    public DefaultFrankensteinDriver(Class mainClass, String[] args, TestReporter testReporter) {
        this(mainClass, args, testReporter, "test");
    }

    public DefaultFrankensteinDriver(Class mainClass, String[] args, TestReporter testReporter, String testName) {
        this(mainClass, args, testReporter, new RegexWorkerThreadMonitor("UIWorker"), testName);
    }

    public DefaultFrankensteinDriver(Class mainClass,
                                     String[] args,
                                     TestReporter testReporter,
                                     WorkerThreadMonitor threadMonitor,
                                     String testName) {
        this(mainClass, args, testReporter, threadMonitor, new DefaultComponentFinder(new DefaultNamingStrategy()), new DefaultWindowContext(), testName);
    }

    public DefaultFrankensteinDriver(Class mainClass,
                                     String[] args,
                                     TestReporter testReporter,
                                     WorkerThreadMonitor threadMonitor,
                                     ComponentFinder componentFinder,
                                     WindowContext windowContext,
                                     String testName) {
        this.testReporter = createCompositeReporter(testReporter);
        this.scriptContext = new DefaultScriptContext(this.testReporter, threadMonitor, windowContext, componentFinder);
        application = createApplication(mainClass);
        application.launch(args);
        startTest(testName);
    }

    public DefaultFrankensteinDriver(Application application,
                                     String[] args,
                                     TestReporter testReporter,
                                     WorkerThreadMonitor threadMonitor,
                                     ComponentFinder componentFinder,
                                     WindowContext windowContext,
                                     String testName) {
        this.testReporter = createCompositeReporter(testReporter);
        this.scriptContext = new DefaultScriptContext(this.testReporter, threadMonitor, windowContext, componentFinder);
        this.application = application;
        this.application.launch(args);
        startTest(testName);
    }


    private Application createApplication(Class mainClass) {
        return new CommandLineApplication(mainClass);
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
        scriptContext.play(new ActivateDialogEvent(dialogTitle));
    }

    public void activateWindow(String windowTitle) {
        scriptContext.play(new ActivateWindowEvent(windowTitle));
    }

    public void activateInternalFrame(String internalFrameTitle) {
        scriptContext.play(new ActivateInternalFrameEvent(internalFrameTitle));
    }

    public void assertValue(String componentName, String ognlExpression, String expectedValue) {
        scriptContext.play(new AssertEvent(componentName, ognlExpression, expectedValue));
    }

    public void assertNumberOfTableRows(String tableName, int expectedNumberOfRows) {
        assertValue(tableName, "rowCount", String.valueOf(expectedNumberOfRows));
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
        scriptContext.play(new AssertLabelEvent(expectedText));
    }

    public void cancelTableEdit(String tableName) {
        scriptContext.play(new CancelTableEditEvent(tableName));
    }

    public void clickButton(String buttonName) {
        scriptContext.play(new ButtonEvent(buttonName, new ClickAction()));
    }

    public void clickCheckBox(String checkBoxName, boolean isChecked) {
        scriptContext.play(new CheckboxEvent(checkBoxName, isChecked, new ClickAction()));
    }

    public void clickRadioButton(String radioButtonName) {
        scriptContext.play(new RadioButtonEvent(radioButtonName, new ClickAction()));
    }

    public void clickTableHeader(String tableHeaderName, String tableColumnName) {
        scriptContext.play(new TableHeaderEvent(tableHeaderName, tableColumnName, new ClickAction()));
    }

    public void closeInternalFrame(String internalFrameTitle) {
        scriptContext.play(new CloseInternalFrameEvent(internalFrameTitle));
    }

    public void closeAllDialogs() {
        scriptContext.play(new CloseAllDialogsEvent(null));
    }

    public void delay(int milliseconds) {
        scriptContext.play(new DelayEvent(String.valueOf(milliseconds)));
    }

    public void dialogClosed(String dialogTitle) {
        scriptContext.play(new DialogClosedEvent(dialogTitle));
    }

    public void dialogShown(String dialogTitle) {
        scriptContext.play(new DialogShownEvent(dialogTitle));
    }

    public void doubleClickTableRow(String tableName, int rowIndex) {
        scriptContext.play(new TableRowEvent(tableName, rowIndex, new DoubleClickAction()));
    }

    public void doubleClickList(String listName, int itemIndex) {
        scriptContext.play(new ListEvent(listName, itemIndex, new DoubleClickAction()));
    }

    public void doubleClickTree(String treeName, String[] pathElements) {
        scriptContext.play(new TreeEvent(treeName, pathElements, new DoubleClickAction()));
    }

    public void enterText(String textFieldname, String text) {
        scriptContext.play(new EnterTextEvent(textFieldname, text));
    }

    public void editTableCell(String tableName, int row, int column) {
        scriptContext.play(new EditTableCellEvent(tableName, row, column));
    }

    public void internalFrameShown(String internalFrameTitle) {
        scriptContext.play(new InternalFrameShownEvent(internalFrameTitle));
    }

    public void keyStroke(String keyModifierAndKeyCodeText) {
        scriptContext.play(new KeyStrokeEvent(keyModifierAndKeyCodeText));
    }

    public void navigate(String pathString) {
        scriptContext.play(new NavigateEvent(pathString));
    }

    public void rightClickTree(String treeName, String[] pathElements) {
        scriptContext.play(new TreeEvent(treeName, pathElements, new RightClickAction()));
    }

    public void rightClickList(String listName, int itemIndex) {
        scriptContext.play(new ListEvent(listName, itemIndex, new RightClickAction()));
    }

    public void rightClickTableRow(String tableName, int rowIndex) {
        scriptContext.play(new TableRowEvent(tableName, rowIndex, new RightClickAction()));
    }

    public void selectDropDown(String comboBoxName, String value) {
        scriptContext.play(new SelectDropDownEvent(comboBoxName, value));
    }

    public void selectFile(String filePath) {
        scriptContext.play(new SelectFileEvent(filePath));
    }

    public void selectFiles(String[] filePaths) {
        scriptContext.play(new SelectFilesEvent(filePaths));
    }

    public void selectList(String listName, String[] listElements) {
        scriptContext.play(new SelectListEvent(listName, listElements));
    }

    public void selectTableRow(String tableName, int[] rows) {
        scriptContext.play(new SelectTableRowEvent(tableName, rows));
    }

    public void selectTree(String treeName, String[] pathElements) {
        scriptContext.play(new SelectTreeEvent(treeName, pathElements));
    }

    public void stopTableEdit(String tableName) {
        scriptContext.play(new StopTableEditEvent(tableName));
    }

    public void switchTab(String tabPaneTitle, String tabTitle) {
        scriptContext.play(new SwitchTabEvent(tabPaneTitle, tabTitle));
    }

    public void moveSlider(String sliderName, int position) {
        scriptContext.play(new MoveSliderEvent(sliderName, position));
    }

    public void finishTest() {
        testReporter.finishTest();
    }
}
