from flask import Flask, request, jsonify
import cors
import boto
from flask import session
import datetime
import base64, json, zlib


app = Flask(__name__)

PUBLIC_IP = "http://ec2-54-183-233-167.us-west-1.compute.amazonaws.com"
CROSS_DOMAIN = "http://ec2-54-183-233-167.us-west-1.compute.amazonaws.com:8080"

filename = "aws_key.properties"

timestampDict = {}
emailDict = {}
accessKey, secretKey = tuple([line.strip().split("=")[1] for line in open(filename, 'r')])
@app.route("/getyears", methods=["GET", "OPTIONS"])
@cors.crossdomain(origin= CROSS_DOMAIN)
def getyears():
    # get years from AWS
    s3conn = boto.connect_s3(accessKey, secretKey)
    bucket = s3conn.get_bucket('noaa-nexrad-level2')
    years = list(bucket.list("", '/'))
    return jsonify([year.name.strip("/") for year in years if year.name != "index.html"])


@app.route("/getmonths", methods=["POST", "OPTIONS"])
@cors.crossdomain(origin= CROSS_DOMAIN)
def getmonths():
    # get months from AWS
    s3conn = boto.connect_s3(accessKey, secretKey)
    bucket = s3conn.get_bucket('noaa-nexrad-level2')
    year = str(request.json["year"])
    print("year", year)
    months = list(bucket.list(year + "/", "/"))
    # print(months)
    return jsonify([month.name.strip("/").split("/")[1] for month in months if month.name.strip("/") != year])


@app.route("/getdays", methods=["POST", "OPTIONS"])
@cors.crossdomain(origin= CROSS_DOMAIN)
def getdays():
    # get days from AWS
    s3conn = boto.connect_s3(accessKey, secretKey)
    bucket = s3conn.get_bucket('noaa-nexrad-level2')
    year = str(request.json["year"])
    month = str(request.json["month"])
    print("month", month)
    days = list(bucket.list(year + "/" + month + "/", "/"))
    return jsonify([day.name.strip("/").split("/")[2] for day in days if day.name != year + "/" + month + "/"])


@app.route("/getlocations", methods=["POST", "OPTIONS"])
@cors.crossdomain(origin= CROSS_DOMAIN)
def getlocations():
    # get locations from AWS
    s3conn = boto.connect_s3(accessKey, secretKey)
    bucket = s3conn.get_bucket('noaa-nexrad-level2')
    year = str(request.json["year"])
    print("year", year)
    month = str(request.json["month"])
    print("month", month)
    day = str(request.json["day"])
    print("day", day)
    locations = list(bucket.list(year + "/" + month + "/" + day + "/", "/"))

    # need to check whether the list is empty. If empty send an error message stating there are no locations for specified date
    if not locations:
        return jsonify('no locations found')
    else:
        return jsonify([location.name.strip("/").split("/")[3] for location in locations if location.name != year + "/" + month + "/" + day + "/" ])


@app.route("/getfiles", methods=["POST", "OPTIONS"])
@cors.crossdomain(origin= CROSS_DOMAIN)
def gettimestamp():
    # get time stamped file names from AWS
    s3conn = boto.connect_s3(accessKey, secretKey)
    bucket = s3conn.get_bucket('noaa-nexrad-level2')
    year = str(request.json["year"])
    month = str(request.json["month"])
    day = str(request.json["day"])
    location = str(request.json["location"])
    print("location", location)
    files = list(bucket.list(year + "/" + month + "/" + day + "/" + location + "/", '/'))
    return jsonify([file.name.strip("/").split("/")[4].strip(".gz") for file in files if file.name.endswith('.gz')])

def decode_base64(data):
    compressed = False
    payload = data
    if payload.startswith('.'):
        compressed = True
        payload = payload[1:]
    data = payload.split('.')[0]
    missing_padding = len(data) % 4
    if missing_padding != 0:
        data += '='* (4 - missing_padding)
    data = base64.urlsafe_b64decode(data)
    if compressed:
        data = zlib.decompress(data)
    return data

@app.route("/isActive", methods=["GET", "OPTIONS"])
@cors.crossdomain(origin= CROSS_DOMAIN)
def isActive():
    global timestampDict, emailDict
    weatherSess = request.cookies.get("weatherSess", None)
    #print("weatherSess: ", weatherSess)
    if weatherSess:
        # decrypt it
        weatherSess = decode_base64(weatherSess).decode("utf-8")
        SGAsid = eval(weatherSess).get("SGAsid", None)
        print("SGAsid: ", SGAsid)
        if SGAsid:
            if SGAsid in timestampDict.keys():
                # if the timestamp is less than 30 mins old, go ahead.
                if ((datetime.datetime.now()- timestampDict[SGAsid]).seconds <= 1800):
                    if(SGAsid in emailDict.keys()):
                        timestampDict[SGAsid] = datetime.datetime.now()
                        return jsonify([SGAsid,emailDict[SGAsid]])
                    else:
                        return jsonify("-1")
                else:
                    del timestampDict[SGAsid]
                    del emailDict[SGAsid]
                    return jsonify("-1")
            else:
                return jsonify("-1")

        else:
            return jsonify("-1")
    else:
        return jsonify("-1")

@app.route("/putSession", methods=["POST", "OPTIONS"])
@cors.crossdomain(origin="*")
def putSession():
    global timestampDict, emailDict
    # storing sid:timestamp every time a new user logs im
    timestampDict[request.json["sid"]] = datetime.datetime.now()
    # storing sid:email of every user who logs in
    emailDict[request.json["sid"]] = request.json["email"]
    return jsonify("OK")

@app.route("/logout", methods=["POST", "OPTIONS"])
@cors.crossdomain(origin=CROSS_DOMAIN)
def logout():
    global timestampDict, emailDict
    weatherSess = request.cookies.get("weatherSess", None)
    if weatherSess:
        weatherSess = decode_base64(weatherSess).decode("utf-8")
        SGAsid = eval(weatherSess).get("SGAsid", None)
        if SGAsid:
            if SGAsid in timestampDict.keys():
                del timestampDict[SGAsid]
            if SGAsid in emailDict.keys():
                del emailDict[SGAsid]
    return jsonify(1)
#if __name__ == "__main__":
#app.run(host='0.0.0.0',port=5001,debug=True)
