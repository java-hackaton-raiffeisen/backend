package hackaton.raiffeisen.backend.utils;

import hackaton.raiffeisen.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private UserRepository userRepository;

    @Autowired
    public ScheduledTasks(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /**
     * налог взымается 13 числа каждого месяца
     */
    @Scheduled(cron = "0 0 0 13 1/1 *")
    public void taxPay(){

    }

}
