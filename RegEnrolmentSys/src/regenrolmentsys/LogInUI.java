package regenrolmentsys;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.HashSet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


 
public class LogInUI extends javax.swing.JPanel {
    private AdminHome ah = null;
    private StudentHome sh = null;
    private AdminMenu am = null;
    private MainFrame mf = null;
    private Connection con = null;
    private ResultSet rs = null;
    /**
     * Creates new form LogInUI
     */
    public LogInUI(MainFrame mf, StudentHome sh, AdminHome ah, AdminMenu am) {
        initComponents();
        this.mf = mf;
        this.sh = sh;
        this.ah = ah;
        this.am = am;
        setSize(1280, 720);
        setMinimumSize(new Dimension(1280, 720));
        setMaximumSize(new Dimension(1280, 720));
        setVisible(true);
        setPreferredSize(new Dimension(1280, 720));
        lblWrongPassword.setVisible(false);
        lblWrongPassword1.setVisible(false);
        lblErrorID1.setVisible(false);
        this.HidePass.setVisible(false);
    }
    
    private void checkLogin() {
        mf.setUserID(UserIDField.getText());
        con = ConnectDB.connect();
        if (con == null) {
            JOptionPane.showMessageDialog(null, "Connection to database failed!", "Connection failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (UserIDField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "User ID cannot be empty!", "Login failed", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                rs = con.prepareStatement("SELECT * FROM finals.STUDENT WHERE student_no = '" + UserIDField.getText() + "'").executeQuery();
                if (rs.next()){
                    rs = con.prepareStatement("SELECT * FROM finals.PASSWORD WHERE user_id = '" + UserIDField.getText() + "' AND password = '" + String.valueOf(password.getPassword()) + "'").executeQuery();
                    if (rs.next()) {
                        mf.switchCard("StudentHomeCard");
                        sh.setUserName();
                        lblErrorID1.setVisible(false);
                        lblWrongPassword.setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(null, "Incorrect password", "Login failed", JOptionPane.ERROR_MESSAGE);
                        lblErrorID1.setVisible(false);
                        lblWrongPassword.setVisible(true);
                    }
                } else {
                    rs = con.prepareStatement("SELECT * FROM finals.EMPLOYEE WHERE employee_id = '" + UserIDField.getText() + "'").executeQuery();
                    if (rs.next()){
                        rs = con.prepareStatement("SELECT * FROM finals.PASSWORD WHERE user_id = '" + UserIDField.getText() + "' AND password = '" + String.valueOf(password.getPassword()) + "'").executeQuery();
                        if (rs.next()) {
                            ah.setUserID(UserIDField.getText());
                            ah.setUserName();
                            mf.switchCard("AdminHomeCard");
                            lblErrorID1.setVisible(false);
                            lblWrongPassword.setVisible(false);
                        } else {
                            JOptionPane.showMessageDialog(null, "Incorrect password", "Login failed", JOptionPane.ERROR_MESSAGE);
                            lblErrorID1.setVisible(false);
                            lblWrongPassword.setVisible(true);
                        }
                    } else {
                        rs = con.prepareStatement("SELECT * FROM finals.ADMIN WHERE admin_id = '" + UserIDField.getText() + "'").executeQuery();
                        if (rs.next()) {
                            rs = con.prepareStatement("SELECT pass FROM finals.ADMIN WHERE admin_id = '" + UserIDField.getText() + "'").executeQuery();
                            if (rs.next()) {
                                if (String.valueOf(password.getPassword()).equals(rs.getString("pass"))) {
                                    mf.switchCard("AdminMenuCard");
                                    am.loadStudentsTab();
                                    am.toggleSelected(0);
                                    lblErrorID1.setVisible(false);
                                    lblWrongPassword.setVisible(false);
                                }
                                else  {
                                    JOptionPane.showMessageDialog(null, "Incorrect password", "Login failed", JOptionPane.ERROR_MESSAGE);
                                    lblErrorID1.setVisible(false);
                                    lblWrongPassword.setVisible(true);
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "User does not exist", "Login failed", JOptionPane.ERROR_MESSAGE);  
                            lblErrorID1.setVisible(true);
                            lblWrongPassword.setVisible(false);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e); //TODO: ERROR MSG
            }
        }
        UserIDField.setText("");
        password.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        LogInBTN = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        MainLBL = new javax.swing.JLabel();
        PLMLogo = new javax.swing.JLabel();
        MinimizeBTN = new javax.swing.JButton();
        CloseBTN = new javax.swing.JButton();
        lblWrongPassword1 = new javax.swing.JLabel();
        lblWrongPassword = new javax.swing.JLabel();
        lblErrorID1 = new javax.swing.JLabel();
        HidePass = new javax.swing.JLabel();
        SeePass = new javax.swing.JLabel();
        PasswordIcon = new javax.swing.JLabel();
        UserIcon = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();
        UserIDField = new javax.swing.JTextField();
        EnrlmntLBL = new javax.swing.JLabel();
        LogInLBL = new javax.swing.JLabel();
        Password = new javax.swing.JLabel();
        UserID = new javax.swing.JLabel();
        SupportBTN = new javax.swing.JButton();
        LogInPanel = new javax.swing.JLabel();
        Background = new javax.swing.JLabel();

        setBackground(new java.awt.Color(234, 230, 230));
        setPreferredSize(new java.awt.Dimension(1280, 720));
        setLayout(null);

        LogInBTN.setBackground(new java.awt.Color(254, 86, 86));
        LogInBTN.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        LogInBTN.setForeground(new java.awt.Color(255, 255, 255));
        LogInBTN.setText("LOG IN");
        LogInBTN.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(254, 86, 86), 0));
        LogInBTN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                LogInBTNMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                LogInBTNMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                LogInBTNMousePressed(evt);
            }
        });
        LogInBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogInBTNActionPerformed(evt);
            }
        });
        add(LogInBTN);
        LogInBTN.setBounds(840, 490, 240, 50);

        jPanel1.setBackground(new java.awt.Color(254, 86, 86));
        jPanel1.setLayout(null);

        MainLBL.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        MainLBL.setForeground(new java.awt.Color(255, 255, 255));
        MainLBL.setText("Enrollment System for Regular Students");
        jPanel1.add(MainLBL);
        MainLBL.setBounds(40, 0, 290, 30);

        PLMLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/PLM_Seal_2013.png"))); // NOI18N
        jPanel1.add(PLMLogo);
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
        jPanel1.add(MinimizeBTN);
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
        jPanel1.add(CloseBTN);
        CloseBTN.setBounds(1250, 0, 30, 30);

        add(jPanel1);
        jPanel1.setBounds(0, 0, 1280, 30);

        lblWrongPassword1.setBackground(new java.awt.Color(255, 51, 51));
        lblWrongPassword1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblWrongPassword1.setForeground(new java.awt.Color(255, 51, 51));
        lblWrongPassword1.setText("Caps Lock is on");
        add(lblWrongPassword1);
        lblWrongPassword1.setBounds(1100, 420, 140, 30);

        lblWrongPassword.setBackground(new java.awt.Color(255, 51, 51));
        lblWrongPassword.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblWrongPassword.setForeground(new java.awt.Color(255, 51, 51));
        lblWrongPassword.setText("Incorrect Password!");
        add(lblWrongPassword);
        lblWrongPassword.setBounds(740, 420, 140, 30);

        lblErrorID1.setBackground(new java.awt.Color(255, 51, 51));
        lblErrorID1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblErrorID1.setForeground(new java.awt.Color(255, 51, 51));
        lblErrorID1.setText("User ID does not exist!");
        add(lblErrorID1);
        lblErrorID1.setBounds(740, 280, 140, 30);

        HidePass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/hidepass.png"))); // NOI18N
        HidePass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                HidePassMousePressed(evt);
            }
        });
        add(HidePass);
        HidePass.setBounds(690, 360, 40, 50);

        SeePass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/seepass.png"))); // NOI18N
        SeePass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                SeePassMousePressed(evt);
            }
        });
        add(SeePass);
        SeePass.setBounds(690, 360, 40, 50);

        PasswordIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/passicon.png"))); // NOI18N
        add(PasswordIcon);
        PasswordIcon.setBounds(1140, 360, 40, 50);

        UserIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/1077114.png"))); // NOI18N
        add(UserIcon);
        UserIcon.setBounds(1140, 210, 50, 70);

        password.setBackground(new java.awt.Color(249, 248, 248));
        password.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        password.setForeground(new java.awt.Color(102, 102, 102));
        password.setText(" Enter Password");
        password.setToolTipText("");
        password.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        password.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                passwordFocusGained(evt);
            }
        });
        password.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordActionPerformed(evt);
            }
        });
        password.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                passwordKeyPressed(evt);
            }
        });
        add(password);
        password.setBounds(740, 350, 450, 70);

        UserIDField.setBackground(new java.awt.Color(249, 248, 248));
        UserIDField.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        UserIDField.setForeground(new java.awt.Color(102, 102, 102));
        UserIDField.setText("  Enter User ID");
        UserIDField.setToolTipText("");
        UserIDField.setActionCommand("<Not Set>");
        UserIDField.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        UserIDField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                UserIDFieldFocusGained(evt);
            }
        });
        UserIDField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UserIDFieldActionPerformed(evt);
            }
        });
        add(UserIDField);
        UserIDField.setBounds(740, 210, 450, 70);

        EnrlmntLBL.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        EnrlmntLBL.setForeground(new java.awt.Color(255, 255, 255));
        EnrlmntLBL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        EnrlmntLBL.setText("Enrollment System for Regular Students");
        EnrlmntLBL.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        EnrlmntLBL.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        add(EnrlmntLBL);
        EnrlmntLBL.setBounds(70, 620, 520, 60);

        LogInLBL.setFont(new java.awt.Font("Poppins", 1, 48)); // NOI18N
        LogInLBL.setText("Log In");
        add(LogInLBL);
        LogInLBL.setBounds(690, 70, 190, 80);

        Password.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        Password.setText("Password");
        add(Password);
        Password.setBounds(740, 320, 180, 30);

        UserID.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        UserID.setText("User ID");
        add(UserID);
        UserID.setBounds(740, 180, 180, 30);

        SupportBTN.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        SupportBTN.setForeground(new java.awt.Color(254, 86, 86));
        SupportBTN.setText("Contact support");
        SupportBTN.setToolTipText("");
        SupportBTN.setBorder(null);
        SupportBTN.setContentAreaFilled(false);
        SupportBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SupportBTNActionPerformed(evt);
            }
        });
        add(SupportBTN);
        SupportBTN.setBounds(890, 560, 140, 30);

        LogInPanel.setBackground(new java.awt.Color(255, 255, 255));
        LogInPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/white-color-solid-background-1920x1080.png"))); // NOI18N
        LogInPanel.setText("LOG IN");
        LogInPanel.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        LogInPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 10, true));
        add(LogInPanel);
        LogInPanel.setBounds(660, 50, 600, 650);

        Background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/LogInShortFinal.gif"))); // NOI18N
        add(Background);
        Background.setBounds(0, 0, 1280, 720);
    }// </editor-fold>//GEN-END:initComponents

    private void UserIDFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UserIDFieldActionPerformed
        // TODO add your handling code here:
        checkLogin();
    }//GEN-LAST:event_UserIDFieldActionPerformed

    private void LogInBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogInBTNActionPerformed
        // TODO add your handling code here:
        checkLogin();
    }//GEN-LAST:event_LogInBTNActionPerformed

    private void LogInBTNMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LogInBTNMouseEntered
             LogInBTN.setBackground(new Color(203,68,68));     
            

             // TODO add your handling code here:
    }//GEN-LAST:event_LogInBTNMouseEntered

    private void LogInBTNMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LogInBTNMouseExited
             LogInBTN.setBackground(new Color(254, 86, 86));
        // TODO add your handling code here:
    }//GEN-LAST:event_LogInBTNMouseExited

    private void SupportBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SupportBTNActionPerformed
    //support
      JOptionPane.showMessageDialog(null, "For inquiries, refer to the following contacts:\n\n\n" +
                "Office of the University Registrar \t-\t registrar@plm.edu.ph\n\n" +
                "Admissions Office \t-\t admission_office@plm.edu.ph\n\n" +
                "Information and Communicaions Technology Office \t-\t ithelp@plm.edu.ph\n\n" +
                "Human Resource Development Office \t-\t hrdo@plm.edu.ph\n\n" +
                "College Of Engineering and Technology \t-\t cet_group@plm.edu.ph", "Contact support\n\n "
                , JOptionPane.PLAIN_MESSAGE);
    }//GEN-LAST:event_SupportBTNActionPerformed

    private void LogInBTNMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LogInBTNMousePressed
        //login
    }//GEN-LAST:event_LogInBTNMousePressed

    private void MinimizeBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MinimizeBTNActionPerformed
    //minimize 
        // TODO add your handling code here:
       mf.minimize();
    }//GEN-LAST:event_MinimizeBTNActionPerformed

    private void CloseBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseBTNActionPerformed

      mf.close();
    }//GEN-LAST:event_CloseBTNActionPerformed

    private void MinimizeBTNMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MinimizeBTNMouseEntered
        MinimizeBTN.setBackground(new Color(203,68,68));   
