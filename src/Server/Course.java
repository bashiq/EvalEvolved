package Server;

import java.util.ArrayList;
import java.util.Set;

public class Course 
{
    int id;
    private String dept;
    private String code;
    private String section;
    private String name;
    private Survey eval=null;
    private Faculty instructor;
    private Set<Student> roster;

    public Course()
    {
        
    }
    
    /**
     * Accesses the ID of a Course object
     * @return ID as an int
     */
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getDept()
    {
        return dept;
    }

    public void setDept(String dept)
    {
        this.dept = dept;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getSection()
    {
        return section;
    }

    public void setSection(String section)
    {
        this.section = section;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Survey getEval()
    {
        return eval;
    }

    public void setEval(Survey eval)
    {
        this.eval = eval;
    }

    public Faculty getInstructor()
    {
        return instructor;
    }

    /**
     * Changes the instructor for this Course object
     * @param instructor New instructor as a Faculty object
     * @return number of changes made as an int
     */
    public int setInstructor(Faculty instructor)
    {
        int ans = (this.instructor == instructor ? 1:0); 
        this.instructor = instructor;
        return ans;
    }
    

    /**
     * Adds a student to the roster for a class
     * @param s Student to be added as a Student Obj
     * @return number of students added as an int
     */
   public int addStudent(Student s)
   {
       int check = roster.size();
       roster.add(s);
       if (roster.size()==check)
       {
           return 1;
       }
       else
       {
           return 0;
       }
   }
}

enum Department
{

}
