package hackaton.raiffeisen.backend.controllers;

import hackaton.raiffeisen.backend.models.User;
import hackaton.raiffeisen.backend.repository.UserRepository;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "Описание сервисов для взаимодействия с пользователем")
@RestController
@RequestMapping(path = "/api")
public class RestUserController {

    private UserRepository userRepository;

    @Autowired
    public RestUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//  TODO: при генерации примера для ApiParam не указывается корневой элемент "user"
    @ApiOperation(value = "Создание пользователя")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Пользователь создан"),
            @ApiResponse(code = 400, message = "Введены некорректные данные о пользователе"),
            @ApiResponse(code = 500, message = "Ошибка при сохранении в БД")
    })
    @PostMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createUser(
            @ApiParam(value = "Объект пользователя из формы", required = true)
            @RequestBody User user) {
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Пользователь создан {\"userId\": " + savedUser.getId() + "}");
    }

}
