import express from "express";
import { afterEach, describe, expect, it } from "vitest";
import bodyParser from "body-parser";

// run tests with: npm t
let testServer;
afterEach(() => testServer.close());

it("executes query", async () => {
  testServer = express()
    .get("/someResource/id123", (_, res) => {
      res.send("Hello, mock server!");
    })
    .listen(3000);
  const apiClient = new MyApiClient("http://localhost:3000");

  const something = await apiClient.getSomething("id123");

  expect(something).toBe("Hello, mock server!");
});

it("executes command", () =>
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

// implementation:
class MyApiClient {
  constructor(baseUrl) {
    this.baseUrl = baseUrl;
  }

  async getSomething(id) {
    const response = await fetch(`${this.baseUrl}/someResource/${id}`);
    return await response.text();
  }

  async postSomething(data) {
    await fetch(`${this.baseUrl}/someResource`, {
      method: "POST",
      body: data,
    });
  }
}
