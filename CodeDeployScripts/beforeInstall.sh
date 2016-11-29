#echo 'check if maven is installed'
#mvn --version
#if [ "$?" -ne 0 ]; then
    #echo 'Installing Maven...'
    #sudo wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo
    #sudo sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo
    #sudo yum install -y apache-maven
    #mvn --version
#fi

#docker -v

#if [ "$?" -ne 0 ]; then
#    echo 'Installing Docker...'
#    sudo yum update -y
#    sudo yum install -y docker
#    sudo service docker start
#    sudo usermod -a -G docker ec2-user
#fi

echo 'check if mongodb is already installed'
mongo -version
if [ "$?" -ne 0 ]; then
	FILE='/etc/yum.repos.d/mongodb-org-3.2.repo'
	if [ -f $FILE ]; then       
		echo 'The  yum install list file already exists'
	else	 
		echo "[mongodb-org-3.2]
name=MongoDB Repository
baseurl=https://repo.mongodb.org/yum/amazon/2013.03/mongodb-org/3.2/x86_64/
gpgcheck=1
enabled=1
gpgkey=https://www.mongodb.org/static/pgp/server-3.2.asc" >> /etc/yum.repos.d/mongodb-org-3.2.repo
	fi

        sudo yum install -y mongodb-org
	sudo sed -i 's/bindIp:/#bindIp:/' /etc/mongod.conf
        sudo service mongod start
        #sudo sed -i 's/#replication:/#replication:\nreplSet=rs0/' /etc/mongod.conf
        sudo chkconfig mongod on
	sudo service mongod stop
        nohup sudo mongod --replSet "rs0" --dbpath "/var/lib/mongo" >mongoRepl.out 2>&1 &
fi
