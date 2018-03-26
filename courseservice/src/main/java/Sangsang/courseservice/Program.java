package Sangsang.courseservice;

import java.util.HashSet;
import java.util.Set;
import sangsang.courseservice.database.DynamoDBSetCoverter;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;

@DynamoDBTable(tableName = "Programs")
public class Program {
	
	public String programId;
	public String programName;
	
	@DynamoDBHashKey(attributeName = "ProgramId")
	public String getId() { return this.programId; } 
	public void setId(String programId) { this.programId = programId; } 
	
	@DynamoDBAttribute(attributeName = "ProgramName")
	public String getProgramName() { return this.programName; }
	public void setProgramName (String programName) { this.programName = programName; }
	
}
