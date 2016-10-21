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

docker run --name mongo -p 27017:27017 mongo

docker run --name usermysqldb -p 3306:3306 usermysqldb

docker run --link usermysqldb:usermysqldb --name sgalogin -p 5000:5000 sgalogin

docker run --name sgahome -p 5001:5001 sgahome

docker run --link mongo:mongo --name sgagateway -p 8080:8080 sgagateway

docker run --link mongo:mongo --name sgadataingest -p 8080:8080 sgadataingest

docker run --link mongo:mongo --name sgastormdetection -p 8080:8080 sgastormdetection

docker run --link mongo:mongo --name sgastormclustering -p 8080:8080 sgastormclustering

docker run --link mongo:mongo --name sgaforecast -p 8080:8080 sgaforecast
