set -e

docker run -d --name mongo -p 27017:27017 imongo

docker run -d --link mongo:mongo --name sgadataingest -p 8081:8080 isgadataingest

