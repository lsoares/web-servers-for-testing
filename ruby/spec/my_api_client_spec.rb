require 'net/http'
require 'webrick'

# run tests with: bundle exec rspec
describe 'my api client tests' do
  let(:test_server) { WEBrick::HTTPServer.new(Port: 8000) }
  after(:each) do
    test_server.shutdown
    @server_thread.join if @server_thread
  end

  it 'performs a query' do
    test_server.mount_proc '/someResource' do |req, res|
      expect(req.request_method).to eq('GET')
      res.body = 'Hello, mock server!'
    end
    @server_thread = Thread.new { test_server.start }
    api_client = MyApiClient.new("http://localhost:#{test_server.config[:Port]}")

    something = api_client.get_something('id_123')

    expect(something).to eq('Hello, mock server!')
  end

  it 'performs a command' do
    posted_data = ''
    test_server.mount_proc '/someResource' do |req, _|
      expect(req.request_method).to eq('POST')
      posted_data = req.body
    end
    @server_thread = Thread.new { test_server.start }
    api_client = MyApiClient.new("http://localhost:#{test_server.config[:Port]}")

    api_client.post_something('some data')

    expect(posted_data).to eq('some data')
  end
end

# implementation:
class MyApiClient
  def initialize(base_url)
    @client = Net::HTTP.new(URI.parse(base_url).host, URI.parse(base_url).port)
  end

  def get_something(id)
    @client.get("/someResource/#{id}").body
  end

  def post_something(data)
    @client.post('/someResource', data)
  end
end
