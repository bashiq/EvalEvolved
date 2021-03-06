/**
 *
 */
package client;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author Bilal
 *This class is basically just a gui to display and help guide the user to enter an ID
 */
public class LoginWindow extends JPanel {

    private JTextField id;
    
    JButton loginbttn;
    private boolean status = false;

    /**
     * used to create and display components on screen
     */
    LoginWindow() {
    	
        this.setLayout(new GridLayout(3, 1));
        this.add(new JLabel("Welcome to EvalEvolved. Enter UserID"));
        this.add(id = new JTextField());
        this.add(loginbttn = new JButton("Login"));

       // ButtonHandler bh = new ButtonHandler();
        //loginbttn.addActionListener(bh);
    }
    
    boolean GetStatus (){
        return status;
    }
    public String getId() {
        return id.getText();
    }

    //not used any more button handler in mainframe is used
    class ButtonHandler implements ActionListener {

        /**
         * Invoked when the button to submit information is pressed calls a
         * ConnectionTodb will be called return a boolean value if connection
         * was successful
         *
         * @param e in this case it wont do anything except allow program to
         * execute proper calls
         */
        @Override
        public void actionPerformed(ActionEvent ae) {
            //magic happens here
            if (id.getText().trim().equals("")) {

                JOptionPane.showMessageDialog(null, "Field is empty, enter user ID",
                        "Yeah Ok", JOptionPane.WARNING_MESSAGE);
                return;
            } else {
                try{
                int blah = Integer.parseInt(id.getText());
                if(blah ==24){
                //send info to db for authentication
                System.out.println("successful");
                status = true;
                }
                }catch(NumberFormatException ex){
                        JOptionPane.showMessageDialog(null, "Enter proper ID",
                        "Yeah Ok", JOptionPane.WARNING_MESSAGE);
                        }
            }

        }
    }
}
