#!/bin/bash
# Define Hostnames
h1="mongodb1"
h2="mongodb2"
h3="mongodb3"	

docker run -P --name mongo1 --hostname="$h1" -d imongo --replSet rs0 --noprealloc --smallfiles
#docker run -P --name mongo2 --hostname="$h2" -d imongo --replSet rs0 --noprealloc --smallfiles
#docker run -P --name mongo3 --hostname="$h3" -d imongo --replSet rs0 --noprealloc --smallfiles

echo $(docker inspect --format '{{ .NetworkSettings.IPAddress }}' mongo1) "$h1" > getip.txt
echo $(docker inspect --format '{{ .NetworkSettings.IPAddress }}' mongo2) "$h2" >> getip.txt
echo $(docker inspect --format '{{ .NetworkSettings.IPAddress }}' mongo3) "$h3" >> getip.txt

docker cp getip.txt mongo1:/etc
docker cp getip.txt mongo2:/etc
docker cp getip.txt mongo3:/etc

echo "#!/bin/bash
cat /etc/hosts > /etc/hosts1
#sed -i '/ttnd.com/d' /etc/hosts1
cat /etc/getip.txt >> /etc/hosts1
cat /etc/hosts1 > /etc/hosts" > updateHost.sh

chmod +x updateHost.sh
docker cp updateHost.sh mongo1:/etc
docker cp updateHost.sh mongo2:/etc
docker cp updateHost.sh mongo3:/etc
docker exec -it mongo{1,2,3} chmod +x /etc/updateHost.sh

docker exec -it mongo1 /etc/updateHost.sh
docker exec -it mongo2 /etc/updateHost.sh
docker exec -it mongo3 /etc/updateHost.sh

docker exec -it mongo1 mongo --eval "rs.status()"
docker exec -it mongo1 mongo --eval "db"
docker exec -it mongo1 mongo --eval "rs.initiate()"
sleep 120
docker exec -it mongo1 mongo --eval "rs.add(\"$h2:27017\")"
docker exec -it mongo1 mongo --eval "rs.add(\"$h3:27017\")"
docker exec -it mongo2 mongo --eval "rs.slaveOk()"
docker exec -it mongo2 mongo --eval "rs.slaveOk()"
