package com.example.arnesh07.help;

public class UserEmails {
    String email1,email2,email3,email4,email5;

    public UserEmails(String email1, String email2, String email3, String email4, String email5) {
        this.email1 = email1;
        this.email2 = email2;
        this.email3 = email3;
        this.email4 = email4;
        this.email5 = email5;
    }

    public UserEmails() {
        //default constructor
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public void setEmail3(String email3) {
        this.email3 = email3;
    }

    public void setEmail4(String email4) {
        this.email4 = email4;
    }

    public void setEmail5(String email5) {
        this.email5 = email5;
    }

    public String getEmail1() {
        return email1;
    }

    public String getEmail2() {
        return email2;
    }

    public String getEmail3() {
        return email3;
    }

    public String getEmail4() {
        return email4;
    }

    public String getEmail5() {
        return email5;
    }
}
