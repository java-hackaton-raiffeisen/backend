package hackaton.raiffeisen.backend.controllers;

import hackaton.raiffeisen.backend.exceptions.ClientNotFoundException;
import hackaton.raiffeisen.backend.exceptions.ClientsNotFoundException;
import hackaton.raiffeisen.backend.exceptions.OrderNotFoundException;
import hackaton.raiffeisen.backend.models.Client;
import hackaton.raiffeisen.backend.models.Contract;
import hackaton.raiffeisen.backend.models.Ord;
import hackaton.raiffeisen.backend.models.User;
import hackaton.raiffeisen.backend.repository.ContractRepository;
import hackaton.raiffeisen.backend.repository.OrderRepository;
import hackaton.raiffeisen.backend.repository.UserRepository;
import hackaton.raiffeisen.backend.viewmodels.OrderView;
import hackaton.raiffeisen.backend.viewmodels.OrdersView;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.*;

@Api(description = "Описание сервисов для взаимодействия с услугами для конкретного клиента")
@RestController
@RequestMapping(path = "/api")
public class RestOrderController {

    private OrderRepository orderRepository;
    private ContractRepository contractRepository;
    private UserRepository userRepository;

    @Autowired
    public RestOrderController(OrderRepository orderRepository,
                               ContractRepository contractRepository,
                               UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.contractRepository = contractRepository;
        this.userRepository = userRepository;
    }

    @ApiOperation(value = "Создание услуги для указанного клиента")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Услуга создана"),
            @ApiResponse(code = 400, message = "Если пользователем введены некорректные данные о клиенте либо пользователя или клиента с указанным id не существует"),
            @ApiResponse(code = 500, message = "Ошибка при сохранении в БД")
    })
    @PostMapping(path = "/users/{userId}/clients/{clientId}/orders")
    @Transactional
    public ResponseEntity createOrder(
            @ApiParam(value = "Данные об услуге")
            @RequestBody OrderView orderForm,
            @ApiParam(value = "id пользователя", example = "1")
            @PathVariable("userId") Long userId,
            @ApiParam(value = "id клиента", example = "1")
            @PathVariable("clientId") Long clientId) {

        User user = null;
        try {
            user = userRepository.getOne(userId);
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
                .filter(clt -> clt.getId().equals(clientId)).findFirst();
        if (!optionalClient.isPresent()) {
            throw new ClientNotFoundException("client with id = " + clientId + " don`t exists in current user client list");
        }
        Client client = optionalClient.get();
        Ord order = new Ord();
        order.setType(orderForm.getType());
        order.setCreationDate(LocalDate.now());
        Contract contract = new Contract();
        contract.setAllPaymentValue(orderForm.getAllPaymentValue());
        contract.setOrd(order);
        contract.setContractCreateDate(LocalDate.now());
        order.setContract(contract);
        order.setClient(client);
        client.getOrd().add(order);
        contractRepository.save(contract);
        Ord orderFromDb = orderRepository.save(order);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Услуга сохранена { " + orderFromDb.getId() + " }");
    }

    @ApiOperation(value = "Удаление услуги у клиента")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Усдуга удалена"),
            @ApiResponse(code = 400, message = "Если пользователя с id = userId, киента с id = clientId, услуги с id = orderId в хранилище не существует"),
            @ApiResponse(code = 500, message = "Ошибка при работе с БД")
    })
    @DeleteMapping(path = "/users/{userId}/clients/{clientId}/orders/{orderId}")
    @Transactional
    public ResponseEntity removeOrder(
            @ApiParam(value = "id пользователя")
            @PathVariable("userId") Long userId,
            @ApiParam(value = "id клиента")
            @PathVariable("clientId") Long clientId,
            @ApiParam(value = "id услуги")
            @PathVariable("orderId") Long orderId) {

        User user = null;
        try {
            user = userRepository.getOne(userId);
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
                .filter(clt -> clt.getId().equals(clientId)).findFirst();
        if (!optionalClient.isPresent()) {
            throw new ClientNotFoundException("client with id = " + clientId + " don`t exists in current user client list");
        }
        Client client = optionalClient.get();
        Optional<Ord> optionalOrder = client.getOrd()
                .stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst();
        if (!optionalOrder.isPresent()) {
            throw new OrderNotFoundException("order with id = " + orderId + " don`t exists in current client list");
        }
        Ord order = optionalOrder.get();
        order.setOrderDeleted(true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @ApiOperation(
            value = "Вывод всех заказов текущего клиента")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Список услуг, предоставленных указанному пользователю"),
                    @ApiResponse(code = 400, message = "Если пользователя с id = userId либо клиента с id = clientId в хранилище не существует"),
                    @ApiResponse(code = 500, message = "Ошибка при работе с БД")
            }
    )
    @GetMapping(path = "/users/{userId}/clients/{clientId}/orders")
    public ResponseEntity<Map<String, List<OrdersView>>> getOrders(
            @ApiParam(value = "id пользователя")
            @PathVariable("userId") Long userId,
            @ApiParam(value = "id клиента")
            @PathVariable("clientId") Long clientId,
            @Value("${bank.commission.percent}") int bankCommissionPercent) {

        User user = null;
        try {
            user = userRepository.getOne(userId);
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
                .filter(clt -> clt.getId().equals(clientId)).findFirst();
        if (!optionalClient.isPresent()) {
            throw new ClientNotFoundException("client with id = " + clientId + " don`t exists in current user client list");
        }

        Client client = optionalClient.get();
        Set<Ord> orders = client.getOrd();
        List<OrdersView> ordersViews = new ArrayList<>();
        if (orders != null && !orders.isEmpty()) {
            orders.forEach(order -> {
                Contract contract = order.getContract();
                boolean isOrderPaid = contract.getPayment() != null;
                LocalDate paymentDate = null;
                if (isOrderPaid) {
                    paymentDate = contract.getPayment().getPaymentDate();
                }
                OrdersView ordersView = OrdersView.builder()
                        .orderNumber(order.getId())
                        .orderCreationDate(order.getCreationDate())
                        .allPaymentValue(contract.getAllPaymentValue())
                        .paymentValue(contract.getAllPaymentValue() / 100 * (100 - bankCommissionPercent))
                        .isOrderPaid(isOrderPaid)
                        .paymentDate(paymentDate)
                        .build();
                ordersViews.add(ordersView);
            });
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Collections.singletonMap("orders", ordersViews));

    }


}
