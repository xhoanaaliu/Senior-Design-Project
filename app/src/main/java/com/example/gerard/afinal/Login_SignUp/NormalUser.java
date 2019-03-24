package com.example.gerard.afinal.Login_SignUp;

import de.hdodenhof.circleimageview.CircleImageView;

public class NormalUser {
    private String username;
    private String usersurname;
    private String email;
    private String password;
    private String location;
    private String preferences;
    private String profile_picture;
    private String user_key;


    public NormalUser() {
    }


    public NormalUser(String user_key, String username, String usersurname, String email, String password, String location, String preferences,String profile_picture) {
        this.user_key =user_key;
        this.username = username;
        this.usersurname = usersurname;
        this.email = email;
        this.password = password;
        this.location = location;
        this.preferences = preferences;
        this.profile_picture = profile_picture;
    }

    public String getusername() {
        return username;
    }

    public String getUser_key() {
        return user_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public String getusersurname() {
        return usersurname;
    }

    public void setusersurname(String usersurname) {
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
