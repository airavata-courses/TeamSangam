echo 'Creating python 3.4 virtual environment'
cd /home/ec2-user/SGA_REST_login/services
sudo /usr/local/bin/virtualenv -p /usr/bin/python3.4 weatherApp
source /home/ec2-user/SGA_REST_login/services/weatherApp/bin/activate

echo 'Installing required python modules..'
sudo /home/ec2-user/SGA_REST_login/services/weatherApp/bin/pip3.4 install -r /home/ec2-user/requirements.txt
cp /home/ec2-user/SGA_REST_login/dependencies/cors.py /home/ec2-user/weatherApp/lib/python3.4/site-packages/flask/cors.py
mv /home/ec2-user/SGA_REST_login/dependencies/cors.py /home/ec2-user/weatherApp/lib64/python3.4/site-packages/flask/cors.py

echo 'Starting login service..'
sudo service mysqld start
/home/ec2-user/weatherApp/bin/python3.4 login.py

cd /home/ec2-user/
