set -e

#docker run -d --name mongo -p 27017:27017 imongo
if [[ $(docker ps -f name=usermysqldb -q) ]]; then
	echo "MySQL docker container is already running."
else	
	echo "Starting new docker container usermysqldb."
	docker run -d --name usermysqldb -p 3306:3306 iusermysqldb
fi

#docker run -d --name usermysqldb -p 3306:3306 iusermysqldb

#docker run -d --name sgahome -p 5001:5001 isgahome

#docker run -d --name sgagateway -p 8080:8080 isgagateway

#docker run -d --link mongo:mongo --name sgadataingest -p 8081:8080 isgadataingest

#docker run -d --name sgastormdetection -p 8082:8080 isgastormdetection

#docker run -d --name sgastormclustering -p 8083:8080 isgastormclustering

#docker run -d --name sgaforecast -p 8084:8080 isgaforecast

#docker run -d --link mongo:mongo --name sgaregistry -p 8085:8080 isgaregistry

if [[ $(docker ps -a -f name=sgalogin -q) ]]; then
	echo "Removing the existing sgalogin docker container."
	docker stop sgalogin || true
	docker rm -f sgalogin || true
fi	
echo "Starting new docker container sgalogin."
docker run -d --link usermysqldb:usermysqldb --name sgalogin -p 5000:5000 isgalogin

if [[ $(docker ps -a -f name=sgagateway -q) ]]; then
	echo "Adding the login files to sgagateway docker container"
	docker cp /home/ec2-user/SGA_REST_login/scripts sgagateway:/usr/local/tomcat/webapps/ROOT/
	docker cp /home/ec2-user/SGA_REST_login/stylesheets sgagateway:/usr/local/tomcat/webapps/ROOT/
	docker cp /home/ec2-user/SGA_REST_login/templates sgagateway:/usr/local/tomcat/webapps/ROOT/
	docker cp /home/ec2-user/SGA_REST_login/login.html sgagateway:/usr/local/tomcat/webapps/ROOT/
	docker cp /home/ec2-user/SGA_REST_login/signup.html sgagateway:/usr/local/tomcat/webapps/ROOT/
	docker cp /home/ec2-user/SGA_REST_login/loginError.html sgagateway:/usr/local/tomcat/webapps/ROOT/
	docker cp /home/ec2-user/SGA_REST_login/index.html sgagateway:/usr/local/tomcat/webapps/ROOT/
fi


echo "Removing dangling nodes, if any"
docker rmi $(docker images -f "dangling=true" -q) || true
