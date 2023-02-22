from flask import abort, Flask, jsonify, render_template, request, Response, g as app_ctx
from flask_cors import CORS
import mysql.connector
import logging
import ecs_logging
import time
import requests
import os
import json

app = Flask(__name__)
CORS(app)

# or configure to use ELASTIC_APM in your application's settings
from elasticapm.contrib.flask import ElasticAPM
from elasticapm.handlers.logging import Formatter
app.config['ELASTIC_APM'] = {
    # Set the required service name. Allowed characters:
    # a-z, A-Z, 0-9, -, _, and space
    'SERVICE_NAME': 'backend-flask',
    # Use if APM Server requires a secret token
    #'SECRET_TOKEN': 'GV8FJjOlBTMVGQqapM',
    # Set the custom APM Server URL (default: http://localhost:8200)
    #'SERVER_URL': 'https://7982e33051ef4284b11cba764a863ad3.apm.us-central1.gcp.cloud.es.io:443',
    # Set the service environment
    #'ENVIRONMENT': 'production',
    'ENVIRONMENT': 'local-docker-compose',

    #'ENVIRONMENT': 'local-docker-compose',
    #'SECRET_TOKEN': 'this_is_secret_token',
    #'SERVER_URL': 'http://lab3.kihyun.me:8200',

    'SECRET_TOKEN': 'PyG1yrCRxpVujaK7fA',
    # Set the custom APM Server URL (default: http://localhost:8200)
    'SERVER_URL': 'https://8df837c35d00455dbc8e54f34c48dbce.apm.ap-northeast-2.aws.elastic-cloud.com:443',

}
apm = ElasticAPM(app)

#### DB Conf
app.config['MYSQL_HOST'] = os.environ.get('MARIADB_HOST')
app.config['MYSQL_USER'] = os.environ.get('MARIADB_USER')
app.config['MYSQL_PASSWORD'] = os.environ.get('MARIADB_PASS')
app.config['MYSQL_DB'] = os.environ.get('MARIADB_DB')
####

@app.before_request
def before_request():
    app_ctx.start_time = time.perf_counter()
    app_ctx.mysql_connector = mysql.connector.connect(
        host=app.config['MYSQL_HOST'],
        database=app.config['MYSQL_DB'],
        user=app.config['MYSQL_USER'],
        passwd=app.config['MYSQL_PASSWORD']
    )
    app_ctx.cursor = app_ctx.mysql_connector.cursor()
    app.logger.info( f"Before process the request to {request.path}" )

@app.after_request
def after_request(response):
    total_time = time.perf_counter() - app_ctx.start_time
    time_in_ms = int(total_time * 1000)
    app.logger.info( f"{request.path}  -  Elapsed time {time_in_ms}ms" )

    return response

@app.teardown_request
def db_disconnect(exception=None):
    app_ctx.cursor.close()
    app_ctx.mysql_connector.close()


#### Controllers
@app.route('/health', methods=['GET'])
def health():
    return "hello world"

@app.route('/passenger', methods=['POST', 'GET'])
def passenger():
    # POST a passenger to database
    if request.method == 'POST':
        body = request.json
        name = body['name']
        birthdate = body['birthdate']
        nationality = body['nationality']

        app_ctx.cursor.execute(f"INSERT INTO passengers(name, nationality, birthdate) VALUES('{name}', '{nationality}', '{birthdate}')")
        app_ctx.mysql_connector.commit()
        return jsonify({
            'status': 'Passenger data is posted to Mariadb!',
            'name': name,
            'birthdate': birthdate,
            'nationality': nationality
        })

    # GET all passenger from database
    if request.method == 'GET':
        app_ctx.cursor.execute('SELECT * FROM passengers')
        passengers = app_ctx.cursor.fetchall()
        app_ctx.mysql_connector.commit()
        allUsers = []


        for i in range(len(passengers)):
            id = passengers[i][0]
            name = passengers[i][1]
            nationality = passengers[i][2]
            birthdate = passengers[i][3]
            dataDict = {
                "id": id,
                "name": name,
                "birthdate": birthdate,
                "nationality": nationality
            }
            allUsers.append(dataDict)

        return jsonify(allUsers)

