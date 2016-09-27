echo 'Creating python 3.4 virtual environment'
cd $CATALINA_HOME/webapps/ROOT/services
sudo /usr/local/bin/virtualenv -p /usr/bin/python3.4 weatherApp
source $CATALINA_HOME/webapps/ROOT/services/weatherApp/bin/activate

echo 'Installing required python modules..'
sudo $CATALINA_HOME/webapps/ROOT/services/weatherApp/bin/pip3.4 install -r /home/ec2-user/requirements.txt

echo 'Starting Tomcat Server'
sh /opt/apache-tomcat-8.0.37/bin/startup.sh
echo 'Starting login service..'
sudo service mysqld start
$CATALINA_HOME/webapps/ROOT/services/weatherApp/bin/python3.4 login.py

cd /home/ec2-user/
