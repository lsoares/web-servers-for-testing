const Koa = require("koa");
const { koaBody } = require("koa-body");
const { MyApiClient } = require("./myApiClient");
const { afterEach, test } = require("node:test");
const assert = require("node:assert");

// run tests with: npm t
let testServer;
afterEach(() => testServer.close());

test("query", async () => {
  testServer = new Koa()
    .use((ctx) => {
      assert.equal(ctx.method, "GET");
      assert.equal(ctx.path, "/someResource/id123");
      ctx.body = "Hello, mock server!";
    })
    .listen();
  const apiClient = new MyApiClient(
    `http://localhost:${testServer.address().port}`
  );

  const something = await apiClient.getSomething("id123");

  assert.equal(something, "Hello, mock server!");
});

test("command", async () => {
  let postedBody = "";
  testServer = new Koa()
    .use(koaBody())
    .use((ctx) => {
      assert.equal(ctx.method, "POST");
      assert.equal(ctx.path, "/someResource");
      postedBody = ctx.request.body;
    })
    .listen();
  const apiClient = new MyApiClient(
    `http://localhost:${testServer.address().port}`
  );

  await apiClient.postSomething("some data");

  assert.equal(postedBody, "some data");
});
