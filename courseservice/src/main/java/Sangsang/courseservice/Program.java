package org.jim.csye6225.courseservice;

import java.util.HashSet;
import java.util.Set;

import org.jim.csye6225.courseservice.database.DynamoDBSetCoverter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;

@DynamoDBTable(tableName = "Programs")
public class Program extends BasicObject{
	
	public String programName;
	public Set<String> courses;
	public Set<String> students;
	
	@DynamoDBHashKey(attributeName = "ProgramId")
	public String getId() { return this.id; } 
	public void setId(String programId) { this.id = programId; } 
	
	@DynamoDBAttribute(attributeName = "ProgramName")
	public String getProgramName() { return this.programName; }
	public void setProgramName (String programName) { this.programName = programName; }
	
	@DynamoDBAttribute(attributeName = "Courses")
	@DynamoDBTypeConverted(converter = DynamoDBSetCoverter.class)
	public Set<String> getCourses() {
		if(this.courses == null)
			this.courses = new HashSet<>();
		return this.courses; 
	}
	
	public void setCourses(Set<String> courses) { this.courses = courses; } 
	
	@DynamoDBAttribute(attributeName = "Students")
	@DynamoDBTypeConverted(converter = DynamoDBSetCoverter.class)
	public Set<String> getStudents() {
		if(this.students == null)
			this.students = new HashSet<>();
		return this.students; 
	}
	
	public void setStudents(Set<String> students) { this.students = students; } 
}
