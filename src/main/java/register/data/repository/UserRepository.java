package register.data.repository;

import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import register.data.entity.User;
import register.data.entity.UserRole;

import java.util.List;

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

    public List<User> search(User user, String name, String username, String email, String introducer, Long phoneNumber, String address) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM User WHERE " +
                "username != :user " +
                (name == null ? "" : "AND name = :name ") +
                (username == null ? "" : "AND username = :username ") +
                (email == null ? "" : "AND email = :email ") +
                (introducer == null ? "" : "AND introducer.username = :introducer ") +
                (phoneNumber == null ? "" : "AND phoneNumber = :phoneNumber ") +
                (address == null ? "" : "AND address = :address "))
                .setParameter("user", user.getUsername());

        if (name != null) {
            query.setParameter("name", name);
        }
        if (username != null) {
            query.setParameter("username", username);
        }
        if (email != null) {
            query.setParameter("email", email);
        }
        if (introducer != null) {
            query.setParameter("introducer", introducer);
        }
        if (phoneNumber != null) {
            query.setParameter("phoneNumber", phoneNumber);
        }
        if (address != null) {
            query.setParameter("address", address);
        }

        List<User> list = (List<User>) query.list();

        return list;
    }
}
