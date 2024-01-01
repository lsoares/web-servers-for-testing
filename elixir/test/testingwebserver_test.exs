# run tests with: mix test
defmodule MyApiClientTest do
  use ExUnit.Case

  test "a query" do
    defmodule TestServer do
      use Francis

      get("someResource/:id", fn
        %{path_params: %{"id" => "id123"}} -> "Hello, mock server!"
      end)
    end

    start_supervised!({Bandit, plug: TestServer, port: 8080})
    apiClient = MyApiClient.build("http://localhost:8080")


    something = MyApiClient.get_something(apiClient, "id123")

    assert something == "Hello, mock server!"
  end

  test "a command" do
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
end
