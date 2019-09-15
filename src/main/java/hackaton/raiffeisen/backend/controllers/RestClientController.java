package hackaton.raiffeisen.backend.controllers;

import hackaton.raiffeisen.backend.exceptions.ClientNotFoundException;
import hackaton.raiffeisen.backend.exceptions.ClientsNotFoundException;
import hackaton.raiffeisen.backend.models.Client;
import hackaton.raiffeisen.backend.models.User;
import hackaton.raiffeisen.backend.repository.ClientRepository;
import hackaton.raiffeisen.backend.repository.UserRepository;
import hackaton.raiffeisen.backend.utils.ClientService;
import hackaton.raiffeisen.backend.viewmodels.ClientsView;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.*;


@Api(description = "Описание сервисов для взаимодействия с клиентом")
@RestController
@RequestMapping(path = "/api")
public class RestClientController {

    private UserRepository userRepository;
    private ClientRepository clientRepository;
    private ClientService clientService;

    @Autowired
    public RestClientController(UserRepository userRepository,
                                ClientRepository clientRepository,
                                ClientService clientService) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.clientService = clientService;
    }

    @ApiOperation(
            value = "Вывод клиентов текущего пользователя")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Список пользователей в формате json"),
                    @ApiResponse(code = 400, message = "Если пользователя с id = userId в хранилище не существует"),
                    @ApiResponse(code = 500, message = "Ошибка при работе с БД")
            }
    )
    @GetMapping(value = "/users/{userId}/clients", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, List<ClientsView>>> getClients(
            @ApiParam(value = "id пользователя", example = "1")
            @PathVariable(name = "userId") Long userId) {

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

        Set<Client> userClients = user.getClients();
        List<ClientsView> clients = new ArrayList<>();
        userClients.forEach(client -> {

            ClientsView clientsView = ClientsView
                    .builder()
                    .client(client)
                    .paymentPercentage(clientService.calculatePaymentPercentage(client))
                    .income(clientService.calculateIncome(client))
                    .build();
            clients.add(clientsView);
        });

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Collections.singletonMap("clients", clients));
    }

    @ApiOperation(
            value = "Вывод клиента по его id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Информация о пользователе в формате json"),
                    @ApiResponse(code = 400, message = "Если пользователя с id = userId либо киента с id = clientId в хранилище не существует"),
                    @ApiResponse(code = 500, message = "Ошибка при работе с БД")
            }
    )
    @GetMapping(path = "/users/{userId}/clients/{clientId}")
    public ResponseEntity<Client> getClient(
            @ApiParam(value = "id пользователя", example = "1")
            @PathVariable(name = "userId") Long userId,
            @ApiParam(value = "id клиента", example = "1")
            @PathVariable(name = "clientId") Long clientId) {

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

        Optional<Client> client = user.getClients()
                .stream()
                .filter(clt -> clt.getId().equals(clientId))
                .findFirst();

        if (!client.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }

        Client clientFromDb = client.get();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clientFromDb);
    }

    @ApiOperation(
            value = "Создание клиента")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "Клиент создан"),
                    @ApiResponse(code = 400, message = "Если пользователем введены некорректные данные о клиенте либо пользователя с указанным userId не существует"),
                    @ApiResponse(code = 500, message = "Ошибка при сохранении в БД")
            }
    )
    @PostMapping(path = "/users/{userId}/clients")
    @Transactional
    public ResponseEntity createClient(
            @ApiParam(value = "Данные о клиенте")
            @RequestBody Client client,
            @ApiParam(value = "id пользователя", example = "1")
            @PathVariable("userId") Long userId) {

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

        client.setUsers(new HashSet<>());
        client.getUsers().add(user);

        Client clientFromDb = clientRepository.save(client);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Клиент создан { \"clientId\" : " + clientFromDb.getId() + "}");

    }

    @ApiOperation(
            value = "Удаление клиента")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Клиент удалён"),
                    @ApiResponse(code = 400, message = "Если пользователя с id = userId либо киента с id = clientId в хранилище не существует"),
                    @ApiResponse(code = 500, message = "Ошибка при работе с БД")
            }
    )
    @DeleteMapping(path = "/users/{userId}/clients/{clientId}")
    @Transactional
    public ResponseEntity deleteClient(
            @ApiParam(value = "id пользователя")
            @PathVariable("userId") Long userId,
            @ApiParam(value = "id клиента")
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
//            TODO: add exception handler
            throw new ClientsNotFoundException("user hasn`t any client");
        }
        Optional<Client> optionalClient = clients
                .stream()
                .filter(clt -> clt.getId().equals(clientId))
                .findFirst();
        if (!optionalClient.isPresent()) {
//            TODO: add exception handler
            throw new ClientNotFoundException("client with id = " + clientId + " don`t exists in current user client list");
        }
        Client client = optionalClient.get();
        clients.remove(client);
        client.getUsers().remove(user);
        if (client.getUsers().isEmpty()) {
            clientRepository.delete(client);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Клиент с \"clientId\" : " + clientId + " удалён");
    }

}
