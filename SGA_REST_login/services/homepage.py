from flask import Flask, request, jsonify
import base64
import cors

app = Flask(__name__)

# defining service for login page.
@app.route("/putSession", methods=["POST", "OPTIONS"])
# Need to replace * with a single domain from which the requests are expected.
@cors.crossdomain(origin='*')
def putSession():
	global activeUsers
	print(request.data)
	sid = request.json["sid"]
	email = request.json["email"]
	activeUsers[sid] = email
	print("from login service: ",sid, email)
	print("No data in request.json.")
	print("My cookie data: ", request.cookies)

	return jsonify(1)


# defining service for login page.
@app.route("/isActive", methods=["POST", "OPTIONS"])
# Need to replace * with a single domain from which the requests are expected.
@cors.crossdomain(origin='*')
def isActive():
	global activeUsers
	weatherSess = request.cookies.get("weatherSess", None)
	print("weatherSess: ", weatherSess)
	if weatherSess:
		# decrypt it
		weatherSess = base64.urlsafe_b64decode(weatherSess+"===")[:49].decode("utf-8")
		print("weatherSess: ", weatherSess)
		SGAsid = eval(weatherSess).get("SGAsid", None)
		print("SGAsid: ", SGAsid)
		if SGAsid:
			if SGAsid in activeUsers.keys():
				# if the timestamp is less than 30 mins old, go ahead.
				# find the sid in sid: email dict. If found, the user is logged in.
				# the user is logged in, update the timestamp
				# in any other case, delete the sid entries form both dictionaries and
				# redirect the user to login(can be done in angular)
				return jsonify("You are logged in")
			else:
				return jsonify("You are not logged in")
		else:
			return jsonify("You are not logged in")
	else:
		return jsonify("You are not logged in")

if __name__ == "__main__":
	activeUsers = {}
	# ------------- Any code above this line ---------------#
	app.run(host="0.0.0.0", port=5001, debug=True)