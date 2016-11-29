echo "Moving the MongoDB files to /home/ec2-user"
#rm -rf /home/ec2-user/SGA_Mongo_Replication
#mv /home/ec2-user/mongo-replication/ /home/ec2-user/


localip=$(ip addr show eth0 | awk '/inet /{split($2,a,"/");print a[1]}')

while IFS=':' read -r server privateip publicip
do
   if [ "$privateip" == "$localip" ]; then
        systemIP=$publicip
        serverID=$server
   fi
done < /home/ec2-user/mongo-replication/system.properties

gateway='GATEWAY'

if [ "$serverID" == "$gateway" ]; then
        mongo --eval "rs.initiate()"
	while [ $(curl -s -o /dev/null --connect-timeout 10 -w "%{http_code}" http://54.183.160.22:27017/) -ne 200 ]
	do
		sleep 20
	done
		mongo --eval 'rs.add("54.183.160.22")'
	
	while [ $(curl -s -o /dev/null --connect-timeout 10 -w "%{http_code}" http://54.193.102.104:27017/) -ne 200 ]
        do
                sleep 20
        done
                mongo --eval 'rs.add("54.193.102.104")'

else
	while [ $(curl -s -o /dev/null --connect-timeout 10 -w "%{http_code}" http://54.183.233.167:27017/) -ne 200 ]
        do
                sleep 120
        done
	
	#mongo --eval rs.conf().members.
	mongo --eval "rs.slaveOk()"	
fi
