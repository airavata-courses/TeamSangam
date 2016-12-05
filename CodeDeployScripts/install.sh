set -e

if [[ $(docker ps -a -f name=sgagateway -q) ]]; then
        echo "Removing the existing sgagateway docker container."
        docker stop sgagateway || true
        docker rm -f sgagateway || true
fi

echo "Starting new docker container sgagateway"
docker run -d --name sgagateway -p 8080:8080 isgagateway

cd /home/ec2-user/SGA_REST_WeatherForecastClient/
nohup mvn exec:java -Dexec.mainClass=edu.sga.sangam.services.ResultMain > /home/ec2-user/SGA_REST_WeatherForecastClient/result.out 2>&1 &

echo "Removing all the dangling docker images"
docker rmi $(docker images -f "dangling=true" -q) || true



