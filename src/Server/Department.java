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
     * Returns all of the faculty in the department (including the chair of the department)
     * @return All of the Faculty as a Set<Faculty>
     */
    public Set<Faculty> getStaff()
    {
        Set<Faculty> ans = new HashSet<Faculty>(staff);
        ans.add(chair);
        return ans;
    }

    /**
     * Gets a set of staff members for a dept
     * @param staff set of staff as a Set<Faculty>
     */
    public void setStaff(Set<Faculty> staff)
    {
        this.staff = staff;
    }

    /**
     * Gets the chair of a Dept
     * @return chair as a Faculty obj
     */
    public Faculty getChair()
    {
        return chair;
    }

    /**
     * Allows a department to reappoint a chair
     * @param chair new Chair as a Faculty object
     */
    public void setChair(Faculty chair)
    {
        this.chair = chair;
    }
}
