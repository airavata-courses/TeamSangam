set -e

#docker run -d --name mongo -p 27017:27017 imongo

docker run -d --name usermysqldb -p 3306:3306 iusermysqldb

#docker run -d --name sgahome -p 5001:5001 isgahome

#docker run -d --name sgagateway -p 8080:8080 isgagateway

#docker run -d --link mongo:mongo --name sgadataingest -p 8081:8080 isgadataingest

#docker run -d --name sgastormdetection -p 8082:8080 isgastormdetection

#docker run -d --name sgastormclustering -p 8083:8080 isgastormclustering

#docker run -d --name sgaforecast -p 8084:8080 isgaforecast

#docker run -d --link mongo:mongo --name sgaregistry -p 8085:8080 isgaregistry

docker run -d --link usermysqldb:usermysqldb --name sgalogin -p 5000:5000 isgalogin
