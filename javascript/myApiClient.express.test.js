import express from "express";
import { afterEach, expect, test } from "vitest";
import bodyParser from "body-parser";
import { MyApiClient } from "myApiClient";

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

  expect(something).toBe("Hello, mock server!");
});

test("command", async (done) => {
  let postedData = "";
  testServer = express()
    .use(bodyParser.text())
    .post("/someResource", (req, _) => {
      expect(req.body).toBe("some data");
      postedData = req.body;
      done();
    })
    .listen();
  const apiClient = new MyApiClient(
    `http://localhost:${testServer.address().port}`
  );

  await apiClient.postSomething("some data");

  expect(postedData).toBe("some data");
});
