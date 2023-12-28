/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package regenrolmentsys;
import java.awt.*;
/**
 *
 * @author harley
 */
public class MainFrame extends javax.swing.JFrame {
    private Container cont = getContentPane();
    private CardLayout cl = new CardLayout();
    private LogInUI panelLogin = new LogInUI(this);
    private StudentMenu panelStudentMenu = new StudentMenu(this);
    private AdminMenu panelAdminMenu = new AdminMenu(this);
    private AdminHome panelAdminHome = new AdminHome(this, panelAdminMenu);
    private StudentHome panelStudentHome = new StudentHome(this, panelStudentMenu);
    private String currentUserID = "";
    public MainFrame() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Enrollment System for Regular Students");
        cont.setLayout(cl);
        cont.add(panelLogin, "LoginCard");
        cont.add(panelAdminHome, "AdminHomeCard");
        cont.add(panelAdminMenu, "AdminMenuCard");
        cont.add(panelStudentHome, "StudentHomeCard");
        cont.add(panelStudentMenu, "StudentMenuCard");
        pack();
        cl.show(cont, "LoginCard");
    }
    
    public void switchCard(String targetCard) {
        cl.show(cont, targetCard);
        panelStudentHome.setUserID(currentUserID);
    }
    //setters
    public void setUserID(String userID) {
        this.currentUserID = userID;
        System.out.println("id set : " + userID);
    }
    
    //getters
    public String getUserID() {
        System.out.println("id get: " + currentUserID);
        return currentUserID;
    }
    
    public CardLayout getCardLayout() {
        return cl;
    }
}
