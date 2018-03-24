package org.jim.csye6225.courseservice;

import java.util.HashSet;
import java.util.Set;

import org.jim.csye6225.courseservice.database.DynamoDBSetCoverter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;

@DynamoDBTable(tableName = "Professors")
public class Professor extends BasicObject{
	
	public String professorName;
	public Set<String> courses;
	
	@DynamoDBHashKey(attributeName = "ProfessorId")
	public String getId() { return this.id; }
	public void setId(String id) { this.id = id; }
	
	@DynamoDBAttribute(attributeName = "ProfessorName")
	public String getName() { return this.professorName; }
	public void setName(String professorName) { this.professorName = professorName; }
	
	@DynamoDBAttribute(attributeName = "Courses")
	@DynamoDBTypeConverted(converter = DynamoDBSetCoverter.class)
	public Set<String> getCourses() {
		if(this.courses == null)
			this.courses = new HashSet<>();
		return this.courses;
	} 
	
	public void setCourses(Set<String> courses) { this.courses = courses; }
	
}
