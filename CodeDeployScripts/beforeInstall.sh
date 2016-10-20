ps -ef | grep tomcat | awk '{print $2}' | xargs sudo kill -9
ps -ef | grep login.py | awk '{print $2}' | xargs sudo kill -9
ps -ef | grep homepage.py | awk '{print $2}' | xargs sudo kill -9
echo 'Installing python 3.4...'
sudo yum -y install python34

echo 'Installing some dependencies for setting up python 3.4 environment...'
sudo yum -y install python34-pip
sudo yum -y install python-devel mysql-devel
sudo yum -y install python34-devel
sudo yum -y install gcc
sudo alternatives --set python /usr/bin/python3.4
sudo pip install virtualenv
sudo alternatives --set python /usr/bin/python2.7

#echo 'Installing MySQL...'
#sudo yum –y install mysql55-server

#sudo service mysqld start

#sudo mysqladmin -u root password root

#sudo mysql -u root -proot << EOF
#create database weatherApp;
#use weatherApp;
#CREATE TABLE weatherApp.users (
#Id INT NOT NULL AUTO_INCREMENT,
#FirstName VARCHAR(45) NOT NULL,
#LastName VARCHAR(45) NOT NULL,
#Email VARCHAR(255) NOT NULL,
#Password VARCHAR(255) NOT NULL,
#Role VARCHAR(10) NOT NULL,
#PRIMARY KEY (Id),
#UNIQUE INDEX Id_UNIQUE (Id ASC));
#EOF

#echo 'check if maven is installed'
#mvn --version
#if [ "$?" -ne 0 ]; then
#    echo 'Installing Maven...'
#    sudo wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo
#    sudo sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo
#    sudo yum install -y apache-maven
#    mvn --version
#fi
