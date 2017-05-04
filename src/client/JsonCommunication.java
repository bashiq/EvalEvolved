package client;

import Server.Survey;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;

/**
 * Class is used to communicate with server
 * it is used to help keep seperation of gui and other stuff
 * @author Bilal
 */
public class JsonCommunication
{
    private Socket sock = null;
    private DataInputStream in = null;
    private DataOutputStream out= null;
    private int rank;////////////////////////////////////////////////////////////////////////////////////////////----
    private String serverMessage;
    private JsonReader jr = null;
    private JsonWriter jw= null;
    private int userID=0;
    
    /**
     * constructor will establish first with server
     */
    public JsonCommunication() {
        try {
            sock = new Socket("localhost", 5143);//10.0.67.95", 5143);//10.0.67.95
            
            in = new DataInputStream(sock.getInputStream());
//               // DataInputStream methods = new DataInputStream(sock.getInputStream());
             out = new DataOutputStream(sock.getOutputStream());
             //t = new Thread(new Reciever(sock));
               // t.start();
           
        } catch (IOException ex) {
            ex.getLocalizedMessage();
        }
    }  

    
    /**
     * Method will be used to login a user, server will send back id is proper and if they are already logged in
     * @param input user id
     * @return arraylist of surveys to be further processed
     * @throws IOException brought up in mainframe
     */
    public ArrayList<Survey> loginInfo(int input) throws IOException{
        
        ArrayList<Survey> s1 = new ArrayList<>();
       // jw =Json.createWriter(out);
        JsonObjectBuilder loginBuild = Json.createObjectBuilder();
        JsonObjectBuilder databuild = Json.createObjectBuilder();
        databuild.add("UID", input);///////////////////////////////////////////////////////////////---
        loginBuild.add("method", "login")
                .add("data", databuild);
        
        JsonObject jo = loginBuild.build();

        System.out.println(jo);
        out.flush();
        out.writeUTF(jo.toString());
       // jw.writeObject(jo);
        
        
       JsonReader jr = Json.createReader(new StringReader(in.readUTF()));
        //System.out.println(jr);
        JsonObject jsonObject = jr.readObject();
        jr.close();
        //jr.close();
        
        rank = jsonObject.getInt("rank");
        
        if(jsonObject.getBoolean("login"))
        {
            JsonArray ja = jsonObject.getJsonArray("surveys");
              userID = input;
              
            
            for(Object obj: ja.toArray())//will take input from server and convert it to proper survey object
            {
                JsonObject s = (JsonObject)obj;
                System.out.println(s.toString());
                s1.add(new Survey(s.getInt("surveyNumber"),s.getInt("SID"), s.getString("name"), s.getInt("CID"), s.getString("STATFILE"), s.getInt("COMPLETION")));
            }
//            System.out.println(s1.get(0).getSID());
        }
        else{
            serverMessage = jsonObject.getString("message");
           // System.out.println(serverMessage);
        }
        //jw.close();
       return s1;
    }
    
    /**
     * Method will send student completed eval to server, will recieve confirmation if successful
     * @param input json object created intaking survey
     * @return arraylist
     * @throws java.io.IOException taken care in another function
     */
    public ArrayList<Survey> StoreStuEvalResults(JsonObject input) throws IOException{
        //out.flush();
        JsonWriter j1 = Json.createWriter(out);
       ArrayList<Survey> out1 = null;
        //send input
        //jw.writeObject(input);
       // recieve confirmation
        //System.out.println(input);
        
       j1.writeObject(input);
        System.out.println(input);
       //j1.close();
       JsonReader jr1 = Json.createReader(in);//reading in json string
        JsonObject jObj = jr1.readObject();
        System.out.println("in "+jObj);
        //jr1.close();
        System.out.println("status "+jObj.getBoolean("status"));
//         JsonArray ja = jObj.getJsonArray("surveys");
////            
//            JsonObject s= null;
//            for(Object obj: ja.toArray())
////            {
//                 s= (JsonObject)obj;
//                out1.add(new Survey(s.getInt("surveyNumber"), s.getInt("SID"),s.getString("name"), s.getInt("CID"), s.getString("STATFILE"), s.getInt("COMPLETION")));
////            }
//            System.out.println(out1.get(0).getCID());
        return null;
    }
    
    
    
    /**
     * Method is used to get course stats
     * @return String or arraylist of survey objs
     * @throws IOException called in facCourseList
     */
    public String CourseStats() throws IOException{
        JsonObjectBuilder loginBuild = Json.createObjectBuilder();
        loginBuild.add("method", "courseStatView")
                .add("data", userID)
                .add("courseID", "Ph202");//fix
        
        JsonObject jo = loginBuild.build();
       //JsonWriter j1 = Json.createWriter(out);
     //  j1.writeObject(jo);
       // JsonReader jr1 = Json.createReader(in);//reading in json string
        //JsonObject jObj = jr1.readObject();
        //System.out.println(jObj.toString());
       //jObj.get("data");
        return questions();
    }
    
    
    
    /**
     * Logg of function to properly close sockets and etc
     * @throws IOException will be taken care in another method
     */
    public void LogOff() throws IOException{
        
        if(userID !=0 ){
        JsonObjectBuilder logOffBuild = Json.createObjectBuilder();
        
        logOffBuild.add("method", "logOff");
        JsonObject jo = logOffBuild.build();
        System.out.println();
        out.flush();
        out.writeUTF(jo.toString());
        JsonReader jr = Json.createReader(new StringReader(in.readUTF()));
        JsonObject inObj = jr.readObject();
        jr.close();
        if(inObj.getBoolean("logOut")){
            in.close();
            out.close();
            sock.close();
//            System.out.println("here");            
        }else
            System.out.println("error");
        }
    }

    /**
     * Used to get rank of user
     * @return the rank as an int, will return -1 if invalid id or already logged in
     */
    public int getRank() {
        return rank;
    }

    /**
     * if the user has an invalid id or already logged in this method will provide reason
     * @return server message as a string
     */
    public String getServerMessage() {
        return serverMessage;
    }

    public int getUserID() {
        return userID;
    }
    
    ////////////////////////////////////////////////
    
    //reads questions from master file
	private static EvalQuestions[] RetrieveQuestions(){
            EvalQuestions questions[] = null;
		ArrayList<EvalQuestions>inputq= null;
		try (ObjectInputStream input
                = new ObjectInputStream(new FileInputStream("test1.txt"))) {
            inputq = (ArrayList<EvalQuestions>)input.readObject();
            questions = inputq.toArray(new EvalQuestions[inputq.size()]);
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
                return questions;
	}
        private String questions(){
        EvalQuestions [] questions;
        EvalQuestions eq = new EvalQuestions();
        Random rand = new Random(); 
        int value = rand.nextInt(50);
        questions= RetrieveQuestions();
        String all = "";
        String [] options= new String [5];
        for(int i = 0; i< questions.length; i++){
            all+= (i+1) + ". " + questions[i].getQuestion()+ '\n';
            
            options=questions[i].getOptions();
            for(int j = 0; j< questions[i].getNumberOptions(); j++){
             all += options[j] + ": "+ rand.nextInt(7) +"\n";   
            }
            all+= '\n';
        }
        return all;
    }
}