package Sangsang.courseservice;

public class Course {
	String board;
    int CourseId;
    Student[] students;
    Lecture[] lectures;
    
    public Course(String board, int CourseId, Student[] students, Lecture[] lectures) {
    	this.board = board;
    	this.CourseId = CourseId;
    	this.students = students;
    	this.lectures = lectures;
    }
}