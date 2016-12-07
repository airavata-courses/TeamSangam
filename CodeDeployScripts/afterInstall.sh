echo "Moving the gateway files to /home/ec2-user"
rm -rf /home/ec2-user/SGA_REST_WeatherForecastClient
mv /home/ec2-user/gateway/SGA_REST_WeatherForecastClient /home/ec2-user/

#DIRECTORY=DB_Dockers
#cd /home/ec2-user
#if [ -d "$DIRECTORY" ]; then
#        echo "Directory already exists"
#else
#        mv /home/ec2-user/gateway/DB_Dockers /home/ec2-user/
#fi

#echo "Building MySQLdb docker image if not existing"
#cd /home/ec2-user/DB_Dockers/userMySQLDB/

#if [[ $(docker images | grep -w "iusermysqldb") ]]; then
#        echo "MySQLDB Docker image already present"
#else
#        docker build -t iusermysqldb .
#fi

#echo "Building login docker image"
#cd /home/ec2-user/SGA_REST_login/
#docker build -t isgalogin .

#echo "Building Homepage docker image"
#cd /home/ec2-user/SGA_REST_Homepage/
#docker build -t isgahome .

echo "Build maven package for WeatherForecastClient"
cd /home/ec2-user/
DIRECTORY=SGA_REST_login

if [ -d "$DIRECTORY" ]; then
	echo "Copying the login webpages into Gateway"
	cd ./SGA_REST_WeatherForecastClient
	sudo cp /home/ec2-user/SGA_REST_login/*.html .
	sudo mkdir -p scripts
	echo "Created scripts directory"
	sudo mkdir -p stylesheets
	echo "Created stylesheets directory"
	sudo mkdir -p templates
	echo "Created templates directory"

	sudo cp /home/ec2-user/SGA_REST_login/scripts/* ./scripts
	sudo cp /home/ec2-user/SGA_REST_login/stylesheets/* ./stylesheets
	sudo cp /home/ec2-user/SGA_REST_login/templates/* ./templates
fi

DIRECTORY=SGA_REST_Homepage
cd /home/ec2-user/

if [ -d "$DIRECTORY" ]; then
        echo "Copying the home webpages into Gateway"
	cd ./SGA_REST_WeatherForecastClient
	sudo mkdir -p scripts
        echo "Created scripts directory"
        sudo mkdir -p stylesheets
        echo "Created stylesheets directory"
        sudo mkdir -p templates
        echo "Created templates directory"
	sudo cp /home/ec2-user/SGA_REST_Homepage/*.html .
	sudo cp /home/ec2-user/SGA_REST_Homepage/scripts/* ./scripts
	sudo cp /home/ec2-user/SGA_REST_Homepage/stylesheets/* ./stylesheets
	echo "Copied the html and javascript files to TOMCAT"
fi

cd SGA_REST_WeatherForecastClient
mvn package
echo "Building gateway docker image"
docker build -t isgagateway .
