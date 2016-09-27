echo 'Installing Sangam Weather App to Maven'

cd '/home/ec2-user/SGA_REST_DataIngest'
mvn -e clean install

cd '/home/ec2-user/SGA_REST_login'
cp -r ./* /opt/apache-tomcat-8.0.37/webapps/ROOT/
