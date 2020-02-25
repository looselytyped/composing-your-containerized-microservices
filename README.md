# Composing your containerized microservices

This is the repository for my "Composing your containerized microservices" talk.

## Set up

- Clone this repository
- Install [`docker-compose`](https://docs.docker.com/compose/install/)

## Usage

- `cd` to the location where you cloned this repository
- `cd compose`
- `docker-compose up --build`

```sh
curl http://localhost:9000/app/api/users
curl http://localhost:9000/app/api/users -H 'Content-type:application/json' -d '{"name": "Raju Gandhi", "email": "raju@gandhi.com"}'

```

## Useful Aliases

```sh
alias d="docker-compose down -v" \
  && alias u="docker-compose up" \
  && alias up="docker-compose up --build"
```
