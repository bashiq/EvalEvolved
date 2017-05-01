package client;

import Server.Survey;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.ArrayList;
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
            sock = new Socket("10.0.67.95", 5143);//10.0.67.95

           in = new DataInputStream(sock.getInputStream());
            out = new DataOutputStream(sock.getOutputStream());
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
        jw =Json.createWriter(out);
        JsonObjectBuilder loginBuild = Json.createObjectBuilder();
        JsonObjectBuilder databuild = Json.createObjectBuilder();
        databuild.add("UID", input);///////////////////////////////////////////////////////////////---
        loginBuild.add("method", "login")
                .add("data", databuild);
        
        JsonObject jo = loginBuild.build();
//        Survey sd = new Survey(333,444,"yay", 2);//create temp survey objs
//        sd.setCourse("pjo2");
//        Survey sd4 = new Survey(333, 555, "yuk", 0);
//        sd4.setCourse("yoyoma");
//        s1.add(sd);
//        s1.add(sd4);
        //System.out.println(jo);
        
        jw.writeObject(jo);
        
        jr = Json.createReader(in);//reading in json string
        //System.out.println(jr);
        JsonObject jsonObject = jr.readObject();
        //jr.close();
        boolean check = jsonObject.getBoolean("login");
        rank = jsonObject.getInt("rank");
        if(check)
        {
            JsonArray ja = jsonObject.getJsonArray("surveys");
              userID = input;
              
            
            for(Object obj: ja.toArray())//will take input from server and convert it to proper survey object
            {
                JsonObject s = (JsonObject)obj;
                s1.add(new Survey(s.getInt("SID"), s.getInt("CID"), s.getString("STATFILE"), s.getInt("COMPLETION")));
            }
            System.out.println(s1.get(0).getSID());
        }
        else{
            serverMessage = jsonObject.getString("message");
           // System.out.println(serverMessage);
        }
       return s1;
    }
    
    /**
     * Method will send student completed eval to server, will recieve confirmation if successful
     * @param input json object created intaking survey
     * @return arraylist
     */
    public ArrayList<Survey> StoreStuEvalResults(JsonObject input) throws IOException{
       ArrayList<Survey> out = null;
        //send input
        //jw.writeObject(input);
       // recieve confirmation
       jw.writeObject(input);
        JsonObject jObj = jr.readObject();
        jObj.getBoolean("status");
         JsonArray ja = jObj.getJsonArray("surveys");
//            
            JsonObject s= null;
            for(Object obj: ja.toArray())
//            {
                 s= (JsonObject)obj;
                out.add(new Survey(s.getInt("SID"), s.getInt("CID"), s.getString("STATFILE"), s.getInt("COMPLETION")));
//            }
        return out;
    }
    /**
     * Method is used to get course stats
     * @param input
     * @return String or arraylist of survey objs
     * @throws IOException called in facCourseList
     */
    public String CourseStats(String input) throws IOException{
        JsonObjectBuilder loginBuild = Json.createObjectBuilder();
        loginBuild.add("method", "courseStatView")
                .add("data", input)
                .add("courseID", "Ph202");//fix
        
        JsonObject jo = loginBuild.build();
       // out.writeUTF(jo.toString());
        System.out.println(in.readUTF());
        return "s1";
    }
    
    /**
     * Logg of function to properly close sockets and etc
     * @throws IOException 
     */
    public void LogOff() throws IOException{
        JsonObjectBuilder logOffBuild = Json.createObjectBuilder();
        JsonObjectBuilder databuild = Json.createObjectBuilder();
       // databuild.add("UID", this.getUserID());///////////////////////////////////////////////////////////////---
        logOffBuild.add("method", "logOff");
                //.add("data", databuild);
        
        JsonObject jo = logOffBuild.build();
        System.out.println();
        //jw.writeObject(jo);
        if(userID != 0)//){jw != null){
            
//            jw.close();
//            jr.close();
            in.close();
            out.close();
            sock.close();
            System.out.println("here");
      //  }
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
}