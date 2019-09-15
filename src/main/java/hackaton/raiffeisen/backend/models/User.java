package hackaton.raiffeisen.backend.models;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@ApiModel(description = "Пользователь, пользующийся сервисом")
@Check(constraints = "balance >= 0")
@Entity
@Table(name = "user")
@JsonRootName(value = "user")
public class User {

    @ApiModelProperty(hidden = true)
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @ApiModelProperty(value = "Имя пользователя", required = true, example = "Alex")
    @Getter
    @Setter
    @Column(name = "first_name", nullable = false)
    @NotEmpty
    private String firstName;

    @ApiModelProperty(value = "Фамилия пользователя", required = true, example = "Serebryakov")
    @Getter
    @Setter
    @Column(name = "last_name", nullable = false)
    @NotEmpty
    private String lastName;

    @ApiModelProperty(value = "Отчество пользователя", required = true, example = "Sergeevich")
    @Getter
    @Setter
    @Column(name = "father_name", nullable = false)
    @NotEmpty
    private String fatherName;

    @ApiModelProperty(value = "Адрес электронной почты пользователя", required = true, example = "serebryakov@yandex.ru")
    @Getter
    @Setter
    @Column(name = "email", nullable = false, unique = true)
    @NotEmpty
    @Email
    private String email;

    @ApiModelProperty(value = "Мобильный телефон пользователя", required = true, example = "89255684798")
    @Getter
    @Setter
    @Column(name = "phone", nullable = false)
    @NotEmpty
    private String phone;

    @ApiModelProperty(value = "Текущий баланс на счету пользователя", hidden = true, example = "500.0")
    @Getter
    @Setter
    @Column(name = "balance")
    @Min(value = 0)
    private double balance;

    @ApiModelProperty(hidden = true)
    @Getter
    @Setter
    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private Set<Client> clients;

    @ApiModelProperty(hidden = true)
    @Getter
    @Setter
    @ManyToMany(mappedBy = "users")
    private Set<Recommendation> recommendations;

}