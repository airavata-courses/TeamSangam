echo 'Creating python 3.4 virtual environment'
cd /opt/apache-tomcat-8.0.37/webapps/ROOT/services
sudo /usr/local/bin/virtualenv -p /usr/bin/python3.4 weatherApp
source /opt/apache-tomcat-8.0.37/webapps/ROOT/services/weatherApp/bin/activate

echo 'Installing required python modules..'
sudo /opt/apache-tomcat-8.0.37/webapps/ROOT/services/weatherApp/bin/pip3.4 install -r /home/ec2-user/requirements.txt

echo 'Starting Tomcat Server'
sh /opt/apache-tomcat-8.0.37/bin/startup.sh
echo 'Starting login service..'
sudo service mysqld start
nohup /opt/apache-tomcat-8.0.37/webapps/ROOT/services/weatherApp/bin/python3.4 login.py > login.log &
nohup /opt/apache-tomcat-8.0.37/webapps/ROOT/services/weatherApp/bin/python3.4 homepage.py > homepage.log &
cd /home/ec2-user/
