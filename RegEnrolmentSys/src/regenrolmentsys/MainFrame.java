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
    public MainFrame() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Enrollment System for Regular Students");
        cont.setLayout(cl);
        add(panelLogin);
        pack();
    }
    
    public CardLayout getCardLayout() {
        return cl;
    }
}
