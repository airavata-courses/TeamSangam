echo 'Installing Sangam Weather App to Maven'

cd '/home/ec2-user/sangamWeatherApp/SGA_REST_DataIngest'
mvn -e clean install

cd '/home/ec2-user/sangamWeatherApp/SGA_REST_login'
cp ./* $CATALINA_HOME/webapps/

