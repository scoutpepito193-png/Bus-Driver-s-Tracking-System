package Model;

public class SubAdmin
{
    private int subAdmin_id;
    
    private String contactNum;
    private String firstName;
    private String lastName;
    private String password;
    private String position;
    
    public int getsubAdmin_id() { return subAdmin_id; }
    public void setsubAdmin_id(int subAdmin_id) { this.subAdmin_id = subAdmin_id; }
    
    public String getcontactNum() { return contactNum; }
    public void setcontactnum(String contactNum) { this.contactNum = contactNum; }
    
    public String getfirstName() { return firstName; }
    public void setfirstName(String firstName) { this.firstName = firstName; }
    
    public String getlastName() { return lastName; }
    public void setlastName(String lastName) { this.lastName = lastName; }
    
    public String getpassword() { return password;}
    public void setpassword(String password) { this.password = password; }
    
    public String getposition() { return position; }
    public void setposition(String position) { this.position = position; }
}
