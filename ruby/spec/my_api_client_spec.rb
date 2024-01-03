require 'net/http'
require 'webrick'

describe 'my api client tests' do
  let(:test_server) { WEBrick::HTTPServer.new(Port: 8000) }
  after(:each) do
    test_server.shutdown
    @server_thread.join if @server_thread
  end

  it 'performs a query' do
    test_server.mount_proc '/someResource' do |req, res|
      res.body = 'Hello, mock server!'
    end
    @server_thread = Thread.new { test_server.start }
    api_client = MyApiClient.new("http://localhost:#{test_server.config[:Port]}")

    something = api_client.get_something('id_123')

    expect(something).to eq('Hello, mock server!')
  end

  it 'performs a command' do
    posted_data = ''
    test_server.mount_proc '/someResource' do |req, res|
      posted_data = req.body
    end
    @server_thread = Thread.new { test_server.start }
    api_client = MyApiClient.new("http://localhost:#{test_server.config[:Port]}")

    api_client.post_something('some data')

    expect(posted_data).to eq('some data')
  end
end

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
    @client.post('/someResource', data)
  end
end
