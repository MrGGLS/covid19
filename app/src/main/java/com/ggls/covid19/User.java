package com.ggls.covid19;


import androidx.annotation.NonNull;

public class User {
    private int id;
    private String name;
    private Status status;
    private String password;


    public User() {
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User(int id, String name,
                Status status, String userID, String password) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }

    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStatusWithString(String status) {
        switch (status) {
            case "Red":
                this.status = Status.RED;
                break;
            case "Green":
                this.status = Status.GREEN;
                break;
            case "Yellow":
                this.status = Status.YELLOW;
                break;
            default:
                this.status = null;
        }
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", password='" + password + '\'' +
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
    }
}