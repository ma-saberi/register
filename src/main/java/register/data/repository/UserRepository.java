package register.data.repository;

import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import register.data.entity.User;
import register.data.entity.UserRole;

@Transactional
@Repository
public class UserRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public User getUserByUsername(String username) {

        Session session = sessionFactory.getCurrentSession();
        return (User) session.createQuery("FROM User WHERE username = :username")
                .setParameter("username", username)
                .uniqueResult();

    }

    public User registerUser(String username, String name, String email, String password, Long number, String address, User introducer, UserRole role) {

        Session session = sessionFactory.getCurrentSession();
        User user = new User();

        user.setName(name);
        user.setUsername(username);
        user.setEmail(email);
        user.setAddress(address);
        user.setPhoneNumber(number);
        user.setIntroducer(introducer);
        user.setPassword(DigestUtils.md5Hex(password));
        user.setRole(role);

        session.save(user);

        return user;
    }

    public void updateUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.update(user);
    }
}
