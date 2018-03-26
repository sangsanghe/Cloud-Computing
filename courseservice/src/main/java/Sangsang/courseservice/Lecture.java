package Sangsang.courseservice;

import java.util.HashSet;
import java.util.Set;

import org.jim.csye6225.courseservice.database.DynamoDBSetCoverter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;

@DynamoDBTable(tableName = "Lectures")
public class Lecture {
	public String topic;
	public Set<String> notes;
	public String lectureId;
	
	@DynamoDBHashKey(attributeName = "LectureId")
	public String getId() { return this.lectureId; }
	public void setId(String lectureId) { this.lectureId = lectureId; }
	
	@DynamoDBAttribute(attributeName = "Topic")
	public String getTopic() { return this.topic; }
	public void setTopic(String topic) { this.topic = topic; }
	
	@DynamoDBAttribute(attributeName = "Notes")
	@DynamoDBTypeConverted(converter = DynamoDBSetCoverter.class)
	public Set<String> getNotes() { 
		if(this.notes == null)
			this.notes = new HashSet<>();
		return this.notes;
	} 
	
	public void setNotes(Set<String> notes) { this.notes = notes; }
}
