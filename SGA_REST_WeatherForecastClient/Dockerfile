FROM tomcat

COPY target/SGA_REST_WeatherForecastClient.war /usr/local/tomcat/webapps/SGA_REST_WeatherForecastClient.war

ADD ./scripts /usr/local/tomcat/webapps/ROOT/scripts
ADD ./stylesheets /usr/local/tomcat/webapps/ROOT/stylesheets
ADD ./templates /usr/local/tomcat/webapps/ROOT/templates
ADD ./homeTemplates /usr/local/tomcat/webapps/ROOT/homeTemplates

COPY index.html /usr/local/tomcat/webapps/ROOT/
COPY login.html /usr/local/tomcat/webapps/ROOT/
COPY admin.html /usr/local/tomcat/webapps/ROOT/
COPY signup.html /usr/local/tomcat/webapps/ROOT/
COPY loginError.html /usr/local/tomcat/webapps/ROOT/

EXPOSE 8080

CMD ["catalina.sh", "run"]

