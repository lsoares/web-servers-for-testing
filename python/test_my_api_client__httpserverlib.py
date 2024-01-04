import requests
from pytest_httpserver import HTTPServer


# to run tests: poetry run pytest
def test_query(httpserver: HTTPServer):
    httpserver \
        .expect_oneshot_request(uri="/someResource/id123", method="GET") \
        .respond_with_data("Hello, mock server!")
    api_client = MyApiClient(httpserver.url_for('/'))

    something = api_client.get_something("id123")

    assert something == "Hello, mock server!"
    assert len(httpserver.log) == 1


def test_command(httpserver: HTTPServer):
    httpserver \
        .expect_oneshot_request(uri="/someResource/id123", method="POST")
    api_client = MyApiClient(httpserver.url_for('/'))

    api_client.post_something("some data")

    assert len(httpserver.log) == 1


# implementation
class MyApiClient:
    def __init__(self, base_url):
        self.base_url = base_url

    def get_something(self, _id):
        response = requests.get(f"{self.base_url}/someResource/{_id}")
        return response.text

    def post_something(self, data):
        response = requests.post(f"{self.base_url}/someResource", data=data)
        return response.text
