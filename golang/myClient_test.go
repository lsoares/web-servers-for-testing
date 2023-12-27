package testingWebServer

import (
	"bytes"
	"fmt"
	"io"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/stretchr/testify/assert"
)

// run tests with: `go test`
func TestQuery(t *testing.T) {
	testServer := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		assert.Equal(t, "/someResource/id123", r.URL.Path)
		w.Write([]byte("Hello, mock server!"))
	}))
	defer testServer.Close()
	apiClient := NewMyApiClient(testServer.URL)

	response, err := apiClient.GetSomething("id123")

	assert.Nil(t, err)
	body, _ := io.ReadAll(response.Body)
	assert.Equal(t, 200, response.StatusCode)
	assert.Equal(t, "Hello, mock server!", string(body))
}

func TestCommand(t *testing.T) {
	testServer := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		assert.Equal(t, "/someResource", r.URL.Path)
		assert.Equal(t, "POST", r.Method)
		body, _ := io.ReadAll(r.Body)
		assert.Equal(t, "some data", string(body))
		w.WriteHeader(201)
	}))
	defer testServer.Close()
	apiClient := NewMyApiClient(testServer.URL)

	response, err := apiClient.PostSomething("some data")

	assert.Equal(t, 201, response.StatusCode)
	assert.Nil(t, err)
}

// implementation:
type MyApiClient struct {
	baseUrl string
	client  *http.Client
}

func (c *MyApiClient) GetSomething(id string) (*http.Response, error) {
	return c.client.Get(fmt.Sprintf("%s/someResource/%s", c.baseUrl, id))
}

func (c *MyApiClient) PostSomething(data string) (*http.Response, error) {
	return c.client.Post(fmt.Sprintf("%s/someResource", c.baseUrl), "application/json", bytes.NewBufferString(data))
}

func NewMyApiClient(baseURL string) *MyApiClient {
	return &MyApiClient{baseUrl: baseURL, client: &http.Client{}}
}
