package com.example.getsoftadmin;

class NotificationDetail {
    private String Name,RequestedUser;

    public void setName(String name) {
        this.Name = name;
    }

    public void setRequestedUser(String requestedUser) {
        this.RequestedUser = requestedUser;
    }

    public String getName() {
        return Name;
    }

    public String getRequestedUser() {
        return RequestedUser;
    }

    public NotificationDetail() {
    }

    public NotificationDetail(String name, String requestedUser) {
        this.Name = name;
        this.RequestedUser = requestedUser;
    }
}
