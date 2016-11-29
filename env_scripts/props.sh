#Scripts saves the environment variables into properties file
#echo 'MYSQL_USER='$MYSQL_USER > ../SGA_REST_login/services/mysql.properties
#echo 'MYSQL_PWD='$MYSQL_PWD >> ../SGA_REST_login/services/mysql.properties
#echo 'API_KEY='$API_KEY > ../SGA_REST_Homepage/services/api_key.properties
#echo 'AWS_ACCESS_KEY='$AWS_ACCESS_KEY > ../SGA_REST_Homepage/services/aws_key.properties
#echo 'AWS_SECRET_KEY='$AWS_SECRET_KEY >> ../SGA_REST_Homepage/services/aws_key.properties
#echo 'GOAUTH_CLIENT_ID='$GOAUTH_CLIENT_ID > ../SGA_REST_login/services/googleOAuth.properties
#echo 'GOAUTH_CLIENT_SECRET='$GOAUTH_CLIENT_SECRET >> ../SGA_REST_login/services/googleOAuth.properties
#echo 'GOAUTH_APP_NAME='$GOAUTH_APP_NAME >> ../SGA_REST_login/services/googleOAuth.properties
echo 'SERVER_1:'$PRIVATE_IP1:$PUBLIC_IP1 > ../system.properties
echo 'SERVER_2:'$PRIVATE_IP2:$PUBLIC_IP2 >> ../system.properties
echo 'GATEWAY:'$GATEWAY_PRIVATE_IP:$GATEWAY_PUBLIC_IP >> ../system.properties
