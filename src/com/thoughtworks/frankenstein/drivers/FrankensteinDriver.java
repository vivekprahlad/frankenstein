package com.thoughtworks.frankenstein.drivers;

/**
 * The Frankenstein driver allows a Swing user interface to be tested.
 * <p/>
 * <p/>
 * The Client of this API should ensure that finishTest() method is called after doing
 * all test assertions without fail.
 * </p>
 *
 * @author Pavan
 * @author Prakash
 */
public interface FrankensteinDriver {

    /**
     * Activates a dialog with a specified title.
     * The title can be specified as a regular expression.
     *
     * @param dialogTitle The title of the dialog to be activated.
     */
    public void activateDialog(String dialogTitle);

    /**
     * Activates a window with a specified title.
     * <p/>
     * The title can be specified as a regular expression.
     *
     * @param windowTitle The title of the window to be acitvated.
     */
    public void activateWindow(String windowTitle);

    /**
     * Activates an internal frame.
     * The title can be specified as a regular expression.
     *
     * @param internalFrameTitle The title of the internal frame to be activated.
     */
    public void activateInternalFrame(String internalFrameTitle);

    /**
     * Checks whether an OGNL expression evaluated against a specified component matches the specified value.
     * The expected value can be specified as a regular expression.
     *
     * @param componentName  The component name.
     * @param ognlExpression The OGNL expression to be evaluated.
     * @param expectedValue  The expected value.
     */
    public void assertValue(String componentName, String ognlExpression, String expectedValue);

    /**
     * Check the number of rows displayed in a table
     *
     * @param tableName            The name of the table.
     * @param expectedNumberOfRows The expected number of rows in the table.
     */
    public void assertNumberOfTableRows(String tableName, int expectedNumberOfRows);

    /**
     * Check the value of a specified table cell. The cell value can be specified as a regular expression.
     *
     * @param tableName         The name of the table.
     * @param row               The row number of the cell.
     * @param column            The column number of the cell.
     * @param expectedCellValue The expected cell value.
     */
    public void assertTableCell(String tableName, int row, int column, String expectedCellValue);

    /**
     * Check the elements of a specified table row.
     * <p/>
     * The cell values can be an arbitrary list of arguments, which are checked from left to
     * right, starting at the first column.
     * <p/>
     * The cell values can be specified as regular expressions.
     *
     * @param tableName          Name of the table.
     * @param row                The row number to be asserted.
     * @param expectedCellValues The expected cell values.
     */
    public void assertTableRow(String tableName, int row, String[] expectedCellValues);

    /**
     * Checks whether the text of a specified text component matches the specified value.
     * The expected text can be specified as a regular expression.
     *
     * @param textComponentName Name of the text component.
     * @param expectedText      The expected text.
     */
    public void assertText(String textComponentName, String expectedText);

    /**
     * Checks whether the specified OGNL expression is true.
     *
     * @param componentName  Name of the component.
     * @param ognlExpression The ognl expression used to assert.
     */
    public void assertTrue(String componentName, String ognlExpression);

    /**
     * Checks whether the specified OGNL expression is false.
     *
     * @param componentName  Name of the component.
     * @param ognlExpression The ognl expression used to assert.
     */
    public void assertFalse(String componentName, String ognlExpression);

    /**
     * Check whether a component with the specified name is enabled.
     *
     * @param componentName Name of the component.
     */
    public void assertEnabled(String componentName);

    /**
     * Check whether a component with the specified name is enabled.
     *
     * @param componentName Name of the component.
     */
    public void assertDisabled(String componentName);

    /**
     * * Check whether a toggle button with the specified name is selected.
     *
     * @param toggleButtonName Name of the toggle button.
     */
    public void assertToggleButtonSelected(String toggleButtonName);

    /**
     * Check whether a toggle button with the specified name is not selected.
     *
     * @param toggleButtonName Name of the toggle button.
     */
    public void assertToggleButtonNotSelected(String toggleButtonName);

    /**
     * Check whether a check box with the specified name is selected.
     *
     * @param checkBoxName Name of the check box.
     */
    public void assertCheckBoxSelected(String checkBoxName);

    /**
     * Check whether a check box with the specified name is not selected.
     *
     * @param checkBoxName Name of the check box.
     */
    public void assertCheckBoxNotSelected(String checkBoxName);

    /**
     * Check whether a radio button with the specified name is selected.
     *
     * @param radioButtonName Name of the radio button.
     */
    public void assertRadioButtonSelected(String radioButtonName);

    /**
     * Check whether a radio button with the specified name is not selected.
     *
     * @param radioButtonName Name of the radio button.
     */
    public void assertRadioButtonNotSelected(String radioButtonName);

    /**
     * Checks whether the text of a specified label component matches the specified value.
     *
     * @param expectedText The expected value in the label.
     */
    public void assertLabel(String expectedText);

    /**
     * Cancels a table edit.
     *
     * @param tableName Name of the table on which to cancel edit.
     */
    public void cancelTableEdit(String tableName);

    /**
     * Click on a specified button.
     *
     * @param buttonName Name of the button to be clicked.
     */
    public void clickButton(String buttonName);

    /**
     * Click on a specified checkbox.
     * The check box will be selected or deselected based on the isChecked value.
     *
     * @param checkBoxName The name of the check box.
     * @param isChecked    true if check box is expected to be checked, false otherwise.
     */
    public void clickCheckBox(String checkBoxName, boolean isChecked);

    /**
     * Click on the specified radio button.
     *
     * @param radioButtonName The name of the radio button to be clicked.
     */
    public void clickRadioButton(String radioButtonName);

    /**
     * Click on a specified column of a table header
     *
     * @param tableHeaderName The name of the table header.
     * @param tableColumnName The name of the table column name.
     */
    public void clickTableHeader(String tableHeaderName, String tableColumnName);

    /**
     * Close an internal frame with the specified title.
     * The title can be specified as a regular expression.
     *
     * @param internalFrameTitle The title of the internal frame to be closed.
     */
    public void closeInternalFrame(String internalFrameTitle);

    /**
     * Close all open dialogs recursively until no more dialogs are open.
     * This method can be used to remove unexpected dialogs at the end of a test.
     */
    public void closeAllDialogs();

    /**
     * Wait for a specified duration (in milliseconds)
     *
     * @param milliseconds The duration to wait in milliseconds.
     */
    public void delay(int milliseconds);

    /**
     * Wait for a dialog with a specified title to be closed.
     * <p/>
     * This method waits for 10 seconds in case the dialog is found to be open.
     * (The wait time will be made configurable in a future release).
     * The title can be specified as a regular expression.
     *
     * @param dialogTitle The title of the dialog to be waited for.
     */
    public void dialogClosed(String dialogTitle);

    /**
     * Wait for a dialog with a specified title to be opened.
     * <p/>
     * This function waits for 10 seconds in case the dialog is not found to be open.
     * (The wait will be made configurable in a future release).
     * The title can be specified as a regular expression.
     *
     * @param dialogTitle The title of the dialog to be waited for.
     */
    public void dialogShown(String dialogTitle);

    /**
     * Double click on a table row of a specified table.
     *
     * @param tableName The name of the table.
     * @param rowIndex  The row of the table.
     */
    public void doubleClickTableRow(String tableName, int rowIndex);

    /**
     * Double click on a list item of a specified list.
     *
     * @param listName  The name of the list.
     * @param itemIndex The row of the list.
     */
    public void doubleClickList(String listName, int itemIndex);

    /**
     * Double clicks on a tree. Supports regular expressions.
     * <p/>
     * Example: doubleClickTree("tree_name", new String[]{"top level", "/.*level/", "third level"});
     * </p>
     *
     * @param treeName     The name of the tree.
     * @param pathElements The array of the path elements of the tree.
     */
    public void doubleClickTree(String treeName, String[] pathElements);

    /**
     * Enter the specified text into a specified text field.
     *
     * @param textFieldname The name of the text field.
     * @param text          The text to be entered into the text field.
     */
    public void enterText(String textFieldname, String text);

    /**
     * Edit the table cell, identified by the row and the column numbers provided.
     *
     * @param tableName The name of the table.
     * @param row       The row number of the cell.
     * @param column    The column number of the cell.
     */
    public void editTableCell(String tableName, int row, int column);

    /**
     * Checks that an internal frame with the specified title has been shown
     * The title can be specified as a regular expression.
     *
     * @param internalFrameTitle The title of the internal frame.
     */
    public void internalFrameShown(String internalFrameTitle);

    /**
     * Enter a keystroke with the specified modifiers.
     * <p/>
     * Example: keyStroke("Ctrl+Alt 0") for the modifiers "Control" & "Alt" and the key "0".
     * </p>
     *
     * @param keyModifierAndKeyCodeText The key modifier and the key code given as a string separated by a space.
     */
    public void keyStroke(String keyModifierAndKeyCodeText);

    /**
     * Navigate to a specified path in a menu item.
     * Both menu bars and popup menus are supported.
     * <p/>
     * The path needs to be specified as a string delimited by the ">" character.
     * <p/>
     * Example: "first level>second level>item".
     * </p>
     * <p/>
     * This function does not have regular expression support.
     *
     * @param pathString The delimited path string.
     */
    public void navigate(String pathString);

    /**
     * Right click on a tree item of the specified tree. Supports regular expressions
     * <p/>
     * Example: rightClickTree("tree_name", new String[]{"top level", "/.*level/", "third level"});
     * </p>
     *
     * @param treeName     The name of the tree.
     * @param pathElements The array of the path elements of the tree.
     */
    public void rightClickTree(String treeName, String[] pathElements);

    /**
     * Right click on a list item of a specified list.
     *
     * @param listName  The name of the list.
     * @param itemIndex The index of the item in the list.
     */
    public void rightClickList(String listName, int itemIndex);

    /**
     * Right click on a table row of a specified table.
     *
     * @param tableName The name of the table.
     * @param rowIndex  The index of the table row to right clicked.
     */
    public void rightClickTableRow(String tableName, int rowIndex);

    /**
     * Select a specified value from a specified combo box.
     *
     * @param comboBoxName The name of the combo box.
     * @param value        The value to be selected in the combo box.
     */
    public void selectDropDown(String comboBoxName, String value);

    /**
     * Select a specified file in a file chooser.
     *
     * @param filePath The path of the file to be choosen.
     */
    public void selectFile(String filePath);

    /**
     * Select multiple files in a file chooser.
     *
     * @param filePaths The array of the file paths.
     */
    public void selectFiles(String[] filePaths);

    /**
     * Select a specified value in a specified list.
     *
     * @param listName     The name of the list.
     * @param listElements The array of the list elements to be selected.
     */
    public void selectList(String listName, String[] listElements);

    /**
     * Selects the specified rows in a table.
     * <p/>
     * Example: selectTableRow("table_name" , new int[]{1,2,3});
     * </p>
     *
     * @param tableName The name of the table.
     * @param rows      The arary of rows to be selected.
     */
    public void selectTableRow(String tableName, int[] rows);

    /**
     * Select a specified tree path. Supports regular expressions
     * <p/>
     * Example: selectTree("tree_name", new String[] {"top level", "/.*level/", "third level"});
     * </p>
     *
     * @param treeName     The name of the tree.
     * @param pathElements The array of the path elements.
     */
    public void selectTree(String treeName, String[] pathElements);

    /**
     * Stop editing a specified table.
     *
     * @param tableName The name of the table.
     */
    public void stopTableEdit(String tableName);

    /**
     * Switch to a tab with the specified title of a specified tabbed pane.
     * The title can be specified as a regular expression.
     *
     * @param tabPaneTitle The title of the tabbed pane.
     * @param tabTitle     The title of the tab.
     */
    public void switchTab(String tabPaneTitle, String tabTitle);

    /**
     * Moves a specified slider to the specified position
     *
     * @param sliderName The name of the slider.
     */
    public void moveSlider(String sliderName, int position);

    /**
     * Finishes the current test session.
     */
    public void finishTest();
}
