package client;

import Server.Survey;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Class will show student completed and uncompleted surveys
 * if uncompleted they can do it
 * @author Bilal
 */
public class StudentSurveyView extends JPanel{
	private JList availibleList, completedList;
	private JButton next;
	private String[] completedStr;//actual data from serever will go here
        private String[] uncompStr;
	private String chosenSurvey= "", results;
	private JsonCommunication jcom;
        private int stuID, fkcourseID;
        private ArrayList<Survey> surveyList;
        
        /**
         * Default constructor used to pass in and create gui for this panel
         * @param jcom The JSoncommunication class
         * @param sur survey arraylist to be displayed
         */
	StudentSurveyView(JsonCommunication jcom, ArrayList<Survey> sur){
            this.SetSurveyList(sur);
            this.jcom = jcom;
            DisplayElements();
	}
        
        /**
         * Will create and display gui elements
         */
	private void DisplayElements(){
            this.setLayout(new BorderLayout());
		this.add(new JLabel("Student Survey List",SwingConstants.CENTER), BorderLayout.NORTH, JLabel.CENTER);
		JPanel main = new JPanel ();
		main.setLayout(new GridLayout(5,1));
		
		//information recieved from server will be displayed here
		//possibly blah.getStudentcompletedList surveys and uncompleted as string arr
		main.add(new JLabel("Availible surveys", SwingConstants.CENTER));
		availibleList = new JList(uncompStr);
		availibleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		availibleList.setFixedCellWidth(250);
		
		
		JPanel advancer = new JPanel();
		advancer.add(availibleList, BorderLayout.CENTER);
		advancer.add(next = new JButton("Take survey"), BorderLayout.EAST);
		
		main.add(advancer);
		
		main.add(new JLabel("Submitted Surveys", SwingConstants.CENTER));
		completedList = new JList(completedStr);
                completedList.setEnabled(false);
		main.add(completedList);
		
		this.add(main, BorderLayout.CENTER);
		
		//handlers
		ButtonHandler bh = new ButtonHandler();
        next.addActionListener(bh);
        SurveyChoiceListener listener = new SurveyChoiceListener();//listener for countries
        availibleList.addListSelectionListener(listener);
		
	}

        /**
         * A method that will be used by the main frame to pass in complete and uncomplete surveys
         * @param surveyList the surveylist as an arraylist
         */
    public void SetSurveyList(ArrayList<Survey> surveyList) {
        this.surveyList = surveyList;
        completedStr = new String [surveyList.size()];
        uncompStr = new String [surveyList.size()];
        for(int i = 0; i < surveyList.size(); i++){
            if(surveyList.get(i).isCompleted()){
                System.out.println(surveyList.get(i).isCompleted());
                completedStr[i] = surveyList.get(i).getCourse();
            }else{
                uncompStr[i]= surveyList.get(i).getCourse();
                System.out.println(surveyList.get(i).getCourse());
                System.out.println("in ssv "+ surveyList.get(i).getSurveyNumber());
            }
        }
    }
    private void Revalidate(){
        this.removeAll();
        this.revalidate();
        //this.repaint();
    }
        
        
        
	
     /**
     * The listener for receiving action events. When the action event occurs,
     * that object's actionPerformed method is invoked.
     */
    public class ButtonHandler implements ActionListener {

        /**
         * Invoked when the button to submit information is pressed calls popupsurvey
         */
        @Override
        public void actionPerformed(ActionEvent ae) {
            
            if(chosenSurvey == ""){
                JOptionPane.showMessageDialog (null, "Select an Eval to Continue",
            		"YEAH", JOptionPane.INFORMATION_MESSAGE);
                    return;
            }
        	
            //magic happens here
            System.out.println(chosenSurvey);
            PopUpSurvey();
            
        }
    }
    /**
     * This method will create a popup window of Takingsurvey
     */
    public void  PopUpSurvey (){ 
        //this.removeAll();
        //this.revalidate();
        //this.repaint();
  
        TakingSurvey taksur = new TakingSurvey();
        taksur.setVisible(true);
        
        taksur.submit.addActionListener(new ActionListener(){//if submit button is pressed on eval
        public void actionPerformed(ActionEvent ae) {
            ArrayList<Survey> incoming= null;
            if(taksur.IsAllQ()){//check to see if all questions were answered
                try {
                    incoming =jcom.StoreStuEvalResults(taksur.toJsonForm(surveyList.get(fkcourseID).getSurveyNumber(), surveyList.get(fkcourseID).getCID()));
                    //System.out.println(fkcourseID);
                } catch (IOException ex) {
                    Logger.getLogger(StudentSurveyView.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(incoming == null){//if null that may mean it wasnt successful
                    JOptionPane.showMessageDialog (null, "success or failure",
            		"YEAH", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                SetSurveyList(incoming);//recreate jpanel with current data
                Revalidate();
                DisplayElements();
                //completedList.add(surveyList.get(fkcourseID).getCourse());
                //refreshwindow

            }
        }   
        });        
    	//this.getContentPane().repaint();
    }
    
    /**
     * Listener that is notified when a survey is chosen
     */
    public class SurveyChoiceListener implements ListSelectionListener {

        /**
         * called when selection value is changed
         * @param e the source of the event
         */
        @Override
        public void valueChanged(ListSelectionEvent e) {
            JList cbox = (JList) e.getSource();
            
            chosenSurvey = uncompStr[cbox.getSelectedIndex()];
            fkcourseID = cbox.getSelectedIndex();
        }
    }

}
