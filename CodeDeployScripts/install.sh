set -e

#if [[ $(docker ps -a -f name=mongo -q) ]]; then
        #echo "MongoDB Docker already running"
#else
        #docker run -d --name mongo -p 27017:27017 imongo
#fi

if [[ $(docker ps -a -f name=sgastormdetection -q) ]]; then
	echo "Removing the existing sgastormdetection docker container."
	docker stop sgastormdetection || true
	docker rm -f sgastormdetection || true
fi

echo "Starting new docker container sgastormdetection"
docker run -d --name sgastormdetection -p 8082:8080 isgastormdetection

localip=$(ip addr show eth0 | awk '/inet /{split($2,a,"/");print a[1]}')

while IFS=':' read -r server privateip publicip
do
   if [ "$privateip" == "$localip" ]; then
        systemIP=$publicip
        serverID=$server
   fi
done < /home/ec2-user/SGA_Rest_StormDetection/system.properties

echo "Changing the directory to SGA_Rest_StormDetection"
cd /home/ec2-user/SGA_Rest_StormDetection

mvn exec:java -Dexec.mainClass=edu.sga.sangam.resources.StormDetection -Dexec.args="$serverID $systemIP 8082"

docker rmi $(docker images -f "dangling=true" -q) || true
