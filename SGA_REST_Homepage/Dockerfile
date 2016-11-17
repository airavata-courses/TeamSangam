FROM python:3.4
MAINTAINER Vikas Palakurthi "vikas1590@gmail.com"

ADD requirements.txt /tmp/requirements.txt

RUN pip install -r /tmp/requirements.txt
RUN apt-get install -y gcc

ADD ./services /app
WORKDIR /app
CMD ["gunicorn", "--bind", "0.0.0.0:5001", "--backlog", "2048", "--timeout", "30", "--log-file", "homeLogs", "--access-logfile", "homeAccessLogs", "homepage:app"]
