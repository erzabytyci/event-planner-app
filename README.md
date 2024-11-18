##Event Management Application

An intuitive application that allows users to effortlessly book reservations for various events. Users can choose from pre-defined offers or manually create reservations, view and manage invoices, and easily modify or delete their bookings as needed.

#Features

-Event Reservations:
 Reserve events manually by specifying custom details.
 Choose from pre-defined offers for quick and easy reservations.
 
-Invoice Management:
 View detailed invoices for reservations.
 Track payment status and pay invoices directly through the system.
 
-Reservation Management:
 View a list of all reservations.
 Modify or delete reservations if plans change.
 
------------------------
Backend: Java
Frontend: JavaFX
Database: PostgreSQL
Build Tool: Maven
Development Environment: IntelliJ IDEA
------------------------

-Setup and Installation
-Prerequisites
Java 19+
Maven 4.0.0+
PostgreSQL
IntelliJ IDEA


Clone the repository:
git clone https://github.com/your-username/event-planner-app.git
cd event-planner-app

Configure the database in application.properties:
spring.datasource.url=jdbc:postgresql://localhost:5432/events
spring.datasource.username=your_username
spring.datasource.password=your_password

Build the project:
mvn clean install

Run the application:
For the backend:
Run Main.java in IntelliJ.

For the JavaFX interface:
Run Main.APP in IntelliJ.

-Usage-

Create Reservations:
Select an offer or enter manual reservation details.

Manage Reservations:
View all reservations.
Modify or delete existing reservations.

View and Pay Invoices:
Access invoices for each reservation.
Track payment status and complete payments.

This project is licensed under the MIT License.
