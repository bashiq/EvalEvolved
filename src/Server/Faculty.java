package Server;

public class Faculty 
{
	String firstName;
	String lastName;
	String dept;
	String passwd;
	int fid;
	int rank;

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getDept()
    {
        return dept;
    }
    
    public static String getDept(int fid)
    {
        String ans = "";
        int determinant = fid/100;
        switch (determinant)
        {
            case 0: ans= "Admin";
                    break;
            case 1: ans= "CS";
                    break;
            case 2: ans="IA";
                    break;
            case 3: ans="SE";
                    break;
            case 4: ans="AE";
                    break;
            case 5: ans="PH";
                    break;  
            case 6: ans="BI";
                    break;
            case 7: ans="CIS";
                    break;
            case 8: ans= "NE";
                    break;
            case 9: ans= "EE";                  
        }
        return ans;
    }
    
    /**
     * Sets a faculty's department according to a numbering scheme for the school
     * @param id faculty id as an int
     */
    public void setDept(int fid)
    {
        int determinant = fid/100;
        switch (determinant)
        {
            case 0: this.setDept("Admin");
                    break;
            case 1: this.setDept("CS");
                    break;
            case 2: this.setDept("IA");
                    break;
            case 3: this.setDept("SE");
                    break;
            case 4: this.setDept("AE");
                    break;
            case 5: this.setDept("PH");
                    break;        
            case 6: this.setDept("BI");
                    break;
            case 7: this.setDept("CIS");
                    break;
            case 8: this.setDept("NE");
                    break;
            case 9: this.setDept("EE");
                    break;        
        }
             
    }

    /**
     * Sets the dept instance variable to the parameter
     * @param dept new Department as a String
     */
    private void setDept(String dept)
    {
        this.dept = dept;
    }

    /**
     * Gets the FacultyID of a Faculty object
     * @return FID as an int
     */
    public int getFid()
    {
        return fid;
    }

    /**
     * Sets the FacultyID of a Faculty object to the given parameter
     * @param fid new FacultyID as an int
     */
    private void setFid(int fid)
    {
        this.fid = fid;
    }

    /**
     * Gets the rank of the current faculty object 
     * @return rank as an int
     */
    public int getRank()
    {
        return rank;
    }

    /**
     * Sets the rank of a Faculty object to the given parameter
     * @param rank new Rank as an int
     */
    private void setRank(int rank)
    {
        this.rank = rank;
    }
}
