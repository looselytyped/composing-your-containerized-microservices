# Demo-ing how `.env`, ENV and overrides work

- `docker-compose` by default picks up the `.env` file in a folder and makes it available to the compose environment.
  This means that you can use those variables within the `docker-compose.yml` file.
  This folder has a `.env` file so you can demonstrate this like so:
  ```sh
  docker-compose up
  ```
- To override environment variables we have two options — Supply them at the CLI or use another `.env` file, so like:
  ```sh
  NAME=NAME_FROM_CLI docker-compose up
  # or
  docker-compose --env-file .env.prod up
  ```
- Finally, we can override the entire `environment` block in the `docker-compose.yml` file like so
  ```sh
  docker-compose --env-file .env.prod -f docker-compose.yml -f docker-compose.prod.yml up
  ```
