package Server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class EvalServer extends JFrame 
{
        //network instance variables
        ServerSocket service = null;
        Thread t;
	
        //database connection instance variables
	Connection db = null;//connection for the database
        private PreparedStatement studentSurveys;
        private PreparedStatement profSurveys;
        private PreparedStatement addSurvey;
        private PreparedStatement loginQuery;
        
        //file I/O instance variables
        private String Path = "C:/EvalEvolved/";//gives the File path for the program data files
        private String statPath= Path + "/stats";
        
        //GUI instance variables
        private JTextArea logs;
        private Date now;
        private JScrollPane logPane;
        
        //login data
        HashMap<Integer, Integer> ranks;
        HashMap<Integer, String> people;
        HashMap<Integer, Boolean> isLoggedIn;//keeps track of who logs in
        HashMap<Integer, Course> courses;//keeps track of courses 
        
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
                connection = DriverManager.getConnection("jdbc:derby://localhost:1527/sample", "app", "app");
                addSurvey = db.prepareStatement("INSERT INTO SURVEY VALUES (?,?,?,?,?,?,?");
                
                //The following block compiles a list of all of the people who are able to sign into the 
                loginQuery = db.prepareStatement("SELECT * FROM PERSON");
                ResultSet logins = loginQuery.executeQuery();
                while (logins.next())
                {
                    Integer personID = logins.getInt("PID");
                    String fullName = logins.getString("FNAME") + " " +logins.getString("LNAME");
                    people.put(personID, fullName);
                    isLoggedIn.put(personID, false);
                    ranks.put(personID, logins.getInt("RANK"));
                }    
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
            logs.append("\n" +s);
        }
	/*
	 * Student methods
	 * -return list of surveys with a status for each
	 * +receive finished surveys and...
	 * -generate a completion token to share with the student and database
	 * -keep separate statFiles for the different sections of a course
	 */
        
        /**
         * Returns the surveys that are applicable to a user id and rank
         * The Completion field will be used to sort finished from unfinished surveys
         * @param id The ID to be searched as an int
         * @param rank The rank of the id as an int
         * @return available surveys as a JsonArray
         */
        public JsonArray getSurveys(int id, int rank)
        {
            assert (rank>0 && rank<4);
            
            //Json variables
            JsonArrayBuilder ansBuilder = Json.createArrayBuilder();
            
            ResultSet rs = null;
            //EXECUTES FOR STUDENTS
            if (rank==0)
            {
                try
                {
                    studentSurveys= db.prepareStatement("Select * from SURVEY where SID=?");
                    studentSurveys.setInt(1, id);
                    rs = studentSurveys.executeQuery();
                    while (rs.next())
                    {
                        JsonObjectBuilder surveyBuilder = Json.createObjectBuilder();
                        surveyBuilder.add("SID", rs.getInt("SID"));
                        surveyBuilder.add("CID", rs.getInt("COURSE"));
                        surveyBuilder.add("STATFILE", rs.getString("STATFILE"));
                        surveyBuilder.add("COMPLETION", rs.getInt("COMPLETION"));
                        ansBuilder.add(surveyBuilder);
                    }
                    return ansBuilder.build();
                }
                catch (SQLException sql)
                {
                    System.out.println("Line 62: Error: SQL: " +sql.getLocalizedMessage());
                }
            }
            //EXECUTES FOR PROFESSORS
            else if (rank ==1)
            {
                try
                {
                    studentSurveys= db.prepareStatement("Select * from SURVEY where INSTRUCTOR=?");
                    studentSurveys.setInt(1, id);
                    rs = studentSurveys.executeQuery();
                    while (rs.next())
                    {
                        JsonObjectBuilder surveyBuilder = Json.createObjectBuilder();
                        surveyBuilder.add("SID", rs.getInt("SID"));
                        surveyBuilder.add("CID", rs.getInt("COURSE"));
                        surveyBuilder.add("STATFILE", rs.getString("STATFILE"));
                        surveyBuilder.add("COMPLETION", rs.getInt("COMPLETION"));
                        ansBuilder.add(surveyBuilder);
                    }
                    return ansBuilder.build();
                }
                catch (SQLException sql)
                {
                    System.out.println("Line 62: Error: SQL: " +sql.getLocalizedMessage());
                }
            }
            //EXECUTES FOR DEPARMENT CHAIRS
            else if(rank==2)
            {
                String dept = Faculty.getDept(id);
                int min = (id/1000)*1000;//seems unnecessary, but acts an ipart solution
                int max = min+100;
                 try
                {
                    studentSurveys= db.prepareStatement("Select * from SURVEY WHERE INSTRUCTOR>=? AND INSTRUCTOR<?");
                    studentSurveys.setInt(1, min);
                    studentSurveys.setInt(2, max);
                    rs = studentSurveys.executeQuery();
                    while (rs.next())
                    {
                        JsonObjectBuilder surveyBuilder = Json.createObjectBuilder();
                        surveyBuilder.add("SID", rs.getInt("SID"));
                        surveyBuilder.add("CID", rs.getInt("COURSE"));
                        surveyBuilder.add("STATFILE", rs.getString("STATFILE"));
                        surveyBuilder.add("COMPLETION", rs.getInt("COMPLETION"));
                        ansBuilder.add(surveyBuilder);
                    }
                    return ansBuilder.build();
                }
                catch (SQLException sql)
                {
                    System.out.println("Line 62: Error: SQL: " +sql.getLocalizedMessage());
                }
            }
            //EXECUTES FOR THE DEAN
            else if (rank==3)
            {
               try
                {
                    studentSurveys= db.prepareStatement("Select * from SURVEY");
                    rs = studentSurveys.executeQuery();
                    while (rs.next())
                    {
                        JsonObjectBuilder surveyBuilder = Json.createObjectBuilder();
                        surveyBuilder.add("SID", rs.getInt("SID"));
                        surveyBuilder.add("CID", rs.getInt("COURSE"));
                        surveyBuilder.add("STATFILE", rs.getString("STATFILE"));
                        surveyBuilder.add("COMPLETION", rs.getInt("COMPLETION"));
                        ansBuilder.add(surveyBuilder);
                    }
                    return ansBuilder.build();
                }
                catch (SQLException sql)
                {
                    System.out.println("Line 62: Error: SQL: " +sql.getLocalizedMessage());
                } 
            }
            return ansBuilder.build();
        }
        
        /**
         * Stores a Survey object to the database and uses 
         * @param answers Selected answers and possible answers as a JsonObject
         * @return Successful execution status as a boolean
         */
        public JsonObject storeSurvey(JsonObject answers)
        {
            JsonObjectBuilder ansBuild = Json.createObjectBuilder();
            //enter the appropriate file
            try
            {
                PreparedStatement findStatement = db.prepareStatement("SELECT STATFILE FROM SURVEY WHERE NUMBER=?");
                ResultSet rs = findStatement.executeQuery();
                while (rs.next())
                {
                    statpath = rs.getString("STATFILE");
                }
            }
            catch (SQLException sql)
            {
                System.out.println("");
            }
                statPath = "";
            
            //get all fields that need to go into the database
            boolean ans = false;
            try
            {
                
                //addSurvey.setInt(1,1);
                //addSurvey.setString(2, s.getCourse());
                //addSurvey.setString(3, s.getFileName());
                //addSurvey.setInt(5, s.returnCompleted());
                //addSurvey.setInt(6, s.getSID());
                ans = addSurvey.execute();
            }
            catch(SQLException sql)
            {
                System.out.println("Line 125: Error: SQL: " +sql.getLocalizedMessage());
            }
            finally
            {
            return ansBuild.build();
            }
        }
        
        public JsonObject sendStats(int cid)
        {
            JsonObjectBuilder ansBuild = Json.createObjectBuilder();
            
        }
        
        
        
            
        public static void main(String[] args)
        {
            EvalServer demo = new EvalServer();
            demo.setSize(200, 400);
            
            ServerSocket service = null;//declares the ServerSocket to run the service
            ArrayList<Socket> clients = new ArrayList<>();
		/*
		 * Establishes the ServerSocket and continues to listen for new connections
		 */
		try 
		{
			service = new ServerSocket(5555);//creates a new TCP socket connection at the specified port number
			Socket client;//holder variable to track the client socket
			
			while (true)
			{
				clients.add(service.accept()); //until a client connects, the server will  listen for a connection
				Thread t = new Thread(new ServerTask());
				//t.start();//starts the newly created thread
			}
		}
		/*
		 * Catches the IOException that may be thrown by trying to initialize the ServerSocket
		 */
		catch (IOException ioe) 
		{
			ioe.printStackTrace();
		}
		
		finally
		{
			try 
			{
				service.close();//closes the socket, preventing future connections
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
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
        User user;
        
        public ServerTask()
        {
            super();
        }
        
        /**
         * Attempt a login with the given UserID
         * @param data login message and proposed user ID as a JsonObject
         * @return  A JsonObject containing login success as a boolean and 
         * appropriate list of courses or surveys as a JsonArray
         */
        public JsonObject tryLogin(int data)
        {   
            //answer variable
            JsonObjectBuilder ansBuild = Json.createObjectBuilder();
                        
            //instantiate list of appropriate surveys
            JsonArrayBuilder surveys = Json.createArrayBuilder();
            JsonObjectBuilder surveyBuilder = Json.createObjectBuilder();
            
            //UID holder variable
            Integer uid = data;
            
            //the following block controls if the UID is valid in the database
            if (people.containsKey(uid))
            {
                Integer ranked = ranks.get(uid);
                Boolean logged = isLoggedIn.get(uid);
                String name = people.get(uid);
                
                //if the valid user is logged in, they will not be permitted to run a second session
                if (logged)
                {
                    ansBuild.add("name", name);
                    ansBuild.add("rank", ranked);
                    ansBuild.add("login", "false");
                    ansBuild.add("message", "A session is already open with this UID");
                    
                }
                /*
                *if the valid user is not logged in elsewhere, they will be permitted to login here
                *However, it will also note in the logins map that this user is logged in
                */
                else
                {
                    ansBuild.add("name", name);
                    ansBuild.add("rank", ranked);
                    ansBuild.add("login", true);
                    ansBuild.add("message", "Welcome");
                    isLoggedIn.replace(uid, !logged);
                    ansBuild.add("surveys", getSurveys(uid, ranked));
                    user = new User(uid, ranked, name);
                    update("Login by " +user.getName());
                }
            }
            //the following block takes control only if the UID is not contained in the database
            else
            {
                ansBuild.add("login", false);
                ansBuild.add("message", "This UID is invalid");
            }
           
	    JsonObject loginJsonObject = ansBuild.build();
            
            return loginJsonObject;
        }
        
        /**
         * Reads the Method JSON from the client and routes control flow to the proper route
         * @param input The JSON object that is passed through the 
         * @return Output with which to update the server log as a String
         */
        public String readMethodJSON(JsonObject input)
        {
            String ans = "";
            switch (input.getString("method"))
            {
                case "login" : tryLogin(input.getInt("data"));
                
                case "getSurveys" : getSurveys(user.getRank(), user.getUid());
                    
                case "storeSurvey" : storeSurvey(input.getJsonObject("data"));
                    
                case "stats" : //send stats
                    
                case "logoff" : //log the person off
            }
            return ans;
        }
        
        @Override
        public void run()
        {
          tryLogin(0);  
        }
        
    }
}