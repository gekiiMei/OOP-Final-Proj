/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package regenrolmentsys;


//import java.awt.Point;
import java.awt.Color;
import java.sql.*;
import javax.swing.JOptionPane;
import java.util.Random;
import javax.swing.table.DefaultTableModel;



/**
 *
 * @author harley
 */
public class StudentMenu extends javax.swing.JPanel {
    private MainFrame mf = null;
    private String currentUser = "";
    private Connection con = null;
    private ResultSet rs = null;
    private PreparedStatement ps = null;
    private PreparedStatement psEnrolConfirm = null;
    /**
     * Creates new form StudentMenu
     */
    public StudentMenu(){
        initComponents();
        jPanel8.setBackground(new Color(0,0,0,300));
    }
    public StudentMenu(MainFrame mf) {
        initComponents();
        this.mf = mf;
        this.currentUser = mf.getUserID();
    }
    
    public javax.swing.JTabbedPane getTabs() {
        return tabs;
    }
    
    public void setUserID(String userID) {
        this.currentUser = userID;
    }
    
    
    public void profileStudentsTab(){
        con = ConnectDB.connect();
        try{
            rs = con.prepareStatement("SELECT * FROM finals.STUDENT WHERE student_no='"+mf.getUserID()+"'").executeQuery();
            while (rs.next()){
                lblStudentNo.setText("STUDENT NUMBER: " +(rs.getString("student_no")));
                lblStudentLN.setText("LAST NAME: "+(rs.getString("last_name")));
                lblStudentFN.setText("FIRST NAME: "+(rs.getString("first_name")));
                lblStudentEmail.setText("EMAIL: "+(rs.getString("email")));
                lblStudentGender.setText("GENDER: "+(rs.getString("gender")));
                lblStudentCourseCode.setText("COURSE CODE: "+(rs.getString("course_code")));
                lblStudentCPNum.setText("MOBILE NUMBER: "+(rs.getString("cp_num")));
                lblStudentAddress.setText("ADDRESS: "+(rs.getString("address")));
                lblStudentBday.setText("BIRTHDAY: "+(rs.getString("bday")));
                lblStudentStatus.setText("STATUS: "+(rs.getString("status")));
            }
       }
       catch (Exception e){
            System.out.println(e);
       } 
    }
    
    public void studentScheduleTab(){
        con = ConnectDB.connect();
        try{
            ps = con.prepareStatement("SELECT sy, semester, subject_code, block_no FROM finals.ENROLLED_SUBJECT WHERE student_no = ? AND status = ?");
            ps.setString(1, this.currentUser);
            ps.setString(2, "Enrolled");
            rs = ps.executeQuery();
            
            StringBuilder subCodeBuilder = new StringBuilder(); // StringBuilder for sub_codes
            String QueredSY = "";
            String QueredSem = "";
            String QueredBlk = "";
                
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "You haven't enrolled any subjects yet!", "No Subjects Enrolled", JOptionPane.INFORMATION_MESSAGE);
                }else{
                     do{
                        QueredSY = rs.getString("sy");
                        QueredSem = rs.getString("semester");
                        QueredBlk = rs.getString("block_no");
                        if (subCodeBuilder.length() > 0) { // checks if it has value inside
                             subCodeBuilder.append(", ");
                            }
                            subCodeBuilder.append("'").append(rs.getString("subject_code")).append("'");
                        } while (rs.next());
                    }
                
