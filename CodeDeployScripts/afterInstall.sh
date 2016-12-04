echo "Moving the dataingest files to /home/ec2-user"
rm -rf /home/ec2-user/SGA_REST_DataIngest
mv /home/ec2-user/dataingest/SGA_REST_DataIngest /home/ec2-user/

#DIRECTORY=DB_Dockers
#cd /home/ec2-user
#if [ -d "$DIRECTORY" ]; then
#	echo "Directory already exists"
#else
#	mv /home/ec2-user/dataingest/DB_Dockers /home/ec2-user/
#fi
	
#echo "Building MongoDB docker image"
#cd /home/ec2-user/DB_Dockers/registry_MongoDB/

#if [[ $(docker images | grep -w "imongo") ]]; then
        #echo "MongoDB Docker image already present"
#else
	#docker build -t imongo .
#fi

echo "Build maven package for Data Ingest"
cd /home/ec2-user/SGA_REST_DataIngest/
declare -a worker
declare -i index
index=0
while IFS=':' read -r server privateip publicip
do
   if [[ "$server" == "SERVER"* ]]; then
	worker[index]=$publicip
        index=index+1
	#serverID=$server
   fi
done < /home/ec2-user/SGA_REST_DataIngest/system.properties

sed -i 's/bootstrap.servers=.*/bootstrap.servers=\'${worker[0]}',\'${worker[1]}',\'${worker[2]}'/' /home/ec2-user/SGA_REST_DataIngest/src/main/resources/producer.props
sed -i 's/bootstrap.servers=.*/bootstrap.servers=\'${worker[0]}',\'${worker[1]}',\'${worker[2]}'/' /home/ec2-user/SGA_REST_DataIngest/src/main/resources/consumer.props

mvn package
echo "Building dataingest docker image"
docker build -t isgadataingest .
