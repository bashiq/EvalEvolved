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

/**
 * This class will create the txt file containg the surveys
 * @author Bilal
 */
public class EvalQuestions implements java.io.Serializable {
	private String question;
	private String [] options;
	private String buttonType;
	
        /**
         * defualt constructors with generic answers
         */
public EvalQuestions() {
	question = "How many tries?";
	options = new String []{"1", "2", "3"};
	setButtonType("radio");
		
	}
	/**
         * Overloaded constructor for creating a question
         * @param question string questions
         * @param options string array of possible answers to choose from
         * @param buttonType enter either "check" or "radio" for appriopriate selection
         */
	public EvalQuestions(String question, String[] options, String buttonType) {
		this.question = question;
		this.options = options;
		this.buttonType = buttonType;
	}

        /**
         * Returns the question text for an EvalQuestion object
         * @return string question
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

        /**
         * set possible answers for a question
         * @param options as an array
         */
	public void setOptions(String[] options) {
		this.options = options;
	}


        /**
         * Method will tell user what type of button is current to question
         * @return type of button being used
         */
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
		//String [] ans1 = {"Learning Center", "CyberLab", "EELab", "Fusion Lab","Tutor"};
		//list1.add(new EvalQuestions("Which resourses did you access while in this class: (Select all that apply)", ans1,"check"));
		
                
                //questions generated for eval
		String [] choices2 = {"Strongly Disagree", "Disagree", "Neither Agree Nor Disagree", "Agree","Strongly Agree"};
                list1.add(new EvalQuestions("The instructor posed questions designed to promote critical thinking.", choices2,"radio"));
                
		list1.add(new EvalQuestions("Course Objectives were clearly defined.", choices2,"radio"));
                System.out.println("");
		list1.add(new EvalQuestions("The course objectives were accomplished.", choices2, "radio"));
                list1.add(new EvalQuestions("The labs demonstrated and reinforced the course objectives.", choices2, "radio"));
                list1.add(new EvalQuestions("The instructor was well prepared to present and discuss course material.", choices2, "radio"));
                System.out.println("here");
		//EvalQuestions []  eq1= (EvalQuestions[]) list1.toArray(); if need be
		for(EvalQuestions eq: list1){	System.out.println(eq);}
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


            hope = (ArrayList<EvalQuestions>)input.readObject();//personal checking to make sure file is written properly
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
     //   System.out.println("here we go"+ hope);
		
	}
}