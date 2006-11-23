require  'socket'
require 'test/unit'

class Regexp
  def to_s
    "regex:" + source
  end
end

module FrankensteinDriver
  @@test_dir =""
  attr_accessor :test_status

  def init(host,port)
    @test_name = self.class.to_s
    @host = host
    @port = port
    @script = ""
  end
  
  def append_to_script(script_line)
    @script += script_line.gsub("\n", "&#xA;") + "\n"
  end
  
  def activate_window(title)
    append_to_script "activate_window \"#{title}\""
  end
  
  def activate_internal_frame(title)
    append_to_script "activate_internal_frame \"#{title}\""
  end

  def assert_checkbox_selected(checkbox_name)
    assert_togglebutton_selected checkbox_name
  end

  def assert_checkbox_not_selected(checkbox_name)
    assert_togglebutton_not_selected checkbox_name
  end

  def assert_radiobutton_selected(radiobutton_name)
    assert_togglebutton_selected radiobutton_name
  end

  def assert_radiobutton_not_selected(radiobutton_name)
    assert_togglebutton_not_selected radiobutton_name
  end

  def assert_togglebutton_selected(togglebutton_name)
    assert_true togglebutton_name,"selected"
  end

  def assert_togglebutton_not_selected(togglebutton_name)
    assert_false togglebutton_name,"selected"
  end

  def assert_enabled(component_name)
    assert_true component_name,"enabled"
  end

  def assert_disabled(component_name)
    assert_false component_name,"enabled"
  end

  def assert_number_of_table_rows(table_name, number_of_rows)
    assert table_name,"rowCount",number_of_rows
  end

  def assert_table_cell(table_name, row, column, value)
    assert table_name, "getValueAt(#{row},#{column})", value
  end

  def assert_table_row(table, row, *cell_values)
    cell_values.each_index {|index| assert_table_cell(table, row, index, cell_values[index])}
  end

  def assert(component_name,ognl_expression,expected_value)
    append_to_script "assert \"#{component_name}\" \"#{ognl_expression}:#{expected_value}\""
  end

  def assert_true(component_name,ognl_expression)
    assert component_name,ognl_expression,"true"
  end

  def assert_false(component_name,ognl_expression)
    assert component_name,ognl_expression,"false"
  end

  def assert_text(text_component, expected_text)
    assert text_component,"text",expected_text
  end

  def cancel_table_edit(table)
    append_to_script "cancel_table_edit \"#{table}\""
  end

  def click_button(button)
    append_to_script "click_button \"#{button}\""
  end

  def click_checkbox(button, value)
    append_to_script "click_checkbox \"#{button}\" \"#{value}\""
  end

  def click_radiobutton(button)
    append_to_script "click_radio_button \"#{button}\""
  end

  def double_click_list(list,item_index)
    append_to_script "double_click_list \"#{list}\" \"#{item_index}\""
  end

  def close_internal_frame(title)
    append_to_script "close_internal_frame \"#{title}\""
  end

  def close_all_dialogs
    append_to_script "close_all_dialogs"
  end

  def delay(duration_in_milliseconds)
    append_to_script "delay \"#{duration_in_milliseconds}\""
  end

  def dialog_closed(title)
    append_to_script "dialog_closed \"#{title}\""
  end

  def dialog_shown(title)
    append_to_script "dialog_shown \"#{title}\""
  end

  def enter_text(textfield, text)
    append_to_script "enter_text \"#{textfield}\" \"#{text}\""
  end

  def edit_table_cell(table, coords)
    append_to_script "edit_table_cell \"#{table}\" \"#{coords}\""
  end
  
  def internal_frame_shown(title)
    append_to_script "internal_frame_shown \"#{title}\""
  end
  
  def key_stroke(modifiers, keycode)
    append_to_script "key_stroke \"#{modifiers},#{keycode}\""
  end
  
  def navigate(path)
    append_to_script "navigate \"#{path}\""
  end

  def right_click_tree(tree, path)
    append_to_script "right_click_tree \"#{tree}\" \"#{path}\""
  end

  def select_drop_down(combo, value)
    append_to_script "select_drop_down \"#{combo}\" \"#{value}\""
  end

  def select_file(path)
    append_to_script "select_file \"#{path}\""
  end

  def select_files(paths)
    append_to_script "select_files \"#{paths}\""
  end

  def select_list(list, value)
    append_to_script "select_list \"#{list}\" \"#{value}\""
  end

  def select_table_row(table, rows)
    append_to_script "select_table_row \"#{table}\" \"#{rows}\""
  end

  def select_tree(tree, *path_elements)
    path = path_elements.join(">")
    append_to_script "select_tree \"#{tree}\" \"#{path}\""
  end
  
  def stop_table_edit(table)
    append_to_script "stop_table_edit \"#{table}\""
  end
  
  def switch_tab(tab, title)
    append_to_script "switch_tab \"#{tab}\" \"#{title}\""
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

 def run_frankenstein_test(host,port)
    init host,port
    start_test @@test_dir == "" ? @test_name : @@test_dir + "/" + @test_name
    test
    finish_test
    puts @test_name + " : " + (@test_status == "F" ? "Failed" : "Passed")
    @test_status
  end

  def FrankensteinDriver.test_dir=(dir)
    @@test_dir = dir
  end

  def FrankensteinDriver.test_dir
    @@test_dir
  end

end

class TestRunner
  def initialize(host="localhost",port=5678)
    @test_reporter = TestReporter.new
    @host = host
    @port = port
  end

  def run(*args)
    args.each {|test| @test_reporter.report_test_result(test,test.new.run_frankenstein_test(host,port))}
    @test_reporter.report
  end
end

class TestResult
  attr_accessor :test
  def initialize(test,status)
    @test,@status = test,status
  end

  def status
    @status == "P" ? "#CFFFCF" : "#FFCFCF"
  end
end

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

class RegexTest < Test::Unit::TestCase
  def test_prefixes_regular_expressions_with_regex
    regex = /test.*/
    assert_equal("regex:test.*", "#{regex}")
  end 
end

class FrankensteinDriverTest < Test::Unit::TestCase
  include FrankensteinDriver

  def setup
    init("localhost",5678)
  end

  def test_escapes_newlines
    append_to_script "foo\n"
    assert_equal("foo&#xA;\n",@script)
  end

  def test_creates_tree_path_with_regex
    select_tree("tree",/.*/,"two","three")
    assert_equal("select_tree \"tree\" \"regex:.*>two>three\"\n", @script)
  end
end
