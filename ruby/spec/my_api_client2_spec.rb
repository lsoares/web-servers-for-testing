# require 'sinatra/base'
# require 'net/http'

# # run tests with: bundle exec rspec
# describe 'my api client tests' do
#   before(:each) do
#     class TestServerApp < Sinatra::Base
#       get '/someResource/:id' do
#         expect(request.request_method).to eq('GET')
#         'Hello, mock server!'
#       end

#       post '/someResource' do
#         expect(request.request_method).to eq('POST')
#         request.body.read
#       end
#     end
#     thread = Thread.new { TestServerApp.run! }
#     # @server_thread = Thread.new { Rack::Handler::WEBrick.run(app, Port: 4567) }
#     # sleep(1) # wait for the server to start
#   end

#   after(:each) do
#     Rack::Handler::WEBrick.shutdown
#     @server_thread.join if @server_thread
#   end

#   it 'performs a query' do
#     api_client = MyApiClient.new("http://localhost:4567")
#     something = api_client.get_something('id_123')
#     expect(something).to eq('Hello, mock server!')
#   end

#   # it 'performs a command' do
#   #   api_client = MyApiClient.new("http://localhost:4567")
#   #   posted_data = api_client.post_something('some data')
#   #   expect(posted_data).to eq('some data')
#   # end
# end

# # implementation:
# class MyApiClient
#   def initialize(base_url)
#     @base_url = base_url
#   end

#   def get_something(id)
#     uri = URI.parse("#{@base_url}/someResource/#{id}")
#     Net::HTTP.get(uri)
#   end

#   def post_something(data)
#     uri = URI.parse("#{@base_url}/someResource")
#     Net::HTTP.post_form(uri, 'data' => data).body
#   end
# end
