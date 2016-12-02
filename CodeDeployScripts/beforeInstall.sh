echo "Check if swap space is at least 4gb or not"


echo "Check if Zookeeper is installed, up & running"

DIRECTORY='zookeeper'
cd /home/ec2-user
if [ -d "$DIRECTORY" ]; then
	echo "Zookeeper is already downloaded and untarred"
else
	echo "Zookeeper is not downloaded and untarred"
	echo "Downloading Zookeeper version 3.4.9"
	wget http://www-eu.apache.org/dist/zookeeper/zookeeper-3.4.9/zookeeper-3.4.9.tar.gz
	echo "Untarring the zookeeper tar file"
	tar -xvf zookeeper-3.4.9.tar.gz
	mv zookeeper-3.4.9 zookeeper
	rm -rf zookeeper-3.4.9.tar.gz
	

