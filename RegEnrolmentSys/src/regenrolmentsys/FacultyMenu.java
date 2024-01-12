/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package regenrolmentsys;

import java.awt.Color;
import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author ariar
 */
public class FacultyMenu extends javax.swing.JPanel {

    /**
     * Creates new form FacultyMenu
     */
    
    private MainFrame mf = null;
    private String currentUser = "";
    private Connection con = null;
    private ResultSet rs = null;
    private PreparedStatement ps = null;
    private boolean cmbSubjCodeLoaded = false, cmbSubjCodeLoaded2 = false;
    
    /**
     * Creates new form AdminMenu
     */
    
    public FacultyMenu(MainFrame mf) {
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

    public FacultyMenu() {
        initComponents();
    }
    
    public void loadClassTab() {
        con = ConnectDB.connect();
        try {
            lblSubjDesc.setText("----------");
            
            //load SY combobox
            rs = con.prepareStatement("SELECT * FROM finals.SY").executeQuery();
            while (rs.next()) {
                cmbSY2.addItem(rs.getString("SY"));
            }
            
            //load semester combobox
            rs = con.prepareStatement("SELECT * FROM finals.SEMESTER").executeQuery();
            while (rs.next()) {
                cmbSem2.addItem(rs.getString("Semester"));
            }
            
            //load subject code combobox
            rs = con.prepareStatement("SELECT * FROM finals.SUBJECT ORDER BY subject_code").executeQuery();
            while (rs.next()) {
                cmbSubjCode2.addItem(rs.getString("subject_code"));
            }
            
            rs = con.prepareStatement("SELECT description FROM finals.SUBJECT "
                    + "WHERE subject_code = '" + cmbSubjCode2.getSelectedItem().toString() + "'").executeQuery();
            while (rs.next()) {
                lblSubjDesc.setText(rs.getString("description"));
            }
            
            cmbSubjCodeLoaded2 = true;
  
            //load block no combobox
            rs = con.prepareStatement("SELECT * FROM finals.BLOCK_NO").executeQuery();
            while (rs.next()) {
                cmbBlockNo2.addItem(rs.getString("block_no"));
            }
            
            loadClassTable();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private void loadClassTable(){
        con = ConnectDB.connect();
        try {
            rs = con.prepareStatement("SELECT * FROM finals.VWCLASSLIST").executeQuery(); 
            if (rs.next()) {
                rs = con.prepareStatement("SELECT * FROM finals.VWCLASSLIST").executeQuery(); 
                tblClassList.setModel(TableUtil.resultSetToTableModel(rs));
                TableUtil.resizeColumnWidth(tblClassList);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void loadGradesTab() {
        con = ConnectDB.connect();
        try {
            lblStudFirstname.setText("----------");
            lblStudLastname.setText("----------");
            lblStudMI.setText("-");
            lblStudGender.setText("-");
            lblCourseCode.setText("----");
            lblCourseDesc.setText("----------");
            lblStudEmail.setText("----------");
            lblStudStatus.setText("-----");
            
            //load comboboxes
            //load student no SY combobox
            rs = con.prepareStatement("SELECT * FROM finals.SY").executeQuery();
            while (rs.next()) {
                cmbStudentNoYear2.addItem(rs.getString("sy").substring(0, 4));
            }
            
            //load SY combobox
            rs = con.prepareStatement("SELECT * FROM finals.SY").executeQuery();
            while (rs.next()) {
                cmbSY.addItem(rs.getString("SY"));
            }
            
            //load semester combobox
            rs = con.prepareStatement("SELECT * FROM finals.SEMESTER").executeQuery();
            while (rs.next()) {
                cmbSem.addItem(rs.getString("Semester"));
            }
            
            //load subject code combobox
            rs = con.prepareStatement("SELECT * FROM finals.SUBJECT ORDER BY subject_code").executeQuery();
            while (rs.next()) {
                cmbSubjCode.addItem(rs.getString("subject_code"));
            }
            
            rs = con.prepareStatement("SELECT description FROM finals.SUBJECT "
                    + "WHERE subject_code = '" + cmbSubjCode.getSelectedItem().toString() + "'").executeQuery();
            while (rs.next()) {
                txtSubjDesc.setText(rs.getString("description"));
            }
            
            cmbSubjCodeLoaded = true;
  
            //load block no combobox
            rs = con.prepareStatement("SELECT * FROM finals.BLOCK_NO").executeQuery();
            while (rs.next()) {
                cmbBlockNo.addItem(rs.getString("block_no"));
            }
            
            loadGradesTable();
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private void loadGradesTable() {
        con = ConnectDB.connect();
        try {
            rs = con.prepareStatement("SELECT * FROM finals.GRADE").executeQuery(); 
            if (rs.next()) {
                rs = con.prepareStatement("SELECT * FROM finals.GRADE").executeQuery(); 
                tblGrades.setModel(TableUtil.resultSetToTableModel(rs));
                TableUtil.resizeColumnWidth(tblGrades);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private void resetClassTable() {
        tblClassList.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {},
                new String [] {"Select the sy, semester, subject code, and block no."}
                ) {
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return false;
                    }
                });
    }
    
    private void resetGradesTable() {
        tblGrades.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {},
                new String [] {"No grade records found."}
                ) {
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return false;
                    }
                });
    }
    
    public void toggleSelected(int index) {
        javax.swing.JPanel covers[] = {select1, select2}; //just fill with the cover JPanels for other menus
        for (int i=0; i<covers.length; ++i) {
            if (i == index) 
                covers[i].setVisible(true);
            else
                covers[i].setVisible(false);
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
        pficon4 = new javax.swing.JLabel();
        pficon = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        btnClassList = new javax.swing.JButton();
        btnGrades = new javax.swing.JButton();
        select2 = new javax.swing.JPanel();
        select1 = new javax.swing.JPanel();
        pficon5 = new javax.swing.JLabel();
        btnBackStudentMenu = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        tabs = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblClassList = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        cmbSubjCode2 = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        btnClassSearch = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        lblSubjDesc = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cmbBlockNo2 = new javax.swing.JComboBox<>();
        jLabel35 = new javax.swing.JLabel();
        cmbSY2 = new javax.swing.JComboBox<>();
        jLabel36 = new javax.swing.JLabel();
        cmbSem2 = new javax.swing.JComboBox<>();
        plmbg = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        cmbStudentNoYear2 = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        txtStudentNo2 = new javax.swing.JTextField();
        btnGradeStudSearch = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        lblCourseDesc = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        lblStudStatus = new javax.swing.JLabel();
        lblStudFirstname = new javax.swing.JLabel();
        lblStudLastname = new javax.swing.JLabel();
        lblStudMI = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        lblStudGender = new javax.swing.JLabel();
        lblCourseCode = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        cmbSem = new javax.swing.JComboBox<>();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        cmbSY = new javax.swing.JComboBox<>();
        jLabel31 = new javax.swing.JLabel();
        cmbBlockNo = new javax.swing.JComboBox<>();
        txtSubjDesc = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        cmbSubjCode = new javax.swing.JComboBox<>();
        jLabel33 = new javax.swing.JLabel();
        txtGrade = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblGrades = new javax.swing.JTable();
        btnGradeAdd = new javax.swing.JButton();
        btnGradeDelete = new javax.swing.JButton();
        btnGradeEdit = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        lblStudEmail = new javax.swing.JLabel();
        plmbg1 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        smallpf = new javax.swing.JLabel();
        PLMLogo = new javax.swing.JLabel();
        MinimizeBTN = new javax.swing.JButton();
        CloseBTN = new javax.swing.JButton();
        MainLBL1 = new javax.swing.JLabel();
        NameTopBar = new javax.swing.JLabel();

        setBackground(new java.awt.Color(230, 68, 68));
        setPreferredSize(new java.awt.Dimension(1243, 720));
        setToolTipText("");

        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setDividerSize(0);

        jPanel1.setBackground(new java.awt.Color(230, 68, 68));
        jPanel1.setLayout(null);

        pficon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/smallpfside.png"))); // NOI18N
        jPanel1.add(pficon1);
        pficon1.setBounds(20, 30, 40, 40);

        pficon4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/smallgrades.png"))); // NOI18N
        jPanel1.add(pficon4);
        pficon4.setBounds(20, 90, 40, 60);

        pficon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/smallLogout.png"))); // NOI18N
        jPanel1.add(pficon);
        pficon.setBounds(20, 560, 40, 50);

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
        btnLogout.setBounds(3, 570, 200, 28);

        btnClassList.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        btnClassList.setForeground(new java.awt.Color(255, 255, 255));
        btnClassList.setText("Class List");
        btnClassList.setBorder(null);
        btnClassList.setContentAreaFilled(false);
        btnClassList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnClassListMouseClicked(evt);
            }
        });
        btnClassList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClassListActionPerformed(evt);
            }
        });
        jPanel1.add(btnClassList);
        btnClassList.setBounds(-4, 19, 210, 70);

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
        btnGrades.setBounds(0, 87, 200, 70);

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

        pficon5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/backsmall.png"))); // NOI18N
        jPanel1.add(pficon5);
        pficon5.setBounds(20, 500, 40, 50);

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

        jSplitPane1.setLeftComponent(jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        tabs.setTabPlacement(javax.swing.JTabbedPane.RIGHT);
        tabs.setToolTipText("");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        tblClassList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No existing class records found"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblClassList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClassListMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblClassList);

        jLabel1.setText("Subject Code:");

        cmbSubjCode2.setToolTipText("");
        cmbSubjCode2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSubjCode2ActionPerformed(evt);
            }
        });

        jLabel11.setText("-");

        btnClassSearch.setText("Search");
        btnClassSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClassSearchActionPerformed(evt);
            }
        });

        jLabel34.setText("CLASS LIST");

        lblSubjDesc.setText("----");

        jLabel2.setText("Block No:");

        cmbBlockNo2.setToolTipText("");
        cmbBlockNo2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbBlockNo2ActionPerformed(evt);
            }
        });

        jLabel35.setText("SY:");

        cmbSY2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSY2ActionPerformed(evt);
            }
        });

        jLabel36.setText("Semester: ");

        plmbg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/plm.png"))); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(370, 370, 370)
                        .addComponent(jLabel34))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(357, 357, 357)
                        .addComponent(btnClassSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbBlockNo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(cmbSubjCode2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblSubjDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel35)
                                .addGap(18, 18, 18)
                                .addComponent(cmbSY2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel36)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbSem2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1056, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(44, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addComponent(plmbg, javax.swing.GroupLayout.PREFERRED_SIZE, 1078, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 28, Short.MAX_VALUE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel34)
                .addGap(56, 56, 56)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbSem2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35)
                    .addComponent(jLabel36)
                    .addComponent(cmbSY2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbSubjCode2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(lblSubjDesc))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbBlockNo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(btnClassSearch)
                .addGap(21, 21, 21)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(plmbg, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        tabs.addTab("", jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setText("Student No. :");

        cmbStudentNoYear2.setToolTipText("");
        cmbStudentNoYear2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbStudentNoYear2ActionPerformed(evt);
            }
        });

        jLabel14.setText("-");

        btnGradeStudSearch.setText("Search");
        btnGradeStudSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGradeStudSearchActionPerformed(evt);
            }
        });

        jLabel15.setText("First Name :");

        jLabel16.setText("Last Name:");

        jLabel17.setText("Middle Inital : ");

        lblCourseDesc.setText("----------");

        jLabel18.setText("Course :");

        jLabel19.setText("Status :");

        lblStudStatus.setText("---");

        lblStudFirstname.setText("----------");

        lblStudLastname.setText("----------");

        lblStudMI.setText("----------");

        jLabel24.setText("Gender :");

        lblStudGender.setText("---");

        lblCourseCode.setText("----");

        jLabel27.setText("-");

        jLabel28.setText("GRADE RECORDS");

        jLabel29.setText("SY:");

        jLabel30.setText("Semester: ");

        cmbSY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSYActionPerformed(evt);
            }
        });

        jLabel31.setText("Subject Code:");

        cmbBlockNo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbBlockNoItemStateChanged(evt);
            }
        });
        cmbBlockNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbBlockNoActionPerformed(evt);
            }
        });

        txtSubjDesc.setText("Subject Description");

        jLabel32.setText("Block No:");

        cmbSubjCode.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSubjCodeItemStateChanged(evt);
            }
        });
        cmbSubjCode.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                cmbSubjCodeCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        cmbSubjCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSubjCodeActionPerformed(evt);
            }
        });

        jLabel33.setText("Grade:");

        txtGrade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGradeActionPerformed(evt);
            }
        });
        txtGrade.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGradeKeyTyped(evt);
            }
        });

        tblGrades.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblGrades.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGradesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblGrades);

        btnGradeAdd.setText("ADD");
        btnGradeAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGradeAddActionPerformed(evt);
            }
        });

        btnGradeDelete.setText("DELETE");
        btnGradeDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGradeDeleteActionPerformed(evt);
            }
        });

        btnGradeEdit.setText("UPDATE");
        btnGradeEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGradeEditActionPerformed(evt);
            }
        });

        jLabel20.setText("Email :");

        lblStudEmail.setText("----------");

        plmbg1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/plm.png"))); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel15)
                                                    .addComponent(jLabel16))
                                                .addGap(24, 24, 24)
                                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(lblStudLastname)
                                                    .addComponent(lblStudFirstname))
                                                .addGap(294, 294, 294)
                                                .addComponent(jLabel18))
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                                        .addComponent(jLabel17)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(lblStudMI))
                                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                                        .addComponent(jLabel13)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(cmbStudentNoYear2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jLabel14)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(txtStudentNo2, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addGap(14, 14, 14)
                                                                .addComponent(btnGradeStudSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                            .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addGap(156, 156, 156)
                                                                .addComponent(jLabel20)))
                                                        .addGap(3, 3, 3))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jLabel19)))))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addComponent(lblCourseCode)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lblCourseDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(lblStudStatus)
                                            .addComponent(lblStudEmail)))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel24)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lblStudGender))))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(395, 395, 395)
                                .addComponent(jLabel28))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel29)
                                .addGap(18, 18, 18)
                                .addComponent(cmbSY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel30)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbSem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 243, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel32)
                                    .addComponent(jLabel33))
                                .addGap(29, 29, 29)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cmbBlockNo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtGrade))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel31)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbSubjCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(btnGradeAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(120, 120, 120)
                                        .addComponent(btnGradeEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(124, 124, 124)
                                        .addComponent(btnGradeDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(txtSubjDesc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 833, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addComponent(plmbg1, javax.swing.GroupLayout.PREFERRED_SIZE, 1078, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 28, Short.MAX_VALUE)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel28)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(cmbStudentNoYear2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtStudentNo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(btnGradeStudSearch))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(lblStudFirstname)
                    .addComponent(jLabel18)
                    .addComponent(lblCourseDesc)
                    .addComponent(lblCourseCode)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(lblStudLastname)
                    .addComponent(jLabel20)
                    .addComponent(lblStudEmail))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(lblStudMI)
                    .addComponent(jLabel19)
                    .addComponent(lblStudStatus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(lblStudGender))
                .addGap(37, 37, 37)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbSem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29)
                    .addComponent(jLabel30)
                    .addComponent(cmbSY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(txtSubjDesc)
                    .addComponent(cmbSubjCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(cmbBlockNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(txtGrade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGradeAdd)
                    .addComponent(btnGradeDelete)
                    .addComponent(btnGradeEdit))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(59, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(plmbg1, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        tabs.addTab("", jPanel5);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 1131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 684, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jSplitPane1.setRightComponent(jPanel2);

        jPanel7.setBackground(new java.awt.Color(254, 86, 86));
        jPanel7.setLayout(null);

        smallpf.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        smallpf.setForeground(new java.awt.Color(255, 255, 255));
        smallpf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/smallprofile.png"))); // NOI18N
        jPanel7.add(smallpf);
        smallpf.setBounds(1040, 0, 40, 30);

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

        MainLBL1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        MainLBL1.setForeground(new java.awt.Color(255, 255, 255));
        MainLBL1.setText("Enrollment System for Regular Students");
        jPanel7.add(MainLBL1);
        MainLBL1.setBounds(40, 0, 290, 30);

        NameTopBar.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        NameTopBar.setForeground(new java.awt.Color(255, 255, 255));
        NameTopBar.setText(" Name | Faculty");
        jPanel7.add(NameTopBar);
        NameTopBar.setBounds(1060, 0, 120, 30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1309, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 995, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        // TODO add your handling code here:
        mf.setUserID("");
        mf.switchCard("LoginCard");
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnClassListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClassListMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnClassListMouseClicked

    private void btnClassListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClassListActionPerformed
        // TODO add your handling code here:
        
        tabs.setSelectedIndex(0);
        toggleSelected(0);
        loadClassTab();
    }//GEN-LAST:event_btnClassListActionPerformed

    private void btnGradesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGradesActionPerformed
        // TODO add your handling code here:
        tabs.setSelectedIndex(1);
        toggleSelected(1);
        loadGradesTab();
    }//GEN-LAST:event_btnGradesActionPerformed

    private void cmbStudentNoYear2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbStudentNoYear2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbStudentNoYear2ActionPerformed

    private void btnGradeStudSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGradeStudSearchActionPerformed
        // TODO add your handling code here:
        con = ConnectDB.connect();
        try {
            String selectedStudentNo = cmbStudentNoYear2.getSelectedItem().toString() + "-" + txtStudentNo2.getText();
            rs = con.prepareStatement("SELECT * FROM finals.VWGRADE_STUDENTINFO WHERE student_no = '" + selectedStudentNo + "'").executeQuery();
            if (rs.next()) {
                lblStudFirstname.setText(rs.getString("first_name"));
                lblStudLastname.setText(rs.getString("last_name"));
                lblStudMI.setText(rs.getString("mi"));
                lblStudEmail.setText(rs.getString("email"));
                if(rs.getString("gender").equals("M"))
                    lblStudGender.setText("MALE");
                else
                    lblStudGender.setText("FEMALE");
                if(rs.getString("status").equals("A"))
                    lblStudStatus.setText("ACTIVE");
                else
                    lblStudStatus.setText("INACTIVE");
                lblCourseCode.setText(rs.getString("course_code"));
                lblCourseDesc.setText(rs.getString("course_desc"));
            } else {
                JOptionPane.showMessageDialog(null, "Student not found", "ERROR", JOptionPane.ERROR_MESSAGE);
                lblStudFirstname.setText("----------");
                lblStudLastname.setText("----------");
                lblStudMI.setText("-");
                lblStudGender.setText("-");
                lblCourseCode.setText("----");
                lblCourseDesc.setText("----------");
                lblStudEmail.setText("----------");
                lblStudStatus.setText("-----");
            }
            
            rs = con.prepareStatement("SELECT * FROM finals.GRADE WHERE student_no = '" + selectedStudentNo + "'").executeQuery();
            System.out.println(selectedStudentNo);
            if (rs.next()) {
                rs = con.prepareStatement("SELECT * FROM finals.GRADE WHERE student_no = '" + selectedStudentNo + "'").executeQuery();
                tblGrades.setModel(TableUtil.resultSetToTableModel(rs));
                TableUtil.resizeColumnWidth(tblGrades);
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_btnGradeStudSearchActionPerformed

    private void cmbSYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSYActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSYActionPerformed

    private void cmbBlockNoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbBlockNoItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbBlockNoItemStateChanged

    private void cmbBlockNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbBlockNoActionPerformed

    }//GEN-LAST:event_cmbBlockNoActionPerformed

    private void cmbSubjCodeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbSubjCodeItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSubjCodeItemStateChanged

    private void cmbSubjCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSubjCodeActionPerformed
        // TODO add your handling code here:
        //display subject desc of selected subj
        con = ConnectDB.connect();
        if (!cmbSubjCodeLoaded)
            return;
        try {
            rs = con.prepareStatement("SELECT description FROM finals.SUBJECT WHERE subject_code = '" + cmbSubjCode.getSelectedItem().toString() + "'").executeQuery();
            if (rs.next())
                txtSubjDesc.setText(rs.getString("description"));
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_cmbSubjCodeActionPerformed

    private void txtGradeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGradeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGradeActionPerformed

    private void btnGradeAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGradeAddActionPerformed
        // TODO add your handling code here:
        con = ConnectDB.connect();
        String selectedStudntNo = cmbStudentNoYear2.getSelectedItem().toString() + "-" + txtStudentNo2.getText();
        int intAnswer = JOptionPane.showConfirmDialog(null, "Add a record?", "Adding Record", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (intAnswer == 0) {
            try {
                ps = con.prepareStatement("SELECT status FROM finals.ENROLLED_SUBJECT "
                        + "WHERE SY = ? AND semester = ? AND student_No = ? AND subject_code = ? AND block_no = ?");
                    ps.setString(1, cmbSY.getSelectedItem().toString());
                    ps.setString(2, cmbSem.getSelectedItem().toString());
                    ps.setString(3, selectedStudntNo);
                    ps.setString(4, cmbSubjCode.getSelectedItem().toString());
                    ps.setString(5,cmbBlockNo.getSelectedItem().toString());
                rs = ps.executeQuery();
                if (rs.next()) {
                    if (rs.getString("status").equals("Enrolled")) {
                        ps = con.prepareStatement("INSERT INTO finals.GRADE VALUES (?,?,?,?,?,?)");
                        ps.setString(1, cmbSY.getSelectedItem().toString());
                        ps.setString(2, cmbSem.getSelectedItem().toString());
                        ps.setString(3, selectedStudntNo);
                        ps.setString(4, cmbSubjCode.getSelectedItem().toString());
                        ps.setString(5,cmbBlockNo.getSelectedItem().toString());
                        ps.setDouble(6, Double.parseDouble(txtGrade.getText()));
                        ps.execute();
                        loadGradesTable();
                        
                        if (Double.parseDouble(txtGrade.getText()) > 3.00) {
                            ps = con.prepareStatement("UPDATE finals.ENROLLED_SUBJECT "
                            + "SET status = 'Failed' "
                            + "WHERE SY = ? AND semester = ? AND student_No = ? AND subject_code = ? AND block_no = ?");
                        } else {
                            ps = con.prepareStatement("UPDATE finals.ENROLLED_SUBJECT "
                            + "SET status = 'Finished' "
                            + "WHERE SY = ? AND semester = ? AND student_No = ? AND subject_code = ? AND block_no = ?");
                        }
                        
                        ps.setString(1, cmbSY.getSelectedItem().toString());
                        ps.setString(2, cmbSem.getSelectedItem().toString());
                        ps.setString(3, selectedStudntNo);
                        ps.setString(4, cmbSubjCode.getSelectedItem().toString());
                        ps.setString(5, cmbBlockNo.getSelectedItem().toString());
                        ps.execute(); 
                        
                        JOptionPane.showMessageDialog(null, "Added record successfully.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Student is already graded", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Cannot enter grade to a subject the student is not enrolled to", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }//GEN-LAST:event_btnGradeAddActionPerformed

    private void btnGradeDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGradeDeleteActionPerformed
        // TODO add your handling code here:
        con = ConnectDB.connect();
        String selectedStudntNo = cmbStudentNoYear2.getSelectedItem().toString() + "-" + txtStudentNo2.getText();
        int intAnswer = JOptionPane.showConfirmDialog(null, "Delete a record?", "Deleting Record", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (intAnswer == 0) {
            try {
                ps = con.prepareStatement("DELETE FROM finals.GRADE "
                    + "WHERE sy = '" + cmbSY.getSelectedItem().toString() + "' AND "
                    + "semester = '" + cmbSem.getSelectedItem().toString() + "' AND "
                    + "student_no = '" + selectedStudntNo + "' AND "
                    + "subject_code = '" + cmbSubjCode.getSelectedItem().toString() + "'");
                ps.execute();
                
                ps = con.prepareStatement("UPDATE finals.ENROLLED_SUBJECT "
                    + "SET status = 'Enrolled' "
                    + "WHERE SY = ? AND semester = ? AND student_No = ? AND subject_code = ? AND block_no = ?");
                    ps.setString(1, cmbSY.getSelectedItem().toString());
                    ps.setString(2, cmbSem.getSelectedItem().toString());
                    ps.setString(3, selectedStudntNo);
                    ps.setString(4, cmbSubjCode.getSelectedItem().toString());
                    ps.setString(5, cmbBlockNo.getSelectedItem().toString());
                int rowsAffected = ps.executeUpdate();
                    
                if (rowsAffected > 0)
                    JOptionPane.showMessageDialog(null, "Deleted record successfully.");
                else
                    JOptionPane.showMessageDialog(null, "Record does not exist", "ERROR", JOptionPane.ERROR_MESSAGE);

                loadGradesTable();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }//GEN-LAST:event_btnGradeDeleteActionPerformed

    private void btnGradeEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGradeEditActionPerformed
        // TODO add your handling code here:
        
        con = ConnectDB.connect();
        String selectedStudntNo = cmbStudentNoYear2.getSelectedItem().toString() + "-" + txtStudentNo2.getText();
        int intAnswer = JOptionPane.showConfirmDialog(null, "Update a record?", "Updating Record", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (intAnswer == 0) {
            try {
                
                if (Double.parseDouble(txtGrade.getText()) < 1.00 || Double.parseDouble(txtGrade.getText()) > 5.00 ) {
                    JOptionPane.showMessageDialog(null, "Invalid Grade", "ERROR", JOptionPane.ERROR_MESSAGE);
                } else {
                    ps = con.prepareStatement("UPDATE finals.GRADE "
                    + "SET grade = " + txtGrade.getText().toString() + " "
                    + "WHERE student_no = '" + selectedStudntNo + "' AND "
                    + "sy = '" + cmbSY.getSelectedItem().toString() + "' AND "
                    + "semester = '" + cmbSem.getSelectedItem().toString() + "' AND "
                    + "subject_code = '" + cmbSubjCode.getSelectedItem().toString() + "' AND "
                    + "block_no = '" + cmbBlockNo.getSelectedItem().toString() + "'");
                    int rowsAffected = ps.executeUpdate();
                    
                    if (rowsAffected > 0)
                        JOptionPane.showMessageDialog(null, "Updated record successfully.");
                    else
                        JOptionPane.showMessageDialog(null, "Only grade values can be modified", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                loadGradesTable();  
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }//GEN-LAST:event_btnGradeEditActionPerformed

    private void tblClassListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClassListMouseClicked
        int intRow = tblClassList.getSelectedRow(); 
        con = ConnectDB.connect();
        
        try {          
            cmbSY2.setSelectedItem(tblClassList.getValueAt(intRow, 0).toString());
            cmbSem2.setSelectedItem(tblClassList.getValueAt(intRow, 1).toString());
            cmbSubjCode2.setSelectedItem(tblClassList.getValueAt(intRow, 6).toString());
            rs = con.prepareStatement("SELECT * FROM finals.SUBJECT "
                    + "WHERE subject_code = '" + cmbSubjCode2.getSelectedItem().toString() + "'").executeQuery();
            while (rs.next())
                lblSubjDesc.setText(rs.getString("description"));
            cmbBlockNo2.setSelectedItem(tblClassList.getValueAt(intRow,  7).toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_tblClassListMouseClicked

    private void cmbSubjCode2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSubjCode2ActionPerformed
        // TODO add your handling code here:
        con = ConnectDB.connect();
        if (!cmbSubjCodeLoaded2)
            return;
        try {
            rs = con.prepareStatement("SELECT description FROM finals.SUBJECT WHERE subject_code = '" + cmbSubjCode.getSelectedItem().toString() + "'").executeQuery();
            if (rs.next())
                lblSubjDesc.setText(rs.getString("description"));
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_cmbSubjCode2ActionPerformed

    private void btnClassSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClassSearchActionPerformed
        con = ConnectDB.connect();
        try {
            ps = con.prepareStatement ("SELECT * FROM finals.vwCLASSLIST WHERE "
                    + "SY = '" + cmbSY2.getSelectedItem().toString() + "' AND "
                    + "SEMESTER = '" + cmbSem2.getSelectedItem().toString() + "' AND "
                    + "\"SUBJECT CODE\" = '" + cmbSubjCode2.getSelectedItem().toString() + "' AND "
                    + "\"BLOCK NO\" = '" + cmbBlockNo2.getSelectedItem().toString() + "'");
            rs = ps.executeQuery();
            if (rs.next()) {
                rs = ps.executeQuery();
                tblClassList.setModel(TableUtil.resultSetToTableModel(rs));
                TableUtil.resizeColumnWidth(tblGrades);
            } else {
                JOptionPane.showMessageDialog(null, "No class records", "ERROR", JOptionPane.ERROR_MESSAGE);
                loadClassTable(); 
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_btnClassSearchActionPerformed

    private void cmbBlockNo2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbBlockNo2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbBlockNo2ActionPerformed

    private void txtGradeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGradeKeyTyped
        // TODO add your handling code here:
        // set input restriction to accept float only
        if (Character.isLetter(evt.getKeyChar()))
            evt.consume();
        else {
            try {
                Double.parseDouble(txtGrade.getText()+evt.getKeyChar());
            } catch (NumberFormatException e) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtGradeKeyTyped

    private void tblGradesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGradesMouseClicked
        // TODO add your handling code here:
        int intRow = tblGrades.getSelectedRow();
        String selectedStudentNo = tblGrades.getValueAt(intRow, 2).toString();
        cmbStudentNoYear2.setSelectedItem(selectedStudentNo.substring(0, 4));
        txtStudentNo2.setText(selectedStudentNo.substring(5));
        
        con = ConnectDB.connect();
        
        try {
            //reflect table selection on student info
            rs = con.prepareStatement("SELECT * FROM finals.VWGRADE_STUDENTINFO WHERE student_no = '" + selectedStudentNo + "'").executeQuery();
            if (rs.next()) {
                lblStudFirstname.setText(rs.getString("first_name"));
                lblStudLastname.setText(rs.getString("last_name"));
                lblStudMI.setText(rs.getString("mi"));
                lblStudEmail.setText(rs.getString("email"));
                if(rs.getString("gender").equals("M"))
                    lblStudGender.setText("MALE");
                else
                    lblStudGender.setText("FEMALE");
                if(rs.getString("status").equals("A"))
                    lblStudStatus.setText("ACTIVE");
                else
                    lblStudStatus.setText("INACTIVE");
                lblCourseCode.setText(rs.getString("course_code"));
                lblCourseDesc.setText(rs.getString("course_desc"));
            }
            
            cmbSY.setSelectedItem(tblGrades.getValueAt(intRow, 0).toString());
            cmbSem.setSelectedItem(tblGrades.getValueAt(intRow, 1).toString());
            cmbSubjCode.setSelectedItem(tblGrades.getValueAt(intRow, 3).toString());
            rs = con.prepareStatement("SELECT * FROM finals.SUBJECT "
                    + "WHERE subject_code = '" + cmbSubjCode.getSelectedItem().toString() + "'").executeQuery();
            while (rs.next())
                txtSubjDesc.setText(rs.getString("description"));
            cmbBlockNo.setSelectedItem(tblGrades.getValueAt(intRow, 4).toString());
            txtGrade.setText(tblGrades.getValueAt(intRow, 5).toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_tblGradesMouseClicked

    private void cmbSubjCodeCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_cmbSubjCodeCaretPositionChanged
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cmbSubjCodeCaretPositionChanged

    private void cmbSY2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSY2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSY2ActionPerformed

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

    private void btnBackStudentMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackStudentMenuActionPerformed
        // TODO add your handling code here:
        mf.switchCard("AdminHomeCard");
    }//GEN-LAST:event_btnBackStudentMenuActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CloseBTN;
    private javax.swing.JLabel MainLBL1;
    private javax.swing.JButton MinimizeBTN;
    private javax.swing.JLabel NameTopBar;
    private javax.swing.JLabel PLMLogo;
    private javax.swing.JButton btnBackStudentMenu;
    private javax.swing.JButton btnClassList;
    private javax.swing.JButton btnClassSearch;
    private javax.swing.JButton btnGradeAdd;
    private javax.swing.JButton btnGradeDelete;
    private javax.swing.JButton btnGradeEdit;
    private javax.swing.JButton btnGradeStudSearch;
    private javax.swing.JButton btnGrades;
    private javax.swing.JButton btnLogout;
    private javax.swing.JComboBox<String> cmbBlockNo;
    private javax.swing.JComboBox<String> cmbBlockNo2;
    private javax.swing.JComboBox<String> cmbSY;
    private javax.swing.JComboBox<String> cmbSY2;
    private javax.swing.JComboBox<String> cmbSem;
    private javax.swing.JComboBox<String> cmbSem2;
    private javax.swing.JComboBox<String> cmbStudentNoYear2;
    private javax.swing.JComboBox<String> cmbSubjCode;
    private javax.swing.JComboBox<String> cmbSubjCode2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel lblCourseCode;
    private javax.swing.JLabel lblCourseDesc;
    private javax.swing.JLabel lblStudEmail;
    private javax.swing.JLabel lblStudFirstname;
    private javax.swing.JLabel lblStudGender;
    private javax.swing.JLabel lblStudLastname;
    private javax.swing.JLabel lblStudMI;
    private javax.swing.JLabel lblStudStatus;
    private javax.swing.JLabel lblSubjDesc;
    private javax.swing.JLabel pficon;
    private javax.swing.JLabel pficon1;
    private javax.swing.JLabel pficon4;
    private javax.swing.JLabel pficon5;
    private javax.swing.JLabel plmbg;
    private javax.swing.JLabel plmbg1;
    private javax.swing.JPanel select1;
    private javax.swing.JPanel select2;
    private javax.swing.JLabel smallpf;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblClassList;
    private javax.swing.JTable tblGrades;
    private javax.swing.JTextField txtGrade;
    private javax.swing.JTextField txtStudentNo2;
    private javax.swing.JLabel txtSubjDesc;
    // End of variables declaration//GEN-END:variables
}
