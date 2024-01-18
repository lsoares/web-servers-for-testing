const http = require("http");
const { MyApiClient } = require("./myApiClient");
const url = require("url");
const { afterEach, test } = require("node:test");
const assert = require("node:assert");

let testServer;
afterEach(async () => await testServer.close());

test("query", async () => {
  testServer = http.createServer((req, res) => {
    const parsedUrl = url.parse(req.url, true);
    assert.equal(req.method, "GET");
    assert.equal(req.url, "/someResource/id123");
    res.end("Hello, mock server!");
  });
  await new Promise((resolve) => testServer.listen(0, "localhost", resolve));
  const apiClient = new MyApiClient(
    `http://localhost:${testServer.address().port}`
  );

  const something = await apiClient.getSomething("id123");

  assert.equal(something, "Hello, mock server!");
});

test("command", async () => {
  let postedBody = "";
  testServer = http.createServer((req, res) => {
    assert.equal(req.method, "POST");
    assert.equal(req.url, "/someResource");
    let body = "";
    req.on("data", (chunk) => (body += chunk));
    req.on("end", () => {
      postedBody = body;
      res.end("");
    });
  });
  await new Promise((resolve) => testServer.listen(0, "localhost", resolve));
  const apiClient = new MyApiClient(
    `http://localhost:${testServer.address().port}`
  );

  await apiClient.postSomething("some data");

  assert.equal(postedBody, "some data");
});
