package client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class StudentSurveyView extends JFrame{
	private JList availible, completed;
	private JButton next;
	private String[] str = {"Dummy Data", "D2"};//actual data from serever will go here
	private String chosenSurvey;
	
	StudentSurveyView(){
		
		DisplayElements();
		
	}
	private void DisplayElements(){
		this.add(new JLabel("Student Survey List"), BorderLayout.NORTH, JLabel.CENTER);
		JPanel main = new JPanel ();
		main.setLayout(new GridLayout(5,1));
		
		//information recieved from server will be displayed here
		//possibly blah.getStudentcompleted surveys and uncompleted as string arr
		main.add(new JLabel("Availible surveys"));
		availible = new JList(str);
		availible.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		availible.setFixedCellWidth(250);
		
		
		JPanel advancer = new JPanel();
		advancer.add(availible, BorderLayout.CENTER);
		advancer.add(next = new JButton("Take survey"), BorderLayout.EAST);
		
		main.add(advancer);
		
		main.add(new JLabel("Submitted Surveys"));
		completed = new JList(str);
		main.add(completed);
		
		this.add(main, BorderLayout.CENTER);
		
		//handlers
		ButtonHandler bh = new ButtonHandler();
        next.addActionListener(bh);
        SurveyChoiceListener listener = new SurveyChoiceListener();//listener for countries
        availible.addListSelectionListener(listener);
		
	}

	public static void main(String[] args) {
		StudentSurveyView window = new StudentSurveyView();
		window.setTitle("Student Survey View");
		window.setResizable(false);
        window.setSize(1100, 900);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
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
        	//if(chosenSurvey.equals(null))//do nothing
        		//return;
        	
        	//magic happens here
        		System.out.println(chosenSurvey);
        		Tester();
        		
        }
    }
    
    public void  Tester (){ 
    	this.getContentPane().removeAll();//this will clear frame
    	this.getContentPane().revalidate();
    	this.getContentPane().repaint();
    	
		this.add(new JLabel("Hello"), BorderLayout.CENTER);
    	
    	
    }
    
    /**
     * Listener that is notified when a country is chosen
     */
    public class SurveyChoiceListener implements ListSelectionListener {

        /**
         * called when selection value is changed
         * @param e
         */
        @Override
        public void valueChanged(ListSelectionEvent e) {
            JList cbox = (JList) e.getSource();
            
            chosenSurvey = str[cbox.getSelectedIndex()];
        }
    }

}
