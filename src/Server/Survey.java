package Server;

import client.EvalQuestions;
import java.util.ArrayList;

/**
 * This class is designed to encapsulate the survey concept and related fields
 * @author kireh
 */
public class Survey
{
    private int surveyNumber;

   
    private int sid;
    private int cid;
    private String fileName;
    private String course;
    private String statFile;
    private boolean status;
    private ArrayList<EvalQuestions> questions;
       
    /**
     * Creates a new Survey object for which data files must be created
     * Meant to construct surveys that do NOT already exist in the database
     * @param s Student to complete the survey as an object
     * @param c Course to be evaluated as a course object 
     */
    public Survey(Student s, Course c)
    {
        this.setSID(s.getID());
        this.setCID(c.getId());
        this.setCompleted(0);
        this.setFileName(c);
        this.setCourse(c.getName());
    }
    
    /**
     * Creates a new Survey object with existing data files
     * Meant to construct Surveys that already exist in the database
     * @param sid Student id as an int
     * @param cid Course ID as an int
     * @param stats relative file path of statsFile as a string
     * @param completion completion status as an int
     */
    public Survey(int sid, int cid, String stats, int completion)
    {
        this.setSID(sid);
        this.setCID(cid);
        this.setStatFile(stats);
        this.setCompleted(completion);
    }
    
    /**
     * Gets the ID number of the survey object
     * @return SurveyID as an int
     */
     public int getSurveyNumber()
    {
        return surveyNumber;
    }

    /**
     * Determines whether a survey is completed or not
     * @return Completion as a boolean
     */
    public boolean isCompleted()
    {
        return status;
    }
    
    /**
     * Determines whether a survey is completed or not
     * @return Completion status as a binary value as an int
     */
    public int returnCompleted()
    {
        return isCompleted() ? 1:0;
    }

    /**
     * Sets a completion status based o
     * @param completed an 1 or any other number
     */
    public void setCompleted(int completed)
    {
        this.status = !(completed==0);
    }

    /**
     * Accesses the sid of the Survey object
     * @return Student ID as an int
     */
    public int getSID()
    {
        return sid;
    }

    /**
     * Sets the student id of a Survey object
     * @param sid new Student ID as an int
     */
    public void setSID(int sid)
    {
        this.sid = sid;
    }

    /**
     * Accesses the course id of the Survey object 
     * @return Course ID as an int
     */
    public int getCID()
    {
        return cid;
    }

    /**
     * Updates the Course ID of a Survey object
     * @param cid COurse ID as an int
     */
    public void setCID(int cid)
    {
        this.cid = cid;
    }

    /**
     * Accesses the relative file path of a Survey object
     * @return relative File path of a survey object as a String
     */
    public String getFileName()
    {
        return fileName;
    }

    /**
     * Sets the relative surveyFile path of a Survey object
     * @param c  Course to be surveyed as a Course object
     */
    public void setFileName(Course c)
    {
        this.fileName = c.getDept()+"/" +c.getCode() +"/"+c.getSection();
    }
    
    /**
     * Sets the relative surveyFile path of a Survey object
     * @param fileName relative surveyFile path as a String
     */
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    /**
     * Gets the name of the course that this survey is designed to evaluate
     * @return Course name as a String
     */
    public String getCourse()
    {
        return course;
    }

    /**
     * Sets the course name of a Survey object
     * @param course Course name as a String
     */
    public void setCourse(String course)
    {
        this.course = course;
    }

    /**
     * Gets the relative statFile path for a Survey object
     * @return relative StatFile path as a String 
     */
    public String getStatFile()
    {
        return statFile;
    }
    
    public void setStateFile(Course c)
    {
        this.statFile= c.getDept()+"/" +c.getCode() +"/" +c.getSection();
    }

    /**
     * Sets the relative statFile path for a Survey object
     * @param statFile relative StatFile path as a String
     */
    public void setStatFile(String statFile)
    {
        this.statFile = statFile;
    }
    
    /**
     * Accesses the questions of a Survey object
     * @return List of questions as a String[]
     */
    public String[] getQuestions()
    {
        String[] ans = new String[questions.size()];
        for (int i =0; i<ans.length; i++)
        {
           EvalQuestions temp = questions.get(i);
           ans[i] = new String(temp.getQuestion());
        }
        return ans;
    }
    
    
    public void toDatabaseFormat()
    {
        
    }
}

