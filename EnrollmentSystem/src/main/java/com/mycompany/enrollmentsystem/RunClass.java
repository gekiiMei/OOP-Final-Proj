package com.mycompany.enrollmentsystem;


import javax.swing.JFrame;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Dennis Jr
 */
public class RunClass {
       public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        LogInUI loginScreen = new LogInUI();
        f.add(loginScreen);
        //f.setUndecorated(true);
        f.pack();
        f.setVisible(true);
        f.setResizable(false);
        

       
    }
}

