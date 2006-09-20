require  'socket'
module FrankensteinDriver
  @@test_dir =""
  attr_accessor :test_status
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
    append_to_script "activate_window \"#{title}\""
  end
  
  def activate_internal_frame(title)
    append_to_script "activate_internal_frame \"#{title}\""
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

  def close_radiobutton(title)
    append_to_script "close_internal_frame \"#{title}\""
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
  
  def keystroke(modifiers, keycode)
    append_to_script "key_stroke \"#{modifiers},#{keycode}\""
  end
  
  def navigate(path)
    append_to_script "navigate \"#{path}\""
  end

  def select_dropdown(combo, value)
    append_to_script "select_drop_down \"#{combo}\" \"#{value}\""
  end

  def select_file(path)
    append_to_script "select_file \"#{path}\""
  end
  
  def select_list(list, value)
    append_to_script "select_list \"#{list}\" \"#{value}\""
  end

  def select_tree(tree, path)
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
    socket = TCPSocket.new("localhost", @port)
    socket.write @script
    socket.close_write
    recvthread = Thread.start do
        @test_status = socket.read
      end
    recvthread.join
    socket.close
  end

 def run
    start_test @@test_dir == "" ? @test_name : @@test_dir + "/" + @test_name
    test
    finish_test
    puts @test_status
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
  def initialize
    @test_reporter = TestReporter.new
  end

  def run(*args)
    args.each {|test| @test_reporter.report_test_result(test,test.new.run)}
    @test_reporter.report
  end
end

class TestReporter
  def initialize
    @test_hash = Hash.new
  end

  def report_test_result(test,result)
    @test_hash.store test,result
  end

  def report
    index_file = File.new(FrankensteinDriver.test_dir + "/" + "index.html", "w+")
    index_file.puts "<html>"
    index_file.puts "<head><title>Test Results</title></head>"
    index_file.puts "<body>"
    index_file.puts "<h3>Test Results</h3><br>"
    index_file.puts "<table BORDER CELLSPACING=0 CELLPADDING=4>"
    @test_hash.each do |key,value| 
      index_file.puts "<tr><td bgcolor=#CFFFCF><font size=2 color=black><a href=\"#{key}.html\">#{key}</a></font></td></tr>"
    end
    index_file.puts "</table>"
    index_file.puts "</body>"
    index_file.puts "</html>"
    index_file.close
  end    
end
