/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package regenrolmentsys;

import java.awt.Toolkit;

/**
 *
 * @author harley
 */
public class MainApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MainFrame mf = new MainFrame();
        mf.setLocationRelativeTo(null);
        mf.setVisible(true);
        //mf.setIconImage(); change icon in taskbar
        mf.setIconImage(Toolkit.getDefaultToolkit().getImage(mf.getClass().getResource("/assets/plmsealicon.png")));
        ConnectDB.connect();
    }
    
}
