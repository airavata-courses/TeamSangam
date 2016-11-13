echo "Building MongoDB docker image"
cd /home/ec2-user/DB_Dockers/registry_MongoDB/
docker build -t imongo .

echo "Build maven package for Data Ingest"
cd /home/ec2-user/SGA_REST_DataIngest/
mvn package
echo "Building dataingest docker image"
docker build -t isgadataingest .

