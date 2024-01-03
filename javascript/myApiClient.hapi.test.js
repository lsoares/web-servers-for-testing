import Hapi from "@hapi/hapi";
import { MyApiClient } from "myApiClient";
import { afterEach, expect, test } from "vitest";

// run tests with: npm t
let testServer;
afterEach(async () => await testServer.stop());

test("query", async () => {
  testServer = Hapi.server({ host: "localhost" });
  testServer.route({
    method: "GET",
    path: "/someResource/id123",
    handler: () => "Hello, mock server!",
  });
  await testServer.start();
  const apiClient = new MyApiClient(testServer.info.uri);

  const something = await apiClient.getSomething("id123");

  expect(something).toBe("Hello, mock server!");
});

test("command", async () => {
  let postedBody = "";
  testServer = Hapi.server({ host: "localhost" });
  testServer.route({
    method: "POST",
    path: "/someResource",
    handler: (req) => {
      postedBody = req.payload;
      return "";
    },
  });
  await testServer.start();
  const apiClient = new MyApiClient(testServer.info.uri);

  await apiClient.postSomething("some data");

  expect(postedBody).toBe("some data");
});
