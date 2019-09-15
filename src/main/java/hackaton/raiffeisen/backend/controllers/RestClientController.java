package hackaton.raiffeisen.backend.controllers;

import hackaton.raiffeisen.backend.exceptions.ClientNotFoundException;
import hackaton.raiffeisen.backend.exceptions.ClientsNotFoundException;
import hackaton.raiffeisen.backend.models.Client;
import hackaton.raiffeisen.backend.models.Ord;
import hackaton.raiffeisen.backend.models.User;
import hackaton.raiffeisen.backend.repository.ClientRepository;
import hackaton.raiffeisen.backend.repository.UserRepository;
import hackaton.raiffeisen.backend.utils.ClientService;
import hackaton.raiffeisen.backend.viewmodels.ClientsView;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.net.URISyntaxException;
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
                    @ApiResponse(code = 400, message = "Если пользователя с id = userId в хранилище не существует")
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
            return ResponseEntity.badRequest().body(null);
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

        return ResponseEntity.ok(Collections.singletonMap("clients", clients));
    }

    @ApiOperation(
            value = "Вывод клиента по его id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 400, message = "Если пользователя с id = userId либо киента с id = clientId в хранилище не существует")
            }
    )
    @GetMapping("/users/{userId}/clients/{clientId}")
    public ResponseEntity<Client> getClient(
            @ApiParam(value = "id пользователя", example = "1")
            @PathVariable(name = "userId") Long userId,
            @ApiParam(value = "id клиента", example = "1")
            @PathVariable(name = "clientId") Long clientId) {

        User user = userRepository.getOne(userId);

        Optional<Client> client = user.getClients()
                .stream()
                .filter(clt -> clt.getId().equals(clientId))
                .findFirst();

        // TODO: if client not found
        Client result = client.get();
        return ResponseEntity.ok(result);
    }

    @ApiOperation(
            value = "Создание клиента")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 400, message = "Если пользователем введены некорректные данные о клиенте")
            }
    )
    @PostMapping(path = "/users/{userId}/clients")
    @Transactional
    public ResponseEntity createClient(
            @ApiParam(value = "Данные о клиенте")
            @RequestBody Client client,
            @ApiParam(value = "id пользователя", example = "1")
            @PathVariable("userId") Long userId) throws URISyntaxException {

        User user = userRepository.getOne(userId);
        client.getUsers().add(user);
//        Client clientFromDb = clientRepository.save(client);

        return ResponseEntity.ok().build();

    }

    @ApiOperation(
            value = "Удаление клиента")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 400, message = "Если пользователя с id = userId либо киента с id = clientId в хранилище не существует")
            }
    )
    @DeleteMapping(path = "/users/{userId}/clients/{clientId}")
    @Transactional
    public ResponseEntity deleteClient(
            @ApiParam(value = "id пользователя")
            @PathVariable("userId") Long userId,
            @ApiParam(value = "id клиента")
            @PathVariable("clientId") Long clientId) {

        User user = userRepository.getOne(userId);

        Set<Client> clients = user.getClients();
        if (clients.isEmpty()) {
            throw new ClientsNotFoundException("user hasn`t any client");
        }
        Optional<Client> optionalClient = clients
                .stream()
                .filter(clt -> clt.getId().equals(clientId))
                .findFirst();
        if(!optionalClient.isPresent()){
            throw new ClientNotFoundException("client with id = " + clientId + " don`t exists in current user client list");
        }
        Client client = optionalClient.get();
        clients.remove(client);
        client.getUsers().remove(user);
        if (client.getUsers().isEmpty()) {
            clientRepository.delete(client);
        }
        return ResponseEntity.ok().build();
    }

}
