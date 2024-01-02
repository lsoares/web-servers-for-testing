require 'sinatra'
require 'net/http'

# run tests with: bundle exec rspec
describe 'my api client tests' do
  it 'performs a query' do
    class TestServer < Sinatra::Base
      get '/someResource/id123' do
        'Hello, World!'
      end
    end
    @server_thread = Thread.new { run TestServer }
    # TestServer.run!
    api_client = MyApiClient.new('http://127.0.0.1:4567')

    something = api_client.get_something("id123")

    expect(something).to be "Hello, World!"
    TestServer.quit!
  end
end

# implementation:
class MyApiClient
  attr_reader :base_url

  def initialize(base_url)
    @base_url = base_url
    @client = Net::HTTP.new(URI.parse(base_url).host, URI.parse(base_url).port)
  end

  def get_something(id)
    response = @client.get("/someResource/#{id}")
    response.body
  end

  def post_something(data)
    response = @client.post('/someResource', data.to_json, headers)
    response.code
  end
end
