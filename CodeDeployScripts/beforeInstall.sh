#echo 'check if maven is installed'
#mvn --version
#if [ "$?" -ne 0 ]; then
#    echo 'Installing Maven...'
#    sudo wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo
#    sudo sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo
#    sudo yum install -y apache-maven
#    mvn --version
#fi

docker -v
if [ "$?" -ne 0 ]; then
    echo 'Installing Docker...'
    sudo yum update -y
    sudo yum install -y docker
    sudo service docker start
    sudo usermod -a -G docker ec2-user
fi

echo "Moving contents to appropriate directory"
ls /home/ec2-user
#mv /home/ec2-user/homepage/SGA_REST_Homepage /home/ec2-user/SGA_REST_Homepage
