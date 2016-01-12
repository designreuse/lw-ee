package ru.tsystems.javaschool.kuzmenkov.logiweb.services.implementation;

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
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.UserService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.PasswordConverter;

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


    /**
     * Load user by name.
     *
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws LogiwebDAOException, UsernameNotFoundException {
        User user = userDAO.findUserByEmail(userName);

        if (user == null) {
            throw new UsernameNotFoundException(userName);
        } else {
            return buildSecurityUserFromUserEntity(user);
        }
    }

    /**
     * Create new logiweb user.
     *
     * @param userEmail
     * @param userPassword
     * @param userRole
     * @return
     * @throws LogiwebValidationException if user with this name already exist
     * @throws LogiwebDAOException if something unexpected happened
     */
    @Override
    @Transactional
    public Integer createNewUser(String userEmail, String userPassword, Role userRole) throws LogiwebDAOException,
            LogiwebValidationException, NoSuchAlgorithmException {
        User newUser = new User();
        newUser.setUserEmail(userEmail);
        newUser.setUserPassword(userPassword);
        newUser.setUserRole(userRole);
        userDAO.create(newUser);

        LOGGER.info("User #" + newUser.getUserId() + " " + userEmail + " created");

        return newUser.getUserId();
    }

    /**
     * Find user by email.
     *
     * @param userEmail
     * @return user
     * @throws LogiwebDAOException if unexpected exception occurred on lower level (not user fault)
     */
    @Override
    @Transactional
    public User findUserByEmail(String userEmail) throws LogiwebDAOException {
        return userDAO.findUserByEmail(userEmail);
    }


    /**
     * Method builds security user.
     *
     * @param userEntity
     */
    private org.springframework.security.core.userdetails.User buildSecurityUserFromUserEntity(User userEntity) {
        String username = userEntity.getUserEmail();
        String password = userEntity.getUserPassword();
        GrantedAuthority userRole = new SimpleGrantedAuthority(userEntity.getUserRole().name());

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(userRole);

        return new org.springframework.security.core.userdetails.User(username, password, authorities);
    }
}