// TODO add your handling code here:
    }//GEN-LAST:event_MinimizeBTNMouseEntered

    private void CloseBTNMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CloseBTNMouseEntered
        CloseBTN.setBackground(new Color(203,68,68));   
        // TODO add your handling code here:
    }//GEN-LAST:event_CloseBTNMouseEntered

    private void MinimizeBTNMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MinimizeBTNMouseExited
        MinimizeBTN.setBackground(new Color(254, 86, 86));
// TODO add your handling code here:
    }//GEN-LAST:event_MinimizeBTNMouseExited

    private void CloseBTNMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CloseBTNMouseExited
        CloseBTN.setBackground(new Color(254, 86, 86));
// TODO add your handling code here:
    }//GEN-LAST:event_CloseBTNMouseExited

    private void UserIDFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_UserIDFieldFocusGained
        UserIDField.setText("");
    }//GEN-LAST:event_UserIDFieldFocusGained

    private void SeePassMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SeePassMousePressed
        // TODO add your handling code here:
        HidePass.setVisible(true);
        SeePass.setVisible(false);
        password.setEchoChar((char)0);
        
    }//GEN-LAST:event_SeePassMousePressed

    private void HidePassMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HidePassMousePressed
        // TODO add your handling code here:
        HidePass.setVisible(false);
        SeePass.setVisible(true);
        password.setEchoChar((char)'•');
        
    }//GEN-LAST:event_HidePassMousePressed

    private void passwordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_passwordFocusGained
        // TODO add your handling code here:
        password.setText("");
        boolean blCapsOn = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
        if (blCapsOn) {
            lblWrongPassword1.setVisible(true);
        } else {
            lblWrongPassword1.setVisible(false);
        }
    }//GEN-LAST:event_passwordFocusGained

    private void passwordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordActionPerformed
        // TODO add your handling code here:
        checkLogin();
    }//GEN-LAST:event_passwordActionPerformed

    private void passwordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordKeyPressed
        // TODO add your handling code here:
        boolean blCapsOn = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
        if (blCapsOn) {
            lblWrongPassword1.setVisible(true);
        } else {
            lblWrongPassword1.setVisible(false);
        }
    }//GEN-LAST:event_passwordKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Background;
    private javax.swing.JButton CloseBTN;
    private javax.swing.JLabel EnrlmntLBL;
    private javax.swing.JLabel HidePass;
    private javax.swing.JButton LogInBTN;
    private javax.swing.JLabel LogInLBL;
    private javax.swing.JLabel LogInPanel;
    private javax.swing.JLabel MainLBL;
    private javax.swing.JButton MinimizeBTN;
    private javax.swing.JLabel PLMLogo;
    private javax.swing.JLabel Password;
    private javax.swing.JLabel PasswordIcon;
    private javax.swing.JLabel SeePass;
    private javax.swing.JButton SupportBTN;
    private javax.swing.JLabel UserID;
    private javax.swing.JTextField UserIDField;
    private javax.swing.JLabel UserIcon;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblErrorID1;
    private javax.swing.JLabel lblWrongPassword;
    private javax.swing.JLabel lblWrongPassword1;
    private javax.swing.JPasswordField password;
    // End of variables declaration//GEN-END:variables
}
