set -e

docker rm -f sga || true

docker rm -f sgalogin || true

docker rm -f sgagateway || true

docker rm -f sgadataingest || true

docker rm -f sgastormdetection || true

docker rm -f sgaforecastdecision || true

docker rm -f sgarunforecast || true
