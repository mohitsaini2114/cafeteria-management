package com.msaini.cafetriamanagementsystem;

import java.io.Serializable;

public class StudentDetails implements Serializable{

    public String studentId;
    public String password, emailId;
    public String name;
    public String ninerWallet;
    public String studentKey;

    /*public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
*/
   // String userID;

    public String getNinerWallet() {
        return ninerWallet;
    }

    public void setNinerWallet(String ninerWallet) {
        this.ninerWallet = ninerWallet;
    }



    public StudentDetails() {
    }

   public StudentDetails(String studentId, String password, String emailId, String name, String ninerWallet) {
        this.studentId = studentId;
       this.password = password;
        this.emailId = emailId;
        this.name = name;
        this.ninerWallet = ninerWallet;
    }

    public StudentDetails(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

  /*  @Override
    public String toString() {
        return "StudentDetails{" +
                "studentId=" + studentId +
                ", password='" + password + '\'' +
                ", emailId='" + emailId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }*/
}
