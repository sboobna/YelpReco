

DROP TABLE IF EXISTS users;
CREATE EXTERNAL TABLE users (uid STRING, name STRING, review_count INT, total_rating INT)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ','
LOCATION 's3://yelpreco/yelp_users/'
TBLPROPERTIES (
        "skip.header.line.count"="1"
    );

DROP TABLE IF EXISTS businesses;
CREATE EXTERNAL TABLE businesses (bid STRING, name STRING, city STRING, state STRING, avg_rating FLOAT, review_count INT, latitude DOUBLE, longitude DOUBLE)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ','
LOCATION 's3://yelpreco/yelp_businesses/'
TBLPROPERTIES (
        "skip.header.line.count"="1"
    );

DROP TABLE IF EXISTS ratings;
CREATE EXTERNAL TABLE ratings (uid STRING, bid STRING, rating INT)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ','
LOCATION 's3://yelpreco/yelp_reviews/'
TBLPROPERTIES (
        "skip.header.line.count"="1"
    );



DROP TABLE IF EXISTS userratings;
CREATE TABLE userratings (
uid STRING,
bid STRING,
rating INT
)
CLUSTERED BY(bid) SORTED BY(bid) INTO 8 BUCKETS
ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

set hive.enforce.bucketing = true;
INSERT OVERWRITE TABLE userratings
SELECT * 
FROM ratings 
WHERE uid='${USERID}';


DROP TABLE IF EXISTS otheruserratings;
CREATE TABLE otheruserratings (
uid STRING,
bid STRING,
rating INT
)
CLUSTERED BY(bid) SORTED BY(bid) INTO 128 BUCKETS
ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

set hive.enforce.bucketing = true;
INSERT OVERWRITE TABLE otheruserratings
SELECT * 
FROM ratings 
WHERE uid<>'${USERID}';




DROP TABLE IF EXISTS sameratedbusinesses;
CREATE TABLE sameratedbusinesses (
uid STRING,
userRating INT,
similaruid STRING,
similarUserRating INT,
bid STRING
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

set hive.optimize.bucketmapjoin = true;
INSERT OVERWRITE TABLE sameratedbusinesses
SELECT /*+ MAPJOIN(otheruserratings) */ r1.uid, r1.rating, r2.uid, r2.rating, r2.bid
FROM userratings r1 JOIN otheruserratings r2 ON (r1.bid = r2.bid);


DROP TABLE IF EXISTS sameratedbusinesseswithtotal;
CREATE TABLE sameratedbusinesseswithtotal (
uid STRING,
userRating INT,
similaruid STRING,
similarUserRating INT,
bid STRING,
total_rating INT
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

INSERT OVERWRITE TABLE sameratedbusinesseswithtotal
SELECT r1.uid, r1.userRating, r1.similaruid, r1.similarUserRating, r1.bid, (b1.avg_rating*b1.review_count) as total_rating
FROM sameratedbusinesses r1 JOIN businesses b1 ON (r1.bid = b1.bid);

DROP TABLE IF EXISTS recopower;
CREATE TABLE recopower (
uid STRING,
power FLOAT
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

--LOCATION 's3://yelpreco/yelp_output/${USERID}/recopower/';


INSERT OVERWRITE TABLE recopower
SELECT similaruid, SUM(userRating*similarUserRating/(total_rating*58))
FROM sameratedbusinesseswithtotal
GROUP BY similaruid;




DROP TABLE IF EXISTS recopowerwithavg;
CREATE TABLE recopowerwithavg (
uid STRING,
power FLOAT,
avg_rating FLOAT
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

INSERT OVERWRITE TABLE recopowerwithavg
SELECT r1.uid, r1.power, (u1.total_rating/u1.review_count) as avg_rating
FROM recopower r1 JOIN users u1 ON (r1.uid = u1.uid);



DROP TABLE IF EXISTS similarsusersrating;
CREATE TABLE similarsusersrating (
uid STRING,
bid STRING,
rating INT,
power FLOAT,
avg_rating FLOAT
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

INSERT OVERWRITE TABLE similarsusersrating
SELECT r1.uid, r2.bid, r2.rating, r1.power, r1.avg_rating
FROM recopowerwithavg r1 JOIN ratings r2 ON (r1.uid = r2.uid)
WHERE r2.bid NOT IN (SELECT DISTINCT(bid) FROM ratings WHERE uid='${USERID}');

DROP TABLE IF EXISTS raw_predictions;
CREATE TABLE raw_predictions (
bid STRING,
prediction FLOAT
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';


INSERT OVERWRITE TABLE raw_predictions
SELECT bid, (3.625 + SUM(power*(rating-avg_rating))) as prediction
FROM similarsusersrating
GROUP BY bid;

DROP TABLE IF EXISTS predictions;
CREATE EXTERNAL TABLE predictions (
bid STRING,
prediction FLOAT,
name STRING,
city STRING,
state STRING,
latitude DOUBLE,
longitude DOUBLE
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ','
LOCATION 's3://yelpreco/yelp_output/${USERID}/prediction/';

INSERT OVERWRITE TABLE predictions
SELECT r1.bid, r1.prediction, b1.name, b1.city, b1.state, b1.latitude, b1.longitude
FROM raw_predictions r1 JOIN businesses b1 ON (r1.bid = b1.bid)
ORDER BY r1.prediction DESC;