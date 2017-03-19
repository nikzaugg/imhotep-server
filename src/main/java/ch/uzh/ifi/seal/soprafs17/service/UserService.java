package ch.uzh.ifi.seal.soprafs17.service;

import ch.uzh.ifi.seal.soprafs17.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs17.entity.Game;
import ch.uzh.ifi.seal.soprafs17.entity.User;
import ch.uzh.ifi.seal.soprafs17.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String name, String username) {

        User newUser = new User();
        newUser.setName(name);
        newUser.setUsername(username);
        newUser.setStatus(UserStatus.OFFLINE);
        newUser.setGames(new ArrayList<Game>());
        newUser.setToken(UUID.randomUUID().toString());

        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);

        return newUser;
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId); //TODO check if user exists
        userRepository.delete(userId);
        log.debug("Deleted User: {}", user);
    }

    // Mo's Testing bullshit
    public List<User> listUsers() {
        log.debug("listUsers");

        List<User> result = new ArrayList<>();
        userRepository.findAll().forEach(result::add);

        return result;
    }

    public User getUser(Long userId){
        log.debug("getUser: " + userId);

        // TODO Implement check to see whether the user exists
        return userRepository.findOne(userId);
    }

    public User getUserByToken(String userToken) {
        log.debug("getUser: " + userToken);

        // TODO Implement check to see whether the user exists
        return userRepository.findByToken(userToken);
    }

    public User login(Long userId){
        log.debug("login: " + userId);

        User user = userRepository.findOne(userId);
        if (user != null) {
            user.setToken(UUID.randomUUID().toString());
            user.setStatus(UserStatus.ONLINE);
            user = userRepository.save(user);

            return user;
        }

        // TODO This situation needs to be handle in a better way: Should not return null if user cannot be found
        return null;
    }

    public void logout(Long userId, String userToken){
        log.debug("getUser: " + userId);

        User user = userRepository.findOne(userId);

        if (user != null && user.getToken().equals(userToken)) {
            user.setStatus(UserStatus.OFFLINE);
            userRepository.save(user);
        }
    }
}
