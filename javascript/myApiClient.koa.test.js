import Koa from "koa";
import koaBody from "koa-body";
import { expect, test, afterEach, assert, beforeAll } from "vitest";
import { MyApiClient } from "myApiClient";

// run tests with: npm t
let testServer;
afterEach(() => testServer.close());

test("query", async () => {
  testServer = new Koa()
    .use((ctx) => {
      expect(ctx.method).toBe("GET");
      expect(ctx.path).toBe("/someResource/id123");
      ctx.body = "Hello, mock server!";
    })
    .listen();
  const apiClient = new MyApiClient(
    `http://localhost:${testServer.address().port}`
  );
  console.log(testServer.address());

  const something = await apiClient.getSomething("id123");

  expect(something).toBe("Hello, mock server!");
});

test("command", async () => {
  let postedBody = "";
  testServer = new Koa()
    .use(koaBody())
    .use((ctx) => {
      expect(ctx.method).toBe("POST");
      expect(ctx.path).toBe("/someResource");
      postedBody = ctx.request.body;
    })
    .listen();
  const apiClient = new MyApiClient(
    `http://localhost:${testServer.address().port}`
  );

  await apiClient.postSomething("some data");

  expect(postedBody).toBe("some data");
});
