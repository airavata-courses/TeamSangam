import os, json
import login
from nose.tools import assert_equal, with_setup
from mock import patch, MagicMock
import model

testDB = "testDB"
testTable = "testUsers"
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

@with_setup(setUp(), tearDown())
def test_invalidLogin():
	resp = client.post('/login', data=json.dumps({"email":"johnbutnotdoe@example.com", "password":"johndoepass"}), content_type='application/json')
	assert_equal(resp.status_code, 200)
	assert_equal(resp.data, bytes('99\n', encoding='UTF-8'))