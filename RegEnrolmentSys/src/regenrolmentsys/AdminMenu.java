/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package regenrolmentsys;
import java.awt.Color;
import java.awt.Point;
import java.sql.*;
import javax.swing.JOptionPane;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
/**
 *
 * @author harley
 */
public class AdminMenu extends javax.swing.JPanel {
    private MainFrame mf = null;
    private String currentUser = "";
    private Connection con = null;
    private ResultSet rs = null;
    private PreparedStatement ps = null;
    private boolean cmbStuCourseLoaded = false, cmbStuCourseLoaded2 = false, cmbCollegeLoaded = false, cmbSubjCodeLoaded = false, cmbFacultyLoaded = false;
    /**
     * Creates new form AdminMenu
     */
    public AdminMenu(MainFrame mf) {
        initComponents();
        this.mf = mf;
        this.currentUser = mf.getUserID();
        
    }
    
    public void setUserID(String userID) {
        this.currentUser = userID;
    }
    
    public javax.swing.JTabbedPane getTabs() {
        return tabs;
    }
    
    public javax.swing.JTable getStudentTable() {
        return tblHistory;
    }
    
    private void logAction(String action) {
        con = ConnectDB.connect();
        LocalTime localCurrTime = LocalTime.now();
        LocalDate localCurrDate = LocalDate.now();
        Time currTime = Time.valueOf(localCurrTime);
        Date currDate = Date.valueOf(localCurrDate);
        
        try {
            ps = con.prepareStatement("INSERT INTO finals.HISTORY VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, currentUser);
            ps.setString(2, action);
            ps.setString(3, "Admin");
            ps.setDate(4, currDate);
            ps.setTime(5, currTime);
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private void resetHistoryTable() {
        tblHistory.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {},
                new String [] {"History log is empty"}
                ) {
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return false;
                    }
                });
    }
    
    public void loadStudentsTab() {
        con = ConnectDB.connect();
        try {
            loadStudentTable();
            rs = con.prepareStatement("SELECT * FROM finals.SY").executeQuery();
            while (rs.next())
                cmbStudentNoYear.addItem(rs.getString("sy").substring(0, 4));
            
            rs = con.prepareStatement("SELECT course_code FROM finals.COURSE").executeQuery();
            while (rs.next()) {
                cmbStuCourse.addItem(rs.getString("course_code"));
            }
            
            rs = con.prepareStatement("SELECT description FROM finals.COURSE WHERE course_code = '" + cmbStuCourse.getSelectedItem().toString() + "'").executeQuery();
            if (rs.next())
                txtStuCourseDesc.setText(rs.getString("description"));
            cmbStuCourseLoaded = true;
                   
        } catch (Exception e) {
            System.out.println(e); //TODO: ADD ERROR MSGS
        }
        
    }
    
    private void loadStudentTable() {
        con = ConnectDB.connect();
        try {
            rs = con.prepareStatement("SELECT * FROM finals.STUDENT").executeQuery(); // TODO: REPLACE WITH VIEW
            if (rs.next()) {
                rs = con.prepareStatement("SELECT * FROM finals.STUDENT").executeQuery();
                tblStudents.setModel(TableUtil.resultSetToTableModel(rs));
                TableUtil.resizeColumnWidth(tblStudents);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private void loadSYSemTab() {
        rbSY.setSelected(false);
        rbSem.setSelected(false);
        txtSY.setText("");
        txtSem.setText("");
        txtSY.setEnabled(false);
        txtSem.setEnabled(false);
        
        resetSYSemTable();
    }
    
    private void loadSYTable() {
        con = ConnectDB.connect();
        try {
            rs = con.prepareStatement("SELECT * FROM finals.SY").executeQuery();
            while(rs.next()) {
                rs = con.prepareStatement("SELECT * FROM finals.SY").executeQuery();
                tblSYSem.setModel(TableUtil.resultSetToTableModel(rs));
                TableUtil.resizeColumnWidth(tblSYSem);
            }
        } catch (Exception e) {
            System.out.println();
        }
    }
    
    private void loadSemTable() {
        con = ConnectDB.connect();
        try {
            rs = con.prepareStatement("SELECT * FROM finals.SEMESTER").executeQuery();
            while(rs.next()) {
                rs = con.prepareStatement("SELECT * FROM finals.SEMESTER").executeQuery();
                tblSYSem.setModel(TableUtil.resultSetToTableModel(rs));
            }
        } catch (Exception e) {
            System.out.println();
        }
    }
    
    private void resetSYSemTable() {
        tblSYSem.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {},
                new String [] {"Select a record to modify."}
                ) {
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return false;
                    }
                });
    }
    
    private void loadSubjectsTab() {
        txtSubjCode.setText("");
        txtSubjDesc.setText("");
        txtUnits.setText("");
        txtCurriculum.setText("");
        lblSubjDesc.setText("----------");
        checkSubjStatus.setSelected(false);
        
        try {
            con = ConnectDB.connect();
            
            rs = con.prepareStatement("SELECT * FROM finals.SEMESTER").executeQuery();
            while (rs.next()) {
                cmbSem2.addItem(rs.getString("Semester"));
            }
            
            rs = con.prepareStatement("SELECT college_code FROM finals.COLLEGE").executeQuery();
            while (rs.next()) {
                cmbCollegeCode2.addItem(rs.getString("college_code"));
            }
            
            rs = con.prepareStatement("SELECT description FROM finals.COLLEGE WHERE college_code = '" + cmbCollegeCode2.getSelectedItem().toString() + "'").executeQuery();
            if (rs.next())
                lblCollegeDesc.setText(rs.getString("description"));
            cmbCollegeLoaded = true;
            
            rs = con.prepareStatement("SELECT course_code FROM finals.COURSE").executeQuery();
            while (rs.next()) {
                cmbCourseCode.addItem(rs.getString("course_code"));
            }
            
            rs = con.prepareStatement("SELECT description FROM finals.COURSE WHERE course_code = '" + cmbCourseCode.getSelectedItem().toString() + "'").executeQuery();
            if (rs.next()) {
                lblCourseDesc.setText(rs.getString("description"));
                cmbStuCourseLoaded2 = true;
            }
            loadSubjectTable();
            
            rs = con.prepareStatement("SELECT * FROM finals.ENCODING").executeQuery();
            if (rs.next()) {
                if (rs.getInt("state") == 0) {
                    chkGradesEncoding.setSelected(false);
                } else {
                    chkGradesEncoding.setSelected(true);
                }
            }
            
        } catch (Exception e) {
            System.out.println(e);
        } 
    }
    
    private void loadSubjectTable() {
        con = ConnectDB.connect();
        try {
            rs = con.prepareStatement("SELECT * FROM finals.SUBJECT").executeQuery();
            if (rs.next()) {
                rs = con.prepareStatement("SELECT * FROM finals.SUBJECT").executeQuery();
                tblSubjects.setModel(TableUtil.resultSetToTableModel(rs));
                TableUtil.resizeColumnWidth(tblSubjects);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    

    private void resetSubjectTable() {
        tblSubjects.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {},
                new String [] {"No subject records found."}
                ) {
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return false;
                    }
                });
    }
    
