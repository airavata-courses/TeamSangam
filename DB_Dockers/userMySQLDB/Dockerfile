FROM mysql:5.5
MAINTAINER Vikas Palakurthi "vikas1590@gmail.com"

ENV MYSQL_ROOT_PASSWORD=root


ADD sql_schema_create.sql /docker-entrypoint-initdb.d

EXPOSE 3306

CMD ["mysqld"]
