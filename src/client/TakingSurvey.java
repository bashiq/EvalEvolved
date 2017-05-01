package client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import javax.swing.*;

public class TakingSurvey extends JFrame {
	JButton previous, next, submit;
	private EvalQuestions [] questions = null;
	private JPanel qPanel;
	private ArrayList jBoxes;
	private ArrayList<String> userSelectedChoice = new ArrayList<String>();
	private String savedAnswers[];
	private int qCounter=0;
        private String toJsonString =null;

	TakingSurvey (){
            
            this.setTitle("Student Evaluation");
            //window.pack();
            this.setSize(1000, 500);
            this.setLocationRelativeTo(null);
            this.setVisible(true);
            this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {//taken from stackOverflow
            @Override//link: http://stackoverflow.com/questions/15449022/show-prompt-before-closing-jframe
                public void windowClosing(WindowEvent we)
                { 
                    String ObjButtons[] = {"Yes","No"};
                        int PromptResult = JOptionPane.showOptionDialog(null,
                                "Are you sure you want to exit?","You won't",
                                JOptionPane.DEFAULT_OPTION,JOptionPane.YES_NO_OPTION,null,ObjButtons,ObjButtons[1]);
                    if(PromptResult==JOptionPane.YES_OPTION){
                        DisposeWindow();
                    }
                }
            });
        
