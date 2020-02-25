#!/bin/bash
set -e

./scripts/wait-for.sh \
  && java -jar /var/app.jar \
     --spring.datasource.url=jdbc:mysql://db:3306/db_example \
     --spring.datasource.username="${1}" \
     --spring.datasource.password="${2}"
