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
    .listen(3000);
  const apiClient = new MyApiClient("http://localhost:3000");

  const something = await apiClient.getSomething("id123");

  expect(something).toBe("Hello, mock server!");
});

test("command", () =>
  new Promise(async (done) => {
    testServer = express()
      .use(bodyParser.text())
      .post("/someResource", async (req, _) => {
        expect(req.body).toBe("some data");
        done();
      })
      .listen(3000);
    const apiClient = new MyApiClient("http://localhost:3000");

    await apiClient.postSomething("some data");
  }));
