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
#/opt/apache-tomcat-8.0.37/webapps/ROOT/services/weatherApp/bin/python3.4 login.py 
echo 'Started login service, starting homepage service..'
nohup /opt/apache-tomcat-8.0.37/webapps/ROOT/services/weatherApp/bin/python3.4 homepage.py 1> homepage.out 2> homepageErr.out
echo 'Started homepage service..'
nohup /opt/apache-tomcat-8.0.37/webapps/ROOT/services/weatherApp/bin/python3.4 login.py 1> login.out 2> loginErr.out
cd /home/ec2-user/
