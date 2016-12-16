package apg.repository;

public class UserRepository implements IUserRepository
{
    @Override
    public void createUser(String username, String password)
    {
        System.out.println("Should create user");
    }
}
