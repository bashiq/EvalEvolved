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
    Socket sock;
    DataInputStream in;
    DataOutputStream out;

    public JsonCommunication() {
        try {
            sock = new Socket("10.200.48.181", 5143);//10.0.67.95

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
        JsonWriter jw =Json.createWriter(out);
        JsonObjectBuilder loginBuild = Json.createObjectBuilder();
        JsonObjectBuilder databuild = Json.createObjectBuilder();
        databuild.add("UID", 12345);
        loginBuild.add("method", "login")
                .add("data", databuild);
        
        JsonObject jo = loginBuild.build();
        System.out.println(jo);
        
        jw.writeObject(jo);
       // out.write(jo);
        //String temp = in.readUTF();//from server
        //String temp2 = in.readUTF();
        
        JsonReader jr = Json.createReader(in);//reading in json string
        //System.out.println(jr);
        JsonObject jsonObject = jr.readObject();
       // JsonObject survs = jr.readObject();
        //jr.close();
        boolean check = jsonObject.getBoolean("login");
        
        if(check)
        {
            JsonArray ja = jsonObject.getJsonArray("surveys");
            
            //JsonObject s4[] = (JsonObject[]) ja.toArray(s.getInt("SID"), s.getInt("CID"), s.getString("STATFILE"), s.getInt("COMPLETION"));
            for(Object obj: ja.toArray())
            {
                JsonObject s = (JsonObject)obj;
                s1.add(new Survey(s.getInt("SID"), s.getInt("CID"), s.getString("STATFILE"), s.getInt("COMPLETION")));
            }
            System.out.println(s1.get(0));
        }
       return s1;
    }
    
    /**
     * Method will send student completed eval to server, will recieve confirmation if successful
     * @param input
     * @return 
     */
    public Survey StoreStuEvalResults(String input) throws IOException{
        Survey s1 = null;
        //send input
        out.writeUTF(input);
       // recieve confirmation
        in.readUTF();
        return s1;
    }
    
    public String CourseStats(String input) throws IOException{
        JsonObjectBuilder loginBuild = Json.createObjectBuilder();
        loginBuild.add("method", "courseStatView")
                .add("data", input)
                .add("courseID", "ph202");
        
        JsonObject jo = loginBuild.build();
        out.writeUTF(jo.toString());
        System.out.println(in.readUTF());
        return "s1";
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