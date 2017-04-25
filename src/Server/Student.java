package Server;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;

/**
 * Encapsulates the concept of a Student
 * @author kireh
 */
public class Student 
{
	private static int nextID = 0;//tracks the next available Student ID
	private String firstName;
	private String lastName;
	private int sid;
	private Schedule classes;
	private Authenticator passwd;
	
	/**
	 * Creates a new Student object
	 * @param fName First name as a String
	 * @param lName Last Name as a String
	 */
	public Student(String fName, String lName)
	{
		setID();
		setFirst(fName);
		setLast(lName);
	}
	
	/**
	 * Accesses the ID of the Student
	 * @return Student ID as an int
	 */
	public int getID()
	{
		return this.sid;
	}
	
	/**
	 * Sets the ID of a student to the next available ID.
	 * Student ID numbers are generated sequentially
	 * ID numbers are not recycled upon graduation
	 */
	private void setID()
	{
		this.sid=Student.nextID;
		Student.nextID++;
	}
	
	/**
	 * Sets the first name of the student to the parameter
	 * @param n The first name of the student as a String
	 */
	public void setFirst(String n)
	{
		this.firstName= n;
	}
	
	/**
	 * Sets the last nae of the student to the parameter
	 * @param n Last name of the student as a String
	 */
	public void setLast(String n)
	{
		this.lastName=n;
	}
	
	public void setPassword(String p)
	{
		
	}
}

class Schedule
{
	ArrayList<Course> list;
	
	
}
