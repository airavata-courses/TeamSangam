#Team Sangam Milestone1 Manual


This is the main branch of the repo for milestone 1. Please follow the below instructions for deploying this on an EC2 instance and running the application.

The .travis.yml has the CI scripts which makes the build process automated and then triggers the code deploy agent on EC2, the configuration for which is specified in the appspec.yml. Making a commit to this branch triggers the build and deployment processes.

Despite that, some of the steps need to be manually executed while setting up the instance for the FIRST TIME. These include setting up the MySQL database and the Tomcat server. Manual execution has been chosen for these steps because of the fact that they are one time executions and any  subsequent deployments do not have changes to these components once installed and setup.

##Setup MySQL on EC2 instance when starting for the first time:

######Install MySQL 5.5:

`sudo yum install mysql55-server`

######Start the server:

`sudo service mysqld start`

######Set a password for root account:

`sudo mysqladmin –u root password <password>`

The username ‘root’ and its corresponding password must be set as Travis environment variables with names MYSQL_USER and MYSQL_PWD respectively. They will be passed to the EC2 instance by an automated script.

######Login to mysql shell
`sudo mysql –u root -p`
Enter the password.
######Create a database with the name ‘weatherApp’:

`create database weatherApp;`

######Switch to the created database:

	use weatherApp;

######Create a table called ‘users’ as follows:

	CREATE TABLE `weatherApp`.`users` (
  	`Id` INT NOT NULL AUTO_INCREMENT,
		`FirstName` VARCHAR(45) NOT NULL,
		`LastName` VARCHAR(45) NOT NULL,
		`Email` VARCHAR(255) NOT NULL,
		`Password` VARCHAR(255) NOT NULL,
		`Role` VARCHAR(10) NOT NULL,
		PRIMARY KEY (`Id`),
		UNIQUE INDEX `Id_UNIQUE` (`Id` ASC));

Note: Copying the create table query from here might cause some syntax errors. This is because, the single quotes in a text document are not the same as the single quotes in MySQL command line interface. So, the single quotes must be manually typed.



##Setup Apache Tomcat server on EC2 instance when starting for the first time:

######Download and extract Tomcat:

	wget http://www-us.apache.org/dist/tomcat/tomcat-8/v8.0.37/bin/apache-tomcat-8.0.37.tar.gz

	tar xvzf apache-tomcat-8.0.37.tar.gz

	sudo mv apache-tomcat-8.0.37 /opt/



Tomcat runs on port 8080. The port has to be opened for all IP inbound traffic so that anyone can send in a request to the Application.
  
To do that login to console.aws.amazon.com
Go to Services -> EC2 -> Instances -> Select the server where you installed Tomcat
  
Click on group present in Security Groups:
Go to Inbound Tab
Click Edit

Add the below rule, now the application will be accessible through internet.

| Type        | Protocol        | Port Range  | Source  | 
| :-----------:|:---------------:| :-----------:|:--------:|
| Custom TCP Rule | TCP | 8080 | 0.0.0.0/0 |
| Custom TCP Rule | TCP | 5000 | 0.0.0.0/0 |
| Custom TCP Rule | TCP | 5001 | 0.0.0.0/0 |


  
  
######Adding Manager Role to Tomcat server:

Edit /opt/apache-tomcat-8.0.37/conf/tomcat-users.xml
Add the below two lines in between <tomcat-users> tag
  
`<role rolename="manager-gui"/>`
`<user username="admin" password="admin" roles="manager-gui"/>`


######Changing maximum accepted file size:
Edit /opt/apache-tomcat-8.0.37/conf/server.xml
Modify the connector tag as below:

```
<Connector port="8080" protocol="HTTP/1.1"
              connectionTimeout="20000"
              redirectPort="8443"
              maxPostSize="67589953" />
```

This is just specifying the maximum accepted file size for Tomcat. By default it is just 2 MB. The above change sets it to 65MB.

##Steps to run the application:
The application can be accessed at the public dns for the instance.

So to login to the service, just go to:

`http://<public ip>:8080/login.html`

Create an account if you do not have one.
Once logged in or signed up, you will be redirected to the homepage, where you can select the different parameters to see the locations of forecasted storms on a Google map at the bottom of the page.
