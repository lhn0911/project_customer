package ra.edu.project_customer.service;


import ra.edu.project_customer.dto.request.UserLogin;
import ra.edu.project_customer.dto.request.UserRegister;
import ra.edu.project_customer.dto.response.JWTResponse;
import ra.edu.project_customer.entity.User;

public interface UserService {
    User registerUser(UserRegister userRegister);
    JWTResponse login(UserLogin userLogin);
    public User getCurrentUser();
    User findById(Integer userId);
    User findByUsername(String username);

}