                    if (subCodeBuilder.length() > 0){
                    String subjectCodesIN = subCodeBuilder.toString();
                    ps = con.prepareStatement("SELECT * "
                                            + "FROM finals.SUBJECT_SCHEDULE "
                                            + "WHERE sy = '"+QueredSY+"' "
                                            + "AND semester = '"+QueredSem+"'"
                                            + "AND subject_code IN ("+subjectCodesIN+")"
                                            + "AND block_no = '"+QueredBlk+"'");
                    rs = ps.executeQuery();
                    
                    if(rs.next()){
                            rs = ps.executeQuery();
                            System.out.println(subjectCodesIN);
                            tblSchedule.setModel(TableUtil.resultSetToTableModel(rs));
                    }
                    }          
        }
        catch(Exception e){
            System.out.println(e);   
        }
    }
    
    public void resetSchedTable(){
        DefaultTableModel model = new DefaultTableModel();
        tblSchedule.setModel(model);
    }
    
        
    public void loadGradesTab() {
         con = ConnectDB.connect();
         try {
             rs = con.prepareStatement("SELECT * FROM finals.SY").executeQuery();
             while (rs.next())
                 cmbGradeSY.addItem(rs.getString("sy"));
             rs = con.prepareStatement("SELECT * FROM finals.SEMESTER").executeQuery();
             while (rs.next())
                 cmbGradeSem.addItem(rs.getString("semester"));
         } catch (Exception e) {
             System.out.println(e); //TODO: ERROR MSG
         }
    }
    
    private void loadGradesTable() {
        con = ConnectDB.connect();
        try {
            ps = con.prepareStatement("SELECT * FROM finals.GRADE WHERE student_no = ? AND SY = ? AND SEMESTER = ?"); //TODO: REPLACE WITH VIEW
            ps.setString(1, currentUser);
            ps.setString(2, cmbGradeSY.getSelectedItem().toString());
            ps.setString(3, cmbGradeSem.getSelectedItem().toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                rs = ps.executeQuery();
                tblGradesTable.setModel(TableUtil.resultSetToTableModel(rs));
            }
            else {
                gradesTableEmpty();
            }
        } catch (Exception e) {
            System.out.println(e); //TODO: ERROR MSG
        }
    }
    
    private void resetGradesTable() {
        tblGradesTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {},
                new String [] {"Select SY and Sem"}
                ) {
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return false;
                    }
                });
    }
    
    private void gradesTableEmpty() {
        tblGradesTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {},
                new String [] {"No grades found in SY and Sem"}
                ) {
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return false;
                    }
                });
    }
    
    public void loadEnrolmentTab() {
        con = ConnectDB.connect();
        try {
             rs = con.prepareStatement("SELECT * FROM finals.SY").executeQuery();
             while (rs.next())
                 cmbEnrolSy.addItem(rs.getString("sy"));
             rs = con.prepareStatement("SELECT * FROM finals.SEMESTER").executeQuery();
             while (rs.next())
                 cmbEnrolSem.addItem(rs.getString("semester"));
         } catch (Exception e) {
             System.out.println(e); //TODO: ERROR MSG
         }
    }
    
    private void resetEnrolmentSchedTable() {
        tblEnrolSchedule.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {},
                new String [] {"Select School Year and Semester"}
                ) {
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return false;
                    }
                });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        btnProfile = new javax.swing.JButton();
        btnEnrolment = new javax.swing.JButton();
        btnSched = new javax.swing.JButton();
        btnGrades = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        btnBackStudentMenu = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        tabs = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblEnrolSchedule = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cmbEnrolSy = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        cmbEnrolSem = new javax.swing.JComboBox<>();
        btnEnrolConfirm = new javax.swing.JButton();
        btnEnrolSchedView = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblSchedule = new javax.swing.JTable();
        jLabel18 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblGradesTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        cmbGradeSY = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        cmbGradeSem = new javax.swing.JComboBox<>();
        btnGradeSearch = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        lblStudentNo = new javax.swing.JLabel();
        lblStudentLN = new javax.swing.JLabel();
        lblStudentFN = new javax.swing.JLabel();
        lblStudentEmail = new javax.swing.JLabel();
        lblStudentGender = new javax.swing.JLabel();
        lblStudentCourseCode = new javax.swing.JLabel();
        lblStudentCPNum = new javax.swing.JLabel();
        lblStudentAddress = new javax.swing.JLabel();
        lblStudentBday = new javax.swing.JLabel();
        lblStudentStatus = new javax.swing.JLabel();
        btnChangePassword = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        MainLBL = new javax.swing.JLabel();
        PLMLogo = new javax.swing.JLabel();
        MinimizeBTN = new javax.swing.JButton();
        CloseBTN = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel10 = new javax.swing.JPanel();
        btnProfile1 = new javax.swing.JButton();
        btnEnrolment1 = new javax.swing.JButton();
        btnSched1 = new javax.swing.JButton();
        btnGrades1 = new javax.swing.JButton();
        btnLogout1 = new javax.swing.JButton();
        btnBackStudentMenu1 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        tabs1 = new javax.swing.JTabbedPane();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblEnrolSchedule1 = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cmbEnrolSy1 = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        cmbEnrolSem1 = new javax.swing.JComboBox<>();
        btnEnrolConfirm1 = new javax.swing.JButton();
        btnEnrolSchedView1 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblSchedule1 = new javax.swing.JTable();
        jLabel17 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblGradesTable1 = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        cmbGradeSY1 = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        cmbGradeSem1 = new javax.swing.JComboBox<>();
        btnGradeSearch1 = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        lblStudentNo1 = new javax.swing.JLabel();
        lblStudentLN1 = new javax.swing.JLabel();
        lblStudentFN1 = new javax.swing.JLabel();
        lblStudentEmail1 = new javax.swing.JLabel();
        lblStudentGender1 = new javax.swing.JLabel();
        lblStudentCourseCode1 = new javax.swing.JLabel();
        lblStudentCPNum1 = new javax.swing.JLabel();
        lblStudentAddress1 = new javax.swing.JLabel();
        lblStudentBday1 = new javax.swing.JLabel();
        lblStudentStatus1 = new javax.swing.JLabel();
        btnChangePassword1 = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        MainLBL1 = new javax.swing.JLabel();
        PLMLogo1 = new javax.swing.JLabel();
        MinimizeBTN1 = new javax.swing.JButton();
        CloseBTN1 = new javax.swing.JButton();

        setBackground(new java.awt.Color(230, 68, 68));
        setPreferredSize(new java.awt.Dimension(1280, 720));

        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setDividerSize(0);

        jPanel1.setBackground(new java.awt.Color(230, 68, 68));
        jPanel1.setLayout(null);

        btnProfile.setBackground(new java.awt.Color(230, 68, 68));
        btnProfile.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnProfile.setForeground(new java.awt.Color(255, 255, 255));
        btnProfile.setIcon(new javax.swing.ImageIcon("C:\\Users\\ENDRIX\\OneDrive\\Pictures\\Saved Pictures\\1 (2).png")); // NOI18N
        btnProfile.setText("Profile");
        btnProfile.setBorder(null);
        btnProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProfileActionPerformed(evt);
            }
        });
        jPanel1.add(btnProfile);
        btnProfile.setBounds(0, 6, 160, 50);

        btnEnrolment.setBackground(new java.awt.Color(230, 68, 68));
        btnEnrolment.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnEnrolment.setForeground(new java.awt.Color(255, 255, 255));
        btnEnrolment.setIcon(new javax.swing.ImageIcon("C:\\Users\\ENDRIX\\OneDrive\\Pictures\\Saved Pictures\\2 (1).png")); // NOI18N
        btnEnrolment.setText("Enrollment");
        btnEnrolment.setBorder(null);
        btnEnrolment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnrolmentActionPerformed(evt);
            }
        });
        jPanel1.add(btnEnrolment);
        btnEnrolment.setBounds(0, 80, 170, 60);

        btnSched.setBackground(new java.awt.Color(230, 68, 68));
        btnSched.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnSched.setForeground(new java.awt.Color(255, 255, 255));
        btnSched.setIcon(new javax.swing.ImageIcon("C:\\Users\\ENDRIX\\OneDrive\\Pictures\\Saved Pictures\\3 (1).png")); // NOI18N
        btnSched.setText("Schedule");
        btnSched.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        btnSched.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSchedActionPerformed(evt);
            }
        });
        jPanel1.add(btnSched);
        btnSched.setBounds(0, 140, 160, 60);

        btnGrades.setBackground(new java.awt.Color(230, 68, 68));
        btnGrades.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnGrades.setForeground(new java.awt.Color(255, 255, 255));
        btnGrades.setIcon(new javax.swing.ImageIcon("C:\\Users\\ENDRIX\\OneDrive\\Pictures\\Saved Pictures\\4 (1).png")); // NOI18N
        btnGrades.setText("Grades");
        btnGrades.setBorder(null);
        btnGrades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGradesActionPerformed(evt);
            }
        });
        jPanel1.add(btnGrades);
        btnGrades.setBounds(0, 210, 140, 60);

        btnLogout.setBackground(new java.awt.Color(230, 68, 68));
        btnLogout.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout.setIcon(new javax.swing.ImageIcon("C:\\Users\\ENDRIX\\OneDrive\\Pictures\\Saved Pictures\\5 (1).png")); // NOI18N
        btnLogout.setText("LogOut");
        btnLogout.setBorder(null);
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });
        jPanel1.add(btnLogout);
        btnLogout.setBounds(20, 550, 139, 60);

        btnBackStudentMenu.setBackground(new java.awt.Color(230, 68, 68));
        btnBackStudentMenu.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnBackStudentMenu.setForeground(new java.awt.Color(255, 255, 255));
        btnBackStudentMenu.setIcon(new javax.swing.ImageIcon("C:\\Users\\ENDRIX\\OneDrive\\Pictures\\Saved Pictures\\icons8-back-arrow-50.png")); // NOI18N
        btnBackStudentMenu.setText("Back");
        btnBackStudentMenu.setBorder(null);
        btnBackStudentMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackStudentMenuActionPerformed(evt);
            }
        });
        jPanel1.add(btnBackStudentMenu);
        btnBackStudentMenu.setBounds(10, 490, 125, 50);

        jSplitPane1.setLeftComponent(jPanel1);

        jPanel2.setBackground(new java.awt.Color(230, 68, 68));

        tabs.setTabPlacement(javax.swing.JTabbedPane.RIGHT);
        tabs.setToolTipText("");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(null);

        tblEnrolSchedule.setBackground(new java.awt.Color(153, 153, 153));
        tblEnrolSchedule.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select School Year and Semester"
            }
        ));
        jScrollPane2.setViewportView(tblEnrolSchedule);

        jPanel4.add(jScrollPane2);
        jScrollPane2.setBounds(20, 100, 820, 402);

        jLabel5.setText("Schedule");
        jPanel4.add(jLabel5);
        jLabel5.setBounds(6, 62, 48, 16);

        jLabel6.setText("School Year :");
        jPanel4.add(jLabel6);
        jLabel6.setBounds(10, 30, 68, 16);

        jPanel4.add(cmbEnrolSy);
        cmbEnrolSy.setBounds(80, 30, 72, 22);

        jLabel2.setText("Semester :");
        jPanel4.add(jLabel2);
        jLabel2.setBounds(176, 24, 54, 16);

        cmbEnrolSem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbEnrolSemActionPerformed(evt);
            }
        });
        jPanel4.add(cmbEnrolSem);
        cmbEnrolSem.setBounds(242, 21, 72, 22);

        btnEnrolConfirm.setText("Confirm Enrolment");
        btnEnrolConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnrolConfirmActionPerformed(evt);
            }
        });
        jPanel4.add(btnEnrolConfirm);
        btnEnrolConfirm.setBounds(870, 500, 132, 23);

        btnEnrolSchedView.setText("View Schedule");
        btnEnrolSchedView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnrolSchedViewActionPerformed(evt);
            }
        });
        jPanel4.add(btnEnrolSchedView);
        btnEnrolSchedView.setBounds(347, 21, 106, 23);

        jLabel8.setIcon(new javax.swing.ImageIcon("C:\\Users\\ENDRIX\\OneDrive\\Pictures\\Saved Pictures\\background plm.png")); // NOI18N
        jLabel8.setText("jLabel8");
        jPanel4.add(jLabel8);
        jLabel8.setBounds(0, 0, 1040, 610);

        tabs.addTab("", jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(null);

        jLabel3.setText("SCHEDULE");
        jPanel5.add(jLabel3);
        jLabel3.setBounds(54, 39, 60, 16);

        tblSchedule.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(tblSchedule);

        jPanel5.add(jScrollPane3);
        jScrollPane3.setBounds(10, 80, 810, 523);

        jLabel18.setIcon(new javax.swing.ImageIcon("C:\\Users\\ENDRIX\\OneDrive\\Pictures\\Saved Pictures\\PLM_background.jpeg")); // NOI18N
        jLabel18.setText("jLabel18");
        jPanel5.add(jLabel18);
        jLabel18.setBounds(6, 6, 860, 680);

        tabs.addTab("", jPanel5);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        tblGradesTable.setBackground(new java.awt.Color(102, 102, 102));
        tblGradesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select SY and Sem"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblGradesTable);
        if (tblGradesTable.getColumnModel().getColumnCount() > 0) {
            tblGradesTable.getColumnModel().getColumn(0).setResizable(false);
        }

        jLabel1.setText("School year :");

        cmbGradeSY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbGradeSYActionPerformed(evt);
            }
        });

        jLabel4.setText("Semester");

        btnGradeSearch.setText("Search");
        btnGradeSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGradeSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 968, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbGradeSY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbGradeSem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnGradeSearch)))
                .addGap(67, 67, 67))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbGradeSY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(cmbGradeSem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGradeSearch))
                .addGap(19, 19, 19)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabs.addTab("", jPanel6);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(null);

        lblStudentNo.setText("STUDENT NUMBER:");
        jPanel3.add(lblStudentNo);
        lblStudentNo.setBounds(76, 22, 103, 16);

        lblStudentLN.setText("LAST NAME:");
        jPanel3.add(lblStudentLN);
        lblStudentLN.setBounds(115, 44, 66, 16);

        lblStudentFN.setText("FIRST NAME:");
        jPanel3.add(lblStudentFN);
        lblStudentFN.setBounds(113, 72, 68, 16);

        lblStudentEmail.setText("EMAIL:");
        jPanel3.add(lblStudentEmail);
        lblStudentEmail.setBounds(145, 100, 37, 16);

        lblStudentGender.setText("GENDER:");
        jPanel3.add(lblStudentGender);
        lblStudentGender.setBounds(133, 128, 47, 16);

        lblStudentCourseCode.setText("COURSE CODE:");
        jPanel3.add(lblStudentCourseCode);
        lblStudentCourseCode.setBounds(100, 156, 81, 16);

        lblStudentCPNum.setText("CELLPHONE NUMBER:");
        jPanel3.add(lblStudentCPNum);
        lblStudentCPNum.setBounds(63, 187, 120, 16);

        lblStudentAddress.setText("ADDRESS:");
        jPanel3.add(lblStudentAddress);
        lblStudentAddress.setBounds(127, 209, 52, 16);

        lblStudentBday.setText("BIRTHDAY:");
        jPanel3.add(lblStudentBday);
        lblStudentBday.setBounds(123, 231, 58, 16);

        lblStudentStatus.setText("STATUS:");
        jPanel3.add(lblStudentStatus);
        lblStudentStatus.setBounds(137, 259, 43, 16);

        btnChangePassword.setText("Change password");
        btnChangePassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangePasswordActionPerformed(evt);
            }
        });
        jPanel3.add(btnChangePassword);
        btnChangePassword.setBounds(57, 311, 124, 23);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 990, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 340, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel8);
        jPanel8.setBounds(30, 10, 990, 340);

        jLabel7.setIcon(new javax.swing.ImageIcon("C:\\Users\\ENDRIX\\OneDrive\\Pictures\\Saved Pictures\\background plm.png")); // NOI18N
        jLabel7.setText("jLabel7");
        jPanel3.add(jLabel7);
        jLabel7.setBounds(20, 10, 1050, 700);

        tabs.addTab("", jPanel3);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 1066, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabs)
                .addContainerGap())
        );

        jSplitPane1.setRightComponent(jPanel2);

        jPanel7.setBackground(new java.awt.Color(254, 86, 86));
        jPanel7.setLayout(null);

        MainLBL.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        MainLBL.setForeground(new java.awt.Color(255, 255, 255));
        MainLBL.setText("Enrollment System for Regular Students");
        jPanel7.add(MainLBL);
        MainLBL.setBounds(40, 0, 290, 30);

        PLMLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/PLM_Seal_2013.png"))); // NOI18N
        jPanel7.add(PLMLogo);
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
        jPanel7.add(MinimizeBTN);
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
        jPanel7.add(CloseBTN);
        CloseBTN.setBounds(1250, 0, 30, 30);

        jPanel9.setBackground(new java.awt.Color(230, 68, 68));
        jPanel9.setPreferredSize(new java.awt.Dimension(1280, 720));

        jSplitPane2.setDividerLocation(200);
        jSplitPane2.setDividerSize(0);

        jPanel10.setBackground(new java.awt.Color(230, 68, 68));
        jPanel10.setLayout(null);

        btnProfile1.setBackground(new java.awt.Color(230, 68, 68));
        btnProfile1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnProfile1.setForeground(new java.awt.Color(255, 255, 255));
        btnProfile1.setIcon(new javax.swing.ImageIcon("C:\\Users\\ENDRIX\\OneDrive\\Pictures\\Saved Pictures\\1 (2).png")); // NOI18N
        btnProfile1.setText("Profile");
        btnProfile1.setBorder(null);
        btnProfile1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProfile1ActionPerformed(evt);
            }
        });
        jPanel10.add(btnProfile1);
        btnProfile1.setBounds(0, 10, 160, 50);

        btnEnrolment1.setBackground(new java.awt.Color(230, 68, 68));
        btnEnrolment1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnEnrolment1.setForeground(new java.awt.Color(255, 255, 255));
        btnEnrolment1.setIcon(new javax.swing.ImageIcon("C:\\Users\\ENDRIX\\OneDrive\\Pictures\\Saved Pictures\\2 (1).png")); // NOI18N
        btnEnrolment1.setText("Enrollment");
        btnEnrolment1.setBorder(null);
        btnEnrolment1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnrolment1ActionPerformed(evt);
            }
        });
        jPanel10.add(btnEnrolment1);
        btnEnrolment1.setBounds(0, 80, 170, 60);

        btnSched1.setBackground(new java.awt.Color(230, 68, 68));
        btnSched1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnSched1.setForeground(new java.awt.Color(255, 255, 255));
        btnSched1.setIcon(new javax.swing.ImageIcon("C:\\Users\\ENDRIX\\OneDrive\\Pictures\\Saved Pictures\\3 (1).png")); // NOI18N
        btnSched1.setText("Schedule");
        btnSched1.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        btnSched1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSched1ActionPerformed(evt);
            }
        });
        jPanel10.add(btnSched1);
        btnSched1.setBounds(0, 140, 160, 60);

        btnGrades1.setBackground(new java.awt.Color(230, 68, 68));
        btnGrades1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnGrades1.setForeground(new java.awt.Color(255, 255, 255));
        btnGrades1.setIcon(new javax.swing.ImageIcon("C:\\Users\\ENDRIX\\OneDrive\\Pictures\\Saved Pictures\\4 (1).png")); // NOI18N
        btnGrades1.setText("Grades");
        btnGrades1.setBorder(null);
        btnGrades1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrades1ActionPerformed(evt);
            }
        });
        jPanel10.add(btnGrades1);
        btnGrades1.setBounds(0, 210, 140, 60);

        btnLogout1.setBackground(new java.awt.Color(230, 68, 68));
        btnLogout1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnLogout1.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout1.setIcon(new javax.swing.ImageIcon("C:\\Users\\ENDRIX\\OneDrive\\Pictures\\Saved Pictures\\5 (1).png")); // NOI18N
        btnLogout1.setText("LogOut");
        btnLogout1.setBorder(null);
        btnLogout1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogout1ActionPerformed(evt);
            }
        });
        jPanel10.add(btnLogout1);
        btnLogout1.setBounds(20, 550, 139, 60);

        btnBackStudentMenu1.setBackground(new java.awt.Color(230, 68, 68));
        btnBackStudentMenu1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnBackStudentMenu1.setForeground(new java.awt.Color(255, 255, 255));
        btnBackStudentMenu1.setIcon(new javax.swing.ImageIcon("C:\\Users\\ENDRIX\\OneDrive\\Pictures\\Saved Pictures\\icons8-back-arrow-50.png")); // NOI18N
        btnBackStudentMenu1.setText("Back");
        btnBackStudentMenu1.setBorder(null);
        btnBackStudentMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackStudentMenu1ActionPerformed(evt);
            }
        });
        jPanel10.add(btnBackStudentMenu1);
        btnBackStudentMenu1.setBounds(10, 490, 125, 50);

        jSplitPane2.setLeftComponent(jPanel10);

        jPanel11.setBackground(new java.awt.Color(230, 68, 68));

        tabs1.setTabPlacement(javax.swing.JTabbedPane.RIGHT);
        tabs1.setToolTipText("");

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setLayout(null);

        tblEnrolSchedule1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select School Year and Semester"
            }
        ));
        jScrollPane4.setViewportView(tblEnrolSchedule1);

        jPanel12.add(jScrollPane4);
        jScrollPane4.setBounds(10, 80, 870, 402);

        jLabel9.setText("Schedule");
        jPanel12.add(jLabel9);
        jLabel9.setBounds(6, 62, 48, 16);

        jLabel10.setText("School Year :");
        jPanel12.add(jLabel10);
        jLabel10.setBounds(10, 30, 68, 16);

        jPanel12.add(cmbEnrolSy1);
        cmbEnrolSy1.setBounds(80, 30, 72, 22);

        jLabel11.setText("Semester :");
        jPanel12.add(jLabel11);
        jLabel11.setBounds(176, 24, 54, 16);

        cmbEnrolSem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbEnrolSem1ActionPerformed(evt);
            }
        });
        jPanel12.add(cmbEnrolSem1);
        cmbEnrolSem1.setBounds(242, 21, 72, 22);

        btnEnrolConfirm1.setText("Confirm Enrolment");
        btnEnrolConfirm1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnrolConfirm1ActionPerformed(evt);
            }
        });
        jPanel12.add(btnEnrolConfirm1);
        btnEnrolConfirm1.setBounds(870, 500, 132, 23);

        btnEnrolSchedView1.setText("View Schedule");
        btnEnrolSchedView1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnrolSchedView1ActionPerformed(evt);
            }
        });
        jPanel12.add(btnEnrolSchedView1);
        btnEnrolSchedView1.setBounds(347, 21, 106, 23);

        jLabel12.setIcon(new javax.swing.ImageIcon("C:\\Users\\ENDRIX\\OneDrive\\Pictures\\Saved Pictures\\background plm.png")); // NOI18N
        jLabel12.setText("jLabel8");
        jPanel12.add(jLabel12);
        jLabel12.setBounds(0, 0, 1040, 610);

        tabs1.addTab("", jPanel12);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setLayout(null);

        jLabel13.setText("SCHEDULE");
        jPanel13.add(jLabel13);
        jLabel13.setBounds(54, 39, 70, 16);

        tblSchedule1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane5.setViewportView(tblSchedule1);

        jPanel13.add(jScrollPane5);
        jScrollPane5.setBounds(10, 70, 850, 523);

        jLabel17.setIcon(new javax.swing.ImageIcon("C:\\Users\\ENDRIX\\OneDrive\\Pictures\\Saved Pictures\\PLM_background.jpeg")); // NOI18N
        jLabel17.setText("jLabel17");
        jPanel13.add(jLabel17);
        jLabel17.setBounds(0, 0, 1040, 610);

        tabs1.addTab("", jPanel13);

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));

        tblGradesTable1.setBackground(new java.awt.Color(102, 102, 102));
        tblGradesTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select SY and Sem"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(tblGradesTable1);
        if (tblGradesTable1.getColumnModel().getColumnCount() > 0) {
            tblGradesTable1.getColumnModel().getColumn(0).setResizable(false);
        }

        jLabel14.setText("School year :");

        cmbGradeSY1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbGradeSY1ActionPerformed(evt);
            }
        });

        jLabel15.setText("Semester");

        btnGradeSearch1.setText("Search");
        btnGradeSearch1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGradeSearch1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 968, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbGradeSY1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbGradeSem1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnGradeSearch1)))
                .addGap(67, 67, 67))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(cmbGradeSY1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(cmbGradeSem1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGradeSearch1))
                .addGap(19, 19, 19)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabs1.addTab("", jPanel14);

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setLayout(null);

        lblStudentNo1.setText("STUDENT NUMBER:");
        jPanel15.add(lblStudentNo1);
        lblStudentNo1.setBounds(76, 22, 103, 16);

        lblStudentLN1.setText("LAST NAME:");
        jPanel15.add(lblStudentLN1);
        lblStudentLN1.setBounds(115, 44, 66, 16);

        lblStudentFN1.setText("FIRST NAME:");
        jPanel15.add(lblStudentFN1);
        lblStudentFN1.setBounds(113, 72, 68, 16);

        lblStudentEmail1.setText("EMAIL:");
        jPanel15.add(lblStudentEmail1);
        lblStudentEmail1.setBounds(145, 100, 37, 16);

        lblStudentGender1.setText("GENDER:");
        jPanel15.add(lblStudentGender1);
        lblStudentGender1.setBounds(133, 128, 47, 16);

        lblStudentCourseCode1.setText("COURSE CODE:");
        jPanel15.add(lblStudentCourseCode1);
        lblStudentCourseCode1.setBounds(100, 156, 81, 16);

        lblStudentCPNum1.setText("CELLPHONE NUMBER:");
        jPanel15.add(lblStudentCPNum1);
        lblStudentCPNum1.setBounds(63, 187, 120, 16);

        lblStudentAddress1.setText("ADDRESS:");
        jPanel15.add(lblStudentAddress1);
        lblStudentAddress1.setBounds(127, 209, 52, 16);

        lblStudentBday1.setText("BIRTHDAY:");
        jPanel15.add(lblStudentBday1);
        lblStudentBday1.setBounds(123, 231, 58, 16);

        lblStudentStatus1.setText("STATUS:");
        jPanel15.add(lblStudentStatus1);
        lblStudentStatus1.setBounds(137, 259, 43, 16);

        btnChangePassword1.setText("Change password");
        btnChangePassword1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangePassword1ActionPerformed(evt);
            }
        });
        jPanel15.add(btnChangePassword1);
        btnChangePassword1.setBounds(57, 311, 124, 23);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 990, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 340, Short.MAX_VALUE)
        );

        jPanel15.add(jPanel16);
        jPanel16.setBounds(30, 10, 990, 340);

        jLabel16.setIcon(new javax.swing.ImageIcon("C:\\Users\\ENDRIX\\OneDrive\\Pictures\\Saved Pictures\\background plm.png")); // NOI18N
        jLabel16.setText("jLabel7");
        jPanel15.add(jLabel16);
        jLabel16.setBounds(20, 10, 1050, 700);

        tabs1.addTab("", jPanel15);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabs1, javax.swing.GroupLayout.PREFERRED_SIZE, 1066, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabs1)
                .addContainerGap())
        );

        jSplitPane2.setRightComponent(jPanel11);

        jPanel17.setBackground(new java.awt.Color(254, 86, 86));
        jPanel17.setLayout(null);

        MainLBL1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        MainLBL1.setForeground(new java.awt.Color(255, 255, 255));
        MainLBL1.setText("Enrollment System for Regular Students");
        jPanel17.add(MainLBL1);
        MainLBL1.setBounds(40, 0, 290, 30);

        PLMLogo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/PLM_Seal_2013.png"))); // NOI18N
        jPanel17.add(PLMLogo1);
        PLMLogo1.setBounds(10, 0, 30, 30);

        MinimizeBTN1.setBackground(new java.awt.Color(254, 86, 86));
        MinimizeBTN1.setFont(new java.awt.Font("Boldfinger", 0, 24)); // NOI18N
        MinimizeBTN1.setForeground(new java.awt.Color(255, 255, 255));
        MinimizeBTN1.setText("-");
        MinimizeBTN1.setToolTipText("");
        MinimizeBTN1.setBorder(null);
        MinimizeBTN1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                MinimizeBTN1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                MinimizeBTN1MouseExited(evt);
            }
        });
        MinimizeBTN1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MinimizeBTN1ActionPerformed(evt);
            }
        });
        jPanel17.add(MinimizeBTN1);
        MinimizeBTN1.setBounds(1220, 0, 30, 30);

        CloseBTN1.setBackground(new java.awt.Color(254, 86, 86));
        CloseBTN1.setFont(new java.awt.Font("Boldfinger", 0, 18)); // NOI18N
        CloseBTN1.setForeground(new java.awt.Color(255, 255, 255));
        CloseBTN1.setText("X");
        CloseBTN1.setBorder(null);
        CloseBTN1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CloseBTN1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                CloseBTN1MouseExited(evt);
            }
        });
        CloseBTN1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseBTN1ActionPerformed(evt);
            }
        });
        jPanel17.add(CloseBTN1);
        CloseBTN1.setBounds(1250, 0, 30, 30);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, 1293, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSplitPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 710, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 1293, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 710, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        // TODO add your handling code here:
        
        int response = JOptionPane.showConfirmDialog(this, "Do you really want to log-out?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (response == 0){
            mf.setUserID("");
            mf.switchCard("LoginCard");
            resetGradesTable();
            resetSchedTable();
        }
        else{
            JOptionPane.showMessageDialog(this, "Cancelled");
        }
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProfileActionPerformed
        // TODO add your handling code here:
        tabs.setSelectedIndex(0);
        profileStudentsTab();
    }//GEN-LAST:event_btnProfileActionPerformed

    private void btnEnrolmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnrolmentActionPerformed
        // TODO add your handling code here:
        loadEnrolmentTab();
        tabs.setSelectedIndex(1);
    }//GEN-LAST:event_btnEnrolmentActionPerformed

    private void btnSchedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSchedActionPerformed
        // TODO add your handling code here:
        tabs.setSelectedIndex(2);
        studentScheduleTab();
    }//GEN-LAST:event_btnSchedActionPerformed

    private void btnGradesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGradesActionPerformed
        // TODO add your handling code here:
        tabs.setSelectedIndex(3);
        loadGradesTab();
    }//GEN-LAST:event_btnGradesActionPerformed

    private void btnBackStudentMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackStudentMenuActionPerformed
        // TODO add your handling code here:
        mf.switchCard("StudentHomeCard");
    }//GEN-LAST:event_btnBackStudentMenuActionPerformed

    private void cmbGradeSYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbGradeSYActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbGradeSYActionPerformed

    private void btnGradeSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGradeSearchActionPerformed
        // TODO add your handling code here:
        loadGradesTable();
    }//GEN-LAST:event_btnGradeSearchActionPerformed

    private void cmbEnrolSemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbEnrolSemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbEnrolSemActionPerformed

    private void btnEnrolSchedViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnrolSchedViewActionPerformed
        // TODO add your handling code here:
        resetEnrolmentSchedTable();
        con = ConnectDB.connect();
        Random rand = new Random();
        String strSelectedSY = cmbEnrolSy.getSelectedItem().toString(), course = "", course_code = "", block = "", enrollQuery = "";
        int intCurrYear = 0, intBlockNo = 0, intBlockCount = 1;
        
        try {
            rs = con.prepareStatement("SELECT course_code FROM finals.STUDENT WHERE student_no = '" + currentUser + "'").executeQuery();
            if (rs.next())
                course_code = rs.getString("course_code");
            
            if (course_code.equals("BSCS-CS"))
                    course = "CS";
            if (course_code.equals("BSIT"))
                    course = "IT";
            
            intCurrYear = (Integer.parseInt(strSelectedSY.substring(0, 4)) - Integer.parseInt(currentUser.substring(0, 4))) + 1;
            
            if (intCurrYear < 1) {
                System.out.println("year cannot be earlier than your enrolment year"); //TODO: error msg
                JOptionPane.showMessageDialog(this, "You cannot enroll to a year earlier than your admittance year!", "Enrollment error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            rs = con.prepareStatement("SELECT sy, semester FROM finals.ENROLLED_SUBJECT WHERE student_no = '" + currentUser + "'").executeQuery();
            while (rs.next()) {
                if (rs.getString("sy").equals(strSelectedSY) && rs.getString("semester").equals(cmbEnrolSem.getSelectedItem().toString())) {
                    System.out.println("already enrolled to this year and semester"); //TODO: error msg
                    JOptionPane.showMessageDialog(this, "You're already enrolled to this school year and semester!", "Enrollment error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            if (intCurrYear == 1) {
                rs = con.prepareStatement("SELECT * FROM finals.BLOCK_NO WHERE block_no LIKE '" + course + Integer.toString(intCurrYear) + "%'").executeQuery();
                if (rs.next()) {
                    while (rs.next())
                        intBlockCount++;
                    block = course + Integer.toString(intCurrYear) + Integer.toString(rand.nextInt(intBlockCount)+1);
                }
            } else if (intCurrYear > 4) { 
                
            }else {
                ps = con.prepareStatement("SELECT block_no FROM finals.ENROLLED_SUBJECT WHERE student_no = ? AND status = ? AND block_no LIKE ?");
                ps.setString(1, currentUser);
                ps.setString(2, "Finished");
                ps.setString(3, course + Integer.toString(intCurrYear - 1) + "%");
                rs = ps.executeQuery();
                if (rs.next()) {
                    block = course + Integer.toString(intCurrYear) + rs.getString("block_no").substring(3);
                } else {
                    System.out.println("missing previous enrolments"); //TODO: error msg
                    JOptionPane.showMessageDialog(this, "You are not eligible for this semester!", "Enrollment error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            ps = con.prepareStatement("SELECT * FROM finals.SUBJECT_SCHEDULE WHERE SY = ? AND SEMESTER = ? AND BLOCK_NO = ?");
            ps.setString(1, strSelectedSY);
            ps.setString(2, cmbEnrolSem.getSelectedItem().toString());
            ps.setString(3, block);
            rs = ps.executeQuery();
            tblEnrolSchedule.setModel(TableUtil.resultSetToTableModel(rs)); //TODO: CHANGE TO VIEW
            rs = ps.executeQuery();
            enrollQuery = "INSERT INTO finals.ENROLLED_SUBJECT VALUES ";
            while (rs.next()) {
                enrollQuery += "('" + strSelectedSY + "', '" + cmbEnrolSem.getSelectedItem().toString() + "', '" + currentUser + "', '" + rs.getString("subject_code") + "', '" + block + "', '" + rs.getString("sequence_no") + "', 'Enrolled'), ";
            }
            enrollQuery = enrollQuery.substring(0, enrollQuery.length()-2);
            System.out.println(enrollQuery);
            psEnrolConfirm = con.prepareStatement(enrollQuery);
        } catch (Exception e) {
            System.out.println(e); //TODO ERROR MSGS
            resetEnrolmentSchedTable();
        }
    }//GEN-LAST:event_btnEnrolSchedViewActionPerformed

    private void btnEnrolConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnrolConfirmActionPerformed
        // TODO add your handling code here:
        try {
            int intAnswer = JOptionPane.showConfirmDialog(null, "Finalize enrollment?", "Enrolling subjects", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (intAnswer == 0) {
                psEnrolConfirm.execute(); //TODO: CONFIRMATION MSG
                resetEnrolmentSchedTable();
                JOptionPane.showMessageDialog(null, "Enrollment successful.");
            } else {
                JOptionPane.showMessageDialog(null, "Enrollment canceled.");
            }
        } catch (Exception e) {
            System.out.println(e); //TODO ERROR MSGS
        }
    }//GEN-LAST:event_btnEnrolConfirmActionPerformed

    private void MinimizeBTNMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MinimizeBTNMouseEntered
        MinimizeBTN.setBackground(new Color(203,68,68));
        // TODO add your handling code here:
    }//GEN-LAST:event_MinimizeBTNMouseEntered

    private void MinimizeBTNMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MinimizeBTNMouseExited
        MinimizeBTN.setBackground(new Color(254, 86, 86));
        // TODO add your handling code here:
    }//GEN-LAST:event_MinimizeBTNMouseExited

    private void MinimizeBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MinimizeBTNActionPerformed
        //minimize button event here:

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
        //exit button event here:

        // TODO add your handling code here:
    }//GEN-LAST:event_CloseBTNActionPerformed

    private void btnChangePasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangePasswordActionPerformed
        // TODO add your handling code here:
        con = ConnectDB.connect();
        String newPassword = JOptionPane.showInputDialog(this, "Enter new password:", "Changing password..", JOptionPane.QUESTION_MESSAGE);
        if (newPassword.isEmpty())
            JOptionPane.showMessageDialog(this, "Password cannot be empty!", "Password error", JOptionPane.ERROR_MESSAGE);
        else {
            String confirmPassword = JOptionPane.showInputDialog(this, "Confirm password:", "Changing password..", JOptionPane.QUESTION_MESSAGE);
            if (newPassword.equals(confirmPassword)) {
                try {
                    con.prepareStatement("UPDATE finals.PASSWORD SET password = '" + newPassword + "' WHERE user_id = '" + currentUser + "'").execute();
                    JOptionPane.showMessageDialog(this, "Passwords successfully updated!", "Changing password..", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    System.out.println(e);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Passwords do not match!", "Password error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnChangePasswordActionPerformed

    private void btnProfile1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProfile1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnProfile1ActionPerformed

    private void btnEnrolment1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnrolment1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEnrolment1ActionPerformed

    private void btnSched1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSched1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSched1ActionPerformed

    private void btnGrades1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrades1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGrades1ActionPerformed

    private void btnLogout1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogout1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLogout1ActionPerformed

    private void btnBackStudentMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackStudentMenu1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBackStudentMenu1ActionPerformed

    private void cmbEnrolSem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbEnrolSem1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbEnrolSem1ActionPerformed

    private void btnEnrolConfirm1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnrolConfirm1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEnrolConfirm1ActionPerformed

    private void btnEnrolSchedView1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnrolSchedView1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEnrolSchedView1ActionPerformed

    private void cmbGradeSY1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbGradeSY1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbGradeSY1ActionPerformed

    private void btnGradeSearch1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGradeSearch1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGradeSearch1ActionPerformed

    private void btnChangePassword1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangePassword1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnChangePassword1ActionPerformed

    private void MinimizeBTN1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MinimizeBTN1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_MinimizeBTN1MouseEntered

    private void MinimizeBTN1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MinimizeBTN1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_MinimizeBTN1MouseExited

    private void MinimizeBTN1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MinimizeBTN1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MinimizeBTN1ActionPerformed

    private void CloseBTN1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CloseBTN1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_CloseBTN1MouseEntered

    private void CloseBTN1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CloseBTN1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_CloseBTN1MouseExited

    private void CloseBTN1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseBTN1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CloseBTN1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CloseBTN;
    private javax.swing.JButton CloseBTN1;
    private javax.swing.JLabel MainLBL;
    private javax.swing.JLabel MainLBL1;
    private javax.swing.JButton MinimizeBTN;
    private javax.swing.JButton MinimizeBTN1;
    private javax.swing.JLabel PLMLogo;
    private javax.swing.JLabel PLMLogo1;
    private javax.swing.JButton btnBackStudentMenu;
    private javax.swing.JButton btnBackStudentMenu1;
    private javax.swing.JButton btnChangePassword;
    private javax.swing.JButton btnChangePassword1;
    private javax.swing.JButton btnEnrolConfirm;
    private javax.swing.JButton btnEnrolConfirm1;
    private javax.swing.JButton btnEnrolSchedView;
    private javax.swing.JButton btnEnrolSchedView1;
    private javax.swing.JButton btnEnrolment;
    private javax.swing.JButton btnEnrolment1;
    private javax.swing.JButton btnGradeSearch;
    private javax.swing.JButton btnGradeSearch1;
    private javax.swing.JButton btnGrades;
    private javax.swing.JButton btnGrades1;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnLogout1;
    private javax.swing.JButton btnProfile;
    private javax.swing.JButton btnProfile1;
    private javax.swing.JButton btnSched;
    private javax.swing.JButton btnSched1;
    private javax.swing.JComboBox<String> cmbEnrolSem;
    private javax.swing.JComboBox<String> cmbEnrolSem1;
    private javax.swing.JComboBox<String> cmbEnrolSy;
    private javax.swing.JComboBox<String> cmbEnrolSy1;
    private javax.swing.JComboBox<String> cmbGradeSY;
    private javax.swing.JComboBox<String> cmbGradeSY1;
    private javax.swing.JComboBox<String> cmbGradeSem;
    private javax.swing.JComboBox<String> cmbGradeSem1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JLabel lblStudentAddress;
    private javax.swing.JLabel lblStudentAddress1;
    private javax.swing.JLabel lblStudentBday;
    private javax.swing.JLabel lblStudentBday1;
    private javax.swing.JLabel lblStudentCPNum;
    private javax.swing.JLabel lblStudentCPNum1;
    private javax.swing.JLabel lblStudentCourseCode;
    private javax.swing.JLabel lblStudentCourseCode1;
    private javax.swing.JLabel lblStudentEmail;
    private javax.swing.JLabel lblStudentEmail1;
    private javax.swing.JLabel lblStudentFN;
    private javax.swing.JLabel lblStudentFN1;
    private javax.swing.JLabel lblStudentGender;
    private javax.swing.JLabel lblStudentGender1;
    private javax.swing.JLabel lblStudentLN;
    private javax.swing.JLabel lblStudentLN1;
    private javax.swing.JLabel lblStudentNo;
    private javax.swing.JLabel lblStudentNo1;
    private javax.swing.JLabel lblStudentStatus;
    private javax.swing.JLabel lblStudentStatus1;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTabbedPane tabs1;
    private javax.swing.JTable tblEnrolSchedule;
    private javax.swing.JTable tblEnrolSchedule1;
    private javax.swing.JTable tblGradesTable;
    private javax.swing.JTable tblGradesTable1;
    private javax.swing.JTable tblSchedule;
    private javax.swing.JTable tblSchedule1;
    // End of variables declaration//GEN-END:variables
}
