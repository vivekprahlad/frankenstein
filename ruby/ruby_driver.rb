require  'socket'

class FrankensteinDriver
  def initialize(port = 5678)
    @test_name = self.class.to_s
    @port = port
    @script = ""
  end
  
  def append_to_script(script_line)
    @script += script_line + "\n"
#    @script += "Delay 3\n"
  end
  
  def activate_window(title)
    append_to_script "activate_window #{title}"
  end
  
  def click_button(button)
    append_to_script "click_button #{button}"
  end

  def click_checkbox(button, value)
    append_to_script "click_checkbox #{button} #{value}"
  end

  def click_radiobutton(button)
    append_to_script "click_radio_button #{button}"
  end

  def dialog_shown(title)
    append_to_script "dialog_shown #{title}"
  end

  def enter_text(textfield, text)
    append_to_script "enter_text #{textfield} #{text}"
  end

  def edit_table_cell(table, row, column)
    append_to_script "edit_table_cell #{table} #{row},#{column}"
  end
  
  def internal_frame_shown(title)
    append_to_script "internal_frame_shown #{title}"
  end
  
  def keystroke(modifiers, keycode)
    append_to_script "key_stroke #{modifiers},#{keycode}"
  end
  
  def navigate(path)
    append_to_script "navigate #{path}"
  end

  def select_dropdown(combo, value)
    append_to_script "select_drop_down #{combo} #{value}"
  end

  def select_list(list, value)
    append_to_script "select_list #{list} #{value}"
  end
  
  def select_tree(tree, path)
    append_to_script "select_tree #{tree} #{path}"
  end
  
  def stop_table_edit(table)
    append_to_script "stop_table_edit #{table}"
  end
  
  def switch_tab(tab, title)
    append_to_script "switch_tab #{tab} #{title}"
  end
  
  def start_test(testname)
    @script +="start_test #{testname}\n"
  end

  def finish_test
    socket = TCPSocket.new("localhost", @port)
    socket.puts @script
    socket.close
  end

 def run
    start_test @test_name
    test
    finish_test
  end
end 

class TestSwingSetAboutBox < FrankensteinDriver

  def test
    activate_window "SwingSet"
	click_button "resources/images/toolbar/JTable.gif"
    show_about_box
  end
  
  def show_about_box
    navigate "File>About"
    dialog_shown "About Swing!"
    click_button "OK"    
  end
end

test = TestSwingSetAboutBox.new
test.run
