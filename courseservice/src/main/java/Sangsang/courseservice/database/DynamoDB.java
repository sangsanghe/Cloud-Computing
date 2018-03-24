package org.jim.csye6225.courseservice.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jim.csye6225.courseservice.Announcement;
import org.jim.csye6225.courseservice.BasicObject;
import org.jim.csye6225.courseservice.Course;
import org.jim.csye6225.courseservice.Lecture;
import org.jim.csye6225.courseservice.Note;
import org.jim.csye6225.courseservice.Professor;
import org.jim.csye6225.courseservice.Program;
import org.jim.csye6225.courseservice.Student;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDeleteExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.util.TableUtils;

public class DynamoDB {
	private static AmazonDynamoDB dynamoDb;
	private static DynamoDBMapper mapper;
	// singleton instance
	private static DynamoDB instance = null;
	private static Map<String, Class> classMap = new HashMap<>();
	
	public DynamoDB() {
		try {
			// Please enter your own access_key_id and secret_access_key
			BasicAWSCredentials awsCreds = new BasicAWSCredentials("access_key_id"
					, "secret_access_key");
			//DefaultAWSCredentialsProviderChain.getInstance()
			dynamoDb = AmazonDynamoDBClientBuilder
					.standard()
					.withCredentials(new AWSStaticCredentialsProvider(awsCreds))
					.withRegion(Regions.US_WEST_2)
					.build();
			
			mapper = new DynamoDBMapper(dynamoDb);
			classMap.put("Programs", Program.class);
			classMap.put("Courses", Course.class);
			classMap.put("Students", Student.class);
			classMap.put("Notes", Note.class);
			classMap.put("Lectures", Lecture.class);
			classMap.put("Announcements", Announcement.class);
			classMap.put("Professors", Professor.class);
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	public static DynamoDB getInstance() {
		if(instance == null)
			instance = new DynamoDB();
		return instance;
	} 
	
	public void createTable(String tableName, String key) throws Exception {
		List<KeySchemaElement> keySchema = new ArrayList<>();
		List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
		
		keySchema.add(new KeySchemaElement().withAttributeName(key)
				.withKeyType(KeyType.HASH));
		
		attributeDefinitions.add(new AttributeDefinition().withAttributeName(key)
				.withAttributeType(ScalarAttributeType.S));
		
		CreateTableRequest createTableRequest = new CreateTableRequest()
				.withTableName(tableName)
				.withKeySchema(keySchema)
				.withAttributeDefinitions(attributeDefinitions)
				.withProvisionedThroughput(
						new ProvisionedThroughput()
						.withReadCapacityUnits(3L)
						.withWriteCapacityUnits(3L));
		
		TableUtils.createTableIfNotExists(dynamoDb, createTableRequest);
		TableUtils.waitUntilActive(dynamoDb, tableName);
	}
	
	public void addOrUpdateItem(BasicObject obj) {
		mapper.save(obj);
	}
	
	public BasicObject getItem(String tableName, String key) {
		BasicObject object = mapper.load(classMap.get(tableName), key);
		return object;
	}
	
	public Set<String> getAllItems(String tableName) {
		List<BasicObject> objs = mapper.scan(classMap.get(tableName)
				, new DynamoDBScanExpression());
		Set<String> result = new HashSet<>();
		for(BasicObject object : objs)
			result.add(object.id);
		return result;
	}
	
	public void deleteItem(String tableName, String key) {
		BasicObject object = mapper.load(classMap.get(tableName), key);
		mapper.delete(object, new DynamoDBDeleteExpression());
	}
	
	public boolean contains(String tableName, String key) {
		BasicObject object = mapper.load(classMap.get(tableName), key);
		return object != null;
	}
	
	public static void main(String[] args) throws Exception{
		DynamoDB dynamoDB = DynamoDB.getInstance();
		dynamoDB.createTable("Programs", "ProgramId");
		dynamoDB.createTable("Courses", "CourseId");
		dynamoDB.createTable("Students", "StudentId");
		dynamoDB.createTable("Lectures", "LectureId");
		dynamoDB.createTable("Notes", "NoteId");
		dynamoDB.createTable("Announcements", "AnnouncementId");
		dynamoDB.createTable("Professors", "ProfessorId");
	}
}
