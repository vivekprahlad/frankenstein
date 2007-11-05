package com.thoughtworks.frankenstein.drivers;

import com.thoughtworks.frankenstein.application.Application;
import com.thoughtworks.frankenstein.events.*;
import com.thoughtworks.frankenstein.events.actions.ClickAction;
import com.thoughtworks.frankenstein.events.actions.DoubleClickAction;
import com.thoughtworks.frankenstein.events.actions.RightClickAction;
import com.thoughtworks.frankenstein.events.assertions.AssertEvent;
import com.thoughtworks.frankenstein.recorders.ScriptContext;
import com.thoughtworks.frankenstein.recorders.ScriptListener;
import com.thoughtworks.frankenstein.script.HtmlTestReporter;
import com.thoughtworks.frankenstein.script.TestReporter;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import java.util.List;

/**
 * Ensures the behaviour of the DefaultFrankensteinDriver.
 *
 * @author Pavan
 * @author Prakash
 */
public class DefaultFrankensteinDriverTest extends MockObjectTestCase {
    private String testName = "test";
    public DefaultFrankensteinDriver frankensteinDriver;
    private Mock scriptContextMock;


    protected void setUp() throws Exception {
        Mock applicationMock = mock(Application.class);
        applicationMock.expects(once()).method("launch").with(ANYTHING);
        frankensteinDriver = new DefaultFrankensteinDriver((Application) applicationMock.proxy(), new String[]{"abc", "def"}, new NullTestReporter(), null, null, null, testName);
        scriptContextMock = mock(ScriptContext.class);
    }

    public void testClickButtonPlaysClickEvent() {
        Mock scriptContextMock = mock(ScriptContext.class);
        String buttonName = "button1";
        ButtonEvent buttonEvent = createButtonEventAction(buttonName);
        scriptContextMock.expects(once()).method("startMonitor");
        scriptContextMock.expects(once()).method("play").with(eq(buttonEvent));
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.clickButton(buttonName);
    }

    public void testClickButtonReceivesAnExceptionWhenPlayingAnEventThrowsAnException() {
        TestScriptContext scriptContext = new TestScriptContext(new HtmlTestReporter());
        String buttonName = "button1";
        frankensteinDriver.setScriptContext(scriptContext);
        try {
            frankensteinDriver.clickButton(buttonName);
            fail();
        } catch (Exception expected) {
        }
    }

    public void testCreationOfDefaultFrankensteinDriverObjectDoesAllInitialization() throws ClassNotFoundException {
        String testName = "test";
        Mock testReporterMock = mock(TestReporter.class);
        testReporterMock.expects(once()).method("startTest").with(eq(testName));
        Mock applicationMock = mock(Application.class);
        applicationMock.expects(once()).method("launch").with(ANYTHING);
        new DefaultFrankensteinDriver((Application) applicationMock.proxy(), new String[]{"abc", "def"}, (TestReporter) testReporterMock.proxy(), null, null, null, testName);
    }

    public void testSettingScriptContextStartsTheNewScriptContextMonitor() {
        Mock scriptContextMock = mock(ScriptContext.class);
        scriptContextMock.expects(once()).method("startMonitor");
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
    }

    public void testActivateAppletEvent() {
        String appletName = "applet";
        ActivateAppletEvent windowEvent = new ActivateAppletEvent(appletName);
        setScriptContextExpectation(windowEvent);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.activateApplet(appletName);
    }

    public void testActivateWindowEvent() {
        String windowTitle = "window";
        ActivateWindowEvent windowEvent = new ActivateWindowEvent(windowTitle);
        setScriptContextExpectation(windowEvent);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.activateWindow(windowTitle);
    }

    public void testActivateInternalFrameEvent() {
        String internalFrameTitle = "window";
        ActivateInternalFrameEvent event = new ActivateInternalFrameEvent(internalFrameTitle);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.activateInternalFrame(internalFrameTitle);
    }

