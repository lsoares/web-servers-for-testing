defmodule Testingwebserver.MixProject do
  use Mix.Project

  def project do
    [
      app: :testingwebserver,
      version: "0.1.0",
      elixir: "~> 1.16",
      start_permanent: Mix.env() == :prod,
      deps: deps()
    ]
  end

  def application do
    [
      extra_applications: [:logger]
    ]
  end

  defp deps do
    [
      {:francis, "~> 0.1.4"},
      {:req, "~> 0.4.8"}
    ]
  end
end
