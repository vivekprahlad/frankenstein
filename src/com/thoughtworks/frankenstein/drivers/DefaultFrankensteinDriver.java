package com.thoughtworks.frankenstein.drivers;

import com.thoughtworks.frankenstein.application.Application;
import com.thoughtworks.frankenstein.application.PlaybackFrankensteinIntegration;
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
import com.thoughtworks.frankenstein.recorders.ScriptContext;
import com.thoughtworks.frankenstein.script.HtmlTestReporter;
import com.thoughtworks.frankenstein.script.TestReporter;

/**
 * Default FrankensteinDriver implementation.
 *
 * @author Pavan
 * @author Prakash
 */
public class DefaultFrankensteinDriver implements FrankensteinDriver {
    private PlaybackFrankensteinIntegration frankensteinIntegration;

    public DefaultFrankensteinDriver(Class mainClass, String[] args) {
        frankensteinIntegration = new PlaybackFrankensteinIntegration(mainClass, new HtmlTestReporter());
        frankensteinIntegration.start(args);
        startTest("test");
    }

    public DefaultFrankensteinDriver(Class mainClass, String[] args, TestReporter testReporter) {
        frankensteinIntegration = new PlaybackFrankensteinIntegration(mainClass, testReporter);
        frankensteinIntegration.start(args);
        startTest("test");
    }

    public DefaultFrankensteinDriver(Class mainClass, String[] args, TestReporter testReporter, String testName) {
        frankensteinIntegration = new PlaybackFrankensteinIntegration(mainClass, testReporter);
        frankensteinIntegration.start(args);
        startTest(testName);
    }

    public DefaultFrankensteinDriver(Class mainClass,
                                     String[] args,
                                     TestReporter testReporter,
                                     WorkerThreadMonitor threadMonitor,
                                     String testName) {
        frankensteinIntegration = new PlaybackFrankensteinIntegration(mainClass, testReporter, threadMonitor, new DefaultComponentFinder(new DefaultNamingStrategy()), new DefaultWindowContext());
        frankensteinIntegration.start(args);
        startTest(testName);
    }

    public DefaultFrankensteinDriver(Class mainClass,
                                     String[] args,
                                     TestReporter testReporter,
                                     WorkerThreadMonitor threadMonitor,
                                     ComponentFinder componentFinder,
                                     WindowContext windowContext,
                                     String testName) {
        frankensteinIntegration = new PlaybackFrankensteinIntegration(mainClass, testReporter, threadMonitor, componentFinder, windowContext);
        frankensteinIntegration.start(args);
        startTest(testName);
    }

    public DefaultFrankensteinDriver(Application application, String[] args) {
        frankensteinIntegration = new PlaybackFrankensteinIntegration(application, new HtmlTestReporter());
        frankensteinIntegration.start(args);
        startTest("test");
    }

    public DefaultFrankensteinDriver(Application application, String[] args, TestReporter testReporter) {
        frankensteinIntegration = new PlaybackFrankensteinIntegration(application, testReporter);
        frankensteinIntegration.start(args);
        startTest("test");
    }

    public DefaultFrankensteinDriver(Application application,
                                     String[] args,
                                     TestReporter testReporter,
                                     String testName) {
        frankensteinIntegration = new PlaybackFrankensteinIntegration(application, testReporter);
        frankensteinIntegration.start(args);
        startTest(testName);
    }

    public DefaultFrankensteinDriver(Application application,
                                     String[] args,
                                     TestReporter testReporter,
                                     RegexWorkerThreadMonitor threadMonitor,
                                     String testName) {
        frankensteinIntegration = new PlaybackFrankensteinIntegration(application, testReporter, threadMonitor, new DefaultComponentFinder(new DefaultNamingStrategy()), new DefaultWindowContext());
        frankensteinIntegration.start(args);
        startTest(testName);
    }

    public DefaultFrankensteinDriver(Application application,
                                     String[] args,
                                     TestReporter testReporter,
                                     WorkerThreadMonitor threadMonitor,
                                     ComponentFinder componentFinder,
                                     WindowContext windowContext,
                                     String testName) {
        frankensteinIntegration = new PlaybackFrankensteinIntegration(application, testReporter, threadMonitor, componentFinder, windowContext);
        frankensteinIntegration.start(args);
        startTest(testName);
    }

    void setScriptContext(ScriptContext scriptContext) {
        frankensteinIntegration.setScriptContext(scriptContext);
    }

    TestReporter getTestReporter() {
        return frankensteinIntegration.getTestReporter();
    }

    private void startTest(String testName) {
        getTestReporter().startTest(testName);
    }

    public void activateApplet(String appletName) {
        frankensteinIntegration.play(new ActivateAppletEvent(appletName));
    }

    public void activateDialog(String dialogTitle) {
        frankensteinIntegration.play(new ActivateDialogEvent(dialogTitle));
    }

    public void activateWindow(String windowTitle) {
        frankensteinIntegration.play(new ActivateWindowEvent(windowTitle));
    }

