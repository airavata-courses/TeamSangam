echo "Moving the Forecast files to /home/ec2-user"
rm -rf /home/ec2-user/SGA_REST_Forecast
rm -rf /home/ec2-user/SGA_REST_ForecastDecision
mv /home/ec2-user/forecast/SGA_REST_Forecast /home/ec2-user/

echo "Moving the ForecastDecision files to /home/ec2-user"
mv /home/ec2-user/forecast/SGA_REST_ForecastDecision /home/ec2-user/

DIRECTORY=DB_Dockers
cd /home/ec2-user
if [ -d "$DIRECTORY" ]; then
	echo "Directory already exists"
else
	mv /home/ec2-user/forecast/DB_Dockers /home/ec2-user/
fi
	
#echo "Building MongoDB docker image"
#cd /home/ec2-user/DB_Dockers/registry_MongoDB/

#if [[ $(docker images | grep -w "imongo") ]]; then
        #echo "MongoDB Docker image already present"
#else
	#docker build -t imongo .
#fi

echo "Building maven package for Forecast"
cd /home/ec2-user/SGA_REST_Forecast/
mvn package

echo "Building maven package for ForecastDecision"
cd /home/ec2-user/SGA_REST_ForecastDecision/
mvn package
mv ./target/SGA_REST_ForecastDecision.war /home/ec2-user/SGA_REST_Forecast/target/SGA_REST_ForecastDecision.war

echo "Building docker image for Forecast Decision & Run Forecast"
cd /home/ec2-user/SGA_REST_Forecast/
docker build -t isgaforecast .
