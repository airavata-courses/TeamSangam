echo "Moving the storm clustering files to /home/ec2-user"
rm -rf /home/ec2-user/SGA_Rest_StormClustering
mv /home/ec2-user/forecast/SGA_Rest_StormClustering /home/ec2-user/

DIRECTORY=DB_Dockers
cd /home/ec2-user
if [ -d "$DIRECTORY" ]; then
	echo "Directory already exists"
else
	mv /home/ec2-user/stormclustering/DB_Dockers /home/ec2-user/
fi
	
echo "Building MongoDB docker image"
cd /home/ec2-user/DB_Dockers/registry_MongoDB/

if [[ $(docker images | grep -w "imongo") ]]; then
        echo "MongoDB Docker image already present"
else
	docker build -t imongo .
fi

echo "Building maven package for storm clustering."
cd /home/ec2-user/SGA_Rest_StormClustering/
mvn package

echo "Building docker image for storm clustering."
cd /home/ec2-user/SGA_Rest_StormClustering/
docker build -t isgastormclustering .
