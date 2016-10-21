set -e

docker rm -f mongo || true

docker rm -f usermysqldb || true

docker rm -f sgalogin || true

docker rm -f sgahome || true

docker rm -f sgagateway || true

docker rm -f sgadataingest || true

docker rm -f sgastormdetection || true

docker rm -f sgastormclustering || true

docker rm -f sgaforecast || true

docker rm -f sgaregistry || true
