package testingWebServerGo

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
	server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		assert.Equal(t, "/someResource/id123", r.URL.Path)
		w.Write([]byte("Hello, mock server!"))
	}))
	defer server.Close()
	myClient := NewMyApiClient(server.URL)

	response, err := myClient.GetSomething("id123")

	assert.Nil(t, err)
	body, _ := io.ReadAll(response.Body)
	assert.Equal(t, "Hello, mock server!", string(body))
}

func TestCommand(t *testing.T) {
	server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		assert.Equal(t, "/someResource", r.URL.Path)
		assert.Equal(t, "POST", r.Method)
		body, _ := io.ReadAll(r.Body)
		assert.Equal(t, "some data", string(body))
	}))
	defer server.Close()
	myClient := NewMyApiClient(server.URL)

	_, err := myClient.PostSomething("/command", "some data")

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

func (c *MyApiClient) PostSomething(endpoint string, data string) (*http.Response, error) {
	return c.client.Post(fmt.Sprintf("%s/someResource", c.baseUrl), "application/json", bytes.NewBufferString(data))
}

func NewMyApiClient(baseURL string) *MyApiClient {
	return &MyApiClient{baseUrl: baseURL, client: &http.Client{}}
}
