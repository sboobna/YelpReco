from pprint import pprint
import json

if __name__ == "__main__":
	business_id = []
	review = []
	i =0
	business_data = open('/home/prateek/Downloads/yelp_data/yelp_academic_dataset_business.json')
	for line in business_data:
		data1 = json.loads(line)
		if data1['city'] == "phoenix" or data1['city'] == "Phoenix" or data1['city'] == "PHOENIX":
			business_id.append(data1['business_id'])
	#print len(business_id)
	
	json_data=open('/home/prateek/Downloads/yelp_data/yelp_academic_dataset_review.json')
	for line in json_data:
		data2 = json.loads(line)
		if data2['business_id'] in business_id:
			review.append (data2['text'])
	
	with open('file.txt', 'a') as file:
    	for item in data2:
	    	file.write(item)
