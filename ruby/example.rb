require 'frankenstein_driver'

module SwingSetTests
  def show_print_dialog (window_name)
    activate_window "#{window_name}"
    click_button "JTable"
    click_button "Printing.Print"
    dialog_shown "Print"
    click_button "Cancel"
    dialog_shown "Printing Result"
    click_button "OptionPane.button"
  end
end
 	
#Example of a test created after recording a series of steps
class TestPrintDialog
  include FrankensteinDriver

  #All test cases need to have a 'test' function
  def test
    activate_window "SwingSet"
    #intentional typo for making the test fail
    click_button "Table"
    click_button "JTable"
    click_button "Printing.Print"
    dialog_shown "Print"
    click_button "Cancel"
    dialog_shown "Printing Result"
    click_button "OptionPane.button"
  end
end

#Example of a test that includes functions defined in a module called 'SwingSetTests'
#This technique may be used to pull out common workflow steps into logical functions
class TestPrintDialogWithModule
  include SwingSetTests
  include FrankensteinDriver

  def test
    show_print_dialog "SwingSet"
  end
end

#Specify the test directory
FrankensteinDriver.test_dir="/home/vivek/reports"
#Use the test runner to run both the test cases.
TestRunner.new.run TestPrintDialogWithModule, TestPrintDialog
