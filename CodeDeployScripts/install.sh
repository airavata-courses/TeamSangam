set -e

if [[ $(docker ps -a -f name=mongo -q) ]]; then
	echo "MongoDB Docker already running"
else	
	docker run -d --name mongo -p 27017:27017 imongo
fi

docker run -d --link mongo:mongo --name sgadataingest -p 8081:8080 isgadataingest

localip=$(ip addr show eth0 | awk '/inet /{split($2,a,"/");print a[1]}')

while IFS=':' read -r server privateip publicip
do
   if [ "$privateip" == "$localip" ]; then
	systemip=$publicip
	serverID=$server 
   fi
done < /home/ec2-user/SGA_REST_DataIngest/server.properties

cd /home/ec2-user/SGA_REST_DataIngest
mvn exec:java -Dexec.mainClass=edu.sga.sangam.resources.DataIngestor -Dexec.args="$systemip $serverID 8080"
