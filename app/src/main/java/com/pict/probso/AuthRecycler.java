package info.androidhive.bottomnavigation;

public class AuthRecycler {

    private String title,Address;

    public AuthRecycler() {
    }

    public AuthRecycler(String title, String address) {
        this.title = title;
        Address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
