set -e

#docker rm -f mongo || true

#docker rm -f usermysqldb || true

#docker rm -f sgalogin || true
docker stop sgahome || true
docker rm -f sgahome || true
docker rmi -f isgahome || true
#docker rm -f sgagateway || true

#docker rm -f sgadataingest || true

#docker rm -f sgastormdetection || true

#docker rm -f sgastormclustering || true

#docker rm -f sgaforecast || true

#docker rm -f sgaregistry || true

#docker rmi imongo || true

#docker rmi iusermysqldb || true

#docker stop $(docker ps -a -q) || true

#docker rm $(docker ps -a -q) || true

#docker rmi $(docker images -q) 
