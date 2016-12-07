set -e

#if [[ $(docker ps -a -f name=mongo -q) ]]; then
	#echo "MongoDB Docker already running"
#else	
	#docker run -d --name mongo -p 27017:27017 imongo
#fi

if [[ $(docker ps -a -f name=sgadataingest -q) ]]; then
	echo "Removing the existing sgadataingest docker container."
	docker stop sgadataingest || true
	docker rm -f sgadataingest || true
fi

echo "Starting new docker container sgadataingest"
docker run -d --name sgadataingest -p 8081:8080 isgadataingest


echo "Changing the directory to SGA_REST_DataIngest"
cd /home/ec2-user/SGA_REST_DataIngest

nohup mvn exec:java -Dexec.mainClass=edu.sga.sangam.resources.DataIngestorMain > /home/ec2-user/SGA_REST_DataIngest/dataingestor.out 2>&1 &


#mvn exec:java -Dexec.mainClass=edu.sga.sangam.resources.DataIngestorMain -Dexec.args="$serverID $systemIP 8081"

echo "Removing all the dangling docker images"
docker rmi $(docker images -f "dangling=true" -q) || true
