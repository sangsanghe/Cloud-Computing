package org.jim.csye6225.courseservice;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
@DynamoDBTable(tableName = "Notes")
public class Note extends BasicObject{
	public String content;

	@DynamoDBHashKey(attributeName = "NoteId")
	public String getId() { return this.id; }
	public void setId(String id) { this.id = id; }
	
	@DynamoDBAttribute(attributeName = "Content")
	public String getContent() { return this.content; } 
	public void setContent(String content) { this.content = content; }
}
