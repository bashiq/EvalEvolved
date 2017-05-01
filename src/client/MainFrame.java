package client;

import Server.Survey;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * This class will start the program. Depending on the userID and its rank the user will directed
 * to its proper screen for further processing.
 * @author Bilal
 */
public class MainFrame extends JFrame {

    private CardLayout mainWindowLayout;
    // mainPanel will be used as the MainFrame's main unit to manage Components
    private JPanel mainPanel;
    private LoginWindow logWin;
    private StudentSurveyView stuSurView;
    JsonCommunication jcom;
    FacultyCourseListView facView;

    /**
     * This method will add all the visual components to the Jframe
     */
    MainFrame() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());//taken from stackoverflow
        } catch (Exception e) {//for visual presentation
            e.printStackTrace();
        }
        jcom = new JsonCommunication();
        this.setLayout(new BorderLayout());
        mainPanel = new JPanel();
        //mainPanel.setPreferredSize(getSize());
        mainWindowLayout = new CardLayout();
        mainPanel.setLayout(mainWindowLayout);
        this.add(mainPanel);
        logWin = new LoginWindow();

        this.setTitle("EvalEvolved");
        mainPanel.add(logWin, "login");

        ButtonHandler bh = new ButtonHandler();

        logWin.loginbttn.addActionListener(bh);

        //log.login
        StudentSurveyView stuSurView = new StudentSurveyView(jcom);
        mainPanel.add(stuSurView, "studentview");
        //mainWindowLayout.show(mainPanel, "login");

        // mainWindowLayout.show(mainPanel, "studentview");
        // log.setVisible(true);
    }

    /**
     * The listener for receiving action events. When the action event occurs,
     * that object's actionPerformed method is invoked.
     */
    class ButtonHandler implements ActionListener {

        /**
         * When the user presses go the user id will be sent to JsonCommunication
         * this method will return the rank and relevant information
         * @param e in this case it wont do anything except allow program to
         * execute proper calls
         */
        public void actionPerformed(ActionEvent ae) {
            JButton inbutton = (JButton) ae.getSource();
            if (inbutton == logWin.loginbttn) {
                if (logWin.getId().trim().equals("")) {//checking to see if field is empty
                    JOptionPane.showMessageDialog(null, "Field is empty, enter user ID",
                            "Yeah Ok", JOptionPane.WARNING_MESSAGE);
                    return;
                } else {
                    try {
                       ArrayList<Survey> income = jcom.loginInfo(Integer.parseInt(logWin.getId()));//sending userid to server to get verified
                       
                        //System.out.println(income.get(1));
                      //  System.out.println(logWin.getId());
                        
                        int rank = 0;
                        //jcom.loginInfo(1);
                        if (rank == 0) {//if user is rank 0 they are assumed to be a student and prompted to StudentSurvey view
                            //send info to db for authentication
                            mainWindowLayout.show(mainPanel, "studentview");

                        }else{//user will be prompted to FacultyCouseListView
                            facView = new FacultyCourseListView(rank);
                            mainPanel.add(facView, "facView");
                            mainWindowLayout.show(mainPanel, "facView");
                        }
                        
                        
                    } catch (NumberFormatException ex) {//will ensure user can only enter integer
                        JOptionPane.showMessageDialog(null, "Enter proper ID",
                                "Yeah Ok", JOptionPane.WARNING_MESSAGE);
                    } catch (IOException ex) {
                        ex.getLocalizedMessage();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        MainFrame window = new MainFrame();
        //window.setTitle("Student Survey View");
        //window.setResizable(false);
        window.setSize(1100, 900);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
