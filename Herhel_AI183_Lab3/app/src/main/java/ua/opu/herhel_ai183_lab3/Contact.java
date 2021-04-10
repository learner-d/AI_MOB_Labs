package ua.opu.herhel_ai183_lab3;

import android.net.Uri;

public class Contact {

    private String name;
    private String email;
    private String phone;
    private Uri uri;

    public Contact(String name, String email, String phone, Uri uri) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}