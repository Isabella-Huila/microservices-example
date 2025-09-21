package com.elgris.usersapi.api;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.elgris.usersapi.models.User;
import com.elgris.usersapi.models.UserRole;
import com.elgris.usersapi.repository.UserRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.jsonwebtoken.Claims;
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @CircuitBreaker(name = "getUsersCB", fallbackMethod = "getUsersFallback")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<User> getUsers() {
        List<User> response = new LinkedList<>();
        userRepository.findAll().forEach(response::add);

        throw new RuntimeException("Simulación: La base de datos no está disponible.");
    }

    public List<User> getUsersFallback(Throwable t) {
        System.out.println("Fallback getUsers triggered: " + t.getMessage());
        return new LinkedList<>(); // lista vacía
    }

    @CircuitBreaker(name = "getUserCB", fallbackMethod = "getUserFallback")
    @RequestMapping(value = "/{username}",  method = RequestMethod.GET)
    public User getUser(HttpServletRequest request, @PathVariable("username") String username) {

        Object requestAttribute = request.getAttribute("claims");
        if((requestAttribute == null) || !(requestAttribute instanceof Claims)){
            throw new RuntimeException("Did not receive required data from JWT token");
        }

        Claims claims = (Claims) requestAttribute;

        if (!username.equalsIgnoreCase((String)claims.get("username"))) {
            throw new AccessDeniedException("No access for requested entity");
        }

        return userRepository.findOneByUsername(username);
    }

    public User getUserFallback(HttpServletRequest request, String username, Throwable t) {
        User fallbackUser = new User();
        fallbackUser.setUsername("unknown");
        fallbackUser.setFirstname("unknown");
        fallbackUser.setLastname("unknown");
        fallbackUser.setRole(UserRole.USER);
        return fallbackUser;
    }
}

// Aqui tenemos dos endpoints:
// - GET /users/: devuelve la lista de todos los usuarios.
// - GET /users/{username}: devuelve los datos del usuario cuyo username se pasa como parámetro.
// Nosotras con la librería import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker; con la entrada @CircuitBreaker(name = "getUsersCB", fallbackMethod = "getUsersFallback") definimos métodos de fallback para cuando el servicio no esté disponible.
// en el de ver todos los usuarios lanzamos una excepción para simular una error fatal y que se active el fallback, que devuelve una lista vacía.
// y así se comprueba que el sistema sigue funcionando aunque un servicio falle.