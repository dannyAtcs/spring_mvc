package jbr.springmvc.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;

import jbr.springmvc.dao.UserDao;
import jbr.springmvc.model.Login;
import jbr.springmvc.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

  @Autowired
  public UserDao userDao;

  public int register(User user) {
    user.setPassword(hashPassword((user.getPassword())));
    return userDao.register(user);
  }

  private String hashPassword(String plainTextPassword){
    return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
  }
  public User validateUser(Login login) {

    return userDao.validateUser(login);
  }


}
