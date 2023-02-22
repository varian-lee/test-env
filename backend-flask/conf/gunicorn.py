#bind = "unix:/tmp/gunicorn.sock"
bind = "0.0.0.0:8080"
workers = 5

loglevel = "debug"

accesslog = "/var/log/flaskapp/gunicorn_access.log"
errorlog = "/var/log/flaskapp/gunicorn_error.log"
