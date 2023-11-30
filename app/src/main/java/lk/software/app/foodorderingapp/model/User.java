package lk.software.app.foodorderingapp.model;

public class User {
    private String full_name;
    private String email;
    private String phone;
    private String profile_img;

    public String getFull_name() {
        return full_name;
    }

    public User(String full_name, String email, String phone, String profile_img) {
        this.full_name = full_name;
        this.email = email;
        this.phone = phone;
        this.profile_img = profile_img;
    }

    public User() {
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }
}
