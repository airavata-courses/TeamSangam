echo 'Installing python 3.4'
sudo yum install python34

echo 'Installing required python modules..'
pip install -r ~/ec2-user/sangamWeatherApp/requirements.txt 
