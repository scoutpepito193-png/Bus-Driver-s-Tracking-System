package Service;

import Model.AuthResult;

import Repository.SuperAdminRepo;
import Repository.SubAdminRepo;
import Repository.DriverRepo;

public class LogInService
{
    SuperAdminRepo saRepo = new SuperAdminRepo();
    SubAdminRepo subRepo = new SubAdminRepo();
    DriverRepo dRepo = new DriverRepo();

    public AuthResult logIn(String publicID, String password)
    {

        if (publicID.startsWith("SA-"))
        {
            return saRepo.logInRepo(publicID, password);
        }

        if (publicID.startsWith("SUB-"))
        {
            return subRepo.subAdminLogIn(publicID, password);
        }

        if (publicID.startsWith("DRV-"))
        {
            return dRepo.driverLogin(publicID, password);
        }

        return null;
    }
}