    public void testAssertValue() {
        String value = "name";
        String ognl = "ognl";
        String expected = "true";
        AssertEvent event = new AssertEvent(value, ognl, expected);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.assertValue(value, ognl, expected);
    }

    public void testAssertNumberOfTableRows() {
        String tableName = "table";
        int numberOfRows = 1;
        AssertEvent event = new AssertEvent(tableName, "rowCount", String.valueOf(numberOfRows));
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.assertNumberOfTableRows(tableName, numberOfRows);
    }

    public void testAssertTableCell() {
        String tableName = "table";
        int row = 1;
        int column = 1;
        String expectedValue = "expected";
        AssertEvent event = new AssertEvent(tableName, "getValueAt(" + row + "," + column + ")", expectedValue);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.assertTableCell(tableName, row, column, expectedValue);
    }

    public void testAssertTableRow() {
        String tableName = "table";
        int row = 1;
        String[] expectedValue = new String[]{"expected"};
        AssertEvent event = new AssertEvent(tableName, "getValueAt(" + row + "," + 0 + ")", expectedValue[0]);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.assertTableRow(tableName, row, expectedValue);
    }

    public void testAssertText() {
        String textComponentName = "text";
        String expectedValue = "expected";
        AssertEvent event = new AssertEvent(textComponentName, "text", expectedValue);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.assertText(textComponentName, expectedValue);
    }

    public void testAssertTrue() {
        String componentName = "text";
        String ognlExpression = "ognl";
        String expectedValue = "true";
        AssertEvent event = new AssertEvent(componentName, ognlExpression, expectedValue);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.assertTrue(componentName, ognlExpression);
    }

    public void testAssertFalse() {
        String componentName = "text";
        String ognlExpression = "ognl";
        String expectedValue = "false";
        AssertEvent event = new AssertEvent(componentName, ognlExpression, expectedValue);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.assertFalse(componentName, ognlExpression);
    }

    public void testAssertEnabled() {
        String componentName = "text";
        String ognlExpression = "enabled";
        String expectedValue = "true";
        AssertEvent event = new AssertEvent(componentName, ognlExpression, expectedValue);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.assertEnabled(componentName);
    }

    public void testAssertDisabled() {
        String componentName = "text";
        String ognlExpression = "enabled";
        String expectedValue = "false";
        AssertEvent event = new AssertEvent(componentName, ognlExpression, expectedValue);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.assertDisabled(componentName);
    }

    public void testAssertToggleButtonSelected() {
        String toggleButtonName = "toggleButton";
        String ognlExpression = "selected";
        String expectedValue = "true";
        AssertEvent event = new AssertEvent(toggleButtonName, ognlExpression, expectedValue);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.assertToggleButtonSelected(toggleButtonName);
    }

    public void testAssertToggleButtonNotSelected() {
        String toggleButtonName = "toggleButton";
        String ognlExpression = "selected";
        String expectedValue = "false";
        AssertEvent event = new AssertEvent(toggleButtonName, ognlExpression, expectedValue);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.assertToggleButtonNotSelected(toggleButtonName);
    }

    public void testAssertCheckBoxSelected() {
        String checkBoxName = "checkBox";
        String ognlExpression = "selected";
        String expectedValue = "true";
        AssertEvent event = new AssertEvent(checkBoxName, ognlExpression, expectedValue);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.assertCheckBoxSelected(checkBoxName);
    }

    public void testAssertCheckBoxNotSelected() {
        String checkBoxName = "checkBox";
        String ognlExpression = "selected";
        String expectedValue = "false";
        AssertEvent event = new AssertEvent(checkBoxName, ognlExpression, expectedValue);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.assertCheckBoxNotSelected(checkBoxName);
    }

    public void testAssertRadioButtonSelected() {
        String radioButtonName = "radioButton";
        String ognlExpression = "selected";
        String expectedValue = "true";
        AssertEvent event = new AssertEvent(radioButtonName, ognlExpression, expectedValue);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.assertRadioButtonSelected(radioButtonName);
    }

