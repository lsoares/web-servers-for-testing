from http.server import BaseHTTPRequestHandler, HTTPServer
from threading import Thread

import pytest as pytest

from test_my_api_client__httpserverlib import MyApiClient


# to run tests: poetry run pytest
def test_performs_query(server: HTTPServer):
    class MyHandler(BaseHTTPRequestHandler):
        def do_GET(self):
            assert self.path == '/someResource/id123'
            self.send_response(200)
            self.end_headers()
            self.wfile.write(b'Hello, mock server!')
    server.RequestHandlerClass = MyHandler
    api_client = MyApiClient('http://localhost:8000')

    something = api_client.get_something('id123')

    assert something == 'Hello, mock server!'


def test_performs_command(server: HTTPServer):
    posted_data = ''
    class PerformsCommandHandler(BaseHTTPRequestHandler):
        def do_POST(self):
            assert self.path == '/someResource'
            nonlocal posted_data
            posted_data = self.rfile.read(int(self.headers['Content-Length'])).decode('utf-8')
            self.send_response(200)
            self.end_headers()
            self.wfile.write(b'')
    server.RequestHandlerClass = PerformsCommandHandler
    api_client = MyApiClient('http://localhost:8000')

    api_client.post_something('some data')

    assert posted_data == 'some data'


@pytest.fixture
def server():
    server = HTTPServer(('localhost', 8000), None)
    server_thread = Thread(target=server.serve_forever)
    server_thread.start()

    yield server

    server.shutdown()
    server.server_close()
    server_thread.join()
