const bodyParser = require("body-parser");
const express = require("express");
const { MyApiClient } = require("./myApiClient");
const { afterEach, test } = require("node:test");
const assert = require("node:assert");

// run tests with: npm t
let testServer;
afterEach(() => testServer.close());

test("query", async () => {
  testServer = express()
    .get("/someResource/id123", (_, res) => {
      res.send("Hello, mock server!");
    })
    .listen();
  const apiClient = new MyApiClient(
    `http://localhost:${testServer.address().port}`
  );

  const something = await apiClient.getSomething("id123");

  assert.equal(something, "Hello, mock server!");
});

test("command", async () => {
  let postedData = "";
  testServer = express()
    .use(bodyParser.text())
    .post("/someResource", (req, res) => {
      postedData = req.body;
      res.send();
    })
    .listen();
  const apiClient = new MyApiClient(
    `http://localhost:${testServer.address().port}`
  );

  await apiClient.postSomething("some data");

  assert.equal(postedData, "some data");
});
