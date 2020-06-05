package com.example.getsoftadmin;

class NotificationDetail {
    private String Name,RequestedUser,RequestedUserId;

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
    public String getRequestedUserId() {
        return RequestedUserId;
    }

    public NotificationDetail() {
    }

    public NotificationDetail(String name, String requestedUser ,String RequestedUserId) {
        this.Name = name;
        this.RequestedUserId=RequestedUserId;
        this.RequestedUser = requestedUser;
    }
}