    private void loadSchedulesTab() {
        con = ConnectDB.connect();
        try {
            
            rs = con.prepareStatement("SELECT * FROM finals.SY").executeQuery();
            while (rs.next()) {
                cmbSchedSY.addItem(rs.getString("SY"));
            }
            
            rs = con.prepareStatement("SELECT * FROM finals.SEMESTER").executeQuery();
            while (rs.next()) {
                cmbSchedSem.addItem(rs.getString("Semester"));
            }
            
            rs = con.prepareStatement("SELECT * FROM finals.BLOCK_NO").executeQuery();
            while (rs.next()) {
                cmbBlockNo.addItem(rs.getString("block_no"));
            }
            
            rs = con.prepareStatement("SELECT subject_code FROM finals.SUBJECT").executeQuery();
            while (rs.next()) {
                cmbSubjCode.addItem(rs.getString("subject_code"));
            }
            
            rs = con.prepareStatement("SELECT * FROM finals.vwSubjectInfo_Sched WHERE subject_code = '" + cmbSubjCode.getSelectedItem().toString() + "'").executeQuery();
            if (rs.next()) {
                lblSubjDesc.setText(rs.getString("subj_desc"));
                lblCollegeCodeSched.setText(rs.getString("college_code"));
                lblCollegeDescSched.setText(rs.getString("college_desc"));
                lblCourseCodeSched.setText(rs.getString("course_code"));
                lblCourseDescSched.setText(rs.getString("course_desc"));
                cmbSubjCodeLoaded = true;
            }
            
            rs = con.prepareStatement("SELECT * FROM finals.EMPLOYEE").executeQuery();
            while (rs.next()) {
                cmbFacultyID.addItem(rs.getString("employee_id"));
            }
            
            rs = con.prepareStatement("SELECT * FROM finals.EMPLOYEE WHERE employee_id = '" + cmbFacultyID.getSelectedItem().toString() + "'").executeQuery();
            if (rs.next()) {
                lblFacultyName.setText(rs.getString("first_name")+" "+rs.getString("last_name"));
                cmbFacultyLoaded = true;
            }
            loadScheduleTable();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private void loadScheduleTable(){
        con = ConnectDB.connect();
        try {
            rs = con.prepareStatement("SELECT * FROM finals.SUBJECT_SCHEDULE").executeQuery();
            while (rs.next()) {
                tblSched.setModel(TableUtil.resultSetToTableModel(rs));
                TableUtil.resizeColumnWidth(tblSched);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private void loadEmployeesTab() {
        txtEmpID.setText("");
        txtEmpFirstname.setText("");
        txtEmpLastname.setText("");
        txtEmpCellNo.setText("");
        txtEmpAddress.setText("");
        cmbEmpGender.setSelectedIndex(0);
        checkEmpStatus.setSelected(false);
        
        loadEmployeeTable();
    }
    
    private void loadEmployeeTable(){
        con = ConnectDB.connect();
        try {
            rs = con.prepareStatement("SELECT * FROM finals.EMPLOYEE").executeQuery();
            if (rs.next()) {
                rs = con.prepareStatement("SELECT * FROM finals.EMPLOYEE").executeQuery();
                tblEmployees.setModel(TableUtil.resultSetToTableModel(rs));
                TableUtil.resizeColumnWidth(tblEmployees);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void toggleSelected(int index) {
        javax.swing.JPanel covers[] = {select1, select2, select3, select4, select5, select6}; //just fill with the cover JPanels for other menus
        for (int i=0; i<covers.length; ++i) {
            if (i == index) 
                covers[i].setVisible(true);
            else
                covers[i].setVisible(false);
        }
    }
    
    public void loadHistoryTable() {
        con = ConnectDB.connect();
        try {
            rs = con.prepareStatement("SELECT * FROM finals.HISTORY").executeQuery();
            if (rs.next()) {
                rs = con.prepareStatement("SELECT * FROM finals.HISTORY").executeQuery();
                tblHistory.setModel(TableUtil.resultSetToTableModel(rs));
                TableUtil.styleTable(tblHistory);
                TableUtil.resizeColumnWidth(tblHistory);
            } else {
                resetHistoryTable();
            }
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
        pficon1 = new javax.swing.JLabel();
        pficon2 = new javax.swing.JLabel();
        pficon3 = new javax.swing.JLabel();
        pficon4 = new javax.swing.JLabel();
        pficon5 = new javax.swing.JLabel();
        pficon6 = new javax.swing.JLabel();
        pficon = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        btnStudents = new javax.swing.JButton();
        btnSYSem = new javax.swing.JButton();
        btnSubjects = new javax.swing.JButton();
        btnSchedules = new javax.swing.JButton();
        btnEmployees = new javax.swing.JButton();
        btnHistory = new javax.swing.JButton();
        select1 = new javax.swing.JPanel();
        select2 = new javax.swing.JPanel();
        select3 = new javax.swing.JPanel();
        select4 = new javax.swing.JPanel();
        select5 = new javax.swing.JPanel();
        select6 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        tabs = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblStudents = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cmbStudentNoYear = new javax.swing.JComboBox<>();
        txtStudentNo = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        btnStudSearch = new javax.swing.JButton();
        txtStuFirstName = new javax.swing.JTextField();
        txtStuLastName = new javax.swing.JTextField();
        cmbStuGender = new javax.swing.JComboBox<>();
        cmbStuCourse = new javax.swing.JComboBox<>();
        txtStuAddress = new javax.swing.JTextField();
        chkStuActive = new javax.swing.JCheckBox();
        dateStuBday = new com.github.lgooddatepicker.components.DatePicker();
        txtStuCourseDesc = new javax.swing.JLabel();
        btnAddStuRec = new javax.swing.JButton();
        btnUpdateStuRec = new javax.swing.JButton();
        btnDeleteStuRec = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        txtStuMidInitial = new javax.swing.JTextField();
        lblStuEmail = new javax.swing.JLabel();
        txtStuPhone = new javax.swing.JTextField();
        btnStudSearchName = new javax.swing.JButton();
        plmbg6 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        rbSY = new javax.swing.JRadioButton();
        rbSem = new javax.swing.JRadioButton();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtSY = new javax.swing.JTextField();
        txtSem = new javax.swing.JTextField();
        btnAddSYSem = new javax.swing.JButton();
        btnDeleteSYSem = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSYSem = new javax.swing.JTable();
        plmbg7 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtSubjCode = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtSubjDesc = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtUnits = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtCurriculum = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        cmbCollegeCode2 = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        lblCollegeDesc = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        checkSubjStatus = new javax.swing.JCheckBox();
        btnSearchSubj = new javax.swing.JButton();
        btnAddSubj = new javax.swing.JButton();
        btnEditSubj = new javax.swing.JButton();
        btnDeleteSubj = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblSubjects = new javax.swing.JTable();
        lblCollegeYear = new javax.swing.JLabel();
        cmbCollegeYr = new javax.swing.JComboBox<>();
        lblSem = new javax.swing.JLabel();
        cmbSem2 = new javax.swing.JComboBox<>();
        jLabel45 = new javax.swing.JLabel();
        cmbCourseCode = new javax.swing.JComboBox<>();
        jLabel51 = new javax.swing.JLabel();
        lblCourseDesc = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        chkGradesEncoding = new javax.swing.JCheckBox();
        plmbg8 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        cmbSchedSY = new javax.swing.JComboBox<>();
        jLabel27 = new javax.swing.JLabel();
        cmbSchedSem = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        lblCollegeDescSched = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        cmbBlockNo = new javax.swing.JComboBox<>();
        jLabel31 = new javax.swing.JLabel();
        cmbSubjCode = new javax.swing.JComboBox<>();
        jLabel32 = new javax.swing.JLabel();
        lblSubjDesc = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        cmbDay = new javax.swing.JComboBox<>();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        txtRoom = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        rbOnline = new javax.swing.JRadioButton();
        rbF2F = new javax.swing.JRadioButton();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        cmbFacultyID = new javax.swing.JComboBox<>();
        jLabel39 = new javax.swing.JLabel();
        lblFacultyName = new javax.swing.JLabel();
        btnAddSched = new javax.swing.JButton();
        btnEditSched = new javax.swing.JButton();
        btnDeleteSched = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblSched = new javax.swing.JTable();
        lblCollegeCodeSched = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        lblCourseCodeSched = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        lblCourseDescSched = new javax.swing.JLabel();
        cmbSequenceNo = new javax.swing.JComboBox<>();
        txtTime = new javax.swing.JTextField();
        plmbg9 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        txtEmpID = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        txtEmpFirstname = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        txtEmpLastname = new javax.swing.JTextField();
        btnSearchEmpID = new javax.swing.JButton();
        btnSearchEmpName = new javax.swing.JButton();
        jLabel44 = new javax.swing.JLabel();
        lblEmpEmail = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        cmbEmpGender = new javax.swing.JComboBox<>();
        jLabel47 = new javax.swing.JLabel();
        txtEmpCellNo = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        dateEmpBday = new com.github.lgooddatepicker.components.DatePicker();
        jLabel49 = new javax.swing.JLabel();
        txtEmpAddress = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        checkEmpStatus = new javax.swing.JCheckBox();
        btnAddEmp = new javax.swing.JButton();
        btnUpdateEmp = new javax.swing.JButton();
        btnDeleteEmp = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblEmployees = new javax.swing.JTable();
        plmbg4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHistory = new javax.swing.JTable();
        plmbg10 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        smallpf = new javax.swing.JLabel();
        PLMLogo = new javax.swing.JLabel();
        MinimizeBTN = new javax.swing.JButton();
        CloseBTN = new javax.swing.JButton();
        MainLBL1 = new javax.swing.JLabel();
        NameTopBar = new javax.swing.JLabel();

        setBackground(new java.awt.Color(230, 68, 68));
        setPreferredSize(new java.awt.Dimension(1280, 720));

        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setDividerSize(0);
        jSplitPane1.setMaximumSize(new java.awt.Dimension(1280, 720));

        jPanel1.setBackground(new java.awt.Color(230, 68, 68));
        jPanel1.setLayout(null);

        pficon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/smallhistory.png"))); // NOI18N
        jPanel1.add(pficon1);
        pficon1.setBounds(20, 370, 40, 70);

        pficon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/smallpfside.png"))); // NOI18N
        jPanel1.add(pficon2);
        pficon2.setBounds(20, 30, 40, 50);

        pficon3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/smallsy.png"))); // NOI18N
        jPanel1.add(pficon3);
        pficon3.setBounds(20, 90, 40, 60);

        pficon4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/smallsubjects.png"))); // NOI18N
        jPanel1.add(pficon4);
        pficon4.setBounds(20, 170, 40, 50);

        pficon5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/smallschedside.png"))); // NOI18N
        jPanel1.add(pficon5);
        pficon5.setBounds(20, 240, 40, 50);

        pficon6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/smallemployee.png"))); // NOI18N
        jPanel1.add(pficon6);
        pficon6.setBounds(20, 310, 40, 60);

        pficon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/smallLogout.png"))); // NOI18N
        jPanel1.add(pficon);
        pficon.setBounds(30, 560, 40, 50);

        btnLogout.setText("Log-out");
        btnLogout.setBorder(null);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });
        jPanel1.add(btnLogout);
        btnLogout.setBounds(0, 570, 230, 25);

        btnStudents.setText("Students");
        btnStudents.setBorder(null);
        btnStudents.setContentAreaFilled(false);
        btnStudents.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        btnStudents.setForeground(new java.awt.Color(255, 255, 255));
        btnStudents.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStudentsActionPerformed(evt);
            }
        });
        jPanel1.add(btnStudents);
        btnStudents.setBounds(-4, 19, 200, 70);

        btnSYSem.setText("      SY and Semester");
        btnSYSem.setBorder(null);
        btnSYSem.setContentAreaFilled(false);
        btnSYSem.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        btnSYSem.setForeground(new java.awt.Color(255, 255, 255));
        btnSYSem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSYSemActionPerformed(evt);
            }
        });
        jPanel1.add(btnSYSem);
        btnSYSem.setBounds(-1, 87, 200, 70);

        btnSubjects.setText("Subjects");
        btnSubjects.setBorder(null);
        btnSubjects.setContentAreaFilled(false);
        btnSubjects.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        btnSubjects.setForeground(new java.awt.Color(255, 255, 255));
        btnSubjects.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubjectsActionPerformed(evt);
            }
        });
        jPanel1.add(btnSubjects);
        btnSubjects.setBounds(-5, 160, 210, 70);

        btnSchedules.setText("Schedules");
        btnSchedules.setBorder(null);
        btnSchedules.setContentAreaFilled(false);
        btnSchedules.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        btnSchedules.setForeground(new java.awt.Color(255, 255, 255));
        btnSchedules.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSchedulesActionPerformed(evt);
            }
        });
        jPanel1.add(btnSchedules);
        btnSchedules.setBounds(-5, 233, 210, 70);

        btnEmployees.setText("Employees");
        btnEmployees.setBorder(null);
        btnEmployees.setContentAreaFilled(false);
        btnEmployees.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        btnEmployees.setForeground(new java.awt.Color(255, 255, 255));
        btnEmployees.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmployeesActionPerformed(evt);
            }
        });
        jPanel1.add(btnEmployees);
        btnEmployees.setBounds(-5, 306, 210, 70);

        btnHistory.setText("History");
        btnHistory.setBorder(null);
        btnHistory.setContentAreaFilled(false);
        btnHistory.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        btnHistory.setForeground(new java.awt.Color(255, 255, 255));
        btnHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistoryActionPerformed(evt);
            }
        });
        jPanel1.add(btnHistory);
        btnHistory.setBounds(-5, 369, 210, 70);

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

        select5.setBackground(new java.awt.Color(179, 52, 52));

        javax.swing.GroupLayout select5Layout = new javax.swing.GroupLayout(select5);
        select5.setLayout(select5Layout);
        select5Layout.setHorizontalGroup(
            select5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        select5Layout.setVerticalGroup(
            select5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
        );

        jPanel1.add(select5);
        select5.setBounds(0, 300, 200, 70);

        select6.setBackground(new java.awt.Color(179, 52, 52));

        javax.swing.GroupLayout select6Layout = new javax.swing.GroupLayout(select6);
        select6.setLayout(select6Layout);
        select6Layout.setHorizontalGroup(
            select6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        select6Layout.setVerticalGroup(
            select6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
        );

        jPanel1.add(select6);
        select6.setBounds(0, 370, 200, 70);

        jSplitPane1.setLeftComponent(jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(1009, 720));

        tabs.setTabPlacement(javax.swing.JTabbedPane.RIGHT);
        tabs.setPreferredSize(new java.awt.Dimension(1003, 720));
        tabs.setToolTipText("");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        tblStudents.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No existing student records found"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblStudents.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblStudentsMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblStudents);

        jLabel1.setText("Student No. :");

        jLabel2.setText("First Name :");

        jLabel3.setText("Last Name");

        jLabel4.setText("Email :");

        jLabel5.setText("Gender :");

        jLabel6.setText("Course :");

        jLabel7.setText("Phone No. :");

        jLabel8.setText("Address :");

        jLabel9.setText("Birthday :");

        jLabel10.setText("Status :");

        cmbStudentNoYear.setToolTipText("");
        cmbStudentNoYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbStudentNoYearActionPerformed(evt);
            }
        });

        jLabel11.setText("-");

        btnStudSearch.setText("Search ID");
        btnStudSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStudSearchActionPerformed(evt);
            }
        });

        txtStuFirstName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStuFirstNameActionPerformed(evt);
            }
        });

        cmbStuGender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "M", "F" }));

        cmbStuCourse.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbStuCourseItemStateChanged(evt);
            }
        });
        cmbStuCourse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbStuCourseActionPerformed(evt);
            }
        });

        chkStuActive.setText("Active");
        chkStuActive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkStuActiveActionPerformed(evt);
            }
        });

        txtStuCourseDesc.setText("err");

        btnAddStuRec.setText("Add");
        btnAddStuRec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddStuRecActionPerformed(evt);
            }
        });

        btnUpdateStuRec.setText("Update");
        btnUpdateStuRec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateStuRecActionPerformed(evt);
            }
        });

        btnDeleteStuRec.setText("Delete");
        btnDeleteStuRec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteStuRecActionPerformed(evt);
            }
        });

        jLabel12.setText("Middle Inital : ");

        txtStuMidInitial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStuMidInitialActionPerformed(evt);
            }
        });

        lblStuEmail.setText("-");

        btnStudSearchName.setText("Search Name");
        btnStudSearchName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStudSearchNameActionPerformed(evt);
            }
        });

        plmbg6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/plmopaque.png"))); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 811, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtStuMidInitial, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtStuFirstName))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbStudentNoYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtStudentNo, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbStuGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtStuLastName)
                                    .addComponent(lblStuEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnStudSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnStudSearchName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(57, 57, 57)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(dateStuBday, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chkStuActive)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(cmbStuCourse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtStuCourseDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtStuAddress, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                                        .addComponent(txtStuPhone, javax.swing.GroupLayout.Alignment.LEADING))))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnAddStuRec)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnUpdateStuRec)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDeleteStuRec)))
                .addContainerGap(378, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(plmbg6, javax.swing.GroupLayout.PREFERRED_SIZE, 1270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel6)
                    .addComponent(cmbStudentNoYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtStudentNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(btnStudSearch)
                    .addComponent(cmbStuCourse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtStuCourseDesc))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel7)
                    .addComponent(txtStuFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtStuPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnStudSearchName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel8)
                    .addComponent(txtStuLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtStuAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(dateStuBday, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(chkStuActive)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(txtStuMidInitial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(lblStuEmail))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(cmbStuGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(60, 60, 60)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddStuRec)
                    .addComponent(btnUpdateStuRec)
                    .addComponent(btnDeleteStuRec))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(plmbg6, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        tabs.addTab("", jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setText("SY AND SEMESTER RECORDS");

        jLabel14.setText("Select a record to modify:");

        rbSY.setText("SY");
        rbSY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbSYActionPerformed(evt);
            }
        });

        rbSem.setText("SEMESTER");
        rbSem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbSemActionPerformed(evt);
            }
        });

        jLabel15.setText("SY:");

        jLabel16.setText("Semester:");

        btnAddSYSem.setText("ADD");
        btnAddSYSem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddSYSemActionPerformed(evt);
            }
        });

        btnDeleteSYSem.setText("DELETE");
        btnDeleteSYSem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteSYSemActionPerformed(evt);
            }
        });

        tblSYSem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Choose a record to modify"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSYSem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSYSemMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblSYSem);

        plmbg7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/plmopaque.png"))); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(205, 205, 205)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtSY, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSem, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(271, 271, 271))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(rbSY)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(rbSem)
                        .addGap(312, 312, 312))))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(392, 392, 392)
                        .addComponent(btnAddSYSem)
                        .addGap(37, 37, 37)
                        .addComponent(btnDeleteSYSem))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(405, 405, 405)
                        .addComponent(jLabel13))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(257, 257, 257)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(561, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(414, 414, 414)
                .addComponent(jLabel14)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(plmbg7, javax.swing.GroupLayout.PREFERRED_SIZE, 1270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel13)
                .addGap(50, 50, 50)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbSY)
                    .addComponent(rbSem))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16)
                    .addComponent(txtSY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddSYSem)
                    .addComponent(btnDeleteSYSem))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(76, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(plmbg7, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        tabs.addTab("", jPanel5);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel17.setText("SUBJECT RECORDS");

        jLabel18.setText("Subject Code:");

        jLabel19.setText("Description:");

        jLabel20.setText("Units:");

        txtUnits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUnitsActionPerformed(evt);
            }
        });

        jLabel21.setText("Curriculum:");

        jLabel22.setText("College Code:");

        cmbCollegeCode2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCollegeCode2ActionPerformed(evt);
            }
        });

        jLabel23.setText("-");

        lblCollegeDesc.setText("College Description");

        jLabel25.setText("Status:");

        checkSubjStatus.setText("Active");
        checkSubjStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkSubjStatusActionPerformed(evt);
            }
        });

        btnSearchSubj.setText("SEARCH");
        btnSearchSubj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchSubjActionPerformed(evt);
            }
        });

        btnAddSubj.setText("Add");
        btnAddSubj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddSubjActionPerformed(evt);
            }
        });

        btnEditSubj.setText("Update");
        btnEditSubj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditSubjActionPerformed(evt);
            }
        });

        btnDeleteSubj.setText("Delete");
        btnDeleteSubj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteSubjActionPerformed(evt);
            }
        });

        tblSubjects.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "No subject records found"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblSubjects.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSubjectsMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tblSubjectsMouseEntered(evt);
            }
        });
        jScrollPane4.setViewportView(tblSubjects);

        lblCollegeYear.setText("College Year:");

        cmbCollegeYr.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4" }));
        cmbCollegeYr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCollegeYrActionPerformed(evt);
            }
        });

        lblSem.setText("Semester:");

        cmbSem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSem2ActionPerformed(evt);
            }
        });

        jLabel45.setText("Course Code:");

        cmbCourseCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCourseCodeActionPerformed(evt);
            }
        });

        jLabel51.setText("-");

        lblCourseDesc.setText("Course Description");

        jLabel52.setText("Encoding of Grades:");

        chkGradesEncoding.setText("Active");

        plmbg8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/plmopaque.png"))); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(134, 134, 134)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 798, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel45)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmbCourseCode, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel51)
                        .addGap(12, 12, 12)
                        .addComponent(lblCourseDesc)
                        .addGap(610, 610, 610))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20)
                            .addComponent(jLabel21)
                            .addComponent(jLabel22))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(cmbCollegeCode2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel23)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblCollegeDesc)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(txtSubjCode, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnSearchSubj)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblCollegeYear)
                                        .addGap(18, 18, 18)
                                        .addComponent(cmbCollegeYr, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(jLabel25))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                                .addComponent(txtSubjDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(lblSem))
                                            .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                    .addComponent(txtCurriculum, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                                    .addComponent(txtUnits, javax.swing.GroupLayout.Alignment.LEADING))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel52)))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cmbSem2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(checkSubjStatus)
                                            .addComponent(chkGradesEncoding))))
                                .addGap(147, 147, 147))))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(428, 428, 428))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(btnAddSubj)
                        .addGap(18, 18, 18)
                        .addComponent(btnEditSubj)
                        .addGap(18, 18, 18)
                        .addComponent(btnDeleteSubj)
                        .addGap(392, 392, 392))))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(plmbg8, javax.swing.GroupLayout.PREFERRED_SIZE, 1270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel17)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtSubjCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearchSubj)
                    .addComponent(lblCollegeYear)
                    .addComponent(cmbCollegeYr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txtSubjDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSem)
                    .addComponent(cmbSem2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txtUnits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25)
                    .addComponent(checkSubjStatus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(txtCurriculum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel52)
                    .addComponent(chkGradesEncoding))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(cmbCollegeCode2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23)
                    .addComponent(lblCollegeDesc))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCourseCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel51)
                    .addComponent(lblCourseDesc))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnEditSubj)
                        .addComponent(btnDeleteSubj))
                    .addComponent(btnAddSubj))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(plmbg8, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        tabs.addTab("", jPanel6);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel24.setText("SUBJECT SCHEDULE RECORDS");

        jLabel26.setText("SY:");

        jLabel27.setText("Semester:");

        jLabel28.setText("College :");

        jLabel29.setText("-");

        lblCollegeDescSched.setText("College Description");

        jLabel30.setText("Block No:");

        jLabel31.setText("Subject Code:");

        cmbSubjCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSubjCodeActionPerformed(evt);
            }
        });

        jLabel32.setText("-");

        lblSubjDesc.setText("Subject Description");

        jLabel33.setText("Day:");

        cmbDay.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "M", "T", "W", "Th", "F", "S" }));

        jLabel34.setText("Time:");

        jLabel35.setText("Room:");

        jLabel36.setText("Type:");

        rbOnline.setText("Online");
        rbOnline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbOnlineActionPerformed(evt);
            }
        });

        rbF2F.setText("F2F");
        rbF2F.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbF2FActionPerformed(evt);
            }
        });

        jLabel37.setText("Sequence No:");

        jLabel38.setText("Faculty ID:");

        cmbFacultyID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbFacultyIDActionPerformed(evt);
            }
        });

        jLabel39.setText("Faculty Name:");

        lblFacultyName.setText("----------");

        btnAddSched.setText("Add");
        btnAddSched.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddSchedActionPerformed(evt);
            }
        });

        btnEditSched.setText("Update");
        btnEditSched.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditSchedActionPerformed(evt);
            }
        });

        btnDeleteSched.setText("Delete");
        btnDeleteSched.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteSchedActionPerformed(evt);
            }
        });

        tblSched.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "No existing schedule records found"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblSched.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSchedMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblSched);

        lblCollegeCodeSched.setText("College Code");

        jLabel53.setText("Course:");

        lblCourseCodeSched.setText("Course Code");

        jLabel54.setText("-");

        lblCourseDescSched.setText("Course Description");

        cmbSequenceNo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", " " }));

        plmbg9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/plmopaque.png"))); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(474, 474, 474)
                        .addComponent(jLabel24))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(197, 197, 197)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                                        .addComponent(jLabel33)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cmbDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel34)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtTime, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                                        .addComponent(jLabel30)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmbBlockNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                                        .addComponent(jLabel37)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cmbSequenceNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 499, Short.MAX_VALUE)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(jLabel53)
                                        .addGap(24, 24, 24)
                                        .addComponent(lblCourseCodeSched)
                                        .addGap(12, 12, 12)
                                        .addComponent(jLabel54)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lblCourseDescSched))
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(jLabel28)
                                        .addGap(18, 18, 18)
                                        .addComponent(lblCollegeCodeSched)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel29)
                                        .addGap(12, 12, 12)
                                        .addComponent(lblCollegeDescSched))))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(jLabel26)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmbSchedSY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel27)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cmbSchedSem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(jLabel31)
                                        .addGap(18, 18, 18)
                                        .addComponent(cmbSubjCode, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel32)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblSubjDesc)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(jLabel38)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmbFacultyID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(jLabel39)
                                        .addGap(18, 18, 18)
                                        .addComponent(lblFacultyName, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(jLabel35)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(jLabel36)
                                        .addGap(18, 18, 18)
                                        .addComponent(rbOnline)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(rbF2F)))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(78, 78, 78))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 296, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(btnAddSched)
                        .addGap(18, 18, 18)
                        .addComponent(btnEditSched)
                        .addGap(18, 18, 18)
                        .addComponent(btnDeleteSched)
                        .addGap(366, 366, 366))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 960, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14))))
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(plmbg9, javax.swing.GroupLayout.PREFERRED_SIZE, 1270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel24)
                .addGap(46, 46, 46)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(cmbSchedSY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27)
                            .addComponent(cmbSchedSem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel31)
                            .addComponent(cmbSubjCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel32)
                            .addComponent(lblSubjDesc)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel38)
                            .addComponent(cmbFacultyID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel39)
                            .addComponent(lblFacultyName))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel30)
                            .addComponent(cmbBlockNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel33)
                            .addComponent(cmbDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel34)
                            .addComponent(txtTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28)
                            .addComponent(jLabel29)
                            .addComponent(lblCollegeDescSched)
                            .addComponent(lblCollegeCodeSched))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel53)
                            .addComponent(jLabel54)
                            .addComponent(lblCourseDescSched)
                            .addComponent(lblCourseCodeSched))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(txtRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(rbOnline)
                    .addComponent(rbF2F))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(cmbSequenceNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnEditSched)
                        .addComponent(btnDeleteSched))
                    .addComponent(btnAddSched))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(plmbg9, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        tabs.addTab("", jPanel7);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jLabel40.setText("EMPLOYEE RECORDS");

        jLabel41.setText("Employee ID:");

        jLabel42.setText("First Name:");

        jLabel43.setText("Last Name:");

        btnSearchEmpID.setText("SEARCH ID");
        btnSearchEmpID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchEmpIDActionPerformed(evt);
            }
        });

        btnSearchEmpName.setText("SEARCH NAME");
        btnSearchEmpName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchEmpNameActionPerformed(evt);
            }
        });

        jLabel44.setText("Email:");

        lblEmpEmail.setText("Email Address");

        jLabel46.setText("Gender:");

        cmbEmpGender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "M", "F" }));
        cmbEmpGender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbEmpGenderActionPerformed(evt);
            }
        });

        jLabel47.setText("Cellphone No:");

        jLabel48.setText("Birthday :");

        jLabel49.setText("Address:");

        jLabel50.setText("Status:");

        checkEmpStatus.setText("Active");
        checkEmpStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkEmpStatusActionPerformed(evt);
            }
        });

        btnAddEmp.setText("Add");
        btnAddEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddEmpActionPerformed(evt);
            }
        });

        btnUpdateEmp.setText("Update");
        btnUpdateEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateEmpActionPerformed(evt);
            }
        });

        btnDeleteEmp.setText("Delete");
        btnDeleteEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteEmpActionPerformed(evt);
            }
        });

        tblEmployees.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No existing employee records found"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblEmployees.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEmployeesMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tblEmployees);

        plmbg4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/plmopaque.png"))); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 752, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(116, 116, 116))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel40)
                        .addGap(427, 427, 427))))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(120, 120, 120)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnAddEmp)
                        .addGap(18, 18, 18)
                        .addComponent(btnUpdateEmp)
                        .addGap(18, 18, 18)
                        .addComponent(btnDeleteEmp)
                        .addGap(359, 359, 359))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel43)
                            .addComponent(jLabel44)
                            .addComponent(jLabel42))
                        .addGap(23, 23, 23)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEmpFirstname, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                    .addComponent(lblEmpEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                    .addComponent(txtEmpLastname, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnSearchEmpName)
                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel41)
                                .addGap(24, 24, 24)
                                .addComponent(txtEmpID, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnSearchEmpID)
                                .addGap(225, 225, 225)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel48)
                                    .addComponent(jLabel46)
                                    .addComponent(jLabel50))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(dateEmpBday, javax.swing.GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(checkEmpStatus)
                                            .addComponent(cmbEmpGender, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel47)
                                    .addComponent(jLabel49))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtEmpCellNo, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtEmpAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(plmbg4, javax.swing.GroupLayout.PREFERRED_SIZE, 1270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel40)
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(txtEmpID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearchEmpID)
                    .addComponent(jLabel46)
                    .addComponent(cmbEmpGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(txtEmpFirstname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel48)
                    .addComponent(dateEmpBday, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(txtEmpLastname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearchEmpName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(lblEmpEmail)
                    .addComponent(jLabel50)
                    .addComponent(checkEmpStatus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtEmpCellNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel49)
                    .addComponent(txtEmpAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnUpdateEmp)
                        .addComponent(btnDeleteEmp))
                    .addComponent(btnAddEmp))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(plmbg4, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        tabs.addTab("", jPanel8);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        tblHistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "History log is empty"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHistory.setCellSelectionEnabled(true);
        jScrollPane1.setViewportView(tblHistory);
        tblHistory.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (tblHistory.getColumnModel().getColumnCount() > 0) {
            tblHistory.getColumnModel().getColumn(0).setResizable(false);
        }

        plmbg10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/plmopaque.png"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1258, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(plmbg10, javax.swing.GroupLayout.PREFERRED_SIZE, 1270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 519, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(181, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(plmbg10, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        tabs.addTab("", jPanel3);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 1068, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(jPanel2);

        jPanel9.setBackground(new java.awt.Color(254, 86, 86));
        jPanel9.setLayout(null);

        smallpf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/smallprofile.png"))); // NOI18N
        smallpf.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        smallpf.setForeground(new java.awt.Color(255, 255, 255));
        jPanel9.add(smallpf);
        smallpf.setBounds(1050, 0, 30, 30);

        PLMLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/PLM_Seal_2013.png"))); // NOI18N
        jPanel9.add(PLMLogo);
        PLMLogo.setBounds(10, 0, 30, 30);

        MinimizeBTN.setText("-");
        MinimizeBTN.setBackground(new java.awt.Color(254, 86, 86));
        MinimizeBTN.setBorder(null);
        MinimizeBTN.setFont(new java.awt.Font("Boldfinger", 0, 24)); // NOI18N
        MinimizeBTN.setForeground(new java.awt.Color(255, 255, 255));
        MinimizeBTN.setToolTipText("");
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
        jPanel9.add(MinimizeBTN);
        MinimizeBTN.setBounds(1220, 0, 30, 30);

        CloseBTN.setText("X");
        CloseBTN.setBackground(new java.awt.Color(254, 86, 86));
        CloseBTN.setBorder(null);
        CloseBTN.setFont(new java.awt.Font("Boldfinger", 0, 18)); // NOI18N
        CloseBTN.setForeground(new java.awt.Color(255, 255, 255));
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
        jPanel9.add(CloseBTN);
        CloseBTN.setBounds(1250, 0, 30, 30);

        MainLBL1.setText("Enrollment System for Regular Students");
        MainLBL1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        MainLBL1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel9.add(MainLBL1);
        MainLBL1.setBounds(40, 0, 290, 30);

        NameTopBar.setText(" Admin");
        NameTopBar.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        NameTopBar.setForeground(new java.awt.Color(255, 255, 255));
        jPanel9.add(NameTopBar);
        NameTopBar.setBounds(1070, 0, 110, 30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 1293, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1279, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 684, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        // TODO add your handling code here:
        int response = JOptionPane.showConfirmDialog(this, "Do you really want to log-out?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (response == 0){
            mf.setUserID("");
            mf.switchCard("LoginCard");
            logAction("Logged out");
        }
        else{
            JOptionPane.showMessageDialog(this, "Canceled");
        }
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnStudentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStudentsActionPerformed
        // TODO add your handling code here:
        tabs.setSelectedIndex(0);
        toggleSelected(0);
        loadStudentsTab();
    }//GEN-LAST:event_btnStudentsActionPerformed

    private void btnSYSemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSYSemActionPerformed
        // TODO add your handling code here:
        tabs.setSelectedIndex(1);
        toggleSelected(1);
        loadSYSemTab();
    }//GEN-LAST:event_btnSYSemActionPerformed

    private void btnSubjectsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubjectsActionPerformed
        // TODO add your handling code here:
        tabs.setSelectedIndex(2);
        toggleSelected(2);
        loadSubjectsTab();
    }//GEN-LAST:event_btnSubjectsActionPerformed

    private void btnSchedulesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSchedulesActionPerformed
        // TODO add your handling code here:
        tabs.setSelectedIndex(3);
        toggleSelected(3);
        loadSchedulesTab();
    }//GEN-LAST:event_btnSchedulesActionPerformed

    private void btnEmployeesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmployeesActionPerformed
        // TODO add your handling code here:
        tabs.setSelectedIndex(4);
        toggleSelected(4);
        loadEmployeesTab();
    }//GEN-LAST:event_btnEmployeesActionPerformed

    private void btnHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistoryActionPerformed
        // TODO add your handling code here:
        loadHistoryTable();
        tabs.setSelectedIndex(5);
        toggleSelected(5);
        //TODO: code for displaying history table
        
    }//GEN-LAST:event_btnHistoryActionPerformed

    private void txtStuFirstNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStuFirstNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStuFirstNameActionPerformed

    private void btnStudSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStudSearchActionPerformed
        // TODO add your handling code here:
        con = ConnectDB.connect();
        try {
            String selectedStudentNo = cmbStudentNoYear.getSelectedItem().toString() + "-" + txtStudentNo.getText();
            rs = con.prepareStatement("SELECT * FROM finals.STUDENT WHERE student_no = '" + selectedStudentNo + "'").executeQuery();
            if (rs.next()) {
                txtStuFirstName.setText(rs.getString("first_name"));
                txtStuLastName.setText(rs.getString("last_name"));
                txtStuMidInitial.setText(rs.getString("mi"));
                lblStuEmail.setText(rs.getString("email"));
                if (rs.getString("gender").equals("M"))
                    cmbStuGender.setSelectedIndex(0);
                else
                    cmbStuGender.setSelectedIndex(1);
                txtStuPhone.setText(rs.getString("cp_num"));
                txtStuAddress.setText(rs.getString("address"));
                dateStuBday.setDate(rs.getDate("bday").toLocalDate());
                chkStuActive.setSelected(rs.getString("status").equals("A"));
                cmbStuCourse.setSelectedItem(rs.getString("course_code"));
            } else {
                rs = con.prepareStatement("SELECT * FROM finals.STUDENT WHERE student_no LIKE '" + selectedStudentNo + "%'").executeQuery();
                if (rs.next()) {
                    rs = con.prepareStatement("SELECT * FROM finals.STUDENT WHERE student_no LIKE '" + selectedStudentNo + "%'").executeQuery();
                    tblStudents.setModel(TableUtil.resultSetToTableModel(rs));
                } else {
                    loadStudentTable();
                    JOptionPane.showMessageDialog(this, "No student found with matching search parameters!", "Search error", JOptionPane.INFORMATION_MESSAGE);
                    return;
                    //TODO: ERROR MSG "NO STUDENT FOUND"
                }
            }
        } catch (Exception e) {
            System.out.println(e); //TODO: ADD ERROR MSG
        }
        
    }//GEN-LAST:event_btnStudSearchActionPerformed

    private void cmbStudentNoYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbStudentNoYearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbStudentNoYearActionPerformed

    private void chkStuActiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkStuActiveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkStuActiveActionPerformed

    private void cmbStuCourseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbStuCourseActionPerformed
        // TODO add your handling code here:
        con = ConnectDB.connect();
        if (!cmbStuCourseLoaded)
            return;
        try {
            rs = con.prepareStatement("SELECT description FROM finals.COURSE WHERE course_code = '" + cmbStuCourse.getSelectedItem().toString() + "'").executeQuery();
            if (rs.next())
                txtStuCourseDesc.setText(rs.getString("description"));
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_cmbStuCourseActionPerformed

    private void cmbStuCourseItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbStuCourseItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbStuCourseItemStateChanged

    private void tblStudentsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblStudentsMouseClicked
        // TODO add your handling code here:
        int intRow = tblStudents.getSelectedRow();
        String selectedStudentNo = tblStudents.getValueAt(intRow, 0).toString();
        cmbStudentNoYear.setSelectedItem(selectedStudentNo.substring(0, 4));
        txtStudentNo.setText(selectedStudentNo.substring(5));
        con = ConnectDB.connect();
        try {
            rs = con.prepareStatement("SELECT * FROM finals.STUDENT WHERE student_no = '" + selectedStudentNo + "'").executeQuery();
            if (rs.next()) {
                txtStuFirstName.setText(rs.getString("first_name"));
                txtStuLastName.setText(rs.getString("last_name"));
                txtStuMidInitial.setText(rs.getString("mi"));
                lblStuEmail.setText(rs.getString("email"));
                if (rs.getString("gender").equals("M"))
                    cmbStuGender.setSelectedIndex(0);
                else
                    cmbStuGender.setSelectedIndex(1);
                txtStuPhone.setText(rs.getString("cp_num"));
                txtStuAddress.setText(rs.getString("address"));
                dateStuBday.setDate(rs.getDate("bday").toLocalDate());
                chkStuActive.setSelected(rs.getString("status").equals("A"));
                cmbStuCourse.setSelectedItem(rs.getString("course_code"));
            }
        } catch (Exception e) {
            System.out.println(e); //TODO: ADD ERROR MSG
        }
        
    }//GEN-LAST:event_tblStudentsMouseClicked

    private void btnAddStuRecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddStuRecActionPerformed
        // TODO add your handling code here:
        con = ConnectDB.connect();
        int intAnswer = JOptionPane.showConfirmDialog(null, "Add a record?", "Adding Record", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (intAnswer == 0) {
            try {
                ps = con.prepareStatement("INSERT INTO finals.STUDENT (student_no, last_name, first_name, mi, gender, course_code, cp_num, address, bday, status)"
                        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setString(1, cmbStudentNoYear.getSelectedItem().toString() + "-" + txtStudentNo.getText());
                ps.setString(2, txtStuLastName.getText());
                ps.setString(3, txtStuFirstName.getText());
                ps.setString(4, txtStuMidInitial.getText());
                ps.setString(5, cmbStuGender.getSelectedItem().toString());
                ps.setString(6, cmbStuCourse.getSelectedItem().toString());
                ps.setString(7, txtStuPhone.getText());
                ps.setString(8, txtStuAddress.getText());
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                ps.setDate(9, new java.sql.Date(df.parse(dateStuBday.toString()).getTime()));
                if (chkStuActive.isSelected()) 
                    ps.setString(10, "A");
                else 
                    ps.setString(10, "I");
                ps.execute();
                loadStudentTable();
                logAction("Added student record");
                JOptionPane.showMessageDialog(null, "Added record successfully.");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }//GEN-LAST:event_btnAddStuRecActionPerformed

    private void btnUpdateStuRecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateStuRecActionPerformed
        // TODO add your handling code here:
        con = ConnectDB.connect();
        String selectedStudntNo = cmbStudentNoYear.getSelectedItem().toString() + "-" + txtStudentNo.getText();
        int intAnswer = JOptionPane.showConfirmDialog(null, "Update a record?", "Updating Record", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (intAnswer == 0) {
            try {
                ps = con.prepareStatement("UPDATE finals.STUDENT " 
                        + "SET last_name = ?"  
                        + ", first_name = ?"  
                        + ", mi = ?" 
                        + ", gender = ?"
                        + ", course_code = ?"
                        + ", cp_num = ?"
                        + ", address = ?"
                        + ", bday = ?"
                        + ",status = ?"
                        + " WHERE student_no = '" + selectedStudntNo + "'"
                );
                ps.setString(1, txtStuLastName.getText());
                ps.setString(2, txtStuFirstName.getText());
                ps.setString(3, txtStuMidInitial.getText());
                ps.setString(4, cmbStuGender.getSelectedItem().toString());
                ps.setString(5, cmbStuCourse.getSelectedItem().toString());
                ps.setString(6, txtStuPhone.getText());
                ps.setString(7, txtStuAddress.getText());
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                ps.setDate(8, new java.sql.Date(df.parse(dateStuBday.toString()).getTime()));
                if (chkStuActive.isSelected()) 
                    ps.setString(9, "A");
                else 
                    ps.setString(9, "I");
                ps.execute();
                loadStudentTable();
                logAction("Updated student record");
                JOptionPane.showMessageDialog(null, "Updated record successfully.");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }//GEN-LAST:event_btnUpdateStuRecActionPerformed

    private void btnDeleteStuRecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteStuRecActionPerformed
        // TODO add your handling code here:
        con = ConnectDB.connect();
        String selectedStudntNo = cmbStudentNoYear.getSelectedItem().toString() + "-" + txtStudentNo.getText();
        int intAnswer = JOptionPane.showConfirmDialog(null, "Delete a record?", "Deleting Record", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (intAnswer == 0) {
            try {
                ps = con.prepareStatement("DELETE FROM finals.student"
                        + " WHERE student_no = '" + selectedStudntNo + "'"
                );
                ps.execute();
                loadStudentTable();
                logAction("Deleted student record");
                JOptionPane.showMessageDialog(null, "Deleted record successfully.");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }//GEN-LAST:event_btnDeleteStuRecActionPerformed

    private void txtStuMidInitialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStuMidInitialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStuMidInitialActionPerformed

    private void rbSYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbSYActionPerformed
        // TODO add your handling code here:
        rbSem.setSelected(false);
        txtSem.setEnabled(false);
        txtSY.setEnabled(true);
        txtSem.setText("");
        
        loadSYTable();
    }//GEN-LAST:event_rbSYActionPerformed

    private void btnAddSYSemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddSYSemActionPerformed
        // TODO add your handling code here:
        con = ConnectDB.connect();
        
        try{
            if (rbSY.isSelected()) {
                if(!(txtSY.getText().toString().equals(""))) {
                    ps = con.prepareStatement("INSERT INTO finals.SY VALUES ('" + txtSY.getText().toString() + "')");
                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected > 0) {
                        logAction("Added school year record");
                        JOptionPane.showMessageDialog(null, "Added record successfully.");
                    } else
                        JOptionPane.showMessageDialog(null, "Invalid SY", "ERROR", JOptionPane.ERROR_MESSAGE);
                     loadSYTable();
                } else {
                    JOptionPane.showMessageDialog(null, "Cannot enter empty value", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } else if (rbSem.isSelected()) {
                if(!(txtSem.getText().toString().equals(""))) {
                    ps = con.prepareStatement("INSERT INTO finals.SEMESTER VALUES ('" + txtSem.getText().toString() + "')");
                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected > 0) {
                        logAction("Added semester record");
                        JOptionPane.showMessageDialog(null, "Added record successfully.");
                    } else
                        JOptionPane.showMessageDialog(null, "Invalid Semester", "ERROR", JOptionPane.ERROR_MESSAGE);
                     loadSemTable();
                } else {
                    JOptionPane.showMessageDialog(null, "Cannot enter empty value", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Select a record to add to", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Cannot add duplicate", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAddSYSemActionPerformed

    private void checkSubjStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkSubjStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkSubjStatusActionPerformed

    private void txtUnitsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUnitsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUnitsActionPerformed

    private void btnSearchSubjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchSubjActionPerformed
        // TODO add your handling code here:
        con = ConnectDB.connect();
        
        try {
            ps = con.prepareStatement("SELECT * FROM finals.SUBJECT WHERE subject_code = '" + txtSubjCode.getText().toString() + "'");
            rs = ps.executeQuery();
            if (rs.next()) {
                rs = ps.executeQuery();
                tblEmployees.setModel(TableUtil.resultSetToTableModel(rs));
                TableUtil.resizeColumnWidth(tblEmployees);
            } else {
                JOptionPane.showMessageDialog(null, "Subject code does not exist", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_btnSearchSubjActionPerformed

    private void btnAddSubjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddSubjActionPerformed
        // TODO add your handling code here:
        con = ConnectDB.connect();
        try {
            ps = con.prepareStatement("INSERT INTO finals.SUBJECT VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, cmbCollegeYr.getSelectedItem().toString());
            ps.setString(2, cmbSem2.getSelectedItem().toString());
            ps.setString(3, txtSubjCode.getText().toString());
            ps.setString(4, txtSubjDesc.getText().toString());
            ps.setString(5, txtUnits.getText().toString());
            ps.setString(6, txtCurriculum.getText().toString());
            ps.setString(7, cmbCollegeCode2.getSelectedItem().toString());
            ps.setString(8, cmbCourseCode.getSelectedItem().toString());
            if(checkSubjStatus.isSelected()) 
                ps.setString(9, "A");
            else
                ps.setString(9, "I");
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                logAction("Added subject record");
                JOptionPane.showMessageDialog(null, "Added record successfully.");
            } else
                JOptionPane.showMessageDialog(null, "Cannot add duplicate", "ERROR", JOptionPane.ERROR_MESSAGE);
            
            loadSubjectTable();
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_btnAddSubjActionPerformed

    private void btnEditSubjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditSubjActionPerformed
        // TODO add your handling code here:
        con = ConnectDB.connect();
        try {
            ps = con.prepareStatement("UPDATE finals.SUBJECT "
                    + "SET college_year = ?, semester = ?, description = ?,"
                    + " units = ?, curriculum = ?, college_code = ?, course_code = ?, status = ?"
                    + " WHERE subject_code = ?");
            ps.setString(1, cmbCollegeYr.getSelectedItem().toString());
            ps.setString(2, cmbSem2.getSelectedItem().toString());
            ps.setString(9, txtSubjCode.getText().toString());
            ps.setString(3, txtSubjDesc.getText().toString());
            ps.setString(4, txtUnits.getText().toString());
            ps.setString(5, txtCurriculum.getText().toString());
            ps.setString(6, cmbCollegeCode2.getSelectedItem().toString());
            ps.setString(7, cmbCourseCode.getSelectedItem().toString());
            if(checkSubjStatus.isSelected()) 
                ps.setString(8, "A");
            else
                ps.setString(8, "I");
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                logAction("Updated subject record");
                JOptionPane.showMessageDialog(null, "Updated record successfully.");
            } else
                JOptionPane.showMessageDialog(null, "Cannot modify subject code", "ERROR", JOptionPane.ERROR_MESSAGE);
            
            loadSubjectTable();
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_btnEditSubjActionPerformed

    private void btnDeleteSubjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteSubjActionPerformed
        // TODO add your handling code here:
        con = ConnectDB.connect();
        try {
            ps = con.prepareStatement("DELETE FROM finals.SUBJECT "
                    + " WHERE subject_code = ?");
            ps.setString(1, txtSubjCode.getText().toString());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                logAction("Deleted subject record");
                JOptionPane.showMessageDialog(null, "Deleted record successfully.");
            } else
                JOptionPane.showMessageDialog(null, "Subject does not exist", "ERROR", JOptionPane.ERROR_MESSAGE);
            
            loadSubjectTable();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cannot delete subject in use", "ERROR", JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }
    }//GEN-LAST:event_btnDeleteSubjActionPerformed

    private void rbOnlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbOnlineActionPerformed
        // TODO add your handling code here:
        rbF2F.setSelected(false);
    }//GEN-LAST:event_rbOnlineActionPerformed

    private void btnAddSchedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddSchedActionPerformed
        // TODO add your handling code here:
        con = ConnectDB.connect();
        String college_code = "";
        
        try {
            rs = con.prepareStatement("SELECT * FROM finals.vwSubjectInfo_Sched WHERE subject_code = '" + cmbSubjCode.getSelectedItem().toString() + "'").executeQuery();
            while (rs.next())
                college_code = rs.getString("college_code");
            
            ps = con.prepareStatement("INSERT INTO finals.SUBJECT_SCHEDULE VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, cmbSchedSY.getSelectedItem().toString());
            ps.setString(2, cmbSchedSem.getSelectedItem().toString());
            ps.setString(3, college_code);
            ps.setString(4, cmbBlockNo.getSelectedItem().toString());
            ps.setString(5, cmbSubjCode.getSelectedItem().toString());
            ps.setString(6, cmbDay.getSelectedItem().toString());
            ps.setString(7, txtTime.getText().toString());
            ps.setString(8, txtRoom.getText().toString());
            if (rbOnline.isSelected())
                ps.setString(9, "OL");
            else 
                ps.setString(9, "F2F");
            ps.setString(10, cmbSequenceNo.getSelectedItem().toString());
            ps.setString(11, cmbFacultyID.getSelectedItem().toString());
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                logAction("Added schedule record");
                JOptionPane.showMessageDialog(null, "Added record successfully.");
            } else
                JOptionPane.showMessageDialog(null, "Cannot add", "ERROR", JOptionPane.ERROR_MESSAGE);
            loadScheduleTable();   
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_btnAddSchedActionPerformed

    private void btnEditSchedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditSchedActionPerformed
        // TODO add your handling code here:
        con = ConnectDB.connect();
        String college_code = "";
        
        try {
            rs = con.prepareStatement("SELECT * FROM finals.vwSubjectInfo_Sched WHERE subject_code = '" + cmbSubjCode.getSelectedItem().toString() + "'").executeQuery();
            while (rs.next())
                college_code = rs.getString("college_code");
            
            ps = con.prepareStatement("UPDATE finals.SUBJECT_SCHEDULE "
                    + "SET day = ?, time = ?, room = ?, type = ?, faculty_id = ? "
                    + "WHERE sy = ? AND semester = ? AND college_code = ? AND block_no = ? AND subject_code = ? AND sequence_no = ?");
            ps.setString(6, cmbSchedSY.getSelectedItem().toString());
            ps.setString(7, cmbSchedSem.getSelectedItem().toString());
            ps.setString(8, college_code);
            ps.setString(9, cmbBlockNo.getSelectedItem().toString());
            ps.setString(10, cmbSubjCode.getSelectedItem().toString());
            ps.setString(1, cmbDay.getSelectedItem().toString());
            ps.setString(2, txtTime.getText().toString());
            ps.setString(3, txtRoom.getText().toString());
            if (rbOnline.isSelected())
                ps.setString(4, "OL");
            else 
                ps.setString(4, "F2F");
            ps.setString(11, cmbSequenceNo.getSelectedItem().toString());
            ps.setString(5, cmbFacultyID.getSelectedItem().toString());
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                logAction("Updated schedule record");
                JOptionPane.showMessageDialog(null, "Updated record successfully.");
            } else
                JOptionPane.showMessageDialog(null, "Cannot update", "ERROR", JOptionPane.ERROR_MESSAGE);
            loadScheduleTable();   
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_btnEditSchedActionPerformed

    private void btnDeleteSchedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteSchedActionPerformed
        // TODO add your handling code here:
        con = ConnectDB.connect();
        String college_code = "";
        
        try {
            rs = con.prepareStatement("SELECT * FROM finals.vwSubjectInfo_Sched WHERE subject_code = '" + cmbSubjCode.getSelectedItem().toString() + "'").executeQuery();
            while (rs.next())
                college_code = rs.getString("college_code");
            
            ps = con.prepareStatement("DELETE FROM finals.SUBJECT_SCHEDULE "
                    + "WHERE sy = ? AND semester = ? AND college_code = ? AND block_no = ? AND subject_code = ? AND sequence_no = ?");
            ps.setString(1, cmbSchedSY.getSelectedItem().toString());
            ps.setString(2, cmbSchedSem.getSelectedItem().toString());
            ps.setString(3, college_code);
            ps.setString(4, cmbBlockNo.getSelectedItem().toString());
            ps.setString(5, cmbSubjCode.getSelectedItem().toString());
            ps.setString(6, cmbSequenceNo.getSelectedItem().toString());
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                logAction("Added schedule record");
                JOptionPane.showMessageDialog(null, "Deleted record successfully.");
            } else
                JOptionPane.showMessageDialog(null, "Cannot delete", "ERROR", JOptionPane.ERROR_MESSAGE);
            loadScheduleTable();   
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_btnDeleteSchedActionPerformed

    private void btnSearchEmpIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchEmpIDActionPerformed
        // TODO add your handling code here:
        con = ConnectDB.connect();
        try {
            ps = con.prepareStatement("SELECT * FROM finals.EMPLOYEE WHERE employee_id = ?");
            ps.setString(1,txtEmpID.getText().toString());
            rs = ps.executeQuery();
            
            if (rs.next()) {
                txtEmpFirstname.setText(rs.getString("first_name").toString());
                txtEmpLastname.setText(rs.getString("last_name").toString());
                txtEmpCellNo.setText(rs.getString("cp_num").toString());
                txtEmpAddress.setText(rs.getString("address").toString());
                lblEmpEmail.setText(rs.getString("email").toString());
                cmbEmpGender.setSelectedItem(rs.getString("gender").toString());
                if (!(rs.getDate("bday")==null)) 
                    dateEmpBday.setDate(rs.getDate("bday").toLocalDate());
                if (rs.getString("status").toString().equals("A"))
                    checkEmpStatus.setSelected(true);
                else
                    checkEmpStatus.setSelected(false);
                
                rs = ps.executeQuery();
                tblEmployees.setModel(TableUtil.resultSetToTableModel(rs));
                TableUtil.resizeColumnWidth(tblEmployees);
                
            } else {
                JOptionPane.showMessageDialog(null, "Employee does not exist", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_btnSearchEmpIDActionPerformed

    private void btnSearchEmpNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchEmpNameActionPerformed
        // TODO add your handling code here:
        con = ConnectDB.connect();
        try {
            ps = con.prepareStatement("SELECT * FROM finals.EMPLOYEE WHERE first_name LIKE ? OR last_name LIKE ?");
            ps.setString(1, "%" + txtEmpFirstname.getText() + "%");
            ps.setString(2, "%" + txtEmpLastname.getText() + "%");
            rs = ps.executeQuery();
            if (rs.next()) {
                txtEmpID.setText(rs.getString("employee_id").toString());
                txtEmpFirstname.setText(rs.getString("first_name").toString());
                txtEmpLastname.setText(rs.getString("last_name").toString());
                txtEmpCellNo.setText(rs.getString("cp_num").toString());
                txtEmpAddress.setText(rs.getString("address").toString());
                lblEmpEmail.setText(rs.getString("email").toString());
                cmbEmpGender.setSelectedItem(rs.getString("gender").toString());
                if (!(rs.getDate("bday")==null)) 
                    dateEmpBday.setDate(rs.getDate("bday").toLocalDate());
                if (rs.getString("status").toString().equals("A"))
                    checkEmpStatus.setSelected(true);
                else
                    checkEmpStatus.setSelected(false);
                
                rs = ps.executeQuery();
                tblEmployees.setModel(TableUtil.resultSetToTableModel(rs));
                TableUtil.resizeColumnWidth(tblEmployees);
            } else {
                loadStudentTable();
                JOptionPane.showMessageDialog(this, "No employee found with matching search parameters!", "Search error", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_btnSearchEmpNameActionPerformed

    private void cmbEmpGenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbEmpGenderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbEmpGenderActionPerformed

    private void checkEmpStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkEmpStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkEmpStatusActionPerformed

    private void btnAddEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddEmpActionPerformed
        // TODO add your handling code here:
        con = ConnectDB.connect();
        try {
            ps = con.prepareStatement("INSERT INTO finals.EMPLOYEE VALUES (?,?,?,default,?,?,?,?,?)");
            ps.setString(1, txtEmpID.getText().toString());
            ps.setString(2, txtEmpLastname.getText().toString());
            ps.setString(3, txtEmpFirstname.getText().toString());
            ps.setString(4, cmbEmpGender.getSelectedItem().toString());
            ps.setString(5, txtEmpCellNo.getText().toString());
            ps.setString(6, txtEmpAddress.getText().toString());
            if (!(dateEmpBday.toString()=="")) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                ps.setDate(7, new java.sql.Date(df.parse(dateEmpBday.toString()).getTime()));
            } else {
                ps.setDate(7, null);
            }
            if (cmbEmpGender.getSelectedItem().equals("A"))
                ps.setString(8,"A");
            else
                ps.setString(8,"I");
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                logAction("Added employee record");
                JOptionPane.showMessageDialog(null, "Added record successfully.");
            } else
                JOptionPane.showMessageDialog(null, "Cannot add", "ERROR", JOptionPane.ERROR_MESSAGE);
            loadEmployeeTable();   
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cannot add duplicate", "ERROR", JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }
    }//GEN-LAST:event_btnAddEmpActionPerformed

    private void btnUpdateEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateEmpActionPerformed
        // TODO add your handling code here:
        con = ConnectDB.connect();
        try {
            ps = con.prepareStatement("UPDATE finals.EMPLOYEE SET "
                    + "last_name = ?, first_name = ?, gender = ?, cp_num = ?, address = ?, bday = ?, status = ? "
                    + "WHERE employee_id = ?");
            ps.setString(8, txtEmpID.getText().toString());
            ps.setString(1, txtEmpLastname.getText().toString());
            ps.setString(2, txtEmpFirstname.getText().toString());
            ps.setString(3, cmbEmpGender.getSelectedItem().toString());
            ps.setString(4, txtEmpCellNo.getText().toString());
            ps.setString(5, txtEmpAddress.getText().toString());
            if (!(dateEmpBday.toString()=="")) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                ps.setDate(6, new java.sql.Date(df.parse(dateEmpBday.toString()).getTime()));
            } else {
                ps.setDate(6, null);
            }
            if (cmbEmpGender.getSelectedItem().equals("A"))
                ps.setString(7,"A");
            else
                ps.setString(7,"I");
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Updated record successfully.");
                logAction("Updated employee record");
            } else
                JOptionPane.showMessageDialog(null, "Cannot modify employee ID1", "ERROR", JOptionPane.ERROR_MESSAGE);
            loadEmployeeTable();   
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cannot modify existing employee ID", "ERROR", JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }
    }//GEN-LAST:event_btnUpdateEmpActionPerformed

    private void btnDeleteEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteEmpActionPerformed
        // TODO add your handling code here:
        con = ConnectDB.connect();
        try {
            ps = con.prepareStatement("DELETE FROM finals.EMPLOYEE "
                    + "WHERE employee_id = ?");
            ps.setString(1, txtEmpID.getText().toString());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Deleted record successfully.");
                logAction("Deleted employee record");
            } else
                JOptionPane.showMessageDialog(null, "Record does not exist", "ERROR", JOptionPane.ERROR_MESSAGE);
            loadEmployeeTable();   
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cannot delete employee ID in use", "ERROR", JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }
    }//GEN-LAST:event_btnDeleteEmpActionPerformed

    private void btnStudSearchNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStudSearchNameActionPerformed
        // TODO add your handling code here:
        con = ConnectDB.connect();
        String query = "";
        if (!txtStuFirstName.getText().isEmpty() && !txtStuLastName.getText().isEmpty()) {
            query = "SELECT * FROM finals.STUDENT WHERE first_name LIKE '%"+txtStuFirstName.getText()+"%' AND last_name LIKE '%"+txtStuLastName.getText()+"%'";
        } else if (!txtStuFirstName.getText().isEmpty()) {
            query = "SELECT * FROM finals.STUDENT WHERE first_name LIKE '%"+txtStuFirstName.getText()+"%'";
        } else if (!txtStuLastName.getText().isEmpty()) {
            query = "SELECT * FROM finals.STUDENT WHERE last_name LIKE '%"+txtStuLastName.getText()+"%'";
        } else {
            loadStudentTable();
        }
        try {
            System.out.println(query);
            rs = con.prepareStatement(query).executeQuery();
            if (rs.next()) {
                rs = con.prepareStatement(query).executeQuery();
                tblStudents.setModel(TableUtil.resultSetToTableModel(rs));
            } else {
                loadStudentTable();
                JOptionPane.showMessageDialog(this, "No student found with matching search parameters!", "Search error", JOptionPane.INFORMATION_MESSAGE);
                return;
                //TODO: ERROR MSG "NO STUDENT FOUND"
            }
        } catch (Exception e) {
            System.out.println(e);
            //TODO: ERROR MSGS
        }
    }//GEN-LAST:event_btnStudSearchNameActionPerformed


    private void MinimizeBTNMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MinimizeBTNMouseEntered
        MinimizeBTN.setBackground(new Color(203,68,68));
        // TODO add your handling code here:
    }//GEN-LAST:event_MinimizeBTNMouseEntered

    private void MinimizeBTNMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MinimizeBTNMouseExited
        MinimizeBTN.setBackground(new Color(254,86,86));
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
  
    private void rbSemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbSemActionPerformed
        // TODO add your handling code here:
        rbSY.setSelected(false);
        txtSY.setEnabled(false);
        txtSem.setEnabled(true);
        txtSY.setText("");
        
        loadSemTable();
    }//GEN-LAST:event_rbSemActionPerformed

    private void btnDeleteSYSemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteSYSemActionPerformed
        // TODO add your handling code here:
        con = ConnectDB.connect();
        
        try{
            if (rbSY.isSelected()) {
                ps = con.prepareStatement("DELETE FROM finals.SY WHERE SY = '" + txtSY.getText().toString() + "'");
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Deleted record successfully.");
                    logAction("Deleted SY record");
                } else
                    JOptionPane.showMessageDialog(null, "SY does not exist", "ERROR", JOptionPane.ERROR_MESSAGE);
                 loadSYTable();
            } else if (rbSem.isSelected()) {
                ps = con.prepareStatement("DELETE FROM finals.SEMESTER WHERE SEMESTER = '" + txtSem.getText().toString() + "'");
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Deleted record successfully.");
                    logAction("Deleted semester record");
                } else
                    JOptionPane.showMessageDialog(null, "Semester does not exist", "ERROR", JOptionPane.ERROR_MESSAGE);
                 loadSemTable();
            } else {
                JOptionPane.showMessageDialog(null, "Select a record to add to", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Cannot delete SY/SEMESTER in use", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnDeleteSYSemActionPerformed

    private void tblSYSemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSYSemMouseClicked
        // TODO add your handling code here:
        int intRow = tblSYSem.getSelectedRow(); 

        if (rbSY.isSelected()) {
            txtSY.setText(tblSYSem.getValueAt(intRow, 0).toString());
        } else if (rbSem.isSelected()) {
            txtSem.setText(tblSYSem.getValueAt(intRow, 0).toString());
        }
    }//GEN-LAST:event_tblSYSemMouseClicked

    private void cmbCollegeCode2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCollegeCode2ActionPerformed
         con = ConnectDB.connect();
        if (!cmbCollegeLoaded)
            return;
        try {
            rs = con.prepareStatement("SELECT description FROM finals.COLLEGE WHERE college_code = '" + cmbCollegeCode2.getSelectedItem().toString() + "'").executeQuery();
            if (rs.next())
                lblCollegeDesc.setText(rs.getString("description"));
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_cmbCollegeCode2ActionPerformed

    private void tblSubjectsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSubjectsMouseClicked
        // TODO add your handling code here:
        int intRow = tblSubjects.getSelectedRow(); 
        
        cmbCollegeYr.setSelectedItem(tblSubjects.getValueAt(intRow, 0).toString());
        cmbSem2.setSelectedItem(tblSubjects.getValueAt(intRow,1).toString());
        txtSubjCode.setText(tblSubjects.getValueAt(intRow, 2).toString());
        txtSubjDesc.setText(tblSubjects.getValueAt(intRow, 3).toString());
        txtUnits.setText(tblSubjects.getValueAt(intRow, 4).toString());
        txtCurriculum.setText(tblSubjects.getValueAt(intRow, 5).toString());
        cmbCollegeCode2.setSelectedItem(tblSubjects.getValueAt(intRow, 6).toString());
        cmbCourseCode.setSelectedItem(tblSubjects.getValueAt(intRow, 7).toString());
        if ((tblSubjects.getValueAt(intRow, 8).toString()).equals("A"))
            checkSubjStatus.setSelected(true);
        else
            checkSubjStatus.setSelected(false);
        
    }//GEN-LAST:event_tblSubjectsMouseClicked

    private void cmbCollegeYrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCollegeYrActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCollegeYrActionPerformed

    private void cmbSem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSem2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSem2ActionPerformed

    private void cmbCourseCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCourseCodeActionPerformed
        // TODO add your handling code here:
        con = ConnectDB.connect();
        if (!cmbStuCourseLoaded2)
            return;
        try {
            rs = con.prepareStatement("SELECT description FROM finals.COURSE WHERE course_code = '" + cmbCourseCode.getSelectedItem().toString() + "'").executeQuery();
            if (rs.next())
                lblCourseDesc.setText(rs.getString("description"));
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_cmbCourseCodeActionPerformed

    private void tblSubjectsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSubjectsMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_tblSubjectsMouseEntered

    private void tblEmployeesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmployeesMouseClicked
        // TODO add your handling code here:
        int intRow = tblEmployees.getSelectedRow();
        
        con = ConnectDB.connect();
        String selectedEmployee = tblEmployees.getValueAt(intRow, 0).toString();
        try {
            ps = con.prepareStatement("SELECT * FROM finals.EMPLOYEE WHERE "
                    + "employee_id = '" + selectedEmployee + "'");
            rs = ps.executeQuery();
            if (rs.next()) {
                txtEmpID.setText(rs.getString("employee_id").toString());
                txtEmpLastname.setText(rs.getString("last_name").toString());
                txtEmpFirstname.setText(rs.getString("first_name").toString());
                lblEmpEmail.setText(rs.getString("email").toString());
                cmbEmpGender.setSelectedItem(rs.getString("gender").toString());
                txtEmpCellNo.setText(rs.getString("cp_num").toString());
                txtEmpAddress.setText(rs.getString("address").toString());
                if (!(rs.getDate("bday")==null))
                    dateEmpBday.setDate(rs.getDate("bday").toLocalDate());
                else
                    dateEmpBday.clear();
                checkEmpStatus.setSelected(rs.getString("status").equals("A"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_tblEmployeesMouseClicked

    private void cmbSubjCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSubjCodeActionPerformed
        // TODO add your handling code here:
        con = ConnectDB.connect();
        if (!cmbSubjCodeLoaded)
            return;
        try {
            rs = con.prepareStatement("SELECT * FROM finals.vwSubjectInfo_Sched WHERE subject_code = '" + cmbSubjCode.getSelectedItem().toString() + "'").executeQuery();
            if (rs.next()) {
                lblSubjDesc.setText(rs.getString("subj_desc"));
                lblCollegeCodeSched.setText(rs.getString("college_code"));
                lblCollegeDescSched.setText(rs.getString("college_desc"));
                lblCourseCodeSched.setText(rs.getString("course_code"));
                lblCourseDescSched.setText(rs.getString("course_desc"));
                cmbSubjCodeLoaded = true;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_cmbSubjCodeActionPerformed

    private void cmbFacultyIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFacultyIDActionPerformed
        // TODO add your handling code here:
        con = ConnectDB.connect();
        if (!cmbFacultyLoaded)
            return;
        try {
            rs = con.prepareStatement("SELECT * FROM finals.EMPLOYEE WHERE employee_id = '" + cmbFacultyID.getSelectedItem().toString() + "'").executeQuery();
            if (rs.next()) 
                lblFacultyName.setText(rs.getString("first_name")+" "+rs.getString("last_name"));
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_cmbFacultyIDActionPerformed

    private void tblSchedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSchedMouseClicked
        // TODO add your handling code here:
        int intRow = tblSched.getSelectedRow();
        
        cmbSchedSY.setSelectedItem(tblSched.getValueAt(intRow, 0).toString());
        cmbSchedSem.setSelectedItem(tblSched.getValueAt(intRow, 1).toString());
        lblCollegeCodeSched.setText(tblSched.getValueAt(intRow, 2).toString());
        cmbBlockNo.setSelectedItem(tblSched.getValueAt(intRow, 3).toString());
        cmbSubjCode.setSelectedItem(tblSched.getValueAt(intRow, 4).toString());
        cmbDay.setSelectedItem(tblSched.getValueAt(intRow, 5).toString());
        txtTime.setText(tblSched.getValueAt(intRow, 6).toString());
        txtRoom.setText(tblSched.getValueAt(intRow, 7).toString());
        if(tblSched.getValueAt(intRow, 8).toString().equals("OL")) {
            rbOnline.setSelected(true);
            rbF2F.setSelected(false);
        } else {
            rbOnline.setSelected(false);
            rbF2F.setSelected(true);
        }
        cmbSequenceNo.setSelectedItem(tblSched.getValueAt(intRow, 9).toString());
        cmbFacultyID.setSelectedItem(tblSched.getValueAt(intRow, 10).toString());
    }//GEN-LAST:event_tblSchedMouseClicked

    private void rbF2FActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbF2FActionPerformed
        // TODO add your handling code here:
        rbOnline.setSelected(false);
    }//GEN-LAST:event_rbF2FActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CloseBTN;
    private javax.swing.JLabel MainLBL1;
    private javax.swing.JButton MinimizeBTN;
    private javax.swing.JLabel NameTopBar;
    private javax.swing.JLabel PLMLogo;
    private javax.swing.JButton btnAddEmp;
    private javax.swing.JButton btnAddSYSem;
    private javax.swing.JButton btnAddSched;
    private javax.swing.JButton btnAddStuRec;
    private javax.swing.JButton btnAddSubj;
    private javax.swing.JButton btnDeleteEmp;
    private javax.swing.JButton btnDeleteSYSem;
    private javax.swing.JButton btnDeleteSched;
    private javax.swing.JButton btnDeleteStuRec;
    private javax.swing.JButton btnDeleteSubj;
    private javax.swing.JButton btnEditSched;
    private javax.swing.JButton btnEditSubj;
    private javax.swing.JButton btnEmployees;
    private javax.swing.JButton btnHistory;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnSYSem;
    private javax.swing.JButton btnSchedules;
    private javax.swing.JButton btnSearchEmpID;
    private javax.swing.JButton btnSearchEmpName;
    private javax.swing.JButton btnSearchSubj;
    private javax.swing.JButton btnStudSearch;
    private javax.swing.JButton btnStudSearchName;
    private javax.swing.JButton btnStudents;
    private javax.swing.JButton btnSubjects;
    private javax.swing.JButton btnUpdateEmp;
    private javax.swing.JButton btnUpdateStuRec;
    private javax.swing.JCheckBox checkEmpStatus;
    private javax.swing.JCheckBox checkSubjStatus;
    private javax.swing.JCheckBox chkGradesEncoding;
    private javax.swing.JCheckBox chkStuActive;
    private javax.swing.JComboBox<String> cmbBlockNo;
    private javax.swing.JComboBox<String> cmbCollegeCode2;
    private javax.swing.JComboBox<String> cmbCollegeYr;
    private javax.swing.JComboBox<String> cmbCourseCode;
    private javax.swing.JComboBox<String> cmbDay;
    private javax.swing.JComboBox<String> cmbEmpGender;
    private javax.swing.JComboBox<String> cmbFacultyID;
    private javax.swing.JComboBox<String> cmbSchedSY;
    private javax.swing.JComboBox<String> cmbSchedSem;
    private javax.swing.JComboBox<String> cmbSem2;
    private javax.swing.JComboBox<String> cmbSequenceNo;
    private javax.swing.JComboBox<String> cmbStuCourse;
    private javax.swing.JComboBox<String> cmbStuGender;
    private javax.swing.JComboBox<String> cmbStudentNoYear;
    private javax.swing.JComboBox<String> cmbSubjCode;
    private com.github.lgooddatepicker.components.DatePicker dateEmpBday;
    private com.github.lgooddatepicker.components.DatePicker dateStuBday;
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
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
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
    private javax.swing.JLabel lblCollegeCodeSched;
    private javax.swing.JLabel lblCollegeDesc;
    private javax.swing.JLabel lblCollegeDescSched;
    private javax.swing.JLabel lblCollegeYear;
    private javax.swing.JLabel lblCourseCodeSched;
    private javax.swing.JLabel lblCourseDesc;
    private javax.swing.JLabel lblCourseDescSched;
    private javax.swing.JLabel lblEmpEmail;
    private javax.swing.JLabel lblFacultyName;
    private javax.swing.JLabel lblSem;
    private javax.swing.JLabel lblStuEmail;
    private javax.swing.JLabel lblSubjDesc;
    private javax.swing.JLabel pficon;
    private javax.swing.JLabel pficon1;
    private javax.swing.JLabel pficon2;
    private javax.swing.JLabel pficon3;
    private javax.swing.JLabel pficon4;
    private javax.swing.JLabel pficon5;
    private javax.swing.JLabel pficon6;
    private javax.swing.JLabel plmbg10;
    private javax.swing.JLabel plmbg4;
    private javax.swing.JLabel plmbg6;
    private javax.swing.JLabel plmbg7;
    private javax.swing.JLabel plmbg8;
    private javax.swing.JLabel plmbg9;
    private javax.swing.JRadioButton rbF2F;
    private javax.swing.JRadioButton rbOnline;
    private javax.swing.JRadioButton rbSY;
    private javax.swing.JRadioButton rbSem;
    private javax.swing.JPanel select1;
    private javax.swing.JPanel select2;
    private javax.swing.JPanel select3;
    private javax.swing.JPanel select4;
    private javax.swing.JPanel select5;
    private javax.swing.JPanel select6;
    private javax.swing.JLabel smallpf;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblEmployees;
    private javax.swing.JTable tblHistory;
    private javax.swing.JTable tblSYSem;
    private javax.swing.JTable tblSched;
    private javax.swing.JTable tblStudents;
    private javax.swing.JTable tblSubjects;
    private javax.swing.JTextField txtCurriculum;
    private javax.swing.JTextField txtEmpAddress;
    private javax.swing.JTextField txtEmpCellNo;
    private javax.swing.JTextField txtEmpFirstname;
    private javax.swing.JTextField txtEmpID;
    private javax.swing.JTextField txtEmpLastname;
    private javax.swing.JTextField txtRoom;
    private javax.swing.JTextField txtSY;
    private javax.swing.JTextField txtSem;
    private javax.swing.JTextField txtStuAddress;
    private javax.swing.JLabel txtStuCourseDesc;
    private javax.swing.JTextField txtStuFirstName;
    private javax.swing.JTextField txtStuLastName;
    private javax.swing.JTextField txtStuMidInitial;
    private javax.swing.JTextField txtStuPhone;
    private javax.swing.JTextField txtStudentNo;
    private javax.swing.JTextField txtSubjCode;
    private javax.swing.JTextField txtSubjDesc;
    private javax.swing.JTextField txtTime;
    private javax.swing.JTextField txtUnits;
    // End of variables declaration//GEN-END:variables
}
