/* Sachin Mohan Sujir ss1037 */

/* Travel Database */

use travel;
/* Question 1  */

/* What are the names and complete addresses of all passengers listed in order by last name */

SELECT p.fname, p.lname, p.street, z.city, z.state, z.zip 
FROM passenger p JOIN zips z  
ON p.zip=z.zip 
ORDER BY LName;

/* End */

/* Question 2  */

/* What are the trip numbers, departure times, and departure locations code for all bus trip */

SELECT t.tripnum, t.departuretime, t.departureloccode 
FROM trip_directory t JOIN tripcodes c 
ON t.triptype=c.triptype 
WHERE c.typename='Bus';

/* End */


/* Question 3 */

/* What are the names of the passengers who are traveling in October? */

SELECT concat(pa.fname,' ',pa.lname) AS 'Passenger' 
FROM trip_people p JOIN passenger pa 
ON p.passengerID=pa.passengerID 
WHERE Date BETWEEN '2015-10-01' AND '2015-10-31';

/* End */


/* Question 4 */

/* How many trips in the trip directory leave from each city */

SELECT l.location AS 'Location', COUNT(t.departureloccode) AS 'Number of Departures' 
FROM locations l JOIN trip_directory t
ON l.locationcode=t.departureloccode 
GROUP BY l.location 
ORDER BY l.Location;

/* End */

/* Question 5 */

/* What staff are working the Boston to Nassau trips? */

SELECT DISTINCT(s.name) 
FROM staff s 
JOIN trip t 
ON s.tripnum=t.tripnum 
WHERE t.departureloccode=(SELECT locationcode FROM locations WHERE location='Boston') 
AND t.arrivalloccode=(SELECT locationcode FROM locations WHERE location='Nassau');


/* End */


/* Question 6 */

/*  Brian Page who works for Rides ‘R’ Us is from Frankfort. Who,if anyone, will he meet from his town when he works on a trip, and during what trip number? */


SELECT t.tripnum, CONCAT(p.fname, ' ', p.lname) AS 'People from Frankfort' 
FROM trip_people t JOIN passenger p 
ON t.passengerid=p.passengerid 
WHERE p.zip=(SELECT zip FROM zips WHERE city='frankfort');

/* End */


/* Question 7 */

/* What people from Rochester,travel by bus? */

SELECT p.fname,p.lname 
FROM passenger p JOIN trip_people tp 
ON p.passengerid=tp.passengerid 
JOIN trip t 
ON t.tripnum=tp.tripnum 
JOIN trip_directory td 
ON t.tripnum=td.tripnum  
WHERE td.triptype IN (SELECT triptype FROM tripcodes WHERE typename='Bus') 
AND p.zip IN(SELECT zip FROM zips WHERE city='Rochester');

/* End */


/* Question 8 */

/* What is the description of the equipment on which Curtis Browntravels? */

SELECT e.equipmentdescription 
FROM equipment e JOIN trip t 
ON e.equipid=t.equipid 
JOIN trip_people tp 
ON t.tripnum=tp.tripnum 
JOIN passenger p  
ON p.passengerid=tp.passengerid  
WHERE p.fname IN(SELECT fname FROM passenger WHERE fname='Curtis') 
AND p.lname IN(SELECT lname FROM passenger WHERE lname='Brown');


/* End */

/* Question 9 */

/* The Boeing 767 is now rated for Mid-Range travel. Update the database accordingly. */


UPDATE equipment 
SET equipmentDescription = 'Mid-Range' 
WHERE equipmentname='Boeing 767';

/* End */


/* Question 9 */

/* On how many trips has each piece of equipment been used? */

SELECT e.equipid, e.equipmentname, count(t.equipid) as 'NumTrips' 
FROM equipment e LEFT JOIN trip t 
on e.equipid=t.equipid 
GROUP BY t.equipid;

/* End */
