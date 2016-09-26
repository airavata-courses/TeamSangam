echo 'Installing Sangam Weather App to Maven'

cd '/home/ec2-user/SGA_REST_DataIngest'
mvn -e clean install

cd '/home/ec2-user/SGA_REST_login'
cp -r ./* $CATALINA_HOME/webapps/ROOT
