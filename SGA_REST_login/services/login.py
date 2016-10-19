import os, uuid, requests, hashlib, base64, json, urllib.request
from flask import Flask, request, jsonify, make_response
from flask import session
import cors
import model

app = Flask(__name__)

discoveryDocument = json.loads(urllib.request.urlopen("https://accounts.google.com/.well-known/openid-configuration").read().decode())

filename = "googleOAuth.properties"
CLIENT_ID, CLIENT_SECRET, APPLICATION_NAME = tuple([line.strip().split("=")[1] for line in open(filename, 'r')])
REDIRECT_URI = "http://54.71.95.40:5000/requestToken"
HOMEPAGE_PUT_SESSION = "http://54.71.95.40:5001/putSession"

def setupSession(email):
	# Generating a unique session ID
	sid = str(uuid.uuid1())
	# The session will expire if any code changes are made, or after the set time.
	session['SGAsid'] = sid
	# send the session data to the homepage service
	sessionData = {"sid": sid, "email": email}
	requests.post(HOMEPAGE_PUT_SESSION, json=sessionData)

# create an anti-forgery state token. Needed security measure for Google OAuth2.
# is invoked when the login page is loaded.
@app.route("/createState", methods=["GET", "OPTIONS"])
# Need to replace * with a single domain from which the requests are expected.
@cors.crossdomain(origin='*')
def createState():
	state = hashlib.sha256(os.urandom(1024)).hexdigest()
	session["state"] = state
	# return jsonify("created state token")
	return jsonify(1)

# defining service for login page.
@app.route("/login", methods=["POST", "OPTIONS"])
# Need to replace * with a single domain from which the requests are expected.
@cors.crossdomain(origin='*')
def login():
	# request.json has all the fields received from the client in a JSON format
	email = request.json["email"]
	password = request.json["password"]
	result = model.findUser(email=email, password=password)
	if result == 11:
		# Login successful, so create a session for the user.
		setupSession(email)
	return jsonify(result)

# Endpoint for google oauth request. Called when the user clicks login using Google.
@app.route("/gAuth", methods=["GET", "OPTIONS"])
# Need to replace * with a single domain from which the requests are expected.
@cors.crossdomain(origin='*')
def gAuth():
	authorization_endpoint = discoveryDocument["authorization_endpoint"]
	client_id = CLIENT_ID
	response_type = "code"
	scope = "openid email"
	redirect_uri = REDIRECT_URI
	state = session["state"]
	# Without this, the user will be prompted for consent every time he/she logs in.
	approval_prompt = "auto"
	params = {"approval_prompt": approval_prompt, "client_id": client_id, "response_type": response_type, "scope": scope, "redirect_uri": redirect_uri, "state": state}
	r = requests.get(authorization_endpoint, params=params)
	if r.status_code == 200:
		# Need to render this using angular, coz this response will be sent to Angular.(popup).
		print("making response")
		s = make_response(r.text)
		print("response made")
		return s
	else:
		# There was some error in authentication. Need to display an error message(try normal signup).
		return jsonify(-1)

# The redirect URI endpoint for Google OAuth.
@app.route("/requestToken", methods=["GET", "OPTIONS"])
# Need to replace * with a single domain from which the requests are expected.
@cors.crossdomain(origin='*')
def requestToken():
	if request.args.get("state", None) != session["state"]:
		# Invalid state parameter
		return jsonify("Invalid state parameter.")
	code = request.args.get("code", None)
	if code:
		# print("Auth code received.")
		# send a post request to google to get the id_token(will redirect to another view).
		# Need to add that view in google api console.
		client_id = CLIENT_ID
		client_secret = CLIENT_SECRET
		redirect_uri = REDIRECT_URI
		grant_type = "authorization_code"
		token_endpoint = discoveryDocument["token_endpoint"]
		params = {"code": code, "client_id": client_id, "client_secret": client_secret, "redirect_uri": redirect_uri, "grant_type": grant_type}
		r = requests.post(token_endpoint, data=params)
		jwt = eval(r.text)
		id_token = jwt.get("id_token", None)
		if id_token:
			# The id token is base64 encrypted.
			# The second segment of the id token is the payload that has the user details.
			# Padding of 3 characters is added as the length of the string being decoded
			# should be a multiple of 4.
			data = base64.urlsafe_b64decode(id_token.split('.')[1]+"===").decode('utf-8')
			email = json.loads(data)["email"]
			# Google authentication successful
			setupSession(email)
			return jsonify(1)
		else:
			# Authentication failed
			return jsonify(-1)
	else:
		# authentication failed. The user should see a message.
		# return make_response("Authentication failed.")
		return jsonify(-1)

# defining service for the signup of new users.
@app.route("/signup", methods=["POST", "OPTIONS"])
# Need to replace * with a single domain from which the requests are expected.
@cors.crossdomain(origin='*')
def signup():
	firstName = request.json["firstName"]
	lastName = request.json["lastName"]
	email = request.json["email"]
	password = request.json["password"]
	result = model.findUser(email=email, password=password)
	if result:
		if result == 99:
			if(model.insertUser(firstName=firstName, lastName=lastName, email=email, password=password)):
				# need to start a session here, coz we are logging the user in.
				setupSession(email)
				return jsonify(888)
			else:
				return jsonify(666)
		else:
			return jsonify(777)
	else:
		return jsonify(666)

if __name__ == "__main__":
	app.config['SESSION_COOKIE_NAME'] = 'weatherSess'
	app.config['SECRET_KEY'] = os.urandom(24)
	# app.config['SERVER_NAME'] = 'dev.localhost.com:5000'
	# app.config['SESSION_COOKIE_DOMAIN'] = '.dev.localhost.com'
	# app.config['SESSION_COOKIE_PATH'] =
	# app.config['SESSION_COOKIE_HTTPONLY'] = False
	# Sessions will timeout(on server) after 30 mins of inactivity on server. DOes not affect the cookies.
	app.config['PERMANENT_SESSION_LIFETIME'] = 1800
	# maintain a session dictionary mapping session ID to user email address
	# activeSessions = {}
	# ------------- Any code above this line ---------------#
	# app.run(host="127.0.0.2", debug=True)
	app.run(host="0.0.0.0",debug=True)