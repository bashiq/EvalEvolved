package client;

import java.awt.List;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import Server.Survey;

public class EvalQuestions implements java.io.Serializable {
	private String question;
	private String [] options;
	private String buttonType;
	
public EvalQuestions() {
	question = "How many tries?";
	options = new String []{"1", "2", "3"};
	setButtonType("radio");
		
	}
	
	public EvalQuestions(String question, String[] options, String buttonType) {
		this.question = question;
		this.options = options;
		this.buttonType = buttonType;
	}

        /**
         * Returns the question text for an EvalQuestion object
         * @return 
         */
	public String getQuestion() {
		return question;
	}


        /**
         * Sets the text of the question to user-given String
         * @param question new question text as a String
         */
	public void setQuestion(String question) {
		this.question = question;
	}


        /**
         *Gets an array of the options for an EvalQuestion object
         * @return List of answer options as a String[]
         */
	public String[] getOptions() {
		return options;
	}
	
	/**
	 * tells how many options there are
	 * @return number of options as an int
	 */
	public int getNumberOptions(){
		return options.length;
	}


	public void setOptions(String[] options) {
		this.options = options;
	}


	public String getButtonType() {
		return buttonType;
	}


	/**
	 * Button type can only be radio or check
	 * @param bt only choose "radio" or "check"
	 */
	public void setButtonType(String bt) {
		switch(bt){
		case "radio":
			buttonType = bt;
		case "check":
			buttonType = bt;
		default:
			buttonType = "radio";
		}
	}
	

	@Override
	public String toString() {
		return "EvalQuestions [question=" + question + ", options=" + Arrays.toString(options) + ", buttonType="
				+ buttonType + "]";
	}

	
	
	
	
	public static void main (String [] args){
		ArrayList<EvalQuestions>  list1 = new ArrayList <EvalQuestions> ();	
		String [] choices = {"hw", "quizes", "exam"};
		list1.add(new EvalQuestions("which do you like?", choices,"check"));
		
		String [] choices2 = {"sabbah", "broom", "smith"};
		list1.add(new EvalQuestions("Favorite professor?", choices2,"radio"));
		list1.add(new EvalQuestions());
	
		//EvalQuestions []  eq1= (EvalQuestions[]) list1.toArray(); if need be
		//for(EvalQuestions eq: list1){	System.out.println(eq);}
		try{
		ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("test1.txt", false));
        //EvalQuestions [] b1 = list1.toArray(new EvalQuestions[list1.size()]);
        //System.out.println("t1" + b1[0].getButtonType());
			output.writeObject(list1);//list1.toArray(new EvalQuestions[list1.size()]));
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.getMessage();
		}
		ArrayList<EvalQuestions> hope= null;
		try (ObjectInputStream input
                = new ObjectInputStream(new FileInputStream("test1.txt"))) {


            hope = (ArrayList<EvalQuestions>)input.readObject();
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
		
        System.out.println("here we go"+ hope);
		
		
		
	}
}