package Model;

import java.time.LocalDate;

public class Driver
{
    private int unitNum;
    private int ranking;
    
    private double salary;
    
    private String public_driver_id;
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate dateOfBirth;
    private String address;
    private String contactNumber;
    private String licenseNum;
    private LocalDate licenseExpiry;
    private String photoURL;
    private String password;
    
   public int getunitNum() { return unitNum; }
   public void setunitNum(int unitNum) { this.unitNum = unitNum; }
   
   public int getranking() { return ranking; }
   public void setranking(int ranking) { this.ranking = ranking; }
   
   public double getsalary() { return salary; }
   public void setsalary(double salary) { this.salary = salary; }
   
   public String getpublic_driver_id() { return public_driver_id; }
   public void setpublic_driver_id(String public_driver_id) { this.public_driver_id = public_driver_id; }
   
   public String getfirstName() { return firstName; }
   public void setfirstName(String firstName) { this.firstName = firstName; }
   
   public String getlastName() {return lastName; }
   public void setlastName(String lastName) { this.lastName = lastName; }
   
   public String getgender() { return gender; }
   public void setgender(String gender) { this.gender = gender; }
   
   public LocalDate getdateOfBirth() { return dateOfBirth; }
   public void setdateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
   
   public String getaddress() { return address; }
   public void setaddress(String address) { this.address = address; }
   
   public String getcontactNumber() { return contactNumber; }
   public void setcontactNumber(String contactNumber) { this.contactNumber = contactNumber; }
   
   public String getlicenseNum() { return licenseNum; }
   public void setlicenseNum(String licenseNum) { this.licenseNum = licenseNum; }
   
   public LocalDate getlicenseExpiry() { return licenseExpiry; }
   public void setlicenseExpiry(LocalDate licenseExpiry) { this.licenseExpiry = licenseExpiry; }
   
   public String getphotoURL() { return photoURL; }
   public void setphotoURL(String photoURL) { this.photoURL = photoURL; }
   
   public String getpassword() { return password; }
   public void setpassword(String password) { this.password = password; }
   
   
}