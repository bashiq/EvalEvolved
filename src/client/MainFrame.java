package client;

import java.awt.LayoutManager;

import javax.swing.JFrame;

public class MainFrame extends JFrame {

    MainFrame() {
        LoginWindow lw = new LoginWindow();
        this.setTitle("Login");
        this.add(lw);
        //String de = lw.sendBlah();
       // System.out.println(de);
        this.remove(lw);
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
