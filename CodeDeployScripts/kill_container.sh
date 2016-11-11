set -e

docker stop sgalogin || true

docker rm -f usermysqldb || true

docker rm -f sgalogin || true

docker rmi isgalogin || true

#docker rmi iusermysqldb || true

#docker stop $(docker ps -a -q) || true

#docker rm $(docker ps -a -q) || true

#docker rmi $(docker images -q) 
