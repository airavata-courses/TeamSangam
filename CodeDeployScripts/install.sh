set -e

#if [[ $(docker ps -a -f name=mongo -q) ]]; then
        #echo "MongoDB Docker already running"
#else
        #docker run -d --name mongo -p 27017:27017 imongo
#fi

if [[ $(docker ps -a -f name=sgaforecast -q) ]]; then
	echo "Removing the existing sgaforecast docker container."
	docker stop sgaforecast || true
	docker rm -f sgaforecast || true
fi

echo "Starting new docker container sgaforecast"
docker run -d --name sgaforecast -p 8084:8080 isgaforecast

#localip=$(ip addr show eth0 | awk '/inet /{split($2,a,"/");print a[1]}')

#while IFS=':' read -r server privateip publicip
#do
   #if [ "$privateip" == "$localip" ]; then
        #systemIP=$publicip
        #serverID=$server
   #fi
#done < /home/ec2-user/SGA_REST_Forecast/system.properties

echo "Changing the directory to SGA_REST_Forecast"
cd /home/ec2-user/SGA_REST_Forecast

nohup mvn exec:java -Dexec.mainClass=edu.sga.sangam.resources.RunForecastMain > /home/ec2-user/maven.out 2>&1 &

echo "Changing the directory to SGA_REST_ForecastDecision"
cd /home/ec2-user/SGA_REST_ForecastDecision

nohup mvn exec:java -Dexec.mainClass=edu.sga.sangam.resources.ForecastDecisionMain > /home/ec2-user/maven.out 2>&1 &

docker rmi $(docker images -f "dangling=true" -q) || true
