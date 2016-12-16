echo "Moving the result files to /home/ec2-user"
rm -rf /home/ec2-user/SGA_REST_WeatherForecastClient
mv /home/ec2-user/result/SGA_REST_WeatherForecastClient /home/ec2-user/

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

cd /home/ec2-user/SGA_REST_WeatherForecastClient
echo "Starting the result consumer thread"

nohup mvn exec:java -Dexec.mainClass=edu.sga.sangam.services.ResultMain > /home/ec2-user/SGA_REST_WeatherForecast/result.out 2>&1 &
