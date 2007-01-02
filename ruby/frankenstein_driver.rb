require  'socket'
class Regexp
  def to_s
    "regex:" + source
  end
end

# Convenient constants.
ANYTHING = /.*/

# The Frankenstein driver allows a Swing user interface to be tested.
# Regular expressions can be specified using the regular Ruby syntax.
module FrankensteinDriver
  @@test_dir =""
  attr_accessor :test_status

  # Activates a dialog with a specified title.
  #
  # The title can be specified as a regular expression
  def activate_dialog(title)
    append_to_script "activate_dialog \"#{title}\""
  end

  # Activates a window with a specified title.
  #
  # The title can be specified as a regular expression
  def activate_window(title)
    append_to_script "activate_window \"#{title}\""
  end

  # Activates an internal frame.
  #
  # The title can be specified as a regular expression
  def activate_internal_frame(title)
    append_to_script "activate_internal_frame \"#{title}\""
  end

  # Check whether a check box with the specified name is selected
  def assert_checkbox_selected(checkbox_name)
    assert_togglebutton_selected checkbox_name
  end

  # Check whether a check box with the specified name is not selected
  def assert_checkbox_not_selected(checkbox_name)
    assert_togglebutton_not_selected checkbox_name
  end

  # Check whether a radio button with the specified name is selected
  def assert_radiobutton_selected(radiobutton_name)
    assert_togglebutton_selected radiobutton_name
  end

  # Check whether a radio button with the specified name is not selected
  def assert_radiobutton_not_selected(radiobutton_name)
    assert_togglebutton_not_selected radiobutton_name
  end

  # Check whether a toggle button with the specified name is selected
  def assert_togglebutton_selected(togglebutton_name)
    assert_true togglebutton_name,"selected"
  end

  # Check whether a toggle button with the specified name is not selected
  def assert_togglebutton_not_selected(togglebutton_name)
    assert_false togglebutton_name,"selected"
  end

  # Check whether a component with the specified name is enabled
  def assert_enabled(component_name)
    assert_true component_name,"enabled"
  end

  # Check whether a component with the specified name is enabled
  def assert_disabled(component_name)
    assert_false component_name,"enabled"
  end

  # Check the number of rows displayed in a table
  def assert_number_of_table_rows(table_name, number_of_rows)
    assert table_name,"rowCount",number_of_rows
  end

  # Check the value of a specified table cell. The cell value can be specified as a regular expression
  def assert_table_cell(table_name, row, column, value)
    assert table_name, "getValueAt(#{row},#{column})", value
  end

  # Check the elements of a specified table row.
  #
  # The cell values can be an arbitrary list of arguments, which are checked from left to
  # right, starting at the first column.
  #
  # The cell values can be specified as regular expressions.
  def assert_table_row(table, row, *cell_values)
    cell_values.each_index {|index| assert_table_cell(table, row, index, cell_values[index])}
  end

  # Checks whether an OGNL expression evaluated against a specified component matches the specified value.
  #
  # The expected value can be specified as a regular expression.
  def assert(component_name,ognl_expression,expected_value)
    append_to_script "assert \"#{component_name}\" \"#{ognl_expression}:#{expected_value}\""
  end

  # Checks whether the specified OGNL expression is true.
  def assert_true(component_name,ognl_expression)
    assert component_name,ognl_expression,"true"
  end

  # Checks whether the specified OGNL expression is false.
  def assert_false(component_name,ognl_expression)
    assert component_name,ognl_expression,"false"
  end

  # Checks whether the text of a specified text component matches the specified value.
  #
  # The expected text can be specified as a regular expression.
  def assert_text(text_component, expected_text)
    assert text_component,"text",expected_text
  end

  # Cancels a table edit.
  def cancel_table_edit(table)
    append_to_script "cancel_table_edit \"#{table}\""
  end

  # Click on a specified button
  def click_button(button)
    append_to_script "click_button \"#{button}\""
  end

  # Click on a specified checkbox.
  #
  # The specified value can be true or false, and will select or deselect the checkbox
  def click_checkbox(button, value)
    append_to_script "click_checkbox \"#{button}\" \"#{value}\""
  end

  # Click on a specified radio button.
  def click_radiobutton(button)
    append_to_script "click_radio_button \"#{button}\""
  end

  # Click on a specified column of a table header
  def click_table_header(header_name,column_name)
    append_to_script "click_table_header \"#(header_name)\" \"#(column_name)\""
  end

  # Close an internal frame with the specified title.
  #
  # The title can be specified as a regular expression.
  def close_internal_frame(title)
    append_to_script "close_internal_frame \"#{title}\""
  end

  # Close all open dialogs recursively until no more dialogs are open.
  #
  # This function can be used to remove unexpected dialogs at the end of a test.
  def close_all_dialogs
    append_to_script "close_all_dialogs"
  end

  # Wait for a specified duration (in milliseconds)
  def delay(duration_in_milliseconds)
    append_to_script "delay \"#{duration_in_milliseconds}\""
  end

  # Wait for a dialog with a specified title to be closed.
  #
  # This function waits for 10 seconds in case the dialog is found to be open.
  # (The wait will be made configurable in a future release).
  # The title can be specified as a regular expression.
  def dialog_closed(title)
    append_to_script "dialog_closed \"#{title}\""
  end

  # Wait for a dialog with a specified title to be opened.
  #
  # This function waits for 10 seconds in case the dialog is not found to be open.
  # (The wait will be made configurable in a future release).
  # The title can be specified as a regular expression.
  def dialog_shown(title)
    append_to_script "dialog_shown \"#{title}\""
  end

  # Double click on a table row of a specified table.
  def double_click_table_row(table, row_index)
    append_to_script "double_click_table_row \"#{table}\",\"#{row_index}\""
  end

  # Double click on a list item of a specified list.
  def double_click_list(list, row_index)
    append_to_script "double_click_list \"#{list}\",\"#{row_index}\""
  end

  # Double clicks on a tree. Supports regular expressions
  #
  # For example: double_click_tree "tree_name", "top level", /.*level/, "third level"
  def double_click_tree(tree,*path)
    path = tree_path(path_elements)
    append_to_script "double_click_tree \"#{tree}\",#{path}"
  end

  # Enter the specified text into a specified text field.
  def enter_text(textfield, text)
    append_to_script "enter_text \"#{textfield}\",\"#{text}\""
  end

  # Edit a table cell at the specified coordinates. The coordinates are specified as a
  # "<row>,<column>" - for example, "1,1"
  def edit_table_cell(table, coords)
    append_to_script "edit_table_cell \"#{table}\",\"#{coords}\""
  end

  # Checks that an internal frame with the specified title has been shown
  # The title can be specified as a regular expression.
  def internal_frame_shown(title)
    append_to_script "internal_frame_shown \"#{title}\""
  end

  # Enter a keystroke with the specified modifiers.
  #
  # This method isn't too tester friendly at the moment (the modifiers and the keycode are both integers).
  # It is recommended that the recorder be used to create keystroke steps.
  # Future releases will make it easier to specify keystrokes.
  def key_stroke(modifiers, keycode)
    append_to_script "key_stroke \"#{modifiers},#{keycode}\""
  end

  # Navigate to a specified path in a menu item.
  # Both menu bars and popup menus are supported.
  #
  # The path needs to be specified as a string delimited by the > character.
  # For example: "first level>second level>item".
  # This function does not have regular expression support.
  def navigate(path)
    append_to_script "navigate \"#{path}\""
  end

  # Right click on a tree item of the specified tree. Supports regular expressions
  #
  # For example: right_click_tree "tree_name", "top level", /.*level/, "third level"
  def right_click_tree(tree, *path_elements)
    path = tree_path(path_elements)
    append_to_script "right_click_tree \"#{tree}\",#{path}"
  end

  # Right click on a list item of a specified list.
  def right_click_list(list,item_index)
    append_to_script "right_click_list \"#{list}\",\"#{item_index}\""
  end

  # Right click on a table row of a specified table.
  def right_click_table_row(table,row_index)
    append_to_script "right_click_table_row \"#{table}\",\"#{row_index}\""
  end

  # Select a specified value from a specified combo box.
  def select_drop_down(combo, value)
    append_to_script "select_drop_down \"#{combo}\",\"#{value}\""
  end

  # Select a specified file in a file chooser.
  def select_file(path)
    append_to_script "select_file \"#{path}\""
  end

  # Select multiple files in a file chooser.
  def select_files(paths)
    append_to_script "select_files \"#{paths}\""
  end

  # Select a specified value in a specified list.
  def select_list(list, value)
    append_to_script "select_list \"#{list}\",\"#{value}\""
  end

  # Selects the specified rows in a table.
  # rows is a comma separated list of table rows.
  #
  # Example: select_table_row "table_name" , "1,2,3"
  def select_table_row(table, rows)
    append_to_script "select_table_row \"#{table}\",\"#{rows}\""
  end

  # Select a specified tree path. Supports regular expressions
  #
  # For example: select_tree "tree_name", "top level", /.*level/, "third level"
  def select_tree(tree, *path_elements)
    path = tree_path(path_elements)
    append_to_script "select_tree \"#{tree}\",#{path}"
  end

  # Stop editing a specified table.
  def stop_table_edit(table)
    append_to_script "stop_table_edit \"#{table}\""
  end

  # Switch to a tab with the specified title of a specified tabbed pane.
  # The title can be specified as a regular expression.
  def switch_tab(tab, title)
    append_to_script "switch_tab \"#{tab}\",\"#{title}\""
  end

  # Moves a specified slider to the specified position
  def move_slider(slider,position)
    append_to_script "move_slider \"#{slider}\",\"#{position}\""
  end

  # Sends a test script to the Frankenstein Java runtime at the specified host and port.
  #
  # Waits for the test to complete, and reports test results.
  def run_frankenstein_test(host,port)
    init host,port
    start_test @@test_dir == "" ? @test_name : @@test_dir + "/" + @test_name
    test
    finish_test
    puts @test_name + " : " + (@test_status == "F" ? "Failed" : "Passed")
    @test_status
  end

  # Sets the location of the Frankenstein test reporting directory. The directory is usually specified before a test run.
  def FrankensteinDriver.test_dir=(dir)
    @@test_dir = dir
  end

  # Returns the location of the current test reporting directory.
  def FrankensteinDriver.test_dir
    @@test_dir
  end

  private
  def init(host,port)
    @test_name = self.class.to_s
    @host = host
    @port = port
    @script = ""
  end

  def append_to_script(script_line)
    @script += script_line.gsub("\n", "&#xA;") + "\n"
  end

  def start_test(testname)
    @script +="start_test \"#{testname}\"\n"
  end

  def finish_test
    socket = TCPSocket.new(@host,@port)
    socket.write @script
    socket.close_write
    recvthread = Thread.start do
        @test_status = socket.read
      end
    recvthread.join
    socket.close
  end

  def tree_path(*path_elements)
    "\"" + path_elements.join("\",\"") + "\""
  end
