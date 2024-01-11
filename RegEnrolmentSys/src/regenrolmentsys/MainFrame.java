/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package regenrolmentsys;
import java.awt.*;
import javax.swing.JFrame;
/**
 *
 * @author harley
 */
public class MainFrame extends javax.swing.JFrame {
    private Container cont = getContentPane();
    private CardLayout cl = new CardLayout();
    private StudentMenu panelStudentMenu = new StudentMenu(this);
    private AdminMenu panelAdminMenu = new AdminMenu(this);
    private FacultyMenu panelFacultyMenu = new FacultyMenu(this);
    private AdminHome panelAdminHome = new AdminHome(this, panelFacultyMenu);
    private StudentHome panelStudentHome = new StudentHome(this, panelStudentMenu);
    private LogInUI panelLogin = new LogInUI(this, panelStudentHome, panelAdminHome);
    private String currentUserID = "";
    public MainFrame() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        //setUndecorated(true);
        setTitle("Enrollment System for Regular Students");
        
        cont.setLayout(cl);
        cont.add(panelLogin, "LoginCard");
        cont.add(panelAdminHome, "AdminHomeCard");
        cont.add(panelAdminMenu, "AdminMenuCard");
        cont.add(panelFacultyMenu, "FacultyMenuCard");
        cont.add(panelStudentHome, "StudentHomeCard");
        cont.add(panelStudentMenu, "StudentMenuCard");
        pack();
        cl.show(cont, "LoginCard");
    }
    
    public void switchCard(String targetCard) {
        cl.show(cont, targetCard);
        panelStudentHome.setUserID(currentUserID);
        panelStudentMenu.setUserID(currentUserID);
        panelFacultyMenu.setUserID(currentUserID);
        panelAdminHome.setUserID(currentUserID);
        panelAdminMenu.setUserID(currentUserID);
    }
    //setters
    public void setUserID(String userID) {
        this.currentUserID = userID;
    }
    
    //getters
    public String getUserID() {
        return currentUserID;
    }
    
    public CardLayout getCardLayout() {
        return cl;
    }
    
    public void minimize(){
        this.setState(this.ICONIFIED);
    }
      
    public void close(){
        this.dispose();
    }
      
}
