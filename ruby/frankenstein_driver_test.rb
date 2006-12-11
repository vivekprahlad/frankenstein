require 'test/unit'
require 'frankenstein_driver'

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