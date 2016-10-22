#echo 'Installing Sangam Weather App to Maven'

#cd '/home/ec2-user/SGA_REST_DataIngest'
#mvn -e clean install

#echo 'Deploying the war file in Tomcat'
#sudo mv ./target/SGA_REST_DataIngest.war /opt/apache-tomcat-8.0.37/webapps/

#echo 'Deploying StormClustering WebService'
#cd '/home/ec2-user/SGA_Rest_StormClustering'
#mvn install
#sudo mv ./target/SGA_Rest_StormClustering.war /opt/apache-tomcat-8.0.37/webapps/

#echo 'Deploying StormForecast WebService'
#cd '/home/ec2-user/SGA_REST_ForecastDecision'
#mvn install
#sudo mv ./target/SGA_REST_ForecastDecision.war /opt/apache-tomcat-8.0.37/webapps/

#echo 'Deploying RunForecast WebService'
#cd '/home/ec2-user/SGA_REST_Forecast'
#mvn install
#sudo mv ./target/SGA_REST_Forecast.war /opt/apache-tomcat-8.0.37/webapps/

#echo 'Deploying StormDetection WebService'
#cd '/home/ec2-user/SGA_Rest_StormDetection'
#mvn install
#sudo mv ./target/SGA_Rest_StormDetection.war /opt/apache-tomcat-8.0.37/webapps/

#echo 'Deploying WeatherForecast client webservice'
#cd '/home/ec2-user/SGA_REST_WeatherForecastClient'
#mvn install
#sudo mv ./target/SGA_REST_WeatherForecastClient.war /opt/apache-tomcat-8.0.37/webapps/

#cd '/home/ec2-user/SGA_REST_login'
#mv ./* /opt/apache-tomcat-8.0.37/webapps/ROOT/
#cd '/home/ec2-user/SGA_REST_Homepage'
#cp -r ./* /opt/apache-tomcat-8.0.37/webapps/ROOT/

#sudo mv index.html /opt/apache-tomcat-8.0.37/webapps/ROOT/

#sudo mv ./scripts/* /opt/apache-tomcat-8.0.37/webapps/ROOT/scripts/
#sudo mv ./services/* /opt/apache-tomcat-8.0.37/webapps/ROOT/services/
#sudo mv ./stylesheets/* /opt/apache-tomcat-8.0.37/webapps/ROOT/stylesheets/

set -e

docker run -d --name mongo -p 27017:27017 imongo

docker run -d --name usermysqldb -p 3306:3306 iusermysqldb

docker run -d --name sgahome -p 5001:5001 isgahome

docker run -d --name sgagateway -p 8080:8080 isgagateway

docker run -d --link mongo:mongo --name sgadataingest -p 8081:8081 isgadataingest

docker run -d --name sgastormdetection -p 8082:8082 isgastormdetection

docker run -d --name sgastormclustering -p 8083:8083 isgastormclustering

docker run -d  --name sgaforecast -p 8084:8084 isgaforecast

docker run -d --link mongo:mongo --name sgaregistry -p 8085:8085 isgaregistry

docker run -d --link usermysqldb:usermysqldb --name sgalogin -p 5000:5000 isgalogin
