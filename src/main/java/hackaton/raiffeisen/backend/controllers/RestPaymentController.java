package hackaton.raiffeisen.backend.controllers;

import hackaton.raiffeisen.backend.exceptions.ClientNotFoundException;
import hackaton.raiffeisen.backend.exceptions.ClientsNotFoundException;
import hackaton.raiffeisen.backend.exceptions.OrderNotFoundException;
import hackaton.raiffeisen.backend.models.*;
import hackaton.raiffeisen.backend.repository.*;
import hackaton.raiffeisen.backend.viewmodels.PaymentView;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Api(value = "Сервис для работы с платежами клиента, пользующегося услугой")
@RestController
public class RestPaymentController {

    private UserRepository userRepository;
    private ClientRepository clientRepository;
    private PaymentRepository paymentRepository;
    private OrderRepository orderRepository;
    private ContractRepository contractRepository;

    @Autowired
    public RestPaymentController(UserRepository userRepository, ClientRepository clientRepository, PaymentRepository paymentRepository, OrderRepository orderRepository, ContractRepository contractRepository) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.contractRepository = contractRepository;
    }

    @ApiOperation(value = "Платёж клиента за предоставленную услугу")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Оплата произведена"),
            @ApiResponse(code = 400, message = "Оплата не произведена"),
            @ApiResponse(code = 500, message = "Ошибка при работе с БД")
    })
    @PostMapping(path = "/payment/callback")
    @Transactional
    public ResponseEntity paymentCallback(
            @ApiParam(value = "Информация о платеже", required = true)
            @RequestBody PaymentView paymentView,
            @Value("${bank.commission.percent}") int bankCommissionPercent) {

        if(paymentView.equals("success")){

            User user = null;
            try {
                user = userRepository.getOne(paymentView.getUserId());
            } catch (EntityNotFoundException e) {
            }

            if (user == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(null);
            }

            Set<Client> clients = user.getClients();
            if (clients.isEmpty()) {
                throw new ClientsNotFoundException("user hasn`t any client");
            }
            Optional<Client> optionalClient = clients
                    .stream()
                    .filter(clt -> clt.getId().equals(paymentView.getClientId())).findFirst();
            if (!optionalClient.isPresent()) {
                throw new ClientNotFoundException("client with id = " + paymentView.getClientId() + " don`t exists in current user client list");
            }

            Client client = optionalClient.get();
            Optional<Ord> optionalOrder = client.getOrd()
                    .stream()
                    .filter(order -> order.getId().equals(paymentView.getOrderId()))
                    .findFirst();
            if (!optionalOrder.isPresent()) {
                throw new OrderNotFoundException("order with id = " + paymentView.getOrderId() + " don`t exists in current client list");
            }
            Ord order = optionalOrder.get();

            Contract contract = order.getContract();
            contract.setAllPaymentValue(paymentView.getPaymentValue());

            Payment payment = new Payment();
            payment.setContract(contract);
            payment.setPaymentValue(paymentView.getPaymentValue());
            payment.setPaymentDate(LocalDate.now());
            paymentRepository.save(payment);

            double userBalance = user.getBalance();
            userBalance += paymentView.getPaymentValue() / 100 * (100 - bankCommissionPercent);
            user.setBalance(userBalance);

            bankCommission(paymentView.getPaymentValue() / 100 * bankCommissionPercent);

        } else {
            ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Оплаты не произведена");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Оплата произведена");
    }

    private void bankCommission(double commission){
//        Перевод комиссии на счёт банка
    }


}
