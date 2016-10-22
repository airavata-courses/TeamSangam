import os
import json
from nose.tools import assert_equal, with_setup
from mock import patch, MagicMock
from requests.models import Response
import login, model

testDB = "testDB"
testTable = "testUsers"
model.host = "localhost"
model.user = "root"
model.password = ""
model.database = testDB
model.table = testTable
client = login.app.test_client()
# Need this for validating the session
login.app.config['SECRET_KEY'] = os.urandom(24)
login.app.config['TESTING'] = True

def setUp():
	user = {
		"first":"John",
		"last":"Doe",
		"email":"johndoe@example.com",
		"pwd":"johndoepass"
	}
	model.setUpTestDB(testDB, testTable, user)

def tearDown():
	model.dropTestDB(testDB)

@patch('requests.post')
def test_setupsession(mock_api_call):
	with login.app.test_request_context():
		mock_api_call.return_value = MagicMock(status_code=200, response=json.dumps(1))
		login.setupSession("johndoe@example.com")

@patch('requests.post')
@with_setup(setUp(), tearDown())
def test_validLogin(mock_api_call):
	with login.app.test_request_context():
		mock_api_call.return_value = MagicMock(status_code=200, response=json.dumps(1))
		resp = client.post('/login', data=json.dumps({"email":"johndoe@example.com", "password":"johndoepass"}), content_type='application/json')
		assert_equal(resp.status_code, 200)
		assert_equal(resp.data, bytes('11\n', encoding='UTF-8'))

@patch('requests.post')
@with_setup(setUp(), tearDown())
def test_invalidLogin(mock_api_call):
	with login.app.test_request_context():
		mock_api_call.return_value = MagicMock(status_code=200, response=json.dumps(1))
		resp = client.post('/login', data=json.dumps({"email":"johnbutnotdoe@example.com", "password":"johndoepass"}), content_type='application/json')
		assert_equal(resp.status_code, 200)
		assert_equal(resp.data, bytes('99\n', encoding='UTF-8'))

@patch('requests.get')
def test_gauthSuccess(mock_api_call):
	with login.app.test_request_context():
		r = Response()
		r.status_code = 200
		r._content = bytes("HTML code", encoding="UTF-8")
		mock_api_call.return_value = r
		resp = client.get('/gAuth')
		assert_equal(resp.status_code, 200)
		assert_equal(resp.data, bytes("HTML code", encoding="UTF-8"))

@patch('requests.get')
def test_gauthFailure(mock_api_call):
	with login.app.test_request_context():
		r = Response()
		r.status_code = 201
		r._content = bytes("HTML code", encoding="UTF-8")
		mock_api_call.return_value = r
		resp = client.get('/gAuth')
		assert_equal(resp.status_code, 200)
		assert_equal(resp.data, bytes("-1\n", encoding="UTF-8"))

@patch('requests.post')
@with_setup(setUp(), tearDown())
def test_validSignup(mock_api_call):
	# New user tries to signup
	with login.app.test_request_context():
		mock_api_call.return_value = MagicMock(status_code=200, response=json.dumps(1))
		resp = client.post('/signup', data=json.dumps({"firstName":"John", "lastName":"Anotherdoe", "email":"johnanotherdoe@example.com", "password":"johndoepass"}), content_type='application/json')
		model.cursor.execute("SELECT * FROM "+testDB+"."+testTable+" WHERE Email='johnanotherdoe@example.com';")
		assert_equal(model.cursor.rowcount, 1)
		assert_equal(resp.status_code, 200)
		assert_equal(resp.data, bytes('888\n', encoding='UTF-8'))

@patch('requests.post')
@with_setup(setUp(), tearDown())
def test_invalidSignup(mock_api_call):
	# Existing user tries to signup
	with login.app.test_request_context():
		mock_api_call.return_value = MagicMock(status_code=200, response=json.dumps(1))
		resp = client.post('/signup', data=json.dumps({"firstName":"John", "lastName":"Doe", "email":"johndoe@example.com", "password":"johndoepass"}), content_type='application/json')
		assert_equal(resp.status_code, 200)
		assert_equal(resp.data, bytes('777\n', encoding='UTF-8'))