@app.route('/passenger/<string:id>', methods=['GET', 'DELETE', 'PUT'])
def passenger_with_id(id):
    # GET a specific passenger by id
    if request.method == 'GET':
        app_ctx.cursor.execute(f"SELECT * FROM passengers WHERE id = {id}")
        passengers = app_ctx.cursor.fetchall()
        app_ctx.mysql_connector.commit()
        passenger = []
        for i in range(len(passengers)):
            id = passengers[i][0]
            name = passengers[i][1]
            nationality = passengers[i][2]
            birthdate = passengers[i][3]
            dataDict = {
                "id": id,
                "name": name,
                "birthdate": birthdate,
                "nationality": nationality
            }
            passenger.append(dataDict)
        return jsonify(passenger[0])

    # DELETE a passenger
    if request.method == 'DELETE':
        app_ctx.cursor.execute(f"DELETE FROM passengers WHERE id = {id}")
        app_ctx.mysql_connector.commit()
        return jsonify({'status': f"Passenger {id} is deleted on Mariadb!"})

    # UPDATE a passenger by id
    if request.method == 'PUT':
        body = request.json
        name = body['name']
        birthdate = body['birthdate']
        nationality = body['nationality']

        app_ctx.cursor.execute(f"UPDATE passengers SET name = '{name}', birthdate = '{birthdate}', nationality = '{nationality}' WHERE id = {id}")
        app_ctx.mysql_connector.commit()
        return jsonify({'status': f"Passenger {id} is updated on Mariadb!"})


@app.route('/airport', methods=['POST', 'GET'])
def airport():
    # POST a passenger to database
    if request.method == 'POST':
        body = request.json
        code = body['code']
        full_name = body['full_name']
        nationality = body['nationality']
        lat = body['lat']
        lon = body['lon']

        app_ctx.cursor.execute(f"INSERT INTO airports (code, full_name, nationality, lat, lon) VALUES ('{code}', '{full_name}', '{nationality}', {lat}, {lon})")
        lastrowid = app_ctx.cursor.lastrowid
        app_ctx.mysql_connector.commit()
        app.logger.info( f"Airport is created! full_name:{full_name}" )
        return jsonify({
            'code': lastrowid,
            'full_name': full_name,
            'nationality': nationality,
            'lat': lat,
            'lon': lon
        })

    # GET all passenger from database
    if request.method == 'GET':
        app.logger.info( f"Getting all airports!" )
        app_ctx.cursor.execute('SELECT * FROM airports')
        airports = app_ctx.cursor.fetchall()
        app_ctx.mysql_connector.commit()
        allAirports = []
        len_airports = len(airports)
        app.logger.info( f"Found total {len_airports} airports!" )
        for i in range(len_airports):
            code = airports[i][0]
            full_name = airports[i][1]
            nationality = airports[i][2]
            lat = airports[i][3]
            lon = airports[i][4]
            dataDict = {
                'code': code,
                'full_name': full_name,
                'nationality': nationality,
                'lat': lat,
                'lon': lon
            }
            allAirports.append(dataDict)

        return jsonify(allAirports)

@app.route('/airport/<string:code>', methods=['GET', 'DELETE', 'PUT'])
def airport_with_id(code):
    # DELETE a airport
    if request.method == 'DELETE':
        app_ctx.cursor.execute(f"DELETE FROM airports WHERE code = '{code}'")
        app_ctx.mysql_connector.commit()
        return jsonify({'status': f"Airport {code} is deleted on Mariadb!"})

    # UPDATE a airport by code
    if request.method == 'PUT':
        body = request.json
        full_name = body['full_name']
        nationality = body['nationality']
        lat = body['lat']
        lon = body['lon']

        app_ctx.cursor.execute(f"UPDATE airports SET full_name = '{full_name}', nationality = '{nationality}', lat = {lat}, lon = {lon} WHERE code = '{code}'")
        app_ctx.mysql_connector.commit()
        return jsonify({'status': f"Airport {code} is updated on Mariadb!"})

    if request.method == 'GET':
        app.logger.info( f"Getting a airport with a code {code}!" )
        app_ctx.cursor.execute(f"SELECT * FROM airports WHERE code = '{code}'")
        airports = app_ctx.cursor.fetchall()
        app_ctx.mysql_connector.commit()
        allAirports = []
        len_airports = len(airports)
        app.logger.info( f"Found total {len_airports} airports!" )
        for i in range(len_airports):
            code = airports[i][0]
            full_name = airports[i][1]
            nationality = airports[i][2]
            lat = airports[i][3]
            lon = airports[i][4]
            dataDict = {
                'code': code,
                'full_name': full_name,
                'nationality': nationality,
                'lat': lat,
                'lon': lon
            }
            allAirports.append(dataDict)

        return jsonify(allAirports)



