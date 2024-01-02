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

// run tests with: go test
func TestQuery(t *testing.T) {
	testServer := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		assert.Equal(t, "/someResource/id123", r.URL.Path)
		w.Write([]byte("Hello, mock server!"))
	}))
	defer testServer.Close()
	apiClient := NewMyApiClient(testServer.URL)

	something := apiClient.GetSomething("id123")

	assert.Equal(t, "Hello, mock server!", something)
}

func TestCommand(t *testing.T) {
	postedBody := ""
	testServer := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		assert.Equal(t, "/someResource", r.URL.Path)
		assert.Equal(t, "POST", r.Method)
		body, _ := io.ReadAll(r.Body)
		postedBody = string(body)
	}))
	defer testServer.Close()
	apiClient := NewMyApiClient(testServer.URL)

	apiClient.PostSomething("some data")

	assert.Equal(t, "some data", postedBody)
}

// implementation:
type MyApiClient struct {
	baseUrl string
	client  *http.Client
}

func (c *MyApiClient) GetSomething(id string) string {
	response, _ := c.client.Get(fmt.Sprintf("%s/someResource/%s", c.baseUrl, id))
	body, _ := io.ReadAll(response.Body)
	return string(body)
}

func (c *MyApiClient) PostSomething(data string) {
	c.client.Post(fmt.Sprintf("%s/someResource", c.baseUrl), "application/json", bytes.NewBufferString(data))
}

func NewMyApiClient(baseURL string) *MyApiClient {
	return &MyApiClient{baseUrl: baseURL, client: &http.Client{}}
}
