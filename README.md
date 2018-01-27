# n26CodingAssignment

A REST application to add transactions and retrieve the statistics of the last 60 seconds

## Run project with Maven.

```
git clone https://github.com/soumyamishra89/n26CodingAssignment.git
cd "folder containing the above repository"
mvn clean install
mvn spring-boot:run
```

To run the test.

```
cd "folder containing the above repository"
mvn test
```
  
## API Documentation

There are two endpoints \transactions and \statistics.

#### transactions
This endpoint will verify if the Transaction received is within the last 60 seconds of the current time and if valid then it will add the amount of the transaction to a Statistic. The transaction will be added to the Statistic corresponding to a particular second of the last minute.   
e.g. if the system time is 12:00:01 and a transaction of time 11:59:40 arrives, then the application will get the Statistic corresponding to '40' second and add the amount to the total amount. Before adding the amount the application will check if the time on the Statistic is equal to time on Transaction (without the millis). If it is not equal then it will override the value on the Statistic with the Transaction values. 
Only 60 seconds of Statistic information is stored and rest are discarded.

**Request**
```
POST /transactions
```

**Request Body**
```
{
	"amount": 12.3,
	"timestamp": 1478192204000
}
```

**Response**
Returns empty body with 201 or 204 status code

* 201:   when a transaction is added to Statistic
* 204:   when the transaction is not valid

 
#### statistics
This endpoint calculates statistics of all the transaction in the last 60 seconds. The overall statistics is calculated when a transaction is sent and this API call only retrieves the value.
**Request**
```
GET /statistics
```
**Response**
Returns statistics json with 200 status code
```
{
	"sum":45.78,
	"avg":11.46,
	"max":19.87,
	"min":5.89,
	"count":4}

```   