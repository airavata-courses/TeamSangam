set -e

docker run -d --name usermysqldb -p 3306:3306 iusermysqldb

docker run -d --link usermysqldb:usermysqldb --name sgalogin -p 5000:5000 isgalogin
