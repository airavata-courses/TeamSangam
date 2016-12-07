echo "Moving the storm detection files to /home/ec2-user"
rm -rf /home/ec2-user/SGA_Rest_StormDetection
mv /home/ec2-user/stormdetection/SGA_Rest_StormDetection /home/ec2-user/

#DIRECTORY=DB_Dockers
#cd /home/ec2-user
#if [ -d "$DIRECTORY" ]; then
	#echo "DB_Dockers directory already exists"
#else
	#mv /home/ec2-user/stormdetection/DB_Dockers /home/ec2-user/
#fi
	
#echo "Building MongoDB docker image"
#cd /home/ec2-user/DB_Dockers/registry_MongoDB/

#if [[ $(docker images | grep -w "imongo") ]]; then
        #echo "MongoDB Docker image already present"
#else
	#docker build -t imongo .
#fi

echo "Building maven package for storm detection."
cd /home/ec2-user/SGA_Rest_StormDetection/
declare -a worker
declare -i index
index=0
while IFS=':' read -r server privateip publicip
do
	if [[ "$server" == "SERVER"* ]]; then
		worker[$index]=$publicip
		index=$index+1
		echo "$index"
	fi
done < /home/ec2-user/SGA_Rest_StormDetection/system.properties
echo ${worker[1]}
echo ${worker[0]}
echo ${worker[2]}

mvn package -Dmaven.test.skip=true

echo "Building docker image for storm detection."
cd /home/ec2-user/SGA_Rest_StormDetection/
docker build -t isgastormdetection .
