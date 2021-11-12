package com.ggls.covid19;


import androidx.annotation.NonNull;

public class User {
    private long id;
    private String name;
    private String travelMapID;
    private Status status;
    private String userID;
    private String password;


    public User() {
    }

    public User(long id, String name, String travelMapID,
                Status status, String userID, String password) {
        this.id = id;
        this.name = name;
        this.travelMapID = travelMapID;
        this.status = status;
        this.userID = userID;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTravelMapID() {
        return travelMapID;
    }

    public Status getStatus() {
        return status;
    }

    public String getUserID() {
        return userID;
    }

    public String getPassword() {
        return password;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTravelMapID(String travelMapID) {
        this.travelMapID = travelMapID;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                " , name='" + name + '\'' +
                " , travelMapID='" + travelMapID + '\'' +
                " , status=" + status.toString() + '\'' +
                " , userID='" + userID + '\'' +
                " , password='" + password + '\'' +
                '}';
    }
}

enum Status {
    RED {
        @NonNull
        @Override
        public String toString() {
            return "Red";
        }
    },
    YELLOW {
        @NonNull
        @Override
        public String toString() {
            return "Yellow";
        }
    },
    GREEN {
        @NonNull
        @Override
        public String toString() {
            return "Green";
        }
    };

    @NonNull
    @Override
    public abstract String toString();
}