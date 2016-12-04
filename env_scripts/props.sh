#Scripts saves the environment variables into properties file

echo 'SERVER_1:'$PRIVATE_IP1:$PUBLIC_IP1 > ../SGA_Rest_StormDetection/system.properties
echo 'SERVER_2:'$PRIVATE_IP2:$PUBLIC_IP2 >> ../SGA_Rest_StormDetection/system.properties
echo 'SERVER_3:'$GATEWAY_PRIVATE_IP:$GATEWAY_PUBLIC_IP >> ../SGA_Rest_StormDetection/system.properties