    public void activateInternalFrame(String internalFrameTitle) {
        frankensteinIntegration.play(new ActivateInternalFrameEvent(internalFrameTitle));
    }

    public void assertValue(String componentName, String ognlExpression, String expectedValue) {
        frankensteinIntegration.play(new AssertEvent(componentName, ognlExpression, expectedValue));
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
        frankensteinIntegration.play(new AssertLabelEvent(expectedText));
    }

    public void cancelTableEdit(String tableName) {
        frankensteinIntegration.play(new CancelTableEditEvent(tableName));
    }

    public void clickButton(String buttonName) {
        frankensteinIntegration.play(new ButtonEvent(buttonName, new ClickAction()));
    }

    public void clickCheckBox(String checkBoxName, boolean isChecked) {
        frankensteinIntegration.play(new CheckboxEvent(checkBoxName, isChecked, new ClickAction()));
    }

    public void clickRadioButton(String radioButtonName) {
        frankensteinIntegration.play(new RadioButtonEvent(radioButtonName, new ClickAction()));
    }

    public void clickTableHeader(String tableHeaderName, String tableColumnName) {
        frankensteinIntegration.play(new TableHeaderEvent(tableHeaderName, tableColumnName, new ClickAction()));
    }

    public void closeInternalFrame(String internalFrameTitle) {
        frankensteinIntegration.play(new CloseInternalFrameEvent(internalFrameTitle));
    }

    public void closeAllDialogs() {
        frankensteinIntegration.play(new CloseAllDialogsEvent(null));
    }

    public void delay(int milliseconds) {
        frankensteinIntegration.play(new DelayEvent(String.valueOf(milliseconds)));
    }

    public void dialogClosed(String dialogTitle) {
        frankensteinIntegration.play(new DialogClosedEvent(dialogTitle));
    }

    public void dialogShown(String dialogTitle) {
        frankensteinIntegration.play(new DialogShownEvent(dialogTitle));
    }

    public void doubleClickTableRow(String tableName, int rowIndex) {
        frankensteinIntegration.play(new TableRowEvent(tableName, rowIndex, new DoubleClickAction()));
    }

    public void doubleClickList(String listName, int itemIndex) {
        frankensteinIntegration.play(new ListEvent(listName, itemIndex, new DoubleClickAction()));
    }

    public void doubleClickTree(String treeName, String[] pathElements) {
        frankensteinIntegration.play(new TreeEvent(treeName, pathElements, new DoubleClickAction()));
    }

    public void enterText(String textFieldname, String text) {
        frankensteinIntegration.play(new EnterTextEvent(textFieldname, text));
    }

    public void editTableCell(String tableName, int row, int column) {
        frankensteinIntegration.play(new EditTableCellEvent(tableName, row, column));
    }

    public void internalFrameShown(String internalFrameTitle) {
        frankensteinIntegration.play(new InternalFrameShownEvent(internalFrameTitle));
    }

    public void keyStroke(String keyModifierAndKeyCodeText) {
        frankensteinIntegration.play(new KeyStrokeEvent(keyModifierAndKeyCodeText));
    }

    public void navigate(String pathString) {
        frankensteinIntegration.play(new NavigateEvent(pathString));
    }

    public void rightClickTree(String treeName, String[] pathElements) {
        frankensteinIntegration.play(new TreeEvent(treeName, pathElements, new RightClickAction()));
    }

    public void rightClickList(String listName, int itemIndex) {
        frankensteinIntegration.play(new ListEvent(listName, itemIndex, new RightClickAction()));
    }

    public void rightClickTableRow(String tableName, int rowIndex) {
        frankensteinIntegration.play(new TableRowEvent(tableName, rowIndex, new RightClickAction()));
    }

    public void selectDropDown(String comboBoxName, String value) {
        frankensteinIntegration.play(new SelectDropDownEvent(comboBoxName, value));
    }

    public void selectFile(String filePath) {
        frankensteinIntegration.play(new SelectFileEvent(filePath));
    }

    public void selectFiles(String[] filePaths) {
        frankensteinIntegration.play(new SelectFilesEvent(filePaths));
    }

    public void selectList(String listName, String[] listElements) {
        frankensteinIntegration.play(new SelectListEvent(listName, listElements));
    }

    public void selectTableRow(String tableName, int[] rows) {
        frankensteinIntegration.play(new SelectTableRowEvent(tableName, rows));
    }

    public void selectTree(String treeName, String[] pathElements) {
        frankensteinIntegration.play(new SelectTreeEvent(treeName, pathElements));
    }

    public void stopTableEdit(String tableName) {
        frankensteinIntegration.play(new StopTableEditEvent(tableName));
    }

    public void switchTab(String tabPaneTitle, String tabTitle) {
        frankensteinIntegration.play(new SwitchTabEvent(tabPaneTitle, tabTitle));
    }

    public void moveSlider(String sliderName, int position) {
        frankensteinIntegration.play(new MoveSliderEvent(sliderName, position));
    }

    public void finishTest() {
        getTestReporter().finishTest();
        frankensteinIntegration.stop();
    }
}
