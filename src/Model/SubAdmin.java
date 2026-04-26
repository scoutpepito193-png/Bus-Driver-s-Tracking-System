package Model;

import java.time.LocalDate;
        
public class SubAdmin
{    
    private String public_sub_id;
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate dateOfBirth;
    private String address;
    private String contactNum;
    private String photoURL;
    private String password;
    private String position;
    
    public String getpublic_sub_id() { return public_sub_id; }
    public void setpublic_sub_id(String public_sub_id) { this.public_sub_id = public_sub_id; }
    
    public String getfirstName() { return firstName; }
    public void setfirstName(String firstName) { this.firstName = firstName; }
    
    public String getlastName() { return lastName; }
    public void setlastName(String lastName) { this.lastName = lastName; }
    
    public String getgender() { return gender; }
    public void setgender(String gender) { this.gender = gender; }
    
    public LocalDate getdateOfBirth() { return dateOfBirth; }
    public void setdateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public String getaddress() { return address; }
    public void setaddress(String address) { this.address = address; }

    public String getcontactNum() { return contactNum; }
    public void setcontactnum(String contactNum) { this.contactNum = contactNum; }
    
    public String getphotoURL() { return photoURL; }
    public void setphotoURL(String photoURL) { this.photoURL = photoURL; }
    
    public String getpassword() { return password;}
    public void setpassword(String password) { this.password = password; }
    
    public String getposition() { return position; }
    public void setposition(String position) { this.position = position; }
}
