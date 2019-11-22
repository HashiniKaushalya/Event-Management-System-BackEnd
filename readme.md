## api

### create student

POST 127.0.0.1:8080/student/register
```
{
	"firstName": "Test first name",
	"lastName": "test last name",
	"email": "test@123.com",
	"password": "test123",
	"indexNo": "123456",
	"faculty": "science",
	"contactNo": "77123456"
}
```

check email and send verification

### student signup verify
POST 127.0.0.1:8080/student/verify
```
{
	"email": "test1@123.com",
	"code": "613793"
}
```

### user auth
POST 127.0.0.1:8080/student/login
```
{
	"email": "test1@123.com",
	"password": "test123"
}
```

Response
```
{
    "token": "cc9642f4-c0e0-4d19-8c16-2c7223d50bd5"
}
```

### create admin

POST 127.0.0.1:8080/admin/register

```
{
	"firstName": "Admin first name",
	"lastName": "Admin last name",
	"email": "admin@123.com",
	"password": "admin123",
	"faculty": "science",
	"contactNo": "77123456"
}
```


check email and send verification

### student signup verify
POST 127.0.0.1:8080/admin/verify
```
{
	"email": "admin@123.com",
	"code": "151648"
}
```


### admin auth
POST 127.0.0.1:8080/admin/login
```
{
	"email": "admin@123.com",
	"password": "admin123"
}
```

Response
```
{
    "token": "cc9642f4-c0e0-4d19-8c16-2c7223d50bd5"
}
```

# Attributes

## Get event types to show on drop down 
GET 127.0.0.1:8080/attribute/event_types
```
[
    {
        "id": 1,
        "name": "Event type 1"
    },
    {
        "id": 2,
        "name": "Event type 2"
    }
]
```


## Get Venues to show on drop down 
GET 127.0.0.1:8080/attribute/venues
```
[
    {
        "id": 1,
        "name": "Venue 1"
    },
    {
        "id": 2,
        "name": "Venue 2"
    }
]
```



## File uploads


POST 127.0.0.1:8080/file/upload

Request Content Type should be:
form data
request param name: file 

Response
```
   {
       "message": "76df9d2a-538a-495e-9969-3040cd259290.pdf"
   }
   
```

## Download file
GET 127.0.0.1:8080/file/76df9d2a-538a-495e-9969-3040cd259290.pdf


##  Event Creation
POST 127.0.0.1:8080/event/create

Add auth key in header
Header name: x-token
Value: login token

```
{
	"name": "Event Sample Name",
	"eventType": 1,
	"eventDate": "2019-10-10",
	"eventTime": "12:00:00",
	"venue": 2,
	"equipment": "Sample quipment",
	"permissionFile": "76df9d2a-538a-495e-9969-3040cd259290.pdf"
}
```


Error RESPONSE

Already Event booked for selected Venue on the Day 

Success RESPONSE
Event created successfully

## list all events

```
[
    {
        "id": 1,
        "eventName": "Event Sample Name",
        "eventDate": "2019-10-10",
        "eventTime": "12:00:00",
        "venue": "Venue 1",
        "eventType": "Event type 1",
        "equipment": null,
        "permissionFile": "76df9d2a-538a-495e-9969-3040cd259290.pdf",
        "status": "pending",
        "createdBy": {
            "firstName": "Test first name",
            "lastName": "test last name"
        }
    },
    {
        "id": 2,
        "eventName": "Event Sample Name",
        "eventDate": "2019-10-10",
        "eventTime": "12:00:00",
        "venue": "Venue 2",
        "eventType": "Event type 1",
        "equipment": null,
        "permissionFile": "76df9d2a-538a-495e-9969-3040cd259290.pdf",
        "status": "pending",
        "createdBy": {
            "firstName": "Test first name",
            "lastName": "test last name"
        }
    }
]
```

## approve event
POST 127.0.0.1:8080/event/status

```
{
	"eventId": 1,
	"status": "approved"
}

```

LOGOUT
add token on header 

POST 127.0.0.1:8080/admin/logout

POST 127.0.0.1:8080/student/logout

