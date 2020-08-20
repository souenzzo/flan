# Flan

> A easy HTTP server for learning clojure


## Get started

- Install [clojure](https://clojure.org/guides/getting_started#_clojure_installer_and_cli_tools)

- Create a empty directory and enter on it.

```bash 
mkdir my-first-webapp
cd my-first-webapp
```

- Create a `deps.edn` file

```bash 
touch deps.edn
```

- Copy and paste this into `deps.edn`

```clojure
{:deps    {br.com.souenzzo/flang {:git/url "https://github.com/souenzzo/flan"
                                  :sha     "848aa1c6c4886449c91e58fd7206c5204aa33a8e"}}
 :aliases {:dev {:main-opts ["-m" "br.com.souenzo.flan" "--dev" "my-first-webapp"]}}} 
```

- Run it!

```
clj -A:dev
```

- It will start at [localhost:8080](http://localhost:8080). Connect your browser into it and flow the instructions!