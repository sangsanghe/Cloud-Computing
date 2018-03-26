package Sangsang.courseservice;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Announcements")
public class Announcement {
	
	public String announcementId;
	public String professorId;
	public String contents;
	
	@DynamoDBHashKey(attributeName = "AnnouncementId")
	public String getId() { return this.announcementId; }
	public void setId(String announcementId) { this.announcementId = announcementId;}
	
	@DynamoDBAttribute(attributeName = "ProfessorId")
	public String getProfessorId() { return this.professorId; }
	public void setProfessorId(String professorId) { this.professorId = professorId; }
	
	@DynamoDBAttribute(attributeName = "Contents")
	public String getContents() { return this.contents; }
	public void setContents(String contents) { this.contents = contents; }
}
