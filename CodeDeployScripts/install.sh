set -e

docker run -d --name mongo -p 27017:27017 imongo

docker run -d --link mongo:mongo --name sgadataingest -p 8081:8080 isgadataingest

localip=$(ip addr show eth0 | awk '/inet /{split($2,a,"/");print a[1]}')

while IFS=':' read -r privateip publicip
do
   if [ "$privateip" == "$localip" ]; then
      systemip=$publicip
   fi
done < ../SGA_REST_DataIngest/server.properties

javac /home/ec2-user/SGA_REST_DataIngest/src/main/java/edu/sga/sangam/resources/DataIngestor.java

java /home/ec2-user/SGA_REST_DataIngest/src/main/java/edu/sga/sangam/resources/DataIngestor.java $systemip dataingestor 8080  
