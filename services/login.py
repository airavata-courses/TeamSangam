from flask import Flask, request, jsonify
from flask import cors
import model
import boto

app = Flask(__name__)

# defining view for login page.
@app.route("/login", methods=["POST", "OPTIONS"])
# Need to replace * with a single domain from which the requests are expected.
@cors.crossdomain(origin='*')
def login():
	# request.json has all the fields received from the client in a JSON format
	email = request.json["email"]
	password = request.json["password"]
	return jsonify(model.findUser(email=email, password=password))

# defining view for the signup of new users.
@app.route("/signup", methods=["POST", "OPTIONS"])
# Need to replace * with a single domain from which the requests are expected.
@cors.crossdomain(origin='*')
def signup():
	firstName = request.json["firstName"]
	lastName = request.json["lastName"]
	email = request.json["email"]
	password = request.json["password"]
	if model.findUser(email=email, password=password) == 99:
		model.insertUser(firstName=firstName, lastName=lastName, email=email, password=password)
		return jsonify(888)
	else:
		return jsonify(777)

# error handler
# 500 code - internal server error

if __name__ == "__main__":
	app.run(debug=True)