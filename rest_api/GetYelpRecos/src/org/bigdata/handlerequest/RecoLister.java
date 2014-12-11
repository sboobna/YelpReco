package org.bigdata.handlerequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class RecoLister {

	private AWSCredentials credentials;
	public static AmazonS3Client s3Client = null;
	public static String bucketName = "yelpreco";
	private static final int NUM_ATTRIBUTES = 7;

	public RecoLister() {
		try {
			init();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void init() throws ClassNotFoundException {

		credentials = new AWSCredentials() {
			public String getAWSSecretKey() {
				return "xxxxxxxxx";
			}

			public String getAWSAccessKeyId() {
				return "xxxxxxxxx";
			}
		};

		if (s3Client == null)
			s3Client = new AmazonS3Client(credentials);
	}

	public JSONArray getRecos(String userId, int numBusinesses)
			throws IOException {

		String prefix = "yelp_output/" + userId + "/prediction/";
		String s3ObjectName = "";
		JSONArray businessList = null;
		try {
			ObjectListing objectListing = s3Client
					.listObjects(new ListObjectsRequest()
							.withBucketName(bucketName).withPrefix(prefix)
							.withDelimiter("/"));
			for (S3ObjectSummary objectSummary : objectListing
					.getObjectSummaries()) {
				s3ObjectName = objectSummary.getKey().toString();
				if (!(s3ObjectName.equals(prefix))
						&& s3ObjectName.contains(prefix))
					break;
			}
			S3Object s3object = s3Client.getObject(new GetObjectRequest(
					bucketName, s3ObjectName));
			businessList = processTextInputStream(s3object.getObjectContent(),
					numBusinesses);

		} catch (AmazonServiceException ase) {
			printAmazonServiceException(ase);
		} catch (AmazonClientException ace) {
			printAmazonClientException(ace);
		}

		return businessList;
	}

	@SuppressWarnings("unchecked")
	private JSONArray processTextInputStream(InputStream input,
			int numBusinesses) throws IOException {
		// Read one text line at a time.
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		int businessCount = 0;
		JSONArray businessList = new JSONArray();
		while (businessCount < numBusinesses) {
			String line = reader.readLine();
			if (line == null)
				break;
			String[] businessAttributes = line.split(",");
			if (businessAttributes.length != NUM_ATTRIBUTES)
				continue;
			JSONObject singleBusiness = new JSONObject();
			singleBusiness.put("bid", businessAttributes[0]);
			singleBusiness.put("predictedRating", businessAttributes[1]);
			singleBusiness.put("name", businessAttributes[2]);
			singleBusiness.put("city", businessAttributes[3]);
			singleBusiness.put("state", businessAttributes[4]);
			singleBusiness.put("latitude", businessAttributes[5]);
			singleBusiness.put("longitude", businessAttributes[6]);
			businessList.add(singleBusiness);
			businessCount++;
		}
		return businessList;
	}

	private void printAmazonServiceException(AmazonServiceException ase) {
		System.out
				.println("Caught an AmazonServiceException, which means your request made it "
						+ "to Amazon S3, but was rejected with an error response for some reason.");
		System.out.println("Error Message:    " + ase.getMessage());
		System.out.println("HTTP Status Code: " + ase.getStatusCode());
		System.out.println("AWS Error Code:   " + ase.getErrorCode());
		System.out.println("Error Type:       " + ase.getErrorType());
		System.out.println("Request ID:       " + ase.getRequestId());
	}

	private void printAmazonClientException(AmazonClientException ace) {
		System.out
				.println("Caught an AmazonClientException, which means the client encountered "
						+ "a serious internal problem while trying to communicate with S3, "
						+ "such as not being able to access the network.");
		System.out.println("Error Message: " + ace.getMessage());
	}

	public static void main(String args[]) {
		String userId = "kJyR4gT1pfCcNjEY9-YMoQ";
		int numBusinesses = 20;
		RecoLister recoLister = new RecoLister();
		try {
			recoLister.getRecos(userId, numBusinesses);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