@app.route('/journey', methods=['POST', 'GET'])
def journey():
    # POST a passenger to database
    if request.method == 'POST':
        body = request.json
        passenger_id = body['passenger_id']
        airport_from = body['airport_from']
        airport_to = body['airport_to']
        start_seconds = body['start_seconds']
        status = 1

        # Check if flight is really there and check remained seat
        flight_url = "http://internal-sample-env-loadbalancer-269490105.ap-northeast-2.elb.amazonaws.com:8080/flight/book"
        flight_data = {"from": airport_from, "to": airport_to, "startSeconds":start_seconds}
        headers = {"Content-Type": "application/json"}
        response = requests.put(url=flight_url, data=json.dumps(flight_data), headers=headers)
        
        if response.text == "OK":
            app_ctx.cursor.execute(f"INSERT INTO journeys(passenger_id, airport_from, airport_to, start_seconds, status) VALUES('{passenger_id}', '{airport_from}', '{airport_to}', '{start_seconds}', '{status}')")
            app_ctx.mysql_connector.commit()
            return jsonify({
                'id':app_ctx.cursor.lastrowid,
                'passenger_id': passenger_id,
                'airport_from': airport_from,
                'airport_to': airport_to,
                'start_seconds': start_seconds,
                'status': status,
                'result':'Succeeded!'
            })
        else :
            app.logger.error( f"Flight booking is failed, journey will not be created! from:{airport_from}, to:{airport_to}" )
            return jsonify({
                'passenger_id': passenger_id,
                'airport_from': airport_from,
                'airport_to': airport_to,
                'start_seconds': start_seconds,
                'status': status,
                'result':'Failed! - Flight booking failed!'
            })

    # GET all passenger from database
    if request.method == 'GET':
        app_ctx.cursor.execute('SELECT * FROM journeys')
        journeys = app_ctx.cursor.fetchall()
        app_ctx.mysql_connector.commit()
        allJourneys = []
        for i in range(len(journeys)):
            id = journeys[i][0]
            passenger_id = journeys[i][1]
            airport_from = journeys[i][2]
            airport_to = journeys[i][3]
            start_seconds = journeys[i][4]
            status = journeys[i][5]
            dataDict = {
                'id': id,
                'passenger_id': passenger_id,
                'airport_from': airport_from,
                'airport_to': airport_to,
                'start_seconds': start_seconds,
                'status': status
            }
            allJourneys.append(dataDict)

        return jsonify(allJourneys)

@app.route('/journey/<string:id>', methods=['GET', 'DELETE', 'PUT'])
def journey_with_id(id):
    # DELETE a journey
    if request.method == 'DELETE':
        app_ctx.cursor.execute(f'SELECT * FROM journeys WHERE id = {id}')
        journeys = app_ctx.cursor.fetchall()

        if len(journeys) != 1 :
            app.logger.error( f"There is no matching journey. input id = {id}" )
            return Response("{'status':'There is no matching journey'}", status=400, mimetype='application/json')
        
        passenger_id = journeys[0][1]
        airport_from = journeys[0][2]
        airport_to = journeys[0][3]
        start_seconds = journeys[0][4]

        app_ctx.cursor.execute(f"DELETE FROM journeys WHERE id = {id}")
        app_ctx.mysql_connector.commit()

        # Check if flight is really there and check remained seat
        flight_url = "http://internal-sample-env-loadbalancer-269490105.ap-northeast-2.elb.amazonaws.com:8080/flight/cancel"
        flight_data = {"from": airport_from, "to": airport_to, "startSeconds":start_seconds}
        headers = {"Content-Type": "application/json"}
        response = requests.put(url=flight_url, data=json.dumps(flight_data), headers=headers)
        
        if response.text == "OK":
            app.logger.info( f"Journey {id} is deleted, and flight is successfully canceled!" )
            return jsonify({'status': f"Journey {id} is deleted!"})
        else:
            app.logger.error( f"Journey {id} is deleted, but flight was not updated! Have a look!" )
            return jsonify({'status': f"Journey {id} is deleted, but flight was not updated! Have a look!"})


    # UPDATE a journey by id
    if request.method == 'PUT':
        body = request.json
        passenger_id = body['passenger_id']
        airport_from = body['airport_from']
        airport_to = body['airport_to']
        start_seconds = body['start_seconds']
        status = body['status']

        app_ctx.cursor.execute(f"UPDATE journeys SET airport_from = '{airport_from}', airport_to = '{airport_to}', start_seconds = '{start_seconds}', status = '{status}' WHERE id = {id}")
        app_ctx.mysql_connector.commit()
        return jsonify({'status': f"Journey {id} is updated on Mariadb!"})

from elasticapm.handlers.logging import LoggingFilter

if __name__ == '__main__' :
    app.run()
else :
    gunicorn_logger = logging.getLogger('gunicorn.error')
    app.logger.handlers = gunicorn_logger.handlers
    app.logger.setLevel(gunicorn_logger.level)


    """
    stream_handler = logging.handlers.RotatingFileHandler(
	"/var/log/flaskapp/flaskapp.ecs.log",  #filename,  ## ~~.log / ~~.txt
        maxBytes=100000000,  ## 한 파일당 몇 몇가 byte까지 만들 수 있는지
        backupCount=5,  ## 몇개의 backup파일을 할지
        delay=False)
    stream_handler.addFilter(LoggingFilter())
    stream_handler.setFormatter(ecs_logging.StdlibFormatter())
    app.logger.addHandler(stream_handler)
    """