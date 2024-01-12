/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package regenrolmentsys;

import java.awt.Color;
import javax.swing.JOptionPane;
import java.sql.*;

/**
 *
 * @author harley
 */
public class AdminHome extends javax.swing.JPanel {
    private Connection con = null;
    private ResultSet rs = null;
    private MainFrame mf = null;
    private FacultyMenu fm = null;
    private String currentUser = "";
    /**
     * Creates new form AdminHome
     */
    public AdminHome(MainFrame mf, FacultyMenu fm) {
        initComponents();
        this.fm = fm;
        this.mf = mf;
        this.currentUser = mf.getUserID();
    }
    
    public void setUserID(String userID) {
        this.currentUser = userID;
        lblTempID1.setText("Current ID: " + userID);
    }
    
    public void setUserName(){
        con = ConnectDB.connect();
        try{
            rs = con.prepareStatement("SELECT * FROM finals.EMPLOYEE WHERE employee_id ='"+currentUser+"'").executeQuery();
            if (rs.next())
                lblUserName1.setText(rs.getString("first_name"));
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        Welcome = new javax.swing.JLabel();
        lblUserName = new javax.swing.JLabel();
        Student = new javax.swing.JLabel();
        lblTempID = new javax.swing.JLabel();
        shadow = new javax.swing.JLabel();
        BG = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        MainLBL2 = new javax.swing.JLabel();
        PLMLogo2 = new javax.swing.JLabel();
        MinimizeBTN2 = new javax.swing.JButton();
        CloseBTN2 = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        BTNClasslist = new com.k33ptoo.components.KButton();
        BTNGrades = new com.k33ptoo.components.KButton();
        BTNLogout = new com.k33ptoo.components.KButton();
        jPanel1 = new javax.swing.JPanel();
        Welcome1 = new javax.swing.JLabel();
        lblUserName1 = new javax.swing.JLabel();
        Faculty = new javax.swing.JLabel();
        lblTempID1 = new javax.swing.JLabel();
        shadow1 = new javax.swing.JLabel();
        BG1 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        MainLBL = new javax.swing.JLabel();
        PLMLogo = new javax.swing.JLabel();
        MinimizeBTN = new javax.swing.JButton();
        CloseBTN = new javax.swing.JButton();

        jPanel5.setLayout(null);

        Welcome.setFont(new java.awt.Font("Poppins", 1, 72)); // NOI18N
        Welcome.setForeground(new java.awt.Color(255, 255, 255));
        Welcome.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Welcome.setText("WELCOME!");
        jPanel5.add(Welcome);
        Welcome.setBounds(690, 130, 470, 90);

        lblUserName.setFont(new java.awt.Font("Poppins", 1, 48)); // NOI18N
        lblUserName.setForeground(new java.awt.Color(255, 255, 255));
        lblUserName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblUserName.setText("name");
        jPanel5.add(lblUserName);
        lblUserName.setBounds(840, 210, 180, 70);

        Student.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        Student.setForeground(new java.awt.Color(255, 255, 255));
        Student.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Student.setText("student");
        jPanel5.add(Student);
        Student.setBounds(870, 250, 120, 60);

        lblTempID.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        lblTempID.setForeground(new java.awt.Color(255, 255, 255));
        lblTempID.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTempID.setText("current ID: ");
        jPanel5.add(lblTempID);
        lblTempID.setBounds(750, 290, 370, 50);

        shadow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/SHADOW.png"))); // NOI18N
        jPanel5.add(shadow);
        shadow.setBounds(470, -60, 1370, 510);

        BG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/LogInShortFinal.gif"))); // NOI18N
        jPanel5.add(BG);
        BG.setBounds(0, 30, 1280, 470);

        jPanel3.setBackground(new java.awt.Color(254, 86, 86));
        jPanel3.setLayout(null);

        MainLBL2.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        MainLBL2.setForeground(new java.awt.Color(255, 255, 255));
        MainLBL2.setText("Enrollment System for Regular Students");
        jPanel3.add(MainLBL2);
        MainLBL2.setBounds(40, 0, 290, 30);

        PLMLogo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/PLM_Seal_2013.png"))); // NOI18N
        jPanel3.add(PLMLogo2);
        PLMLogo2.setBounds(10, 0, 30, 30);

        MinimizeBTN2.setBackground(new java.awt.Color(254, 86, 86));
        MinimizeBTN2.setFont(new java.awt.Font("Boldfinger", 0, 24)); // NOI18N
        MinimizeBTN2.setForeground(new java.awt.Color(255, 255, 255));
        MinimizeBTN2.setText("-");
        MinimizeBTN2.setToolTipText("");
        MinimizeBTN2.setBorder(null);
        MinimizeBTN2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                MinimizeBTN2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                MinimizeBTN2MouseExited(evt);
            }
        });
        MinimizeBTN2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MinimizeBTN2ActionPerformed(evt);
            }
        });
        jPanel3.add(MinimizeBTN2);
        MinimizeBTN2.setBounds(1220, 0, 30, 30);

        CloseBTN2.setBackground(new java.awt.Color(254, 86, 86));
        CloseBTN2.setFont(new java.awt.Font("Boldfinger", 0, 18)); // NOI18N
        CloseBTN2.setForeground(new java.awt.Color(255, 255, 255));
        CloseBTN2.setText("X");
        CloseBTN2.setBorder(null);
        CloseBTN2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CloseBTN2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                CloseBTN2MouseExited(evt);
            }
        });
        CloseBTN2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseBTN2ActionPerformed(evt);
            }
        });
        jPanel3.add(CloseBTN2);
        CloseBTN2.setBounds(1250, 0, 30, 30);

        jPanel5.add(jPanel3);
        jPanel3.setBounds(0, 0, 1280, 30);

        jSplitPane1.setDividerLocation(450);
        jSplitPane1.setDividerSize(0);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        BTNClasslist.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/1.png"))); // NOI18N
        BTNClasslist.setText("Class List");
        BTNClasslist.setToolTipText("");
        BTNClasslist.setBorderPainted(false);
        BTNClasslist.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        BTNClasslist.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BTNClasslist.setIconTextGap(180);
        BTNClasslist.setkBorderRadius(20);
        BTNClasslist.setkEndColor(new java.awt.Color(51, 255, 51));
        BTNClasslist.setkHoverEndColor(new java.awt.Color(0, 153, 51));
        BTNClasslist.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        BTNClasslist.setkHoverStartColor(new java.awt.Color(51, 102, 0));
        BTNClasslist.setkStartColor(new java.awt.Color(0, 102, 0));
        BTNClasslist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNClasslistActionPerformed(evt);
            }
        });

        BTNGrades.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/4.png"))); // NOI18N
        BTNGrades.setText("Grades");
        BTNGrades.setBorderPainted(false);
        BTNGrades.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        BTNGrades.setIconTextGap(180);
        BTNGrades.setkBorderRadius(20);
        BTNGrades.setkEndColor(new java.awt.Color(153, 255, 255));
        BTNGrades.setkHoverEndColor(new java.awt.Color(51, 0, 255));
        BTNGrades.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        BTNGrades.setkHoverStartColor(new java.awt.Color(0, 0, 153));
        BTNGrades.setkStartColor(new java.awt.Color(51, 51, 255));
        BTNGrades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNGradesActionPerformed(evt);
            }
        });

        BTNLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/5.png"))); // NOI18N
        BTNLogout.setText("Log-out");
        BTNLogout.setBorderPainted(false);
        BTNLogout.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        BTNLogout.setIconTextGap(100);
        BTNLogout.setkBorderRadius(20);
        BTNLogout.setkEndColor(new java.awt.Color(255, 0, 255));
        BTNLogout.setkHoverEndColor(new java.awt.Color(153, 0, 153));
        BTNLogout.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        BTNLogout.setkHoverStartColor(new java.awt.Color(102, 0, 102));
        BTNLogout.setkStartColor(new java.awt.Color(153, 0, 153));
        BTNLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNLogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(BTNClasslist, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addComponent(BTNGrades, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 375, Short.MAX_VALUE)
                .addComponent(BTNLogout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BTNClasslist, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BTNGrades, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BTNLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60))
        );

        jSplitPane1.setRightComponent(jPanel2);

        jPanel1.setLayout(null);

        Welcome1.setFont(new java.awt.Font("Poppins", 1, 72)); // NOI18N
        Welcome1.setForeground(new java.awt.Color(255, 255, 255));
        Welcome1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Welcome1.setText("WELCOME!");
        jPanel1.add(Welcome1);
        Welcome1.setBounds(690, 130, 470, 90);

        lblUserName1.setFont(new java.awt.Font("Poppins", 1, 48)); // NOI18N
        lblUserName1.setForeground(new java.awt.Color(255, 255, 255));
        lblUserName1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblUserName1.setText("name");
        jPanel1.add(lblUserName1);
        lblUserName1.setBounds(840, 210, 180, 70);

        Faculty.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        Faculty.setForeground(new java.awt.Color(255, 255, 255));
        Faculty.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Faculty.setText("faculty");
        jPanel1.add(Faculty);
        Faculty.setBounds(870, 250, 120, 60);

        lblTempID1.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        lblTempID1.setForeground(new java.awt.Color(255, 255, 255));
        lblTempID1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTempID1.setText("current ID: ");
        jPanel1.add(lblTempID1);
        lblTempID1.setBounds(750, 290, 370, 50);

        shadow1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/SHADOW.png"))); // NOI18N
        jPanel1.add(shadow1);
        shadow1.setBounds(470, -60, 1370, 510);

        BG1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/LogInShortFinal.gif"))); // NOI18N
        jPanel1.add(BG1);
        BG1.setBounds(0, 30, 1380, 470);

        jPanel6.setBackground(new java.awt.Color(254, 86, 86));
        jPanel6.setLayout(null);

        MainLBL.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        MainLBL.setForeground(new java.awt.Color(255, 255, 255));
        MainLBL.setText("Enrollment System for Regular Students");
        jPanel6.add(MainLBL);
        MainLBL.setBounds(40, 0, 290, 30);

        PLMLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/PLM_Seal_2013.png"))); // NOI18N
        jPanel6.add(PLMLogo);
        PLMLogo.setBounds(10, 0, 30, 30);

        MinimizeBTN.setBackground(new java.awt.Color(254, 86, 86));
        MinimizeBTN.setFont(new java.awt.Font("Boldfinger", 0, 24)); // NOI18N
        MinimizeBTN.setForeground(new java.awt.Color(255, 255, 255));
        MinimizeBTN.setText("-");
        MinimizeBTN.setToolTipText("");
        MinimizeBTN.setBorder(null);
        MinimizeBTN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                MinimizeBTNMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                MinimizeBTNMouseExited(evt);
            }
        });
        MinimizeBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MinimizeBTNActionPerformed(evt);
            }
        });
        jPanel6.add(MinimizeBTN);
        MinimizeBTN.setBounds(1220, 0, 30, 30);

        CloseBTN.setBackground(new java.awt.Color(254, 86, 86));
        CloseBTN.setFont(new java.awt.Font("Boldfinger", 0, 18)); // NOI18N
        CloseBTN.setForeground(new java.awt.Color(255, 255, 255));
        CloseBTN.setText("X");
        CloseBTN.setBorder(null);
        CloseBTN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CloseBTNMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                CloseBTNMouseExited(evt);
            }
        });
        CloseBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseBTNActionPerformed(evt);
            }
        });
        jPanel6.add(CloseBTN);
        CloseBTN.setBounds(1250, 0, 30, 30);

        jPanel1.add(jPanel6);
        jPanel6.setBounds(0, 0, 1420, 30);

        jSplitPane1.setTopComponent(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnGradesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGradesActionPerformed
        // TODO add your handling code here:
        mf.switchCard("FacultyMenuCard");
        fm.getTabs().setSelectedIndex(1);
        fm.loadGradesTab();
        //TODO: code for displaying history table
    }//GEN-LAST:event_btnGradesActionPerformed

    private void btnClassListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClassListActionPerformed
        // TODO add your handling code here:
        mf.switchCard("FacultyMenuCard");
        fm.getTabs().setSelectedIndex(0);
        fm.loadClassTab();
    }//GEN-LAST:event_btnClassListActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        // TODO add your handling code here:
        int response = JOptionPane.showConfirmDialog(this, "Do you really want to log-out?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (response == 0){
            mf.setUserID("");
            mf.switchCard("LoginCard");
        }
        else{
            JOptionPane.showMessageDialog(this, "Canceled");
        }
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void MinimizeBTN2MouseEntered(java.awt.event.MouseEvent evt) {                                          
        MinimizeBTN.setBackground(new Color(203,68,68));
        // TODO add your handling code here:
        mf.switchCard("FacultyMenuCard");
        fm.getTabs().setSelectedIndex(0);
        fm.loadClassTab();
    }                                            

    private void MinimizeBTN2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MinimizeBTN2MouseExited
        MinimizeBTN.setBackground(new Color(254, 86, 86));
        // TODO add your handling code here:
    }//GEN-LAST:event_MinimizeBTN2MouseExited

    private void MinimizeBTN2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MinimizeBTN2ActionPerformed
        //minimize button event here:

        // TODO add your handling code here:
    }//GEN-LAST:event_MinimizeBTN2ActionPerformed

    private void CloseBTN2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CloseBTN2MouseEntered
        CloseBTN.setBackground(new Color(203,68,68));
        // TODO add your handling code here:
    }//GEN-LAST:event_CloseBTN2MouseEntered

    private void CloseBTN2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CloseBTN2MouseExited
        CloseBTN.setBackground(new Color(254, 86, 86));
        // TODO add your handling code here:
    }//GEN-LAST:event_CloseBTN2MouseExited

    private void CloseBTN2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseBTN2ActionPerformed
        //exit button event here:

        // TODO add your handling code here:
    }//GEN-LAST:event_CloseBTN2ActionPerformed

    private void MinimizeBTNMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MinimizeBTNMouseEntered
        MinimizeBTN.setBackground(new Color(203,68,68));
        // TODO add your handling code here:
    }//GEN-LAST:event_MinimizeBTNMouseEntered

    private void MinimizeBTNMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MinimizeBTNMouseExited
        MinimizeBTN.setBackground(new Color(254, 86, 86));
        // TODO add your handling code here:
    }//GEN-LAST:event_MinimizeBTNMouseExited

    private void MinimizeBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MinimizeBTNActionPerformed
        mf.minimize();

        // TODO add your handling code here:
    }//GEN-LAST:event_MinimizeBTNActionPerformed

    private void CloseBTNMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CloseBTNMouseEntered
        CloseBTN.setBackground(new Color(203,68,68));
        // TODO add your handling code here:
    }//GEN-LAST:event_CloseBTNMouseEntered

    private void CloseBTNMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CloseBTNMouseExited
        CloseBTN.setBackground(new Color(254, 86, 86));
        // TODO add your handling code here:
    }//GEN-LAST:event_CloseBTNMouseExited

    private void CloseBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseBTNActionPerformed
        mf.close();
        // TODO add your handling code here:
    }//GEN-LAST:event_CloseBTNActionPerformed

    private void BTNClasslistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNClasslistActionPerformed
        // TODO add your handling code here:
        mf.switchCard("FacultyMenuCard");
        fm.getTabs().setSelectedIndex(0);
        fm.toggleSelected(0);
    }//GEN-LAST:event_BTNClasslistActionPerformed

    private void BTNGradesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNGradesActionPerformed
        // TODO add your handling code here:
        
        mf.switchCard("FacultyMenuCard");
        fm.getTabs().setSelectedIndex(1);
        fm.loadGradesTab();
        fm.toggleSelected(1);
    }//GEN-LAST:event_BTNGradesActionPerformed

    private void BTNLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNLogoutActionPerformed
        // TODO add your handling code here:
        int response = JOptionPane.showConfirmDialog(null, "Do you really want to log-out?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (response == 0){
            mf.setUserID("");
            mf.switchCard("LoginCard");
        }
        else{
            JOptionPane.showMessageDialog(null, "Canceled");
        }
    }//GEN-LAST:event_BTNLogoutActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BG;
    private javax.swing.JLabel BG1;
    private com.k33ptoo.components.KButton BTNClasslist;
    private com.k33ptoo.components.KButton BTNGrades;
    private com.k33ptoo.components.KButton BTNLogout;
    private javax.swing.JButton CloseBTN;
    private javax.swing.JButton CloseBTN2;
    private javax.swing.JLabel Faculty;
    private javax.swing.JLabel MainLBL;
    private javax.swing.JLabel MainLBL2;
    private javax.swing.JButton MinimizeBTN;
    private javax.swing.JButton MinimizeBTN2;
    private javax.swing.JLabel PLMLogo;
    private javax.swing.JLabel PLMLogo2;
    private javax.swing.JLabel Student;
    private javax.swing.JLabel Welcome;
    private javax.swing.JLabel Welcome1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel lblTempID;
    private javax.swing.JLabel lblTempID1;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JLabel lblUserName1;
    private javax.swing.JLabel shadow;
    private javax.swing.JLabel shadow1;
    // End of variables declaration//GEN-END:variables
}
