package apg.service;

import apg.repository.IUserRepository;
import apg.repository.UserRepository;

public class UserService
{
    private IUserRepository repository;

    public UserService()
    {
        this.repository = new UserRepository();
    }
}
