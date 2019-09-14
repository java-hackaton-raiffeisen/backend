package hackaton.raiffeisen.backend.controllers;

import hackaton.raiffeisen.backend.models.Client;
import hackaton.raiffeisen.backend.models.User;
import hackaton.raiffeisen.backend.repository.ClientRepository;
import hackaton.raiffeisen.backend.repository.UserRepository;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Set;


@org.springframework.web.bind.annotation.RestController
@RequestMapping(path = "/api")
@Api(description = "Описание сервисов")
public class RestClientController {

    private UserRepository userRepository;
    private ClientRepository clientRepository;

    @Autowired
    public RestClientController(UserRepository userRepository,
                                ClientRepository clientRepository) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
    }

    @ApiOperation(
            value = "Вывод клиентов текущего пользователя")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 400, message = "Если пользователя с id = userId в хранилище не существует")
            }
    )
    @GetMapping(value = "/users/{userId}/clients", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Set<Client>> getClients(
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

        Set<Client> clients = user.getClients();

        return ResponseEntity.ok(clients);
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
    @PostMapping("/users/{userId}/clients")
    public ResponseEntity<URI> createClient(
            @ApiParam(value = "Данные о клиенте")
            @RequestBody Client client,
            @ApiParam(value = "id пользователя", example = "1")
            @PathVariable("userId") Long userId) throws URISyntaxException {

        User user = userRepository.getOne(userId);
        client.setUser(user);
        Client clientFromDb = clientRepository.save(client);

        return ResponseEntity.created(new URI("localhost:8080/clients/" + clientFromDb.getId())).build();

    }

}
