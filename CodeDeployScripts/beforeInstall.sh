docker -v
if [ "$?" -ne 0 ]; then
    echo 'Installing Docker...'
    sudo yum update -y
    sudo yum install -y docker
    sudo service docker start
    sudo usermod -a -G docker ec2-user
fi
