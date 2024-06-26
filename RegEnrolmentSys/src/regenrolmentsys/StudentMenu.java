/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package regenrolmentsys;


//import java.awt.Point;
import java.awt.Color;
import java.awt.Font;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
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
    private PreparedStatement psLog = null;
    private PreparedStatement psEnrolConfirm = null;
    /**
     * Creates new form StudentMenu
     */
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
                studentNo.setText(rs.getString("student_no"));
                lastName.setText(rs.getString("last_name"));
                firstName.setText(rs.getString("first_name"));
                email.setText(rs.getString("email"));
                gender.setText(rs.getString("gender"));
                courseCode.setText(rs.getString("course_code"));
                mobileNumber.setText(rs.getString("cp_num"));
                address.setText(rs.getString("address"));
                bday.setText(rs.getString("bday"));
                status.setText(rs.getString("status"));
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
                            TableUtil.styleTable(tblSchedule);
                            TableUtil.resizeColumnWidth(tblSchedule);
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
        double gwa = 0.0;
        int totalUnits = 0;
        try {
            ps = con.prepareStatement("SELECT subject, units, grade, Remarks FROM finals.vwSTUDENTGRADES WHERE student_no = ? AND SY = ? AND Semester = ?"); //TODO: REPLACE WITH VIEW
            ps.setString(1, currentUser);
            ps.setString(2, cmbGradeSY.getSelectedItem().toString());
            ps.setString(3, cmbGradeSem.getSelectedItem().toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                logAction("Viewed grades");
                rs = ps.executeQuery();
                tblGradesTable.setModel(TableUtil.resultSetToTableModel(rs));
                TableUtil.styleTable(tblGradesTable);
                TableUtil.resizeColumnWidth(tblGradesTable);
                rs = ps.executeQuery();
                while (rs.next()) {
                    totalUnits += rs.getInt("units");
                    gwa += rs.getDouble("grade") * rs.getInt("units");
                }
                gwa /= totalUnits;
                lblGradeGWA.setText("GWA: " + new DecimalFormat("#.##").format(gwa));
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
    
    public void toggleSelected(int index) {
        javax.swing.JPanel covers[] = {select1, select2, select3, select4}; //just fill with the cover JPanels for other menus
        for (int i=0; i<covers.length; ++i) {
            if (i == index) 
                covers[i].setVisible(true);
            else
                covers[i].setVisible(false);
        }
    }
    
    private void logAction(String action) {
        con = ConnectDB.connect();
        LocalTime localCurrTime = LocalTime.now();
        LocalDate localCurrDate = LocalDate.now();
        Time currTime = Time.valueOf(localCurrTime);
        Date currDate = Date.valueOf(localCurrDate);
        
        try {
            psLog = con.prepareStatement("INSERT INTO finals.HISTORY VALUES (?, ?, ?, ?, ?)");
            psLog.setString(1, currentUser);
            psLog.setString(2, action);
            psLog.setString(3, "Student");
            psLog.setDate(4, currDate);
            psLog.setTime(5, currTime);
            psLog.execute();
        } catch (Exception e) {
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

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        btnProfile = new javax.swing.JButton();
        btnEnrolment = new javax.swing.JButton();
        btnSched = new javax.swing.JButton();
        btnGrades = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        btnBackStudentMenu = new javax.swing.JButton();
        pficon = new javax.swing.JLabel();
        pficon1 = new javax.swing.JLabel();
        pficon2 = new javax.swing.JLabel();
        pficon3 = new javax.swing.JLabel();
        pficon4 = new javax.swing.JLabel();
        pficon5 = new javax.swing.JLabel();
        select4 = new javax.swing.JPanel();
        select3 = new javax.swing.JPanel();
        select2 = new javax.swing.JPanel();
        select1 = new javax.swing.JPanel();
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
        lblGuideText = new javax.swing.JLabel();
        plmbg3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblSchedule = new javax.swing.JTable();
        plmbg2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblGradesTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        cmbGradeSY = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        cmbGradeSem = new javax.swing.JComboBox<>();
        btnGradeSearch = new javax.swing.JButton();
        plmbg1 = new javax.swing.JLabel();
        lblGradeGWA = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnChangePassword = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        lblStudentNo = new javax.swing.JLabel();
        lblStudentFN = new javax.swing.JLabel();
        lblStudentEmail = new javax.swing.JLabel();
        lblStudentGender = new javax.swing.JLabel();
        lblStudentCourseCode = new javax.swing.JLabel();
        lblStudentCPNum = new javax.swing.JLabel();
        lblStudentAddress = new javax.swing.JLabel();
        lblStudentBday = new javax.swing.JLabel();
        lblStudentStatus = new javax.swing.JLabel();
        lblStudentLN = new javax.swing.JLabel();
        studentNo = new javax.swing.JLabel();
        lastName = new javax.swing.JLabel();
        firstName = new javax.swing.JLabel();
        email = new javax.swing.JLabel();
        gender = new javax.swing.JLabel();
        courseCode = new javax.swing.JLabel();
        mobileNumber = new javax.swing.JLabel();
        address = new javax.swing.JLabel();
        bday = new javax.swing.JLabel();
        status = new javax.swing.JLabel();
        plmbg = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        MainLBL = new javax.swing.JLabel();
        PLMLogo = new javax.swing.JLabel();
        MinimizeBTN = new javax.swing.JButton();
        CloseBTN = new javax.swing.JButton();

        setBackground(new java.awt.Color(230, 68, 68));
        setPreferredSize(new java.awt.Dimension(1280, 720));

        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setDividerSize(0);

        jPanel1.setBackground(new java.awt.Color(230, 68, 68));
        jPanel1.setLayout(null);

        btnProfile.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        btnProfile.setForeground(new java.awt.Color(255, 255, 255));
        btnProfile.setText("Profile");
        btnProfile.setBorder(null);
        btnProfile.setContentAreaFilled(false);
        btnProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProfileActionPerformed(evt);
            }
        });
        jPanel1.add(btnProfile);
        btnProfile.setBounds(0, 20, 200, 70);

        btnEnrolment.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        btnEnrolment.setForeground(new java.awt.Color(255, 255, 255));
        btnEnrolment.setText("Enrollment");
        btnEnrolment.setBorder(null);
        btnEnrolment.setContentAreaFilled(false);
        btnEnrolment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnrolmentActionPerformed(evt);
            }
        });
        jPanel1.add(btnEnrolment);
        btnEnrolment.setBounds(0, 90, 200, 70);

        btnSched.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        btnSched.setForeground(new java.awt.Color(255, 255, 255));
        btnSched.setText("Schedule");
        btnSched.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        btnSched.setContentAreaFilled(false);
        btnSched.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSchedActionPerformed(evt);
            }
        });
        jPanel1.add(btnSched);
        btnSched.setBounds(0, 160, 200, 70);

        btnGrades.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        btnGrades.setForeground(new java.awt.Color(255, 255, 255));
        btnGrades.setText("Grades");
        btnGrades.setBorder(null);
        btnGrades.setContentAreaFilled(false);
        btnGrades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGradesActionPerformed(evt);
            }
        });
        jPanel1.add(btnGrades);
        btnGrades.setBounds(0, 230, 200, 70);

        btnLogout.setBackground(new java.awt.Color(230, 68, 68));
        btnLogout.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout.setText("Log-out");
        btnLogout.setBorder(null);
        btnLogout.setContentAreaFilled(false);
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });
        jPanel1.add(btnLogout);
        btnLogout.setBounds(-1, 570, 200, 28);

        btnBackStudentMenu.setBackground(new java.awt.Color(230, 68, 68));
        btnBackStudentMenu.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        btnBackStudentMenu.setForeground(new java.awt.Color(255, 255, 255));
        btnBackStudentMenu.setText("              Back");
        btnBackStudentMenu.setBorder(null);
        btnBackStudentMenu.setContentAreaFilled(false);
        btnBackStudentMenu.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnBackStudentMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackStudentMenuActionPerformed(evt);
            }
        });
        jPanel1.add(btnBackStudentMenu);
        btnBackStudentMenu.setBounds(-5, 510, 210, 28);

        pficon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/smallLogout.png"))); // NOI18N
        jPanel1.add(pficon);
        pficon.setBounds(20, 560, 40, 50);

        pficon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/smallpfside.png"))); // NOI18N
        jPanel1.add(pficon1);
        pficon1.setBounds(20, 30, 40, 50);

        pficon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/smallenrollside.png"))); // NOI18N
        jPanel1.add(pficon2);
        pficon2.setBounds(20, 90, 40, 70);

        pficon3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/smallschedside.png"))); // NOI18N
        jPanel1.add(pficon3);
        pficon3.setBounds(20, 160, 40, 70);

        pficon4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/smallgrades.png"))); // NOI18N
        jPanel1.add(pficon4);
        pficon4.setBounds(20, 230, 40, 70);

        pficon5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/backsmall.png"))); // NOI18N
        jPanel1.add(pficon5);
        pficon5.setBounds(20, 500, 40, 50);

        select4.setBackground(new java.awt.Color(179, 52, 52));

        javax.swing.GroupLayout select4Layout = new javax.swing.GroupLayout(select4);
        select4.setLayout(select4Layout);
        select4Layout.setHorizontalGroup(
            select4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        select4Layout.setVerticalGroup(
            select4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
        );

        jPanel1.add(select4);
        select4.setBounds(0, 230, 200, 70);

        select3.setBackground(new java.awt.Color(179, 52, 52));

        javax.swing.GroupLayout select3Layout = new javax.swing.GroupLayout(select3);
        select3.setLayout(select3Layout);
        select3Layout.setHorizontalGroup(
            select3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        select3Layout.setVerticalGroup(
            select3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
        );

        jPanel1.add(select3);
        select3.setBounds(0, 160, 200, 70);

        select2.setBackground(new java.awt.Color(179, 52, 52));

        javax.swing.GroupLayout select2Layout = new javax.swing.GroupLayout(select2);
        select2.setLayout(select2Layout);
        select2Layout.setHorizontalGroup(
            select2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        select2Layout.setVerticalGroup(
            select2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
        );

        jPanel1.add(select2);
        select2.setBounds(0, 90, 200, 70);

        select1.setBackground(new java.awt.Color(179, 52, 52));

        javax.swing.GroupLayout select1Layout = new javax.swing.GroupLayout(select1);
        select1.setLayout(select1Layout);
        select1Layout.setHorizontalGroup(
            select1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        select1Layout.setVerticalGroup(
            select1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
        );

        jPanel1.add(select1);
        select1.setBounds(0, 20, 200, 70);

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
        jLabel5.setBounds(20, 70, 48, 16);

        jLabel6.setText("School Year :");
        jPanel4.add(jLabel6);
        jLabel6.setBounds(10, 30, 68, 16);

        jPanel4.add(cmbEnrolSy);
        cmbEnrolSy.setBounds(80, 30, 72, 22);

        jLabel2.setText("Semester :");
        jPanel4.add(jLabel2);
        jLabel2.setBounds(180, 30, 54, 16);

        cmbEnrolSem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbEnrolSemActionPerformed(evt);
            }
        });
        jPanel4.add(cmbEnrolSem);
        cmbEnrolSem.setBounds(240, 30, 72, 22);

        btnEnrolConfirm.setText("Confirm Enrolment");
        btnEnrolConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnrolConfirmActionPerformed(evt);
            }
        });
        jPanel4.add(btnEnrolConfirm);
        btnEnrolConfirm.setBounds(710, 510, 132, 23);

        btnEnrolSchedView.setText("View Schedule");
        btnEnrolSchedView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnrolSchedViewActionPerformed(evt);
            }
        });
        jPanel4.add(btnEnrolSchedView);
        btnEnrolSchedView.setBounds(350, 30, 106, 23);

        lblGuideText.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblGuideText.setForeground(new java.awt.Color(102, 102, 102));
        lblGuideText.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/guideicon.png"))); // NOI18N
        lblGuideText.setText("Select your desired school year and semester for the schedule");
        jPanel4.add(lblGuideText);
        lblGuideText.setBounds(0, 0, 380, 30);

        plmbg3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/plm.png"))); // NOI18N
        jPanel4.add(plmbg3);
        plmbg3.setBounds(0, 10, 1050, 700);

        tabs.addTab("", jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(null);

        jLabel3.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel3.setText("SCHEDULE");
        jPanel5.add(jLabel3);
        jLabel3.setBounds(50, 30, 140, 37);

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
        jScrollPane3.setBounds(10, 70, 820, 523);

        plmbg2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/plm.png"))); // NOI18N
        jPanel5.add(plmbg2);
        plmbg2.setBounds(0, 10, 1050, 700);

        tabs.addTab("", jPanel5);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 102, 102));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/guideicon.png"))); // NOI18N
        jLabel7.setText("Select the school year and semester for your grade");

        tblGradesTable.setBackground(new java.awt.Color(102, 102, 102));
        tblGradesTable.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
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

        jLabel1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel1.setText("School year :");

        cmbGradeSY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbGradeSYActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel4.setText("Semester");

        cmbGradeSem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbGradeSemActionPerformed(evt);
            }
        });

        btnGradeSearch.setText("Search");
        btnGradeSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGradeSearchActionPerformed(evt);
            }
        });

        plmbg1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/plm.png"))); // NOI18N

        lblGradeGWA.setText("GWA: ");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbGradeSY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbGradeSem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnGradeSearch))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 857, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblGradeGWA))
                .addGap(187, 187, 187))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(plmbg1, javax.swing.GroupLayout.PREFERRED_SIZE, 1050, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbGradeSY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(cmbGradeSem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGradeSearch))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblGradeGWA)
                .addContainerGap(69, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(plmbg1, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        tabs.addTab("", jPanel6);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(null);

        btnChangePassword.setText("Change password");
        btnChangePassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangePasswordActionPerformed(evt);
            }
        });
        jPanel3.add(btnChangePassword);
        btnChangePassword.setBounds(57, 311, 210, 23);

        lblStudentNo.setText("STUDENT NUMBER:");

        lblStudentFN.setText("FIRST NAME:");

        lblStudentEmail.setText("EMAIL:");

        lblStudentGender.setText("GENDER:");

        lblStudentCourseCode.setText("COURSE CODE:");

        lblStudentCPNum.setText("MOBILE NUMBER:");

        lblStudentAddress.setText("ADDRESS:");

        lblStudentBday.setText("BIRTHDAY:");

        lblStudentStatus.setText("STATUS:");

        lblStudentLN.setText("LAST NAME:");

        studentNo.setForeground(new java.awt.Color(255, 102, 102));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                            .addGap(56, 56, 56)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(lblStudentStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblStudentBday, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(4, 4, 4)
                                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(bday, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(status, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                    .addComponent(lblStudentCPNum)
                                    .addGap(18, 18, 18)
                                    .addComponent(mobileNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                    .addComponent(lblStudentAddress)
                                    .addGap(18, 18, 18)
                                    .addComponent(address, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(40, 40, 40))
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addGap(83, 83, 83)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                    .addComponent(lblStudentFN, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(firstName, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                    .addComponent(lblStudentLN, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(studentNo, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lastName, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addGap(47, 47, 47)
                            .addComponent(lblStudentNo)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(72, 72, 72)
                                .addComponent(lblStudentCourseCode, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(courseCode, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblStudentEmail)
                                    .addComponent(lblStudentGender))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(gender, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(36, 36, 36)))
                .addContainerGap(372, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblStudentNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(studentNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblStudentLN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lastName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(firstName, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStudentFN))
                .addGap(29, 29, 29)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblStudentEmail)
                    .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblStudentGender, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(gender, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(courseCode, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStudentCourseCode))
                .addGap(7, 7, 7)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblStudentCPNum)
                    .addComponent(mobileNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(address, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStudentAddress))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblStudentBday)
                    .addComponent(bday, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(status, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStudentStatus))
                .addContainerGap(87, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel8);
        jPanel8.setBounds(30, 10, 820, 340);

        plmbg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/plm.png"))); // NOI18N
        jPanel3.add(plmbg);
        plmbg.setBounds(0, 10, 1050, 700);

        jLabel8.setText("jLabel8");
        jPanel3.add(jLabel8);
        jLabel8.setBounds(210, 20, 260, 16);

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 1293, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1279, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 710, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        // TODO add your handling code here:
        
        int response = JOptionPane.showConfirmDialog(this, "Do you really want to log-out?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (response == 0){
            logAction("Logged out");
            mf.setUserID("");
            mf.switchCard("LoginCard");
            resetGradesTable();
            resetSchedTable();
        }
        else{
            JOptionPane.showMessageDialog(this, "Cancelled");
        }
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnEnrolmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnrolmentActionPerformed
        // TODO add your handling code here:
        toggleSelected(1);
        loadEnrolmentTab();
        tabs.setSelectedIndex(0);
    }//GEN-LAST:event_btnEnrolmentActionPerformed

    private void btnSchedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSchedActionPerformed
        // TODO add your handling code here:
        toggleSelected(2);
        tabs.setSelectedIndex(1);
        studentScheduleTab();
    }//GEN-LAST:event_btnSchedActionPerformed

    private void btnGradesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGradesActionPerformed
        // TODO add your handling code here:
        toggleSelected(3);
        tabs.setSelectedIndex(2);
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
                    JOptionPane.showMessageDialog(this, "You're already enrolled to, or have finished this semester!", "Enrollment error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            if (intCurrYear == 1) {
                if (cmbEnrolSem.getSelectedItem().toString().equals("1")) {
                    rs = con.prepareStatement("SELECT * FROM finals.BLOCK_NO WHERE block_no LIKE '" + course + Integer.toString(intCurrYear) + "%'").executeQuery();
                    if (rs.next()) {
                        while (rs.next())
                            intBlockCount++;
                        block = course + Integer.toString(intCurrYear) + Integer.toString(rand.nextInt(intBlockCount)+1);
                        System.out.println(block);
                    }
                } else {
                    ps = con.prepareStatement("SELECT * FROM finals.ENROLLED_SUBJECT WHERE student_no = ? AND status != ? AND block_no LIKE ?");
                    ps.setString(1, currentUser);
                    ps.setString(2, "Finished");
                    ps.setString(3, course + "1%");
                    rs = ps.executeQuery();
                    if (!rs.next()) {
                        ps = con.prepareStatement("SELECT * FROM finals.ENROLLED_SUBJECT WHERE student_no = ? AND status = ? AND block_no LIKE ?");
                        ps.setString(1, currentUser);
                        ps.setString(2, "Finished");
                        ps.setString(3, course + "1%");
                        rs = ps.executeQuery();
                        if (rs.next())
                            block = course + "1" + rs.getString("block_no").substring(3);
                        else {
                            JOptionPane.showMessageDialog(this, "You are not eligible for this semester!", "Enrollment error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "You are not eligible for this semester!", "Enrollment error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                
            } else if (intCurrYear > 4) { 
                JOptionPane.showMessageDialog(this, "You cannot go beyond the regular school years for your course!", "Enrollment error", JOptionPane.ERROR_MESSAGE);
            }else {
                if (cmbEnrolSem.getSelectedItem().toString().equals("1")) {
                    ps = con.prepareStatement("SELECT block_no FROM finals.ENROLLED_SUBJECT WHERE student_no = ? AND status != ? AND block_no LIKE ?");
                    ps.setString(1, currentUser);
                    ps.setString(2, "Finished");
                    ps.setString(3, course + Integer.toString(intCurrYear - 1) + "%");
                    rs = ps.executeQuery();
                    if (!rs.next()) {
                        ps = con.prepareStatement("SELECT block_no FROM finals.ENROLLED_SUBJECT WHERE student_no = ? AND status = ? AND block_no LIKE ?");
                        ps.setString(1, currentUser);
                        ps.setString(2, "Finished");
                        ps.setString(3, course + Integer.toString(intCurrYear - 1) + "%");
                        rs = ps.executeQuery();
                        if (rs.next())
                            block = course + Integer.toString(intCurrYear) + rs.getString("block_no").substring(3);
                        else {
                            JOptionPane.showMessageDialog(this, "You are not eligible for this semester!", "Enrollment error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } else {
                        System.out.println("missing previous enrolments"); //TODO: error msg
                        JOptionPane.showMessageDialog(this, "You are not eligible for this semester!", "Enrollment error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else {
                    ps = con.prepareStatement("SELECT block_no FROM finals.ENROLLED_SUBJECT WHERE student_no = ? AND status != ? AND block_no LIKE ?");
                    ps.setString(1, currentUser);
                    ps.setString(2, "Finished");
                    ps.setString(3, course + Integer.toString(intCurrYear) + "%");
                    rs = ps.executeQuery();
                    if (!rs.next()) {
                        ps = con.prepareStatement("SELECT block_no FROM finals.ENROLLED_SUBJECT WHERE student_no = ? AND status = ? AND block_no LIKE ?");
                        ps.setString(1, currentUser);
                        ps.setString(2, "Finished");
                        ps.setString(3, course + Integer.toString(intCurrYear) + "%");
                        rs = ps.executeQuery();
                        if (rs.next())
                            block = course + Integer.toString(intCurrYear) + rs.getString("block_no").substring(3);
                        else {
                            JOptionPane.showMessageDialog(this, "You are not eligible for this semester!", "Enrollment error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } else {
                        System.out.println("missing previous enrolments"); //TODO: error msg
                        JOptionPane.showMessageDialog(this, "You are not eligible for this semester!", "Enrollment error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }
            ps = con.prepareStatement("SELECT * FROM finals.SUBJECT_SCHEDULE WHERE SY = ? AND SEMESTER = ? AND BLOCK_NO = ?");
            ps.setString(1, strSelectedSY);
            ps.setString(2, cmbEnrolSem.getSelectedItem().toString());
            ps.setString(3, block);
            rs = ps.executeQuery();
            if (rs.next()) {
                rs = ps.executeQuery();
                tblEnrolSchedule.setModel(TableUtil.resultSetToTableModel(rs)); //TODO: CHANGE TO VIEW
                TableUtil.styleTable(tblEnrolSchedule);
                TableUtil.resizeColumnWidth(tblEnrolSchedule);
            } else {
                JOptionPane.showMessageDialog(this, "No viable schedules found!", "Enrollment error", JOptionPane.ERROR_MESSAGE);
                return;
            }
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
                logAction("Enrolled subjects");
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

    private void btnChangePasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangePasswordActionPerformed
        // TODO add your handling code here:
        con = ConnectDB.connect();
        String newPassword = JOptionPane.showInputDialog(this, "Enter new password:", "Changing password..", JOptionPane.QUESTION_MESSAGE);
        if (newPassword == null || newPassword.isEmpty())
            JOptionPane.showMessageDialog(this, "Password cannot be empty!", "Password error", JOptionPane.ERROR_MESSAGE);
        else {
            String confirmPassword = JOptionPane.showInputDialog(this, "Confirm password:", "Changing password..", JOptionPane.QUESTION_MESSAGE);
            if (newPassword.equals(confirmPassword)) {
                try {
                    logAction("Changed password");
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

    private void btnProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProfileActionPerformed
        // TODO add your handling code here:
        toggleSelected(0);
        tabs.setSelectedIndex(3);
        profileStudentsTab();
    }//GEN-LAST:event_btnProfileActionPerformed

    private void cmbGradeSemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbGradeSemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbGradeSemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CloseBTN;
    private javax.swing.JLabel MainLBL;
    private javax.swing.JButton MinimizeBTN;
    private javax.swing.JLabel PLMLogo;
    private javax.swing.JLabel address;
    private javax.swing.JLabel bday;
    private javax.swing.JButton btnBackStudentMenu;
    private javax.swing.JButton btnChangePassword;
    private javax.swing.JButton btnEnrolConfirm;
    private javax.swing.JButton btnEnrolSchedView;
    private javax.swing.JButton btnEnrolment;
    private javax.swing.JButton btnGradeSearch;
    private javax.swing.JButton btnGrades;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnProfile;
    private javax.swing.JButton btnSched;
    private javax.swing.JComboBox<String> cmbEnrolSem;
    private javax.swing.JComboBox<String> cmbEnrolSy;
    private javax.swing.JComboBox<String> cmbGradeSY;
    private javax.swing.JComboBox<String> cmbGradeSem;
    private javax.swing.JLabel courseCode;
    private javax.swing.JLabel email;
    private javax.swing.JLabel firstName;
    private javax.swing.JLabel gender;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel lastName;
    private javax.swing.JLabel lblGradeGWA;
    private javax.swing.JLabel lblGuideText;
    private javax.swing.JLabel lblStudentAddress;
    private javax.swing.JLabel lblStudentBday;
    private javax.swing.JLabel lblStudentCPNum;
    private javax.swing.JLabel lblStudentCourseCode;
    private javax.swing.JLabel lblStudentEmail;
    private javax.swing.JLabel lblStudentFN;
    private javax.swing.JLabel lblStudentGender;
    private javax.swing.JLabel lblStudentLN;
    private javax.swing.JLabel lblStudentNo;
    private javax.swing.JLabel lblStudentStatus;
    private javax.swing.JLabel mobileNumber;
    private javax.swing.JLabel pficon;
    private javax.swing.JLabel pficon1;
    private javax.swing.JLabel pficon2;
    private javax.swing.JLabel pficon3;
    private javax.swing.JLabel pficon4;
    private javax.swing.JLabel pficon5;
    private javax.swing.JLabel plmbg;
    private javax.swing.JLabel plmbg1;
    private javax.swing.JLabel plmbg2;
    private javax.swing.JLabel plmbg3;
    private javax.swing.JPanel select1;
    private javax.swing.JPanel select2;
    private javax.swing.JPanel select3;
    private javax.swing.JPanel select4;
    private javax.swing.JLabel status;
    private javax.swing.JLabel studentNo;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblEnrolSchedule;
    private javax.swing.JTable tblGradesTable;
    private javax.swing.JTable tblSchedule;
    // End of variables declaration//GEN-END:variables
}
