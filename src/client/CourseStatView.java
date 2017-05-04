package client;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.*;

/**
 * When a faculty member succesfully logs and choose which course stats they would like to view, this
 * screen will pop up display stats
 * @author Bilal
 */
public class CourseStatView extends JFrame {

    private JTextArea statArea;
    private JList unsubmitted;
    private String names[] = {"Rick Sanchez", "Henrick Ibsen"};
    private JButton back;
    private GridBagConstraints c;
    private int rank = 1;
    private JsonCommunication jcom;

    /**
     * This constructor will recieve the faculty rank and show them the appriopriate screen from there
     * @param inrank faculty rank
     */
    public CourseStatView(int inrank, JsonCommunication jcom) throws IOException {
        this.jcom = jcom;
        rank = inrank;
        
        
       this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(1100, 900);
        setLocationRelativeTo(null);
        setVisible(true);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());//taken from stackoverflow
        } catch (Exception e) {//for visual presentation
            e.printStackTrace();
        }

        this.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        //   c.weightx = .5;
        //   c.weighty = .5;

        if (rank == 2 || rank == 3) {
            DeanChairView();
        } else {
            ProfessorView();
        }
        back = new JButton("Back");
        c.insets = new Insets(0, 0, 0, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = .1;   //request any extra vertical space
        c.anchor = GridBagConstraints.LAST_LINE_START;
        c.ipady = 80;      //make this component tall
        c.gridwidth = 3;
        this.add(back, c);
        ButtonHandler bh = new ButtonHandler();
        back.addActionListener(bh);
    }

    /**
     * The dean and department chair will be able to view stats for that course
     */
    void DeanChairView() throws IOException {

        JPanel statPanel = new JPanel(new BorderLayout());
        statPanel.add(new JLabel("Statistics for this class"), BorderLayout.NORTH);
        statArea = new JTextArea(15, 70);
        statArea.setText(jcom.CourseStats());
        JScrollPane scroll = new JScrollPane(statArea);
        statArea.setEditable(false);
        statPanel.add(scroll, BorderLayout.CENTER);
        // c.fill = GridBagConstraints.HORIZONTAL;
        //c.gridwidth= 1;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weightx = .5;
        c.ipady = 400;
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        //  c.gridheight=2;

        this.add(statPanel, c);

        c.ipady = 0;
        JPanel unsubPanel = new JPanel(new BorderLayout());
        unsubPanel.add(new JLabel("Students who have not completed Eval"), BorderLayout.NORTH);

        unsubmitted = new JList(names);//people who have not completed evals for this course
        unsubmitted.setEnabled(false);
        JScrollPane scroll2 = new JScrollPane(unsubmitted);
        unsubPanel.add(scroll2, BorderLayout.CENTER);
        c.insets = new Insets(0, 60, 0, 0);  //top padding
        // c.weightx =0.1;
        c.gridx = 2;
        c.gridy = 1;
        this.add(unsubPanel, c);
    }

    /**
     * professor will be limited in what stats they may view if can view any
     */
    void ProfessorView() {
        c.gridx = 1;
        c.gridy = 1;
        //c.anchor = GridBagConstraints.CENTER;
        this.add(new JLabel("As a professor you do not have permissions to view stats on courses"), c);
    }
    
    void DisposeWindow(){
        this.dispose();
    }

    public class ButtonHandler implements ActionListener {

        /**
         * Invoked when the button to go back to facultycourselistview is pressed
         *
         * @param ae in this case it wont do anything except allow program to
         * execute proper calls
         */
        @Override
        public void actionPerformed(ActionEvent ae) {
            //magic happens here
            if (ae.getSource() == back) {
                DisposeWindow();
                
            }
        }
    }

//    public static void main(String[] args) {
//        CourseStatView window = new CourseStatView();
//        window.setTitle("Statistics");
//        //window.setResizable(false);
//        window.setSize(1100, 900);
//        window.setLocationRelativeTo(null);
//        window.setVisible(true);
//        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }

}
