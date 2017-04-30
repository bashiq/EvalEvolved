package Server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Department 
{
        String[] d = {"CS", "IA", "SE", "AE", "PH", "BI", "CIS", "NE", "EE"};
        private String name;
	private Set<Course> classes;
	private Set<Faculty> staff;
	private Faculty chair;

    /**
     * Gets the name of a Department
     * @return name as a String
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of a Department to a given String
     * @param name new Department name as a String
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Gets the courses in a Department's scope
     * @return Courses in a department as a Set<Course>
     */
    public Set<Course> getCourses()
    {
        return classes;
    }
    

    /**
     * Adds a Course to the Department
     * @param c course to be added to the Department as a Course Object
     */
    public void addCourse(Course c)
    {
        classes.add(c);
    }

    /**
     * Returns all of the faculty in the department (including the chair of the department)
     * @return All of the Faculty as a Set<Faculty>
     */
    public Set<Faculty> getStaff()
    {
        Set<Faculty> ans = new HashSet<Faculty>(staff);
        ans.add(chair);
        return ans;
    }

    public void setStaff(Set<Faculty> staff)
    {
        this.staff = staff;
    }

    public Faculty getChair()
    {
        return chair;
    }

    /**
     * Allows a department to reappoint a chair
     * @param chair 
     */
    public void setChair(Faculty chair)
    {
        this.chair = chair;
    }
	
    public static int getMax(String dept)
    {
        if 
    }
}
