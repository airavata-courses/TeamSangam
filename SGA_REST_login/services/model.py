import MySQLdb as mysql
from passlib.handlers.sha2_crypt import sha256_crypt

filename = "mysql.properties"
user, password = tuple([line.strip().split("=")[1] for line in open(filename, 'r')])
host = "localhost"
port = 3306
database = "weatherApp"

db = mysql.connect(host=host, port=port, user=user, passwd=password, db=database)
cursor = db.cursor()
def insertUser(firstName, lastName, email, password, role="guest"):
# Adds new user details to the table. By default, users are guests.
	insertQuery = """
	INSERT INTO """+database+""".users (FirstName, LastName, Email, Password, Role)
	VALUES
	({firstName}, {lastName}, {email}, {password}, {role})
	;
	""".format(firstName="'"+firstName+"'", lastName="'"+lastName+"'", email="'"+email+"'", password="'"+sha256_crypt.encrypt(password)+"'", role="'"+role+"'")
	try:
		cursor.execute(insertQuery)
		db.commit()
	except Exception as e:
		print(e)
		return False
	else:
		return True

def findUser(email, password=""):
# check if email is in the table.
# if in the table, find the corresponding password
	findQuery = "SELECT Password FROM "+database+".users WHERE Email='"+email+"';"
	try:
		cursor.execute(findQuery)
	except Exception as e:
		print(e)
		return False
	else:
		dbPwd = cursor.fetchone()
		if not dbPwd:
			#user non existent
			return 99
		else:
			if sha256_crypt.verify(password, dbPwd[0]):
				# user exists and correct password
				return 11
			else:
				# user exists, but password does not match
				return 19
