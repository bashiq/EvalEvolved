package Server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class EvalServer extends JFrame 
{
        //network instance variables
        ServerSocket client = null;
	
        //database connection instance variables
	Connection db = null;//connection for the database
        private PreparedStatement studentSurveys;
        private PreparedStatement profSurveys;
        private PreparedStatement addSurvey;
        
        //file I/O instance variables
        private String Path = "C:/EvalEvolved/";//gives the File path for the program data files
        private String surveyPath = Path + "/survey";
        private String statPath= Path + "/stats";
        
        //GUI instance variables
        private JTextArea logs;
        private Date now;
        private JScrollPane logPane;
        
        /**
         * Creates a new EvalServer object, connects it to the database
         */
        public EvalServer()
        {
            super("EvalEvolved Server Software");
            Connection connection = null;
            try
            {
                Class.forName("org.apache.derby.jdbc.ClientDriver");  
                connection = DriverManager.getConnection
                        ("jdbc:derby://localhost:1527/sample", "app", "app");
                addSurvey = db.prepareStatement("INSERT INTO SURVEY VALUES (?,?,?,?,?,?,?");
            }
            //Should the class not exist, the following catch block will execute.
            catch (ClassNotFoundException cnf)
            {
                System.out.println("Line 26 error: Class Not Found: " +cnf.getLocalizedMessage());
            } 
            //Should the Database connection not work properly, the following catch block will execute
            catch (SQLException sql) 
            {
                System.out.println("Line 30 error: SQL Connection: " +sql.getLocalizedMessage());
            }   
        }
	
        /**
         * Appends new information to the server log window
         * @param s Message to be appended to the server as a String
         */
        public void update(String s)
        {
            
        }
	//authenticate and return whether Student, Professor, Chair, or Dean
	
	
	/*
	 * Student methods
	 * -return list of surveys with a status for each
	 * +receive finished surveys and...
	 * +generate a completion token to share with the student and database
	 * +keep separate statFiles for the different sections of a course
	 */
        
        /**
         * Returns the surveys that are applicable to a given student
         * The Completion field will be used to sort finished from unfinished surveys
         * @param sid The student ID to be searched
         * @return available surveys as an ArrayList<Survey>
         */
        public ArrayList<Survey> getStudentSurveys(int sid)
        {
            ArrayList<Survey> ans = new ArrayList<Survey>();
            ResultSet rs = null;
            try
            {
                studentSurveys= db.prepareStatement("Select * from SURVEY where SID=" +sid);
                rs = studentSurveys.executeQuery();
                while (rs.next())
                {
                    Survey next;
                    next = new Survey(rs.getInt("SID"), rs.getInt("COURSE"), rs.getString("FILENAME"), rs.getString("STATFILE"), rs.getInt("COMPLETION"));
                    ans.add(next);
                }
            }
            catch (SQLException sql)
            {
                System.out.println("Line 62: Error: SQL: " +sql.getLocalizedMessage());
            }
            return ans;
        }
        
        /**
         * Retrieves the relevant binary data file for a given survey
         * @param surveyFile the relative path of the surveyFile as a String
         * @return proper binar data File as a File object
         */
        public void retrieveSurvey(String surveyFile)
        {
            try
            {
                FileWriter ans = new FileWriter(surveyPath+ surveyFile);
            } catch (IOException ex)
            {
                Logger.getLogger(EvalServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        /**
         * Stores a Survey object to the database
         * @param s Survey to be stored as a Survey object
         * @return Successful execution status as a boolean
         */
        public boolean storeSurvey(Survey s)
        {
            //receive the file
            //get all fields that need to go into the database
            boolean ans = false;
            try
            {
                addSurvey.setInt(1, s.getSurveyNumber());
                addSurvey.setString(2, s.getCourse());
                addSurvey.setString(3, s.getFileName());
                addSurvey.setInt(5, s.returnCompleted());
                addSurvey.setInt(6, s.getSID());
                ans = addSurvey.execute();
            }
            catch(SQLException sql)
            {
                System.out.println("Line 125: Error: SQL: " +sql.getLocalizedMessage());
            }
            finally
            {
            return ans;
            }
        }
        
	/*
	 * Admin Methods
	 * +Store new surveys
	 * +Assign new surveys  to existing courses
	 * 
	 */
        
    class ServerTask implements Runnable
    {

        @Override
        public void run()
        {
            
        }
        
    }
}
