set -e

if [[ $(docker ps -a -f name=mongo -q) ]]; then
	echo "MongoDB Docker already running"
else	
	docker run -d --name mongo -p 27017:27017 imongo
fi

if [[ $(docker ps -a -f name=sgadataingest -q) ]]; then
	echo "Removing the existing sgadataingest docker container."
	docker stop sgadataingest || true
	docker rm -f sgadataingest || true
fi

echo "Starting new docker container sgadataingest"
docker run -d --link mongo:mongo --name sgadataingest -p 8081:8080 isgadataingest

localip=$(ip addr show eth0 | awk '/inet /{split($2,a,"/");print a[1]}')

while IFS=':' read -r server privateip publicip
do
   if [ "$privateip" == "$localip" ]; then
	systemIP=$publicip
	serverID=$server 
   fi
done < /home/ec2-user/SGA_REST_DataIngest/system.properties

echo "Changing the directory to SGA_REST_DataIngest"
cd /home/ec2-user/SGA_REST_DataIngest

echo "The current systemIP is"
echo $systemip

echo "The current systemID is"
echo $serverID

mvn exec:java -Dexec.mainClass=edu.sga.sangam.resources.DataIngestor -Dexec.args="$serverID $systemIP 8081"

echo "Removing all the dangling docker images"
docker rmi $(docker images -f "dangling=true" -q) || true
