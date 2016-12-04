set -e

#if [[ $(docker ps -a -f name=mongo -q) ]]; then
        #echo "MongoDB Docker already running"
#else
        #docker run -d --name mongo -p 27017:27017 imongo
#fi

if [[ $(docker ps -a -f name=sgastormclustering -q) ]]; then
	echo "Removing the existing sgastormclustering docker container."
	docker stop sgastormclustering || true
	docker rm -f sgastormclustering || true
fi

echo "Starting new docker container sgastormclustering"
docker run -d --name sgastormclustering -p 8083:8080 isgastormclustering

#localip=$(ip addr show eth0 | awk '/inet /{split($2,a,"/");print a[1]}')


echo "Changing the directory to SGA_Rest_StormClustering"
cd /home/ec2-user/SGA_Rest_StormClustering

nohup mvn exec:java -Dexec.mainClass=edu.sga.sangam.resources.StormClusteringMain &

docker rmi $(docker images -f "dangling=true" -q) || true
