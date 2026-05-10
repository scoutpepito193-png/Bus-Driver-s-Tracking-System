package Model;

import Model.Role;

public class AuthResult {

    private Object user;
    private Role role;

    public AuthResult(Object user, Role role)
    {
        this.user = user;
        this.role = role;
    }

    public Object getUser()
    {
        return user;
    }

    public Role getRole()
    {
        return role;
    }
}