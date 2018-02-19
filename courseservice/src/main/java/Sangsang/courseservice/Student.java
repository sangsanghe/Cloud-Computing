package Sangsang.courseservice;

public class Student {
	String Name;
    int StudentId;
    String image;    
    Course[] courses;
    String program;
    
    public Student(String Name, int StudentId, String image, Course[] courses, String program) {
    	this.Name = Name;
    	this.StudentId = StudentId;
    	this.image = image;
    	this.courses = courses;
    	this.program = program;
    }
	
}
