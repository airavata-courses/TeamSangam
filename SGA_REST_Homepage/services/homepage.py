from flask import Flask, request, jsonify
import cors
import boto
from flask import session
import datetime
import base64, json


app = Flask(__name__)

filename = "aws_key.properties"
timestampDict = {}
emailDict = {}
accessKey, secretKey = tuple([line.strip().split("=")[1] for line in open(filename, 'r')])
@app.route("/getyears", methods=["GET", "OPTIONS"])
@cors.crossdomain(origin='*')
def getyears():
    # get years from AWS
    s3conn = boto.connect_s3(accessKey, secretKey)
    bucket = s3conn.get_bucket('noaa-nexrad-level2')
    years = list(bucket.list("", '/'))
    return jsonify([year.name.strip("/") for year in years if year.name != "index.html"])


@app.route("/getmonths", methods=["POST", "OPTIONS"])
@cors.crossdomain(origin='*')
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
@cors.crossdomain(origin='*')
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
@cors.crossdomain(origin='*')
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
@cors.crossdomain(origin='*')
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

@app.route("/isActive", methods=["GET", "OPTIONS"])
@cors.crossdomain(origin="*")
def isActive():
    global timestampDict, emailDict
    weatherSess = request.cookies.get("weatherSess", None)
    #print("weatherSess: ", weatherSess)
    if weatherSess:
        # decrypt it
        weatherSess = base64.urlsafe_b64decode(weatherSess + "===")[:49].decode("utf-8")
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
    timestampDict[request.json("sid")] = datetime.datetime.now()
    # storing sid:email of every user who logs in
    emailDict[request.json("sid")] = request.json("email")
    return jsonify("OK")

@app.route("/res", methods=["POST", "OPTIONS"])
@cors.crossdomain(origin="*")
def res():
    return jsonify([1,'abc@gmail.com'])




if __name__ == "__main__":
    app.run(host= "0.0.0.0",port=5001,debug=True)
