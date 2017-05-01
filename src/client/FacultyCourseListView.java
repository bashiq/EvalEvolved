package client;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * This Jpanel will allow the faculty to view the list of course and when they select one they will
 * be prompted to another screen showing them the stats on that course
 * @author Bilal
 */
public class FacultyCourseListView extends JPanel {

    private JComboBox professors, departments;
    private JList classes;
    private JButton go;

    private String[] profarr = {"broom", "mehri", "Obama"};//byebye
    private String[] deptarr = {"CS", "IA", "CE"};
    private String[] coursearr = {"CS225", "El262", "MA124"};
    private String chosen = null;
    private GridBagConstraints c;
    private int rank =3;
    private int permissions;
    private ComboListener cl;
    
/**
 * Constructor will take in rank and show them appriopriate screen so they may view stats for an individual course
 * @param inrank rank of faculty
 */
    public FacultyCourseListView(int inrank) {
        rank = inrank;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());//taken from stackoverflow
        } catch (Exception e) {//for visual presentation
            e.printStackTrace();
        }
        //rank
        switch (rank) {
            case 1://if professor
                permissions =101;
                break;
            case 2:
                permissions =102;
                break;
            default://if dean
                permissions =103;
                break;
        }
        
        this.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = .5;
        c.weighty = .5;
        c.gridwidth = 3;
        c.gridx = 2;
        c.gridy = 0;

        JLabel text = new JLabel("You may use the department and professor combo box to narrow the list of courses");
        this.add(text, c);
        c.gridwidth = 1;
        cl = new ComboListener();//listener for comboboxes
        ClassesList(permissions);
        DepartmentList(permissions);
        ProfessorList(permissions);
        ButtonDisplayer();
    }
    /**
     * Will display button to view advanced and show a popup coursestatwindow
     */
    void ButtonDisplayer(){
        go = new JButton("View stats for this course");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 2;
        c.weighty = .1;   //request any extra vertical space
        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.ipady = 80;      //make this component tall
        c.gridwidth = 2;
        this.add(go, c);
        
        ButtonHandler bh = new ButtonHandler();
        go.addActionListener(bh);
    }

    /**
     * This method will display all courses to view stats on
     * @param placement the rank of the faculty will ultimately determine where is displayed on the screen
     */
    private void ClassesList(int placement) {
        if(placement == 101) placement =3; 
        else placement =0;
        JPanel cla = new JPanel(new BorderLayout());
        cla.add(new JLabel("Classes"), BorderLayout.NORTH);
        ButtonGroup bg = new ButtonGroup();
        classes = new JList(coursearr);
        classes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(classes);
        cla.add(scroll);
        c.gridx = placement;
        c.gridy = 1;
        this.add(cla, c);
        CourseListener cl = new CourseListener();
        classes.addListSelectionListener(cl);
    }

    /**
     * Only Dean will be able to view this component
     * user can narrow classes by department
     * @param placement 
     */
    private void DepartmentList(int placement) {
        if(placement == 103) 
            placement = 2; 
        else return;
        JPanel dep = new JPanel();
        dep.add(new JLabel("Departments"), BorderLayout.NORTH);
        dep.add(departments = new JComboBox(deptarr));
        departments.setActionCommand("depart");
        c.gridx = placement;
        c.gridy = 1;

        this.add(dep, c);
        departments.addItemListener(cl);
        
    }

    /**
     * Dean and chair will be able to view professor list to help narrow classes
     * @param placement 
     */
    void ProfessorList(int placement) {
        if(placement ==101) return;
        if(placement ==102) placement =2; else placement =3;;
        
        JPanel pro = new JPanel();
        pro.add(new JLabel("professors"), BorderLayout.NORTH);
        pro.add(professors = new JComboBox(profarr));
        professors.setActionCommand("prof");
        c.gridx = placement;
        c.gridy = 1;

        this.add(pro, c);
        professors.addItemListener(cl);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * The listener for receiving action events. When the action event occurs,
     * that object's actionPerformed method is invoked.
     */
    public class ButtonHandler implements ActionListener {
        /**
         * Invoked when the button to get course stats for a specific course
         * in this case this method will check to see if something is selected before continuing
         * @param ae in this case it wont do anything except allow program to
         * execute proper calls
         */
        @Override
        public void actionPerformed(ActionEvent ae) {
            //magic happens here
           // if(ae.getSource() == go){
                if(chosen != null){
                    System.out.println(chosen);
                    CourseStatView csv = new CourseStatView(rank);
                    csv.setVisible(true);
                }else{
                    JOptionPane.showMessageDialog(null, "Select a course to view stats",
                        "Yeah Ok", JOptionPane.WARNING_MESSAGE);
                    return;
                }
        }
    }
    
     /**
     * Listener recieving item events for the the options/ jcombobox
     */
    public class ComboListener implements ItemListener {

        /**
         * invoked when item has beem selected or deselected
         *
         * @param e
         */
        @Override//handler method
        public void itemStateChanged(ItemEvent e) {
            JComboBox cbox = (JComboBox) e.getSource();
            
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if ("depart".equals(cbox.getActionCommand())) {//if user chose department
                    System.out.println("heredepart");
                    
                }
                else{
                    System.out.println("hereproffessor");
                }
            }
        }
    }
    /**
     * Listener that is notified when a course is chosen
     */
    public class CourseListener implements ListSelectionListener {

        /**
         * called when selection value is changed
         *
         * @param e
         */
        @Override
        public void valueChanged(ListSelectionEvent e) {
            JList cbox = (JList) e.getSource();
            String ex = "";
            int selected = cbox.getSelectedIndex();
            
            if (e.getValueIsAdjusting()) {
                //System.out.println("Selected: "+coursearr [selected]);
                chosen = coursearr[selected];
            }
        }
    }

//    public static void main(String[] args) {
//        FacultyCourseListView window = new FacultyCourseListView();
//        window.setTitle("Statistics");
//        //window.setResizable(false);
//        window.setSize(1100, 900);
//        window.setLocationRelativeTo(null);
//        window.setVisible(true);
//        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }
}
