package ru.tsystems.javaschool.kuzmenkov.logiweb.services.Impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.UserDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.User;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.Role;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.UserService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nikolay Kuzmenkov.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        try {
            User user = userDAO.findUserByEmail(userName);

            if (user == null) {
                throw new UsernameNotFoundException(userName);
            } else {
                return buildSecurityUserFromUserEntity(user);
            }

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Something unexpected happend.", e);
            throw new UsernameNotFoundException(userName);
        }
    }

    @Override
    @Transactional
    public Integer createNewUser(String userEmail, String userPassword, Role userRole) throws LogiwebServiceException, //
            LogiwebValidationException {
        if (userEmail == null || userEmail.isEmpty()) {
            throw new LogiwebValidationException(
                    "Username can't be empty.");
        }

        try {
            User userWithSameMail = userDAO.findUserByEmail(userEmail);
            if (userWithSameMail != null) {
                throw new LogiwebValidationException(
                        "User with email: " + userEmail + " already exist.");
            }

            User newUser = new User();
            newUser.setUserEmail(userEmail);
            newUser.setUserPassword(getMD5Hash(userPassword));
            newUser.setUserRole(userRole);
            userDAO.create(newUser);

            LOGGER.info("User #" + newUser.getUserId() + " " + userEmail + " created");

            return newUser.getUserId();

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Something unexpected happend.", e);
            throw new LogiwebServiceException(e);
        }
    }

    @Override
    @Transactional
    public User findUserById(Integer userId) throws LogiwebServiceException { //
        try {
            return userDAO.findById(userId);

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Something unexcpected happend.");
            throw new LogiwebServiceException(e);
        }
    }

    public String getMD5Hash(String userPassword) throws LogiwebServiceException { //
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            byte[] array = md.digest(userPassword.getBytes());
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            LOGGER.warn("MD5 hashing failed", e);
            throw new LogiwebServiceException("MD5 hashing failed", e);
        }
    }

    private org.springframework.security.core.userdetails.User buildSecurityUserFromUserEntity(User userEntity) {
        String username = userEntity.getUserEmail();
        String password = userEntity.getUserPassword();
        GrantedAuthority userRole = new SimpleGrantedAuthority(userEntity.getUserRole().name());

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(userRole);

        return new org.springframework.security.core.userdetails.User(username, password, authorities);
    }
}
