# Course Registration Management API

As part of a web-based course registration app, this api allows for students and faculty to perform basic CRUD (Create, Read, Update, Delete) operations on courses that are available for registration.

## Setup: 

This api connects to a MongoDB database with a series of collections inside of it.

### MongoDB:

Once your database is created, there are 3 collections it needs inside of it: student, faculty, and course.
There are .json files with sample data for each collection in src/main/resources/

#### student:

student documents in MongoDB should look like this:
> {
>   "firstName": "Tony",
>   "lastName": "Stark",
>   "email": "tony.stark@starkindustries.com",
>   "password": "iAmIronman",
> }

#### faculty:

faculty documents in MongoDB should look like this:
> {
>   "firstName": "Ruby",
>   "lastName": "Gold",
>   "email": "ruby.gold@teacher.edu",
>   "password": "rubyWazHere",
> }

#### course:

course documents in mongoDB chould loook like this:
> {
>   "number": "eng 102",
>   "name": "college research paper",
>   "description": "required liberal arts course for all undergraduate students",
>   "professor": "ruby.gold@teacher.edu",
>   "capacity": 25,
>   "students": [
>     "emerald@gem.com",
>     "saphire@gem.com",
>     "diamon@gem.com"
>   ]
> }

### app.properties:

The api depends on a certain file in order to know where to access MongoDB and to set certain values for authorization tokens.
On your local machine, make a file called app.properties inside src/main/resources/. Include in it the following:
(note that "jwt.prefix=Bearer " has a trailing space)

---

ipAddress=yourIpAddressHere<br>
port=27017<br>
dbName=yourDbName<br>
username=yourMongoUsername<br>
password=yourMongoPassword<br>

jwt.header=Authorization<br>
jwt.prefix=Bearer <br>
jwt.secret=yourThingHere<br>
jwt.expiration=86400000

---

## Using the API

Below is described each endpoint of the API, what HTTP request methods are allowed on it, and what their request/response bodies will look like.

Unless otherwise stated, each method requires an authorization token provided in a header called "Authorization".

### /authstudent

#### POST

the POST request on /authstudent authenticates a student. in the request body, provide JSON with email: and password: fields.
The response body has JSON with an ID, email, and role (student) on successful login.

This method does **not** require an authorization token.

### /authfaculty

#### POST

the POST request on /authfaculty authenticates a faculty. in the request body, provide JSON with email: and password: fields.
The response body has JSON with an ID, email, and role (faculty) on successful login.

This method does **not** require an authorization token.

### /health

#### GET

the /health endpoint is pinged frequently by elastic beanstalk, where we deployed the API, to check its status. Is sends back a simple 200: ok

This method does **not** require an authorization token.

### /student

#### GET

the GET method on /student returns a student's information. The response is an array of objects. The first (index 0) object includes the first name, last name, and email of the requesting student. The other elements in the array are courses, which have number, name, description, professor, and students[] fields.

#### POST

the POST method on /student expects a request body of JSON with firstName, lastName, email, and password fields. it will respond in JSON with the newly registered user's ID, email, and role (student).

This method does **not** require an authorization token.

### /faculty

#### GET

the GET method on /faculty responds with the ID, email, and role (faculty) of the authorized user.

### /registration

#### GET

the GET method on /registration responds with a list of all courses in database (that is, all courses available for registration).

#### PUT

the PUT method on /registration registers and unregisters a student from a course. The request body, in JSON, should include the following fields:
- action: (either "register" or "unregister")
- courseNumber: (the number (i.e. "math 100") the student wishes to register/unregister for)

### /course

#### GET

the GET method on /course responds with a list of courses that the faculty member currently authenticated teaches.

#### POST

the POST method on /course adds a new course to the database. the request body (JSON) should have the following fields:
- number: 
- name: 
- description: 
- professor: 
- capacity: 
- students: []

note the students field is an empty string.

#### PUT

the PUT method on /course edits an existing course. The request body (JSON) should have the following fields:
- currentNumber: (the current number of the course being edited)
- field: (the field being edited)
- newValue: (the new value to give to that field)

#### DELETE

the DELETE method on /course deletes an existing course. The request body (JSON) should contain the number (i.e. "math 100") of the course to be deleted.