    public void testAssertRadioButtonNotSelected() {
        String radioButtonName = "radioButton";
        String ognlExpression = "selected";
        String expectedValue = "false";
        AssertEvent event = new AssertEvent(radioButtonName, ognlExpression, expectedValue);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.assertRadioButtonNotSelected(radioButtonName);
    }

    public void testAssertLabel() {
        String expectedText = "expected";
        AssertLabelEvent event = new AssertLabelEvent(expectedText);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.assertLabel(expectedText);
    }

    public void testCancelTableEdit() {
        String tableName = "table";
        CancelTableEditEvent event = new CancelTableEditEvent(tableName);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.cancelTableEdit(tableName);
    }

    public void testClickCheckBoxSelected() {
        String checkBoxName = "checkBox";
        CheckboxEvent event = new CheckboxEvent(checkBoxName, true, new ClickAction());
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.clickCheckBox(checkBoxName, true);
    }

    public void testClickCheckBoxDeSelected() {
        String checkBoxName = "checkBox";
        CheckboxEvent event = new CheckboxEvent(checkBoxName, false, new ClickAction());
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.clickCheckBox(checkBoxName, false);
    }

    public void testClickRadioButton() {
        String radioButtonName = "radioButton";
        RadioButtonEvent event = new RadioButtonEvent(radioButtonName, new ClickAction());
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.clickRadioButton(radioButtonName);
    }

    public void testClickTableHeader() {
        String tableName = "table";
        String tableColumnName = "column";
        TableHeaderEvent event = new TableHeaderEvent(tableName, tableColumnName, new ClickAction());
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.clickTableHeader(tableName, tableColumnName);
    }

    public void testCloseInternalFrame() {
        String internalFrameTitle = "title";
        CloseInternalFrameEvent event = new CloseInternalFrameEvent(internalFrameTitle);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.closeInternalFrame(internalFrameTitle);
    }

    public void testCloseAllDialogs() {
        CloseAllDialogsEvent event = new CloseAllDialogsEvent(null);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.closeAllDialogs();
    }

    public void testDelay() {
        DelayEvent event = new DelayEvent(String.valueOf(100));
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.delay(100);
    }

    public void testDialogClosed() {
        String dialogTitle = "title";
        DialogClosedEvent event = new DialogClosedEvent(dialogTitle);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.dialogClosed(dialogTitle);
    }

    public void testDialogShown() {
        String dialogTitle = "title";
        DialogShownEvent event = new DialogShownEvent(dialogTitle);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.dialogShown(dialogTitle);
    }

    public void testDoubleClickTableRow() {
        String tableName = "table";
        int row = 1;
        TableRowEvent event = new TableRowEvent(tableName, row, new DoubleClickAction());
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.doubleClickTableRow(tableName, row);
    }

    public void testDoubleClickList() {
        String listName = "list";
        int itemIndex = 1;
        ListEvent event = new ListEvent(listName, itemIndex, new DoubleClickAction());
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.doubleClickList(listName, itemIndex);
    }

    public void testDoubleClickTree() {
        String treeName = "tree";
        String[] pathElements = new String[]{"1>2"};
        TreeEvent event = new TreeEvent(treeName, pathElements, new DoubleClickAction());
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.doubleClickTree(treeName, pathElements);
    }

    public void testEnterText() {
        String textFieldName = "textField";
        String text = "text";
        EnterTextEvent event = new EnterTextEvent(textFieldName, text);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.enterText(textFieldName, text);
    }

    public void testEditTableCell() {
        String tableName = "table";
        int row = 1;
        int column = 2;
        EditTableCellEvent event = new EditTableCellEvent(tableName, row, column);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.editTableCell(tableName, row, column);
    }

    public void testInternalFrameShown() {
        String internalFrameTitle = "internalFrame";
        InternalFrameShownEvent event = new InternalFrameShownEvent(internalFrameTitle);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.internalFrameShown(internalFrameTitle);
    }

    public void testKeyStroke() {
        String keyStrokeAndModifiers = "A";
        KeyStrokeEvent event = new KeyStrokeEvent(keyStrokeAndModifiers);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.keyStroke(keyStrokeAndModifiers);
    }

