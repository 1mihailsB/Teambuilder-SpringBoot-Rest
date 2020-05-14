# Teamplanner
## Practice Spring Boot project
  
### Don't forget to edit application.properties when trying to run this project and register your appliaction in Google's developer's console to configure Oauth2 and add google.client.** properties in properties file. Here's the link to Google dev console: https://console.developers.google.com/apis/dashboard. Also create a database and run the script from this repository to create the table necessary to run the code at this point.

Rest API to be consumed by front end app. Website project for practice.
Will include Google Authentication: on the front end - retrieval of Authorization Code,
on back end - verification of this Authorization Code using Google's https://oauth2.googleapis.com/token endpoint and recording the user to the database.
