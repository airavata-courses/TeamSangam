import MySQLdb as mysql
from passlib.handlers.sha2_crypt import sha256_crypt

filename = "mysql.properties"
user, password = tuple([line.strip().split("=")[1] for line in open(filename, 'r')])
host = "usermysqldb"
port = 3306
# Keep in mind that database names in windows are case insensitive whereas database names in most linux systems are case sensitive.
database = "weatherApp"
table = "users"

def insertUser(firstName, lastName, email, password, role="guest"):
# Adds new user details to the table. By default, users are guests.
	db = mysql.connect(host=host, port=port, user=SQLuser, passwd=SQLpassword, db=database)
	cursor = db.cursor()
	insertQuery = """
	INSERT INTO """+database+"""."""+table+""" (FirstName, LastName, Email, Password, Role)
	VALUES
	({firstName}, {lastName}, {email}, {password}, {role})
	;
	""".format(firstName="'"+firstName+"'", lastName="'"+lastName+"'", email="'"+email+"'", password="'"+sha256_crypt.encrypt(password)+"'", role="'"+role+"'")
	try:
		cursor.execute(insertQuery)
		db.commit()
	except Exception as e:
		print(e)
		db.close()
		return False
	else:
		db.close()
		return True

def findUser(email, password=""):
# check if email is in the table.
# if in the table, find the corresponding password
	db = mysql.connect(host=host, port=port, user=SQLuser, passwd=SQLpassword, db=database)
	cursor = db.cursor()
	findQuery = "SELECT Password FROM "+database+"."+table+" WHERE Email='"+email+"';"
	try:
		cursor.execute(findQuery)
	except Exception as e:
		print(e)
		db.close()
		return False
	else:
		dbPwd = cursor.fetchone()
		db.close()
		if not dbPwd:
			# user non existent
			return 99
		else:
			if sha256_crypt.verify(password, dbPwd[0]):
				# user exists and correct password
				return 11
			else:
				# user exists, but password does not match
				return 19

def setUpTestDB(testDB, testTable, user):
	db = mysql.connect(host=host, port=port, user=SQLuser, passwd=SQLpassword)
	cursor = db.cursor()
	createDBQuery = "create database "+testDB+";"
	try:
		cursor.execute(createDBQuery)
	except Exception as e:
		print(e)
		db.close()
		return False
	useDBQuery = "use "+testDB+";"
	try:
		cursor.execute(useDBQuery)
	except Exception as e:
		print(e)
		db.close()
		return False
	createTableQuery = """CREATE TABLE """+testDB+"""."""+testTable+""" (Id INT NOT NULL AUTO_INCREMENT, FirstName VARCHAR(45) NOT NULL, LastName VARCHAR(45) NOT NULL, Email VARCHAR(255) NOT NULL, Password VARCHAR(255) NOT NULL, Role VARCHAR(10) NOT NULL, PRIMARY KEY (Id),	UNIQUE INDEX Id_UNIQUE (Id ASC));"""
	try:
		cursor.execute(createTableQuery)
	except Exception as e:
		print(e)
		db.close()
		return False
	firstName, lastName, email, usrPwd, role = user['first'], user['last'], user['email'], user['pwd'], "guest"
	insertQuery = """INSERT INTO """+testTable+""" (FirstName, LastName, Email, Password, Role) VALUES ({firstName}, {lastName}, {email}, {password}, {role});""".format(firstName="'"+firstName+"'", lastName="'"+lastName+"'", email="'"+email+"'", password="'"+sha256_crypt.encrypt(usrPwd)+"'", role="'"+role+"'")
	try:
		cursor.execute(insertQuery)
		db.commit()
	except Exception as e:
		print(e)
		db.close()
		return False
	db.close()
	return True

def dropTestDB(testDB):
	db = mysql.connect(host=host, port=port, user=SQLuser, passwd=SQLpassword, db=database)
	cursor = db.cursor()
	dropQuery = "DROP DATABASE "+testDB+";"
	try:
		cursor.execute(dropQuery)
	except Exception as e:
		print(e)
		db.close()
		return False
	db.close()
	return True