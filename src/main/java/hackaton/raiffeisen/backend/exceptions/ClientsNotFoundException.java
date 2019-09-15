package hackaton.raiffeisen.backend.exceptions;

public class ClientsNotFoundException extends RuntimeException {

    public ClientsNotFoundException(){}

    public ClientsNotFoundException(String message){
        super(message);
    }

}
