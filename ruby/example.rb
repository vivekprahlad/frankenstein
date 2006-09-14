require 'frankenstein_driver'

module SwingSetTests
    def show_print_dialog (*args)
	activate_window "#{args[0]}"
	click_button "resources/images/toolbar/JTable.gif"
	click_button "Printing.Print"
	dialog_shown "Print"
	click_button "Cancel"
	dialog_shown "Printing Result"
	click_button "OptionPane.button"
    end
end
 	

class TestSwingSetAboutBox 
include SwingSetTests
include FrankensteinDriver
  def test
    show_print_dialog "SwingSet", "Test"
  end  
end

class TestSwingSetTwo 
include FrankensteinDriver

  def test
	activate_window "SwingSet"
	click_button "resources/images/toolbar/JTable.gif"
	click_button "Printing.Print"
	dialog_shown "Print"
	click_button "Cancel"
	dialog_shown "Printing Result"
	click_button "OptionPane.button"
  end
end

class TestRunner
  def run(*args)
        args.each{|test| test.new.run}
  end
end


test_suite = TestRunner.new
test_suite.run(
TestSwingSetAboutBox,
TestSwingSetTwo
)
