echo 'Installing Sangam Weather App to Maven'

cd '/home/ec2-user/SGA_REST_DataIngest'
mvn -e clean install

cd '/home/ec2-user/SGA_REST_login'
cp -r ./* /opt/apache-tomcat-8.0.37/webapps/ROOT/
cd '/home/ec2-user/SGA_REST_Homepage'
#cp -r ./* /opt/apache-tomcat-8.0.37/webapps/ROOT/


sudo cp index.html /opt/apache-tomcat-8.0.37/webapps/ROOT/
sudo cp ./scripts/angular.min.js /opt/apache-tomcat-8.0.37/webapps/ROOT/scripts/
sudo cp ./scripts/main.js /opt/apache-tomcat-8.0.37/webapps/ROOT/scripts/
sudo cp ./services/cors.py /opt/apache-tomcat-8.0.37/webapps/ROOT/services/
sudo cp ./services/homepage.py /opt/apache-tomcat-8.0.37/webapps/ROOT/services/
sudo cp ./services/aws_key.properties /opt/apache-tomcat-8.0.37/webapps/ROOT/services/
sudo cp ./stylesheets/bootstrap.min.css /opt/apache-tomcat-8.0.37/webapps/ROOT/stylesheets/

