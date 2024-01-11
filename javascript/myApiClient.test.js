import http from "http";
import { MyApiClient } from "myApiClient";
import url from "url";
import { afterEach, expect, test } from "vitest";

let testServer;
afterEach(async () => await testServer.close());

test("query", async () => {
  testServer = http.createServer((req, res) => {
    const parsedUrl = url.parse(req.url, true);
    expect(req.method).toBe("GET");
    expect(req.url).toBe("/someResource/id123");
    res.end("Hello, mock server!");
  });
  await new Promise((resolve) => testServer.listen(0, "localhost", resolve));
  const apiClient = new MyApiClient(
    `http://localhost:${testServer.address().port}`
  );

  const something = await apiClient.getSomething("id123");

  expect(something).toBe("Hello, mock server!");
});

test("command", async () => {
  let postedBody = "";
  testServer = http.createServer((req, res) => {
    expect(req.method).toBe("POST");
    expect(req.url).toBe("/someResource");
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

  expect(postedBody).toBe("some data");
});
