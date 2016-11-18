From ubuntu:latest

MAINTAINER ramarvab@gmail.com

RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv EA312927 && echo "deb http://repo.mongodb.org/apt/ubuntu trusty/mongodb-org/3.2 multiverse" | tee /etc/apt/sources.list.d/mongodb-org-3.2.list
RUN apt-get update; apt-get install -y mongodb-org
RUN mkdir -p /data/db

VOLUME ["/data/db"]

EXPOSE 27017

ENTRYPOINT ["/usr/bin/mongod"]
CMD ["--port", "27017", "--dbpath", "/data/db", "--smallfiles"]
