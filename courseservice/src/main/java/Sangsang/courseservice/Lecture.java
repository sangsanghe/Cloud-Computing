package org.jim.csye6225.courseservice;

import java.util.HashSet;
import java.util.Set;

import org.jim.csye6225.courseservice.database.DynamoDBSetCoverter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;

@DynamoDBTable(tableName = "Lectures")
public class Lecture extends BasicObject{
	public String topic;
	public Set<String> notes;
	
	@DynamoDBHashKey(attributeName = "LectureId")
	public String getId() { return this.id; }
	public void setId(String id) { this.id = id; }
	
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
