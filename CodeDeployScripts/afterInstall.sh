echo "Moving the registry files to /home/ec2-user"
rm -rf /home/ec2-user/SGA_REST_Registry
mv /home/ec2-user/registry/SGA_REST_Registry /home/ec2-user/

DIRECTORY=DB_Dockers
cd /home/ec2-user
if [ -d "$DIRECTORY" ]; then
        echo "Directory already exists"
else
        mv /home/ec2-user/registry/DB_Dockers /home/ec2-user/
fi


echo "Building MongoDB docker image"
cd /home/ec2-user/DB_Dockers/registry_MongoDB/

if [[ $(docker images | grep -w "imongo") ]]; then
        echo "MongoDB Docker image already present"
else
        docker build -t imongo .
fi


echo "Building maven package for Registry"
cd /home/ec2-user/SGA_REST_Registry/
mvn package

echo "Building docker image for Registry"
docker build -t isgaregistry .
