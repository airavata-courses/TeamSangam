FROM python:3.4
MAINTAINER Vikas Palakurthi "vikas1590@gmail.com"
ADD requirements.txt /tmp/requirements.txt
RUN pip install -r /tmp/requirements.txt
RUN apt-get install -y gcc

ADD ./services /app
WORKDIR /app
CMD ["python","login.py"]
