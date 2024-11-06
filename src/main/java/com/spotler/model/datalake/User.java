package com.spotler.model.datalake;

public class User {
    private Integer userId;
    private String emailAddress;
    private String firstName;
    private String lastName;
    private Integer mainAccountId;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getMainAccountId() {
        return mainAccountId;
    }

    public void setMainAccountId(Integer mainAccountId) {
        this.mainAccountId = mainAccountId;
    }
}
