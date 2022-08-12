package jbr.springmvc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import jbr.springmvc.model.Login;
import jbr.springmvc.model.User;

public class UserDaoImpl implements UserDao {

  @Autowired
  DataSource datasource;

  @Autowired
  JdbcTemplate jdbcTemplate;

  public int register(User user) {
    String sql = "insert into users values(?,?,?,?,?,?,?)";

    return jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getFirstname(),
            user.getLastname(), user.getEmail(), user.getAddress(), user.getPincode());
  }

  public User validateUser(Login login) {

    //Validate User by decryption
    User u = findUserByUsername(login);

    if (BCrypt.checkpw(login.getPassword(), u.getPassword()))
      {
        String sql = "select * from users where username='" + login.getUsername() + "'";
        List<User> users = jdbcTemplate.query(sql, new UserMapper());

        return users.size() > 0 ? users.get(0) : null;
      }

   return null;
  }

  public User findUserByUsername(Login login)
  {
    String sel = "select * from users";
    List<User> users1 = jdbcTemplate.query(sel,new UserMapper());

    for(User u:users1)
    {
         if(u.getUsername().equals(login.getUsername()))
         {
           return u;
         }
    }
    return null;
  }


}

class UserMapper implements RowMapper<User> {

  public User mapRow(ResultSet rs, int arg1) throws SQLException {
    User user = new User();

    user.setUsername(rs.getString("username"));
    user.setPassword(rs.getString("password"));
    user.setFirstname(rs.getString("firstname"));
    user.setLastname(rs.getString("lastname"));
    user.setEmail(rs.getString("email"));
    user.setAddress(rs.getString("address"));
    user.setPincode(rs.getInt("Pincode"));

    return user;
  }
}