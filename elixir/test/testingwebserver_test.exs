# run tests with: mix test
defmodule MyApiClientTest do
  use ExUnit.Case

  test "a query" do
    defmodule TestServer1 do
      use Francis

      get("someResource/:id", fn
        %{path_params: %{"id" => "id123"}} -> "Hello, mock server!"
      end)
    end

    start_supervised!({Bandit, plug: TestServer1, port: 8080})
    apiClient = MyApiClient.build("http://localhost:8080")

    something = MyApiClient.get_something(apiClient, "id123")

    assert something == "Hello, mock server!"
  end

  test "a command" do
    postedData = ""

    defmodule TestServer2 do
      use Francis

      post("someResource", fn
        %{"body" => data} ->
          postedData = data
          ""
      end)
    end

    start_supervised!({Bandit, plug: TestServer2, port: 8080})
    api_client = MyApiClient.build("http://localhost:8080")

    MyApiClient.post_something(api_client, "some data")

    assert postedData == "some data"
  end
end

# implementation:
defmodule MyApiClient do
  defstruct [:base_url]

  def build(base_url), do: %__MODULE__{base_url: base_url}

  def get_something(%MyApiClient{base_url: base_url}, id) do
    case Req.get!("#{base_url}/someResource/#{id}") do
      %Req.Response{status: 200, body: body} -> body
      %Req.Response{status: 404} -> :not_found
    end
  end

  def post_something(%MyApiClient{base_url: base_url}, data) do
    Req.post!("#{base_url}/someResource", body: data)
    :ok
  end
end
