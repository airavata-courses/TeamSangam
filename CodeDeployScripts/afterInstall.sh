echo "Moving the storm clustering files to /home/ec2-user"
rm -rf /home/ec2-user/SGA_Rest_StormClustering
mv /home/ec2-user/stormclustering/SGA_Rest_StormClustering /home/ec2-user/

#DIRECTORY=DB_Dockers
#cd /home/ec2-user
#if [ -d "$DIRECTORY" ]; then
#	echo "Directory already exists"
#else
#	mv /home/ec2-user/stormclustering/DB_Dockers /home/ec2-user/
#fi
	
#echo "Building MongoDB docker image"
#cd /home/ec2-user/DB_Dockers/registry_MongoDB/

#if [[ $(docker images | grep -w "imongo") ]]; then
        #echo "MongoDB Docker image already present"
#else
	#docker build -t imongo .
#fi

echo "Building maven package for storm clustering."
cd /home/ec2-user/SGA_Rest_StormClustering/
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
done < /home/ec2-user/SGA_Rest_StormClustering/system.properties
sed -i 's/bootstrap.servers=.*/bootstrap.servers=\'${worker[0]}',\'${worker[1]}',\'${worker[2]}'/' /home/ec2-user/SGA_Rest_StormClustering/src/main/resources/producer.props
sed -i 's/bootstrap.servers=.*/bootstrap.servers=\'${worker[0]}',\'${worker[1]}',\'${worker[2]}'/' /home/ec2-user/SGA_Rest_StormClustering/src/main/resources/consumer.props

mvn package -Dmaven.test.skip=true

echo "Building docker image for storm clustering."
cd /home/ec2-user/SGA_Rest_StormClustering/
docker build -t isgastormclustering .
