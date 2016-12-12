set -e

cd /opt

DIRECTORY=mysql
if [ -d "$DIRECTORY" ]; then
	echo "MySQL database volume exists already"
else
	echo "Creating MySQL database volume"
	mkdir mysql
	chown -R ec2-user:ec2-user mysql
fi

#docker run -d --name mongo -p 27017:27017 imongo
if [[ $(docker ps -f name=usermysqldb -q) ]]; then
	echo "MySQL docker container is already running."
else	
	if [[ $(docker ps -a -f name=usermysqldb -q) ]]; then
		docker rm -f usermysqldb || true
		echo "Starting new docker container usermysqldb."
		docker run -d --name usermysqldb -v /opt/mysql:/var/lib/mysql -p 3306:3306 iusermysqldb
	else
		echo "Starting new docker container usermysqldb."
		docker run -d --name usermysqldb -v /opt/mysql:/var/lib/mysql -p 3306:3306 iusermysqldb
	fi
fi

#docker run -d --name usermysqldb -p 3306:3306 iusermysqldb

#docker run -d --name sgahome -p 5001:5001 isgahome

#docker run -d --name sgagateway -p 8080:8080 isgagateway


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
