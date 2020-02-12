


```sh
java -DjdbcConnUrl=jdbc:mysql://localhost:3306/db_example?createDatabaseIfNotExist=true \
       -DdbUserName=root \
       -DdbPassword=my-secret-pw \
       -DdbName=db_example \
       -Dtask=update \
       -Dtag=1.0 \
       -jar build/libs/liquibase-scripts-1.0.jar
```