		RetrieveQuestions();
		//this.add(new JTextArea());
		SouthBar();
		NewQuestion();
	}
	
	//reads questions from master file
	private void RetrieveQuestions(){
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
		savedAnswers = new String[questions.length];
	}
	
	private void SouthBar(){//bottom bar with buttons
		ButtonHandler bh = new ButtonHandler();
		
		JPanel southBar = new JPanel();
		southBar.setLayout(new BorderLayout());
		JPanel innerSB = new JPanel();
		innerSB.add(previous = new JButton("Previous"));
		
		ArrayList<JButton> jNumList = new ArrayList<JButton>();
		JPanel numbers = new JPanel();
		for(int i =0; i < questions.length; i++){//buttons for each numnber
			JButton num = new JButton((i+1)+ "");
			jNumList.add(num);
			num.addActionListener(bh);
			numbers.add(num);
		}
		innerSB.add(numbers);
		
		innerSB.add(next = new JButton("Next"));
		
		
		southBar.add(innerSB);
		southBar.add(submit = new JButton("Submit"), BorderLayout.EAST);
		
		this.add(southBar, BorderLayout.SOUTH);
		
		
        next.addActionListener(bh);
        previous.addActionListener(bh);
        submit.addActionListener(bh);
        
        previous.setEnabled(false);
        submit.setEnabled(false);
        //and other numbers
	}
	
	//need method to rretrive questions and type of answers
	
	
	private void NewQuestion(){
		
		userSelectedChoice.clear();
		
		qPanel = new JPanel();
        qPanel.setLayout(new BoxLayout(qPanel, BoxLayout.Y_AXIS));
		
		radioListener rl1 = new radioListener();
		CheckListener chekListener = new CheckListener();
		
		qPanel.add(new JLabel((qCounter+1) +". " + questions[qCounter].getQuestion()));
		//type of button goes here
		if(questions[qCounter].getButtonType().equals("check")){//checkboxes
			
			jBoxes = new ArrayList<JCheckBox>();
			String[] op = questions[qCounter].getOptions();
			JCheckBox box = null;
			for(int i =0; i < questions[qCounter].getNumberOptions();i++){	
				box = new JCheckBox(op[i]);
				
				CheckInfoBox(op[i], box);//check to see if any box needs to be selected
				jBoxes.add(box);
				box.addItemListener(chekListener);
				qPanel.add((JCheckBox)jBoxes.get(i));
			}
			
		}else{//radiobuttons
			jBoxes = new ArrayList<JRadioButton>();
			String[] op = questions[qCounter].getOptions();
			ButtonGroup bg = new ButtonGroup();
			for(int i =0; i < questions[qCounter].getNumberOptions();i++){
				JRadioButton box = new JRadioButton(op[i]);
				bg.add(box);
				CheckInfoRadio(op[i], box);
				jBoxes.add(box);
				box.addItemListener(rl1);//radio listener
				qPanel.add((JRadioButton)jBoxes.get(i));
			}
		}
		
		//add all
                
		this.add(qPanel, BorderLayout.CENTER);
	}
	
	/**
	 * Takes saved answers and select them if user decides to go back and forth between questions
	 * @param input string of chosen answrs
	 * @param jcb the box so it can be selected
	 */
	void CheckInfoBox(String input, JCheckBox jcb){

    	if(savedAnswers[qCounter] == null)
    		return;
    	if(savedAnswers[qCounter].contains("["))
    		savedAnswers[qCounter] = savedAnswers[qCounter].substring(1, savedAnswers[qCounter].length()-1);
    	String strArray[] = savedAnswers[qCounter].split(",");
    	//ArrayList<String> options = new ArrayList<String> (Arrays.asList(input));
    		
    	for(int i=0; i < strArray.length; i++){
    		if(strArray[i].contains(input))
    			jcb.setSelected(true);
    		
    		userSelectedChoice.add(savedAnswers[qCounter]);
   		}
    }
	
	
	/**
	 * Takes saved answers and select them if user decides to go back and forth between questions
	 * @param input string of chosen answrs
	 * @param jcb the radiobutton so it can be selected
	 */
	void CheckInfoRadio(String input, JRadioButton jcb){
		
    	if(savedAnswers[qCounter] == null)
    		return;
    	if(savedAnswers[qCounter].contains("["))
    		savedAnswers[qCounter] = savedAnswers[qCounter].substring(1, savedAnswers[qCounter].length()-1);
    	String strArray[] = savedAnswers[qCounter].split(",");
    		
    				if(strArray[0].contains(input))
    				jcb.setSelected(true);
    	userSelectedChoice.add(savedAnswers[qCounter]);
    }
	
	
	//for when user advances to any other question
	private void resetQuestion(){
		
		qPanel.removeAll();
		qPanel.revalidate();
		//qPanel.repaint(); dont need 
	}
        
        public String getEvalJson(){
            //this.dispose();
            return toJsonString;
        }
        void DisposeWindow(){
            this.dispose();
        }
        boolean IsAllQ(){
            savedAnswers[qCounter]=(userSelectedChoice.toString());
            for(int i =0; i < savedAnswers.length; i++)
        	if(savedAnswers[i] == null){
                    JOptionPane.showMessageDialog (null, "Question "+ (i+1) + " has not been answered.",
            		"Yeah Ok", JOptionPane.WARNING_MESSAGE);
                    return false;
        	}
                return true;
            }
        
        String toJsonForm (int stuID, int surveyID){
            //DataOutputStream out = new DataOutputStream(sock.getOutputStream());
            //creating json object
            JsonObjectBuilder ultimateBuilder = Json.createObjectBuilder();
            JsonObjectBuilder dataBuilder = Json.createObjectBuilder();
            JsonObjectBuilder questionBuilder = Json.createObjectBuilder();
            JsonArrayBuilder questionArrayBuilder = Json.createArrayBuilder();

            for (int i = 0; i < questions.length; i++) {
                questionBuilder.add("choices",Arrays.toString(questions[i].getOptions()))
                    .add("possibleAnswers", savedAnswers[i]);
            JsonObject jo = questionBuilder.build();
            questionArrayBuilder.add(jo);
            }
            
           // JsonArray jsonarr = loanArrayBuilder.build();
            ultimateBuilder.add("method", "storeSurvey");
            dataBuilder.add("stuID", stuID)
                .add("SurveyID", surveyID)
                .add("Results", questionArrayBuilder);
            ultimateBuilder.add("data", dataBuilder);
            JsonObject ultimateOb = ultimateBuilder.build();
           // out.flush();
            //out.writeUTF(LoanJsonObject.toString());//sending it
            System.out.println(ultimateOb.toString());
            
            String result = (ultimateOb.toString());
            DisposeWindow();
           return ultimateOb.toString();
        }
       
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	/**
     * The listener for receiving action events. When the action event occurs,
     * that object's actionPerformed method is invoked.
     */
    public class ButtonHandler implements ActionListener {

        /**
         * Invoked when the button to submit information is pressed calls a
         * ConnectionTodb will be called return a boolean value if connection was successful
         * @param e in this case it wont do anything except allow program to execute proper calls
         */
        @Override
        public void actionPerformed(ActionEvent ae) {
        	
        	//user must select choice before moving to new screen 
        	if(userSelectedChoice.isEmpty()){
        		JOptionPane.showMessageDialog (null, "Answer question before advancing! ",
        				"Yeah Ok", JOptionPane.WARNING_MESSAGE);
    			return;
    		}
        	
        		savedAnswers[qCounter]=(userSelectedChoice.toString());
        	
        	if(ae.getSource()== next){
        		NextQuestion();
        	}else if(ae.getSource()== previous){
        		PreviousQuestion();
        	}else if(ae.getSource()== submit){
        		;//SubmitEval();//method to see if all questions are answered
        	}else{

        		qCounter = Integer.parseInt(ae.getActionCommand())-1;
        		customQ();
        	}
        	if (qCounter == 0)  previous.setEnabled(false); else previous.setEnabled(true);
    		if (qCounter == (questions.length-1)) next.setEnabled(false); else next.setEnabled(true);
    		if (qCounter == (questions.length-1)) submit.setEnabled(true);
        }
        
        void NextQuestion(){
        	qCounter++;
        	//getInfo();
    		resetQuestion();
    		NewQuestion();
        }
        
        void PreviousQuestion(){
        	qCounter--;
    		resetQuestion();
    		NewQuestion();
        }
        void customQ (){
        	resetQuestion();
    		NewQuestion();
        }
    }
    
    
    /**
     * listener to see if the checkbox has been pressed
     */
    public class CheckListener implements ItemListener {

        @Override//handler method
        /**
         * if box has been pressed some characteristics will be enabled
         */
        public void itemStateChanged(ItemEvent e) {
        	
        	userSelectedChoice.clear();
        	for (JCheckBox checkBox : (ArrayList<JCheckBox>) jBoxes) {
                if (checkBox.isSelected() && !userSelectedChoice.contains(checkBox.getText())) {
                	
                	userSelectedChoice.add(checkBox.getText());//adds anaswe to arraylsit    
                	//System.out.println(checkBox.getText());
                }
        	}
        	// savedAnswers.add(qCounter, userSelectedChoice.toString());
            //for(String s1: userSelectedChoice)
            //System.out.println("here" + userSelectedChoice.toString());
        }
    }
    
    /**
     * listener for radiobuttons. if pressed this class will be called
     */
    public class radioListener implements ItemListener {

        /**
         * If the radiobutton has been changed/pressed some settings will be changed
         * @param e the button pressed
         */
        @Override//handler method
        public void itemStateChanged(ItemEvent e) {
            JRadioButton rbutton = (JRadioButton) e.getSource();
            if (e.getStateChange() == ItemEvent.SELECTED) {
                //save answers here
            	//System.out.print(rbutton.getText());
            	userSelectedChoice.add(rbutton.getText());//adds chosen answer to arraylist
            }
            //savedAnswers.add(qCounter, userSelectedChoice.toString());
        }
    }
	
//	public static void main(String[] args) {
//		 TakingSurvey window = new TakingSurvey();
//		window.setTitle("Student Evaluation");
//		//window.setResizable(false);
//        window.setSize(1100, 900);
//        window.setLocationRelativeTo(null);
//        window.setVisible(true);
//        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	}
}