    public void testNavigate() {
        String pathString = "1>2>3";
        NavigateEvent event = new NavigateEvent(pathString);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.navigate(pathString);
    }

    public void testRightClickTree() {
        String treeName = "tree";
        String[] pathElements = new String[]{"1", "2", "3"};
        TreeEvent event = new TreeEvent(treeName, pathElements, new RightClickAction());
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.rightClickTree(treeName, pathElements);
    }

    public void testRightClickList() {
        String listName = "list";
        int itemIndex = 1;
        ListEvent event = new ListEvent(listName, itemIndex, new RightClickAction());
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.rightClickList(listName, itemIndex);
    }

    public void testRightClickTableRow() {
        String tableName = "table";
        int rowIndex = 1;
        TableRowEvent event = new TableRowEvent(tableName, rowIndex, new RightClickAction());
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.rightClickTableRow(tableName, rowIndex);
    }

    public void testSelectDropDown() {
        String comboBoxName = "combo";
        String value = "value";
        SelectDropDownEvent event = new SelectDropDownEvent(comboBoxName, value);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.selectDropDown(comboBoxName, value);
    }

    public void testSelectFile() {
        String filePath = "file";
        SelectFileEvent event = new SelectFileEvent(filePath);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.selectFile(filePath);
    }

    public void testSelectFiles() {
        String[] filePaths = new String[]{"file1", "file2"};
        SelectFilesEvent event = new SelectFilesEvent(filePaths);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.selectFiles(filePaths);
    }

    public void testSelectList() {
        String listName = "list";
        String[] listElements = new String[]{"1", "2"};
        SelectListEvent event = new SelectListEvent(listName, listElements);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.selectList(listName, listElements);
    }

    public void testSelectTableRow() {
        String tableName = "table";
        int[] rows = {1, 2};
        SelectTableRowEvent event = new SelectTableRowEvent(tableName, rows);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.selectTableRow(tableName, rows);
    }

    public void testSelectTree() {
        String treeName = "tree";
        String[] pathElements = {"1", "2"};
        SelectTreeEvent event = new SelectTreeEvent(treeName, pathElements);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.selectTree(treeName, pathElements);
    }

    public void testStopTableEdit() {
        String tableName = "table";
        StopTableEditEvent event = new StopTableEditEvent(tableName);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.stopTableEdit(tableName);
    }

    public void testSwitchTab() {
        String tabPaneTitle = "tabPane";
        String tabTitle = "tab";
        SwitchTabEvent event = new SwitchTabEvent(tabPaneTitle, tabTitle);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.switchTab(tabPaneTitle, tabTitle);
    }

    public void testMoveSlider() {
        String sliderName = "slider";
        int position = 2;
        MoveSliderEvent event = new MoveSliderEvent(sliderName, position);
        setScriptContextExpectation(event);
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.moveSlider(sliderName, position);
    }

    private void setScriptContextExpectation(FrankensteinEvent event) {
        scriptContextMock.expects(once()).method("startMonitor");
        scriptContextMock.expects(once()).method("play").with(eq(event));
    }

    private ButtonEvent createButtonEventAction(String buttonName) {
        return new ButtonEvent(buttonName, new ClickAction());
    }

    private class TestScriptContext implements ScriptContext {
        private TestReporter reporter;

        public TestScriptContext(TestReporter reporter) {
            this.reporter = reporter;
        }

        public void startTest(String testName) {
        }

        public void play(List events) {
        }

        public boolean isScriptPassed() {
            return false;
        }

        public void addScriptListener(ScriptListener listener) {
        }

        public void removeScriptListener(ScriptListener listener) {
        }

        public void play(FrankensteinEvent event) {
            reporter.reportFailure(event, new Exception(), null);
        }

        public void startMonitor() {
        }
    }

    protected void tearDown() throws Exception {
        frankensteinDriver.finishTest();
        super.tearDown();
    }
}
