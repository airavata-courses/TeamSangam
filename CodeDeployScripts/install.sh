echo 'Installing Sangam Weather App to Maven'

cd '/home/ec2-user/SGA_REST_DataIngest'
mvn -e clean install

echo 'Deploying the war file in Tomcat'
sudo mv ./target/SGA_REST_DataIngest.war /opt/apache-tomcat-8.0.37/webapps/

echo 'Deploying StormClustering WebService'
cd '/home/ec2-user/SGA_Rest_StormClustering'
mvn install
sudo mv ./target/SGA_Rest_StormClustering.war /opt/apache-tomcat-8.0.37/webapps/

echo 'Deploying StormForecast WebService'
cd '/home/ec2-user/SGA_REST_ForecastDecision'
mvn install
sudo mv ./target/SGA_REST_ForecastDecision.war /opt/apache-tomcat-8.0.37/webapps/

echo 'Deploying RunForecast WebService'
cd '/home/ec2-user/SGA_REST_Forecast'
mvn install
sudo mv ./target/SGA_REST_Forecast.war /opt/apache-tomcat-8.0.37/webapps/

echo 'Deploying StormDetection WebService'
cd '/home/ec2-user/SGA_Rest_StormDetection'
mvn install
sudo mv ./target/SGA_Rest_StormDetection.war /opt/apache-tomcat-8.0.37/webapps/

echo 'Deploying WeatherForecast client webservice'
cd '/home/ec2-user/SGA_REST_WeatherForecastClient'
mvn install
sudo mv ./target/SGA_REST_WeatherForecastClient.war /opt/apache-tomcat-8.0.37/webapps/

cd '/home/ec2-user/SGA_REST_login'
mv -r ./* /opt/apache-tomcat-8.0.37/webapps/ROOT/
cd '/home/ec2-user/SGA_REST_Homepage'
#cp -r ./* /opt/apache-tomcat-8.0.37/webapps/ROOT/


sudo mv index.html /opt/apache-tomcat-8.0.37/webapps/ROOT/
sudo mv ./scripts/angular.min.js /opt/apache-tomcat-8.0.37/webapps/ROOT/scripts/
sudo mv ./scripts/main.js /opt/apache-tomcat-8.0.37/webapps/ROOT/scripts/
sudo mv ./services/cors.py /opt/apache-tomcat-8.0.37/webapps/ROOT/services/
sudo mv ./services/homepage.py /opt/apache-tomcat-8.0.37/webapps/ROOT/services/
sudo mv ./services/aws_key.properties /opt/apache-tomcat-8.0.37/webapps/ROOT/services/
sudo mv ./stylesheets/bootstrap.min.css /opt/apache-tomcat-8.0.37/webapps/ROOT/stylesheets/

