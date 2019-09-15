package hackaton.raiffeisen.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@ApiModel(value = "Клиент, пользующийся услугами пользователя сервиса")
@Entity
@Table(name = "client")
@JsonRootName(value = "client")
public class Client {

    @ApiModelProperty(hidden = true)
    @Id
    @Column(name = "client_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @ApiModelProperty(value = "Имя клиента", required = true, example = "Alex")
    @Getter
    @Setter
    @Column(name = "first_name", nullable = false)
    @NotEmpty
    private String firstName;

    @ApiModelProperty(value = "Фамилия клиента", required = true, example = "Serebryakov")
    @Getter
    @Setter
    @Column(name = "last_name", nullable = false)
    @NotEmpty
    private String lastName;

    @ApiModelProperty(value = "Отчество клиента", required = true, example = "Sergeevich")
    @Getter
    @Setter
    @Column(name = "father_name", nullable = false)
    @NotEmpty
    private String fatherName;

    @ApiModelProperty(value = "Адрес электронной почты клиента", required = true, example = "serebryakov@yandex.ru")
    @Getter
    @Setter
    @Column(name = "email", nullable = false)
    @NotEmpty
    private String email;

    @ApiModelProperty(value = "Мобильный телефон клиента", required = true, example = "89255684798")
    @Getter
    @Setter
    @Column(name = "phone", nullable = false)
    @NotEmpty
    private String phone;

    @ApiModelProperty(hidden = true)
    @Getter
    @Setter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_client",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private Set<User> users;

    @ApiModelProperty(hidden = true)
    @Getter
    @Setter
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Ord> ord;

}
