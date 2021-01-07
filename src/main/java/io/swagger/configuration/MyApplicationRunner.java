package io.swagger.configuration;

import io.swagger.model.*;

import io.swagger.repository.*;

import io.swagger.service.GeneralMethodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import java.util.Timer;
import java.util.TimerTask;

@Component
public class MyApplicationRunner implements ApplicationRunner {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AuthTokenRepository authTokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RegisterRequestRepository registerRequestRepository;
    @Autowired
    private GeneralMethodsService generalMethodsService;

    private Timer expiredTokenDeleteTimer;

    public MyApplicationRunner() {

        this.expiredTokenDeleteTimer = new Timer();


    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {


        Files.lines(Paths.get("src/main/resources/accounts.csv"))
                .forEach(
                        line -> accountRepository.save(
                                new Account(line.split(",")[0],
                                        line.split(",")[1],
                                        Integer.parseInt(line.split(",")[2]),
                                        Double.parseDouble(line.split(",")[3]),
                                        Double.parseDouble(line.split(",")[4]),
                                        Integer.parseInt(line.split(",")[5]),
                                        Double.parseDouble(line.split(",")[6]))
                        )
                );


        Files.lines(Paths.get("src/main/resources/users.csv"))
                .forEach(
                        line -> userRepository.save(
                                new User(line.split(",")[0],
                                        line.split(",")[1],
                                        generalMethodsService.cryptWithMD5(line.split(",")[2]),
                                        line.split(",")[3],
                                        line.split(",")[4])
                        )
                );

        Files.lines(Paths.get("src/main/resources/registerRequests.csv"))
                .forEach(
                        line -> registerRequestRepository.save(
                                new RegisterRequest(line.split(",")[0],
                                        line.split(",")[1],
                                        generalMethodsService.cryptWithMD5(line.split(",")[2]),
                                        line.split(",")[3])
                        )
                );
        Files.lines(Paths.get("src/main/resources/transactions.csv"))
                .forEach(
                        line -> transactionRepository.save(
                                new Transaction(line.split(",")[0],
                                        LocalDateTime.now(),
                                        line.split(",")[2],
                                        line.split(",")[3],
                                        Double.parseDouble(line.split(",")[4]),
                                        Integer.parseInt(line.split(",")[5]))
                        )
                );

        Files.lines(Paths.get("src/main/resources/authTokens.csv"))
                .forEach(
                        line -> authTokenRepository.save(
                                new AuthToken(line.split(",")[0],
                                        Integer.parseInt(line.split(",")[1]),
                                        LocalDateTime.now(),
                                        LocalDateTime.now().plusYears(1))
                        )
                );

        transactionRepository.findAll()
                .forEach(System.out::println);

        userRepository.findAll()
                .forEach(System.out::println);


        authTokenRepository.findAll()
                .forEach(System.out::println);

        accountRepository.findAll()
                .forEach(System.out::println);


        setAuthTokenExpireDeleteSystem();

    }

    private void setAuthTokenExpireDeleteSystem()
    {
        //delete tokens after they are expired
        int delay = 5000;   // delay for 5 sec.
        int period = 3000;  // repeat every 3 sec.

        expiredTokenDeleteTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                authTokenRepository.deleteAuthTokenByDate(LocalDateTime.now());
            }
        }, delay, period);
    }
}

