package info.androidhive.bottomnavigation;

public class UserData {

    private String FirstName,Mobno,Age;
    private String LastName,Address,Gender,Type;

    public UserData(){
        //Constructor
    }

    public  UserData(String FirstName,String LastName,String Address,String Gender,String Age,String Mobno,String Type)
    {
        this.FirstName=FirstName;
        this.LastName=LastName;
        this.Address= Address;
        this.Gender=Gender;
        this.Mobno=Mobno;
        this.Type=Type;
        this.Age=Age;
    }

    public String getAddress() {
        return Address;
    }

    public String getAge() {
        return Age;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getGender() {
        return Gender;
    }

    public String getLastName() {
        return LastName;
    }

    public String getMobno() {
        return Mobno;
    }

    public String getType() {
        return Type;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public void setAge(String age) {
        this.Age = age;
    }

    public void setFirstName(String firstName) {
        this.FirstName = firstName;
    }

    public void setGender(String gender) {
        this.Gender = gender;
    }

    public void setLastName(String lastName) {
        this.LastName = lastName;
    }

    public void setMobno(String mobno) {
        this.Mobno = mobno;
    }

    public void setType(String type) {
        this.Type = type;
    }
}