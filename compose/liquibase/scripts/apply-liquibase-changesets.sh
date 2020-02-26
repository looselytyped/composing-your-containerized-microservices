#!/bin/bash

# Exit on error. Append "|| true" if you expect an error.
set -o errexit
# Exit on error inside any functions or subshells.
set -o errtrace
# Do not allow use of undefined vars. Use ${VAR:-} to use an undefined VAR
set -o nounset
# Catch the error in case mysqldump fails (but gzip succeeds) in `mysqldump |gzip`
set -o pipefail

/scripts/wait-for-it.sh \
    -t 0 \
    db:3306 \
    -- java -DjdbcConnUrl=jdbc:mysql://db:3306/db_example?createDatabaseIfNotExist=true \
            -DdbName=db_example \
            -Dtask=update \
            -Dtag="${TAG}" \
            -DdbUserName="${1}" \
            -DdbPassword="${2}" \
            -jar /app/liquibase-scripts.jar
