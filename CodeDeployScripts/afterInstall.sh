echo "Moving the dataingest files to /home/ec2-user"
rm -rf /home/ec2-user/SGA_REST_DataIngest
mv /home/ec2-user/dataingest/SGA_REST_DataIngest /home/ec2-user/

DIRECTORY=DB_Dockers
cd /home/ec2-user
if [ -d "$DIRECTORY" ]; then
	echo "Directory already exists"
else
	mv /home/ec2-user/dataingest/DB_Dockers /home/ec2-user/
fi
	
echo "Building MongoDB docker image"
cd /home/ec2-user/DB_Dockers/registry_MongoDB/

if [[ $(docker images | grep -w "imongo") ]]; then
        echo "MongoDB Docker image already present"
else
	docker build -t imongo .
fi

echo "Build maven package for Data Ingest"
cd /home/ec2-user/SGA_REST_DataIngest/
mvn package
echo "Building dataingest docker image"
docker build -t isgadataingest .