set -e

if [[ $(docker ps -a -f name=mongo -q) ]]; then
        echo "MongoDB Docker already running"
else
        docker run -d --name mongo -p 27017:27017 imongo
fi

if [[ $(docker ps -a -f name=sgaregistry -q) ]]; then
        echo "Removing the existing sgaregistry docker container."
        docker stop sgaregistry || true
        docker rm -f sgaregistry || true
fi

echo "Starting new docker container sgaregistry"

docker run -d --link mongo:mongo --name sgaregistry -p 8085:8080 isgaregistry

localip=$(ip addr show eth0 | awk '/inet /{split($2,a,"/");print a[1]}')

while IFS=':' read -r server privateip publicip
do
   if [ "$privateip" == "$localip" ]; then
        systemIP=$publicip
        serverID=$server
   fi
done < /home/ec2-user/SGA_REST_Registry/system.properties

echo "Changing the directory to SGA_REST_Registry"
cd /home/ec2-user/SGA_REST_Registry

mvn exec:java -Dexec.mainClass=edu.sga.sangam.resources.Registry -Dexec.args="$serverID $systemIP 8085"

docker rmi $(docker images -f "dangling=true" -q) || true
