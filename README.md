# GameOn
Repository for group project of Distributed Systems and Middleware Technologies

Project Structure
-----
All information relating to the structure of the project can be found in its official documentation, while as regards the code it is possible to see it in the appropriate folders.
In the GameOn folder you will find the javaEE project that has be deployed on TomCat, while in web_server_erlang you will find the code to implement the cowboy web server, plus the code for other system processes.

Execute
-----
First you have to load the GameOn_war folder on the TomCat web server (in the webapps folder), then you can start TomCat and the CowBoy web server.
Then, to access the web app, type the following address on your browser: http://localhost:8080/GameOn_war/
Note: if you want change the config.xml file, you need to change it in GameOn_war/WEB-INF/classes/config.xml

To start the application on windows you can use the startGameOn.bat file, first changing the path to those of your system. 
Similar commands can be used on Unix systems, but there is no ready-made execution script.

