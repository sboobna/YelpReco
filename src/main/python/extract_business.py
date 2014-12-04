#!/usr/bin/python

import csv
import json

TYPE_STRING = 'type'
BUSINESS_ID = 'business_id'
USER_ID = 'user_id'
BNAME = 'name'
UNAME = 'name'
BSTATE = 'state'
BCITY = 'city'
STARS = 'stars'
REVIEW_COUNT = 'review_count'
REVIEW_TEXT = 'text'
LATITUDE = 'latitude'
LONGITUDE = 'longitude'
CURRENT_STATE = 'AZ'

business_id_set = set()
user_id_set = set()


def business_extractor():
	dataset = open('/home/bigdata/Project/yelp_academic_dataset_business.json', 'r')
	writer = csv.writer(open('/home/bigdata/Project/yelp_businesses.csv', 'w'))
	writer.writerow(['BID', 'BName', 'City', 'State', 'Stars', 'Review_Count', 'Latitude', 'Longitude'])


	for line in dataset:
		# print(line)
		line_dict = json.loads(line)
		bstate = line_dict[BSTATE].encode('utf-8')
		if bstate != CURRENT_STATE:
			continue

		btype = line_dict[TYPE_STRING].encode('utf-8')
		bid = line_dict[BUSINESS_ID].encode('utf-8')
		business_id_set.add(bid)

		bname = line_dict[BNAME].encode('utf-8')
		bcity = line_dict[BCITY].encode('utf-8')
		stars = line_dict[STARS]
		review_count = line_dict[REVIEW_COUNT]
		latitude = line_dict[LATITUDE]
		longitude = line_dict[LONGITUDE]
		writer.writerow([bid, bname, bcity, bstate, stars, review_count, latitude, longitude])

	dataset.close()


def review_extractor():
	dataset = open('/home/bigdata/Project/yelp_academic_dataset_review.json', 'r')
	writer = csv.writer(open('/home/bigdata/Project/yelp_reviews.csv', 'w'))
	writer.writerow(['UID', 'BID', 'Stars', 'Review_Text'])

	for line in dataset:
		line_dict = json.loads(line)
		bid = line_dict[BUSINESS_ID].encode('utf-8')
		if bid not in business_id_set:
			continue

		uid = line_dict[USER_ID].encode('utf-8')
		user_id_set.add(uid)

		rtype = line_dict[TYPE_STRING].encode('utf-8')
		stars = line_dict[STARS]
		review_text = line_dict[REVIEW_TEXT].encode('utf-8')
		writer.writerow([uid, bid, stars, review_text])

	dataset.close()


def user_extractor():
	dataset = open('/home/bigdata/Project/yelp_academic_dataset_user.json', 'r')
	writer = csv.writer(open('/home/bigdata/Project/yelp_users.csv', 'w'))
	writer.writerow(['UID', 'Name', 'Review_Count'])

	for line in dataset:
		line_dict = json.loads(line)
		uid = line_dict[USER_ID].encode('utf-8')
		if uid not in user_id_set:
			continue

		utype = line_dict[TYPE_STRING].encode('utf-8')
		name = line_dict[UNAME].encode('utf-8')
		review_count = line_dict[REVIEW_COUNT]
		writer.writerow([uid, name, review_count])


	dataset.close()


def main():

	business_extractor()
	review_extractor()
	user_extractor()

main()
