/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Bilal
 */
public class tempera {
    static EvalQuestions [] questions;
    public static void main(String[] args) {
        EvalQuestions eq = new EvalQuestions();
        Random rand = new Random(); 
        int value = rand.nextInt(50);
        RetrieveQuestions();
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
        System.out.println(all);
    }
    
    
    
    
    //reads questions from master file
	private static void RetrieveQuestions(){
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
	}
}
