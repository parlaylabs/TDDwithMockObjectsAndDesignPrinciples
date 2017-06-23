import unittest
from mock import MagicMock, patch

from unicode_to_html_converter import UnicodeFileToHtmlTextConverter


class UnicodeFileToHtmlTextConverterTest(unittest.TestCase):

    FILE_NAME = "unicode_file"

    @staticmethod
    def mock_open(file_contents):
        m = MagicMock(name='open', spec=open)
        m.return_value = iter(file_contents)
        return m

    @staticmethod
    def mock_open_exception(exception_class):
        m = MagicMock(name='open', spec=open)
        m.side_effect = exception_class()
        return m

    def convert_with_mocked_open(self, mocked_open):
        with patch('builtins.open', mocked_open):
            return self.converter.convert_to_html()

    def validate_convert(self, file_contents, expected):
        mocked_open = self.mock_open(file_contents)
        converted = self.convert_with_mocked_open(mocked_open)
        self.assertEqual(converted, expected)
        mocked_open.assert_called_once_with(self.FILE_NAME, 'r')

    def setUp(self):
        self.converter = UnicodeFileToHtmlTextConverter(self.FILE_NAME)

    def test_file_not_found(self):
        m = self.mock_open_exception(FileNotFoundError)
        with self.assertRaises(FileNotFoundError) as context:
            self.convert_with_mocked_open(m)
        m.assert_called_once_with(self.FILE_NAME, 'r')
        self.assertTrue(isinstance(context.exception, FileNotFoundError))

    def test_append_newline(self):
        line = 'This is a line.'
        expected = line + '<br />'
        self.validate_convert([line], expected)

    def test_strip_whitespace(self):
        line = 'This is a line with ending whitespace.              '
        expected = 'This is a line with ending whitespace.<br />'
        self.validate_convert([line], expected)

    def test_escape_special_characters(self):
        line = '\'<a href="test_link&query-parameters">'
        expected = '&#x27;&lt;a href=&quot;test_link&amp;query-parameters&quot;&gt;<br />'
        self.validate_convert([line], expected)

    def test_multiline(self):
        line = ['This is a line with ending whitespace.              ',
                '<a href="test_link&query-parameters">']
        expected = 'This is a line with ending whitespace.<br />' + \
                   '&lt;a href=&quot;test_link&amp;query-parameters&quot;&gt;<br />'
        self.validate_convert(line, expected)


if __name__ == "__main__":
    unittest.main()
