#Scripts saves the environment variables into properties file
echo 'MYSQL_USER='$MYSQL_USER > ../SGA_REST_login/services/mysql.properties
echo 'MYSQL_PWD='$MYSQL_PWD >> ../SGA_REST_login/services/mysql.properties
echo 'API_KEY='$API_KEY > ../SGA_REST_Homepage/services/api_key.properties
echo 'AWS_ACCESS_KEY='$AWS_ACCESS_KEY > ../SGA_REST_Homepage/services/aws_key.properties
echo 'AWS_SECRET_KEY='$AWS_SECRET_KEY >> ../SGA_REST_Homepage/services/aws_key.properties

