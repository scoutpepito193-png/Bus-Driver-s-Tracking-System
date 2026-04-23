package Model;

public class SuperAdmin
{
    private String publicID;
    private String password;
    private String firstName;
    private String lastName;
    private String contactNum;
    private String position;
    private String photoURL;
    
    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) {this.password = password; }
    
    public String getfirstName() { return firstName; }
    public void setfirstName(String firstName) { this.firstName = firstName; }
    
    public String getlastName() { return lastName; }
    public void setlastName(String lastName) { this.lastName = lastName; }
    
    public String getcontactNum() { return contactNum; }
    public void setcontactNum(String contactNum) { this.contactNum = contactNum; }
    
    public String getposition() { return position; }
    public void setposition(String position) { this.position = position; }
    
    public String getphotoURL() { return photoURL; }
    public void setphotoURL(String photoURL) { this.photoURL = photoURL; }
}
