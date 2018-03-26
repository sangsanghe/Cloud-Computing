package Sangsang.courseservice;

import java.util.HashSet;
import java.util.Set;
import Sangsang.courseservice.database.DynamoDBSetCoverter;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;

@DynamoDBTable(tableName = "Professors")
public class Professor {
	
	public String professorId;
	public String professorName;
	public String courseId;
	
	@DynamoDBHashKey(attributeName = "ProfessorId")
	public String getId() { return this.professorId; }
	public void setId(String professorId) { this.professorId = professorId; }
	
	@DynamoDBAttribute(attributeName = "ProfessorName")
	public String getName() { return this.professorName; }
	public void setName(String professorName) { this.professorName = professorName; }
	
	@DynamoDBAttribute(attributeName = "CourseId")
	@DynamoDBTypeConverted(converter = DynamoDBSetCoverter.class)
	public String getCourse() {
		if(this.courseId == null)
			this.courseId = "";
		return this.courseId;
	} 
	
	public void setCourse(String course) { this.course = course; }
	
}
