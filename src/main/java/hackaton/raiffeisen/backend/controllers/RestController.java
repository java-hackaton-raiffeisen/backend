package hackaton.raiffeisen.backend.controllers;

import hackaton.raiffeisen.backend.models.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@org.springframework.web.bind.annotation.RestController
@RequestMapping(path = "/api")
@Api(description = "Описание сервисов")
public class RestController {

//    @ApiOperation(value = "qweeeeeeee")
//    @GetMapping(path = "/users", produces = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<List<User>> getUsers() {
//        return ResponseEntity.ok();
//    }
//
//    @PostMapping(path = "/users")
//    public ResponseEntity createUser(@RequestBody User user) {
//        return ResponseEntity.ok().build();
//    }

    @GetMapping("/clients")
    public ResponseEntity getClients(){
        return ResponseEntity.ok().build();
    }

}
