package io.swagger.service;


import io.swagger.model.BankAccount;
import io.swagger.model.User;
import io.swagger.model.Login;
import io.swagger.model.RegisterRequest;
import io.swagger.model.AuthToken;

import io.swagger.repository.AuthTokenRepository;
import io.swagger.repository.RegisterRequestRepository;
import io.swagger.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
@Transactional
public class AuthenticationService {
    private AuthTokenRepository authTokenRepository;
    private UserRepository userRepository;
    private RegisterRequestRepository registerRequestRepository;
    private GeneralMethodsService generalMethodsService;
    private BankAccount bankAccount = BankAccount.getBankAccount();
    private DateTimeFormatter formatter;


    public AuthenticationService(AuthTokenRepository authTokenRepository, UserRepository userRepository, RegisterRequestRepository registerRequestRepository,GeneralMethodsService generalMethodsService) {
        this.authTokenRepository = authTokenRepository;
        this.userRepository = userRepository;
        this.registerRequestRepository = registerRequestRepository;
        this.generalMethodsService = generalMethodsService;
        this.formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    }

    public RegisterRequest createRegisterRequest(RegisterRequest registerRequest)
    {
        //if user already reguested
        if(registerRequestRepository.findUserByEmail(registerRequest.getEmail()) != null)
            return registerRequest;
        else if(!registerRequest.getEmail().matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$"))
            return null;
        else if(!generalMethodsService.isValidPassword(registerRequest.getPassword()))
            return null;

        registerRequest.setPassword(generalMethodsService.cryptWithMD5(registerRequest.getPassword()));
        return registerRequestRepository.save(registerRequest);
    }
    public int deleteRegisterRequest(int requestId)
    {
        if(!registerRequestRepository.existsById(requestId))
            return 406;
        registerRequestRepository.deleteById(requestId);
        return 204;
    }

    public Integer signOutUser(String authToken)
    {
        if(!authTokenRepository.existsById(authToken))
            return 406;
        authTokenRepository.deleteById(authToken);
        return 204;
    }


    public boolean isUserAuthenticated(String token, int userId, boolean isEmployeeRequest)
    {
        //token does not exist
        if(!authTokenRepository.existsById(token))
            return false;

        AuthToken authToken = authTokenRepository.findById(token).get();

        //user performing request on bank own account that is not bank owner will be denied.
        if(userId == bankAccount.getBankAccountOwnerId() && authToken.getUserId() != bankAccount.getBankAccountOwnerId())
            return false;

        User.UserTypeEnum userType = userRepository.findById(authToken.getUserId()).get().getUserType();

        //wrong role
        if(isEmployeeRequest && (userType == User.UserTypeEnum.CUSTOMER))
            return false;

        //if user in path given check if user connected to token is requesting or its an employee
        if(userId != 0) {
            //employee requested
            if (userType == User.UserTypeEnum.EMPLOYEE || userType == User.UserTypeEnum.CUSTOMERANDEMPLOYEE)
                return true;
            //customer requested
            else if(userId == authToken.getUserId())
                return true;
            else
                return false;
        }
        else
            return true;
    }

    public AuthToken validateUserAndReturnAuthToken(Login login)
    {
        User user;
        if((user = userRepository.findUserByUserCredentials(login.getUsername(), generalMethodsService.cryptWithMD5(login.getPassword()))) == null)
            return null;
        AuthToken authToken = authTokenRepository.findAuthTokenByUser(user.getUserId());
        //user already has token
        if(authToken != null) {
            return authToken;
        }

        //token will expire after 30min from now
        return authTokenRepository.save(new AuthToken(createAuthToken(), user.getUserId(), LocalDateTime.now(), LocalDateTime.now().plusMinutes(30)));
    }

    private String createAuthToken()
    {
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rand = new Random();
        String token = "";

        List<String> tokenParts = new ArrayList<>();
        tokenParts.add("");
        tokenParts.add("");
        tokenParts.add("");
        tokenParts.add("");

        int partCounter = 1;
        for(String part : tokenParts) {
            for (int i = 1; i < 5; i++) {
                if(partCounter % 2 == 1 )
                    part += Integer.toString(rand.nextInt(10));
                else
                    part += alphabet.charAt(rand.nextInt(alphabet.length()));
                if(i == 4) {
                    token += "-" + part;
                    partCounter++;
                }

            }
        }
        //remove first -
        token = token.replaceFirst("-", "");
        return token;
    }

}
