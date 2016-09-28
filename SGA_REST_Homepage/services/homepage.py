from flask import Flask, request, jsonify
import cors
import boto

app = Flask(__name__)

filename = "aws_key.properties"
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

@app.route("/res", methods=["GET", "OPTIONS"])
@cors.crossdomain(origin="*")
def res():
    message = {"kml": {
   "Document": {"Placemark": [
       {
           "open": 1,
           "name": "KTLX",
           "Point": {"coordinates": "36.48186157447076,-90.60346116707984"}
       },
       {
           "open": 1,
           "name": "KTLX",
           "Point": {"coordinates": "36.518427376289324,-75.73092432002373"}
       },
       {
           "open": 1,
           "name": "KTLX",
           "Point": {"coordinates": "44.44810845373752,-85.63620104851064"}
       },
       {
           "open": 1,
           "name": "KTLX",
           "Point": {"coordinates": "37.24269712782977,-103.17095591688364"}
       },
       {
           "open": 1,
           "name": "KTLX",
           "Point": {"coordinates": "34.584760871874515,-84.40361656077127"}
       },
       {
           "open": 1,
           "name": "KTLX",
           "Point": {"coordinates": "30.244600900033777,-95.29411699759761"}
       },
       {
           "open": 1,
           "name": "KTLX",
           "Point": {"coordinates": "30.39844419457594,-90.22835014297976"}
       },
       {
           "open": 1,
           "name": "KTLX",
           "Point": {"coordinates": "46.34654961131859,-77.94429299634923"}
       },
       {
           "open": 1,
           "name": "KTLX",
           "Point": {"coordinates": "47.98634371865219,-95.3499010218505"}
       },
       {
           "open": 1,
           "name": "KTLX",
           "Point": {"coordinates": "42.83619740565784,-109.53316616549208"}
       }
   ]},
   "xmlns:xal": "urn:oasis:names:tc:ciq:xsdschema:xAL:2.0",
   "xmlns:gx": "http://www.google.com/kml/ext/2.2",
   "xmlns": "http://www.opengis.net/kml/2.2",
   "xmlns:atom": "http://www.w3.org/2005/Atom"
}}
    return jsonify(message)

if __name__ == "__main__":
    app.run(host= "0.0.0.0",port=5001,debug=True)
