package Server;

import java.awt.Dimension;
import java.awt.ScrollPane;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonWriter;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * This class encapsulates a running Evaluation server that will be able to sned, process, and receive course evaluation surveys
 * @author kireh
 */
public class EvalServer extends JFrame 
{
        //network instance variables
        ServerSocket service = null;
        Thread t;
	static ArrayList<Socket> clients; 
        static ArrayList<Thread> clientThreads;
        
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
        HashMap<Integer, Integer> ranks = new HashMap<>();
        HashMap<Integer, String> people = new HashMap<>();
        HashMap<Integer, Boolean> isLoggedIn = new HashMap<>();//keeps track of who logs in
        HashMap<Integer, Course> courses = new HashMap<>();//keeps track of courses 
        
        /**
         * Creates a new EvalServer object, connects it to the database, and loads in data structures for login
         */
        public EvalServer()
        {
            super("EvalEvolved Server Software");
            this.setSize(new Dimension(500, 600));
            this.setResizable(false);
            logs = new JTextArea(300,600);
            this.add(new JScrollPane(logs));
            this.setAlwaysOnTop(true);
            try
            {
                update("Connecting to database");
                Class.forName("org.apache.derby.jdbc.ClientDriver");  
                db = DriverManager.getConnection("jdbc:derby://localhost:1527/sample", "app", "app");
                update("Connected to database");
                //addSurvey = db.prepareStatement("INSERT INTO SURVEY VALUES (?,?,?,?,?,?,?");
                
                //The following block compiles a list of all of the people who are able to sign into the
                update("Loading data structures");
                
                //The following lines get a resultSet of all legal IDs
                loginQuery = db.prepareStatement("SELECT * FROM PERSON");
                ResultSet logins = loginQuery.executeQuery();
                
                /*
                The following block will translate the resultSet into Maps of users' names, ranks and login states
                */
                while (logins.next())
                {
                    Integer personID = logins.getInt("PID");
                    String fullName = logins.getString("FNAME") + " " +logins.getString("LNAME");
                    people.put(personID, fullName);
                    isLoggedIn.put(personID, false);
                    ranks.put(personID, logins.getInt("RANK"));
                }
                //the server is ready for logins at this point
                update("Ready for login");
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
        private void update(String s)
        {
            //The following lines get the proper time
            Calendar c = Calendar.getInstance(TimeZone.getDefault());
            String time ="[" +c.get(Calendar.MONTH)+"/"+c.get(Calendar.DATE)+"/"+c.get(Calendar.YEAR)+"@" +c.get(Calendar.HOUR_OF_DAY)+":" +c.get(Calendar.MINUTE) +"] "; 
            
            logs.append("\n" +time +s);
        }
	/*
	 * Student methods
	 * -return list of surveys with a status for each
	 * +receive finished surveys and...
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
                    studentSurveys= db.prepareStatement("Select * from SURVEY where STUDENT=?");
                    studentSurveys.setInt(1, id);
                    rs = studentSurveys.executeQuery();
                    while (rs.next())
                    {
                        JsonObjectBuilder surveyBuilder = Json.createObjectBuilder();
                        surveyBuilder.add("SID", rs.getInt("STUDENT"));
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
                        surveyBuilder.add("SID", rs.getInt("STUDENT"))
                                .add("surveyNumber",rs.getInt("NUM"))
                                .add("name",rs.getInt("NAME"));
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
                        surveyBuilder.add("SID", rs.getInt("STUDENT"))
                                .add("surveyNumber",rs.getInt("NUM"))
                                .add("name",rs.getInt("NAME"));
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
                        surveyBuilder.add("SID", rs.getInt("STUDENT"))
                                    .add("surveyNumber",rs.getInt("NUM"))
                                    .add("name",rs.getInt("NAME"));
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
                    statPath = rs.getString("STATFILE");
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

            return ansBuild.build();
        }
        
        public void threadCreate(DataInputStream is, DataOutputStream os, InetAddress newAdd)
        {
            Thread t = new Thread(new ServerTask(is, os));
            t.start();
            update("New thread started at " +newAdd.toString());
        }
        
            
        public static void main(String[] args)
        {
            EvalServer demo = new EvalServer();
            demo.setVisible(true);
            demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            ServerSocket service = null;//declares the ServerSocket to run the service
            clients = new ArrayList<>();
		/*
		 * Establishes the ServerSocket and continues to listen for new connections
		 */
		try 
		{
			service = new ServerSocket(5143);//creates a new TCP socket connection at the specified port number
			Socket client= null;//holder variable to track the client socket
                        
			
			while (true)
			{
				client=service.accept(); //until a client connects, the server will  listen for a connection
                                
				demo.threadCreate(new DataInputStream(client.getInputStream()), new DataOutputStream(client.getOutputStream()), client.getInetAddress());
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
        DataInputStream input;
        DataOutputStream output;
        
        public ServerTask(DataInputStream is, DataOutputStream os)
        {
            super();
            input = is;
            output = os;
        }
        
        /**
         * Attempt a login with the given UserID
         * @param data login message and proposed user ID as a JsonObject
         * @return  A JsonObject containing login success as a boolean and 
         * appropriate list of courses or surveys as a JsonArray
         */
        public JsonObject tryLogin(JsonObject data)
        {   
            //answer variable
            JsonObjectBuilder ansBuild = Json.createObjectBuilder();
                        
            //instantiate list of appropriate surveys
            JsonArrayBuilder surveys = Json.createArrayBuilder();
            JsonObjectBuilder surveyBuilder = Json.createObjectBuilder();
            
            //UID holder variable
            Integer uid = data.getInt("UID");
            
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
                    //ansBuild.add("rank", ranked);
                    ansBuild.add("login", "false");
                    ansBuild.add("message", "A session is already open with this UID");
                    update("Failed login attempt by " +name);
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
                ansBuild.add("message", "This UID (" +uid +") is invalid. \nPlease try again.");
                ansBuild.add("rank", -1);
                update("Invalid login attempt");
            }
           //return the loginJSON
	    JsonObject loginJsonObject = ansBuild.build();
            System.out.println("faculty" + loginJsonObject);
            return loginJsonObject;
        }
        
        /**
         * Attempts to log out a user so that they may start another session without restarting the server
         */
        public JsonObject tryLogOut()
        {
            JsonObjectBuilder ansBuild = Json.createObjectBuilder();
            String name = user.getName();
            int uid = user.getUid();
            boolean ans = isLoggedIn.get(uid);
            //if the user is logged in, this code will execute
            if (ans)
            {
                isLoggedIn.replace(uid, !ans);
                ansBuild.add("logOut", true);
                update(name +"has logged out");
            }
            //if the user is not logged in, this code will execute
            else
            {
                ansBuild.add("logout", false)
                        .add("message", "Connection Error");
            }
            return ansBuild.build();
        }
        
        /**
         * Reads the Method JSON from the client and routes control flow to the proper route
         * @param input The JSON object that is passed through the 
         * @return Output to send to the client as a JsonStructure
         */
        public JsonStructure readMethodJSON(JsonObject input)
        {
            String ans = "";
            switch (input.getString("method"))
            {
                case "login" : return tryLogin(input.getJsonObject("data"));
                                    
                case "getSurveys" : return getSurveys(user.getRank(), user.getUid());
                    
                case "returnSurvey" : return storeSurvey(input.getJsonObject("data"));
                    
                case "stats" : //send stats
                    
                case "logOff" : return tryLogOut();
            }
            return null;
        }
        
        @Override
        public void run()
        {
            try
            {
                while(true){
                JsonReader jr =Json.createReader(new StringReader(input.readUTF()));
                //JsonWriter jw = Json.createWriter(output);
                //DataInputStream read = new DataInputStream(input);
                
              //  while (input.available()>0)
                //{ 
                    //output.writeUTF("The server is still connected");
                    JsonObject in = jr.readObject();
                    jr.close();
                    //System.out.println(in.toString());
                    JsonObject ans = (JsonObject)readMethodJSON(in);
                    System.out.println("here "+ans);
                    //jw.writeObject(ans);
                    output.flush();
                    output.writeUTF(ans.toString());
                }
            } catch (IOException ex)
            {
                Logger.getLogger(EvalServer.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
        }  
    }
