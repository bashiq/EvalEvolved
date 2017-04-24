package Server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class EvalServer extends JFrame 
{
	//database connection
	Connection db = null;//connection for the database
        private String Path = "C:/EvalEvolved/";//gives the File path for the program data files
        private String surveyPath = Path + "/surveys";
        private String statPath= Path + "/stats";
        private PreparedStatement studentSurveys;
        private PreparedStatement profSurveys;
        
        /**
         * Creates a new EvalServer object, connects it to the database
         */
        public EvalServer()
        {
            Connection connection = null;
            try
            {
                Class.forName("org.apache.derby.jdbc.ClientDriver");  
                connection = DriverManager.getConnection
                        ("jdbc:derby://localhost:1527/sample", "app", "app");
           
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
         * @param sid The student ID to be searched
         * @return available surveys as a Survey[]
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
         * @return 
         */
        public File retrieveSurvey(String surveyFile)
        {
            try
            {
                FileWriter ans = new FileWriter(surveyPath+ surveyFile);
            } catch (IOException ex)
            {
                Logger.getLogger(EvalServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
	
	/*
	 * Admin Methods
	 * +Store new surveys
	 * +Assign new surveys  to existing courses
	 * 
	 */
        
}
