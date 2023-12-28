from dataclasses import dataclass

import pytest as pytest
from pytest_httpserver import HTTPServer
from aiohttp import ClientSession


# to run tests: poetry run pytest
@pytest.mark.asyncio
async def test_query(httpserver: HTTPServer):
    httpserver \
        .expect_oneshot_request(uri="/someResource/id123", method="GET") \
        .respond_with_data("Hello, mock server!")
    api_client = MyApiClient(httpserver.url_for('/'))

    something = await api_client.get_something("id123")

    assert something == "Hello, mock server!"
    assert len(httpserver.log) == 1


@pytest.mark.asyncio
async def test_command(httpserver: HTTPServer):
    httpserver \
        .expect_oneshot_request(uri="/someResource/id123", method="POST")
    api_client = MyApiClient(httpserver.url_for('/'))

    await api_client.post_something("some data")

    assert len(httpserver.log) == 1


# implementation
@dataclass
class MyApiClient:
    base_url: str

    async def get_something(self, id: str) -> str:
        async with ClientSession() as session:
            async with session.get(url=f"{self.base_url}/someResource/{id}") as response:
                return await response.text()

    async def post_something(self, data: str) -> None:
        async with ClientSession() as session:
            async with session.post(
                url=f"{self.base_url}/someResource",
                headers={"Content-Type": "application/json"},
                data=data,
            ):
                ...
