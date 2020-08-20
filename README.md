# Flan

> A easy HTTP server for learning clojure


## Get started

- Install [clojure](https://clojure.org/guides/getting_started#_clojure_installer_and_cli_tools)

- Create a empty directory and enter on it.

```bash 
mkdir my-first-webapp
cd my-first-webapp
```

- Create a `deps.edn` file and a `src` dir for sources

```bash 
touch deps.edn
mkdir src
```

- Copy and paste this into `deps.edn`

```clojure
{:deps    {br.com.souenzzo/flang {:git/url "https://github.com/souenzzo/flan"
                                  :sha     "36e8f3ebbf0e277f331b37a88ac8f5be18cbc70b"}}
 :aliases {:dev {:main-opts ["-m" "br.com.souenzo.flan" "--dev" "my-first-webapp"]}}} 
```

- Run it!

```
clj -A:dev
```

- It will start at [localhost:8080](http://localhost:8080). Connect your browser into it and flow the instructions!