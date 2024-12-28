**Overview**
The Travel Heatmap project is a web application designed to visualize popular travel destinations. Users can contribute data about places they’ve visited, and the application aggregates this information to generate a heatmap of travel activity.
Technologies Used
**Frontend:** React with TypeScript
**Backend:** Spring Boot
**Database:** MongoDB

**Features**
**Frontend**
Interactive heatmap visualization.
User-friendly interface to submit location data.
Integration with APIs for fetching and displaying map data.

**Backend**
RESTful API for handling user data and heatmap data.
Data storage and retrieval using MongoDB.

**Prerequisites**
Java 17 or later
Maven
MongoDB (running locally or hosted)

**Installation**
**Clone the repository:**
git clone [<backend-repo-url>](https://github.com/lavanyasoundar17/Heatmap-BE.git)
cd Heatmap-BE
**Configure MongoDB:**
Update the application.properties file with your MongoDB connection details:
spring.data.mongodb.uri=mongodb://<username>:<password>@<host>:<port>/<database> server.port=8080
**Build the application:**
mvn clean install
Run the Application

**License**
This project is licensed under the MIT License. See the LICENSE file for details



