#echo 'Creating python 3.4 virtual environment'
#cd /opt/apache-tomcat-8.0.37/webapps/ROOT/services
#sudo /usr/local/bin/virtualenv -p /usr/bin/python3.4 weatherApp
#source /opt/apache-tomcat-8.0.37/webapps/ROOT/services/weatherApp/bin/activate

#echo 'Installing required python modules..'
#sudo /opt/apache-tomcat-8.0.37/webapps/ROOT/services/weatherApp/bin/pip3.4 install -r /home/ec2-user/requirements.txt

#echo 'Starting Tomcat Server'
#sh /opt/apache-tomcat-8.0.37/bin/startup.sh
#echo 'Starting login service..'
#sudo service mysqld start
#/opt/apache-tomcat-8.0.37/webapps/ROOT/services/weatherApp/bin/python3.4 login.py 
#echo 'Started login service, starting homepage service..'
#nohup /opt/apache-tomcat-8.0.37/webapps/ROOT/services/weatherApp/bin/python3.4 homepage.py 1> homepage.out 2> homepageErr.out &
#echo 'Started homepage service..'
#nohup /opt/apache-tomcat-8.0.37/webapps/ROOT/services/weatherApp/bin/python3.4 login.py 1> login.out 2> loginErr.out &
#cd /home/ec2-user/

#echo "Building MongoDB docker image"
#cd /home/ec2-user/DB_Dockers/registry_MongoDB/
#docker build -t imongo .

#echo "Building mysqlDB docker image"
#cd /home/ec2-user/DB_Dockers/userMySQLDB/
#docker build -t iusermysqldb .

#echo "Building login docker image"
#cd /home/ec2-user/SGA_REST_login/
#docker build -t isgalogin .
ls /home/ec2-user
ls /home/ec2-user/homepage
echo "Building Homepage docker image."
cd /home/ec2-user/homepage/SGA_REST_Homepage
docker build -t isgahome .
#if [[ $(docker images isgahome) ]]; then
#	echo "Deleting the old isgahome image."
#	docker rmi -f isgahome || true
#else	
#	echo "Building new Homepage docker image"
#	docker build -t isgahome .
#fi

#echo "Build maven package for WeatherForecastClient"
#cd /home/ec2-user/SGA_REST_WeatherForecastClient
#echo "Copying the webpages into Gateway"
#sudo cp /home/ec2-user/SGA_REST_login/*.html .
#sudo cp /home/ec2-user/SGA_REST_login/requirements.txt .
#sudo mkdir -p scripts
#echo "Created scripts directory"
#sudo mkdir -p stylesheets
#echo "Created stylesheets directory"
#sudo mkdir -p templates
#echo "Created templates directory"

#sudo cp /home/ec2-user/SGA_REST_login/scripts/* ./scripts
#sudo cp /home/ec2-user/SGA_REST_login/stylesheets/* ./stylesheets
#sudo cp /home/ec2-user/SGA_REST_login/templates/* ./templates
#sudo cp /home/ec2-user/SGA_REST_Homepage/*.html .
#sudo cp /home/ec2-user/SGA_REST_Homepage/scripts/* ./scripts
#sudo cp /home/ec2-user/SGA_REST_Homepage/stylesheets/* ./stylesheets
#echo "Copied the html and javascript files to TOMCAT"

#mvn package
#echo "Building gateway docker image"
#docker build -t isgagateway .

#echo "Build maven package for Data Ingest"
#cd /home/ec2-user/SGA_REST_DataIngest/
#mvn package
#echo "Building dataingest docker image"
#docker build -t isgadataingest .

#echo "Build maven package for StormDetection"
#cd /home/ec2-user/SGA_Rest_StormDetection/
#mvn package
#echo "Building stormdetection docker image"
#docker build -t isgastormdetection .

#echo "Building maven package for StormClustering"
#cd /home/ec2-user/SGA_Rest_StormClustering/
#mvn package
#echo "Building stormclustering docker image"
#docker build -t isgastormclustering .

#echo "Building maven package for Forecast"
#cd /home/ec2-user/SGA_REST_Forecast/
#mvn package

#echo "Building maven package for ForecastDecision"
#cd /home/ec2-user/SGA_REST_ForecastDecision/
#mvn package
#mv ./target/SGA_REST_ForecastDecision.war /home/ec2-user/SGA_REST_Forecast/target/SGA_REST_ForecastDecision.war

#echo "Building docker image for Forecast Decision & Run Forecast"
#cd /home/ec2-user/SGA_REST_Forecast/
#docker build -t isgaforecast .


#echo "Building maven package for Registry"
#cd /home/ec2-user/SGA_REST_Registry/
#mvn package

#echo "Building docker image for Registry"
#docker build -t isgaregistry .
