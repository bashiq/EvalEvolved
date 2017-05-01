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


public class JsonCommunication
{
    private Socket sock = null;
    private DataInputStream in = null;
    private DataOutputStream out= null;
    private int rank = 0;////////////////////////////////////////////////////////////////////////////////////////////----
    private String serverMessage;
    private JsonReader jr = null;
    private JsonWriter jw= null;
    private int userID;
    
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
     * @return 
     * @throws IOException 
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
//        Survey sd = new Survey(333,444,"yay", 2);
//        sd.setCourse("pjo2");
//        Survey sd4 = new Survey(333, 555, "yuk", 0);
//        sd4.setCourse("yoyoma");
//        s1.add(sd);
//        s1.add(sd4);
        System.out.println(jo);
        
        jw.writeObject(jo);
        
        jr = Json.createReader(in);//reading in json string
        //System.out.println(jr);
        JsonObject jsonObject = jr.readObject();
       // JsonObject survs = jr.readObject();
        //jr.close();
        boolean check = jsonObject.getBoolean("login");
        
        if(check)
        {
            JsonArray ja = jsonObject.getJsonArray("surveys");
              userID = input;
              rank = jsonObject.getInt("rank");
            
            for(Object obj: ja.toArray())
            {
                JsonObject s = (JsonObject)obj;
                s1.add(new Survey(s.getInt("SID"), s.getInt("CID"), s.getString("STATFILE"), s.getInt("COMPLETION")));
            }
            System.out.println(s1.get(0));
        }
        else{
            rank = -1;
            serverMessage = jsonObject.getString("message");
            System.out.println(serverMessage);
        }
       return s1;
    }
    
    /**
     * Method will send student completed eval to server, will recieve confirmation if successful
     * @param input
     * @return 
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
    
    public String CourseStats(String input) throws IOException{
        JsonObjectBuilder loginBuild = Json.createObjectBuilder();
        loginBuild.add("method", "courseStatView")
                .add("data", input)
                .add("courseID", "Ph202");
        
        JsonObject jo = loginBuild.build();
       // out.writeUTF(jo.toString());
        System.out.println(in.readUTF());
        return "s1";
    }
    
    public void LogOff() throws IOException{
        JsonObjectBuilder logOffBuild = Json.createObjectBuilder();
        JsonObjectBuilder databuild = Json.createObjectBuilder();
        databuild.add("UID", this.getUserID());///////////////////////////////////////////////////////////////---
        logOffBuild.add("method", "logOff");
                //.add("data", databuild);
        
        JsonObject jo = logOffBuild.build();
        if(jw != null){
            jw.writeObject(jo);
//            jw.close();
//            jr.close();
//            in.close();
//            out.close();
            sock.close();
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
    
    
    
    //
    void forKireh() throws IOException{//user login
        String callMethod;
        int userID;
         JsonReader jr = Json.createReader(new StringReader(in.readUTF()));//reading in json string

        JsonObject jsonObject = jr.readObject();
        jr.close();
        callMethod = jsonObject.getString("method");
        userID =jsonObject.getInt("data");
        ////////////////////////////////////////////////////////////////////////
        
        
    }
    
    //public static void main(String[] args) throws IOException {
      //  JsonCommunication j = new JsonCommunication ();
     //j.loginInfo(3);   
   //  j.forKireh();
    //}
}