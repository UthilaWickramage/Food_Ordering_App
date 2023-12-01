package lk.software.app.foodorderingapp.model;

public class User {
    private String full_name;
    private String email;
    private String phone;
    private String profile_img;
    private String register_date;
    private String register_time;

    public String getFull_name() {
        return full_name;
    }



    public User() {
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getRegister_date() {
        return register_date;
    }

    public void setRegister_date(String register_date) {
        this.register_date = register_date;
    }

    public String getRegister_time() {
        return register_time;
    }

    public void setRegister_time(String register_time) {
        this.register_time = register_time;
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
