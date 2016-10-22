FROM tomcat

COPY target/SGA_REST_Forecast.war  /usr/local/tomcat/webapps/SGA_REST_Forecast.war

COPY target/SGA_REST_ForecastDecision.war  /usr/local/tomcat/webapps/SGA_REST_ForecastDecision.war

EXPOSE 8080

CMD ["catalina.sh", "run"]

