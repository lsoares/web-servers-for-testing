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

module.exports = { MyApiClient };
