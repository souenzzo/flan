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
{:deps {br.com.souenzzo/flang {:git/url "https://github.com/souenzzo/flan"
                               :sha     "29720fb38329609c0f93a5b1fdb2fd2d43a112ee"}}} 
```

- Run it!

```
clj -m br.com.souenzzo.flan --dev my-first-webapp
```

- It will start at [localhost:8080](http://localhost:8080). Connect your browser into it and flow the instructions!