end

# Runs multiple tests in a suite. Generates an index file that displays whether a test passed or failed.
class TestRunner
  def initialize(host="localhost",port=5678)
    @test_reporter = TestReporter.new
    @host = host
    @port = port
  end

  # Runs multiple tests. The argument to the function is a list of Frankenstein test classes.
  def run(*tests)
    tests.each {|test| @test_reporter.report_test_result(test,test.new.run_frankenstein_test(@host,@port))}
    @test_reporter.report
  end
end

# Stores the result of a test run.
class TestResult
  attr_accessor :test
  def initialize(test,status)
    @test,@status = test,status
  end

  def status
    @status == "P" ? "#CFFFCF" : "#FFCFCF"
  end
end

# Creates an HTML report of a test run. Lists out the names of the tests that ran, and color codes tests that passed or failed.
class TestReporter
  def initialize
    @tests = []
  end

  def report_test_result(test,result)
    @tests.push TestResult.new(test,result)
  end

  def report
    index_file = File.new(FrankensteinDriver.test_dir + "/" + "index.html", "w+")
    index_file.puts "<html>"
    index_file.puts "<head><title>Test Results</title></head>"
    index_file.puts "<body>"
    index_file.puts "<h3>Test Results</h3><br>"
    index_file.puts "<table BORDER CELLSPACING=0 CELLPADDING=4>"
    @tests.each do |value|
      index_file.puts "<tr><td bgcolor=#{value.status}><font size=2 color=black><a href=\"#{value.test}.html\">#{value.test}</a></font></td></tr>"
    end
    index_file.puts "</table>"
    index_file.puts "</body>"
    index_file.puts "</html>"
    index_file.close
  end
end