import html as html_converter

class UnicodeFileToHtmlTextConverter(object):

    def __init__(self, full_filename_with_path):
        self.full_filename_with_path = full_filename_with_path

    def convert_to_html(self):
        f = open(self.full_filename_with_path, "r")
        return self.convert_to_html_from_iterator(f)

    def convert_to_html_from_iterator(self, iterator):
        html = ""
        for line in iterator:
            line = line.rstrip()
            html += html_converter.escape(line, quote=True)
            html += "<br />"

        return html
