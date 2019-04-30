package com.example.gerard.afinal.Login_SignUp;

import de.hdodenhof.circleimageview.CircleImageView;

public class NormalUser {
    private String user_id;
    private String username;
    private String usersurname;
    private String email;
    private String password;
    private String location;
    private String preferences;
    private String profile_picture;



    public NormalUser() {
    }

    public NormalUser(String user_id, String username, String usersurname, String email, String password, String location, String preferences, String profile_picture) {
        this.user_id = user_id;
        this.username = username;
        this.usersurname = usersurname;
        this.email = email;
        this.password = password;
        this.location = location;
        this.preferences = preferences;
        this.profile_picture = profile_picture;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsersurname() {
        return usersurname;
    }

    public void setUsersurname(String usersurname) {
        this.usersurname = usersurname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }
}
