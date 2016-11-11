echo "Building Homepage docker image"
cd /home/ec2-user/SGA_REST_Homepage/
docker build -t isgahome .

sudo mkdir -p scripts
echo "Created scripts directory"
sudo mkdir -p stylesheets
echo "Created stylesheets directory"
sudo mkdir -p templates
echo "Created templates directory"

sudo cp /home/ec2-user/SGA_REST_Homepage/*.html .
sudo cp /home/ec2-user/SGA_REST_Homepage/scripts/* ./scripts
sudo cp /home/ec2-user/SGA_REST_Homepage/stylesheets/* ./stylesheets
echo "Copied the html and javascript files to TOMCAT"

