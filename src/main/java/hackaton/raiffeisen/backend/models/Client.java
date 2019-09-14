package hackaton.raiffeisen.backend.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @Column(name = "client_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Setter
    @Column(name = "full_name")
    private String fullName;

    @Getter
    @Setter
    @Column(name = "email")
    private String email;

    @Getter
    @Setter
    @Column(name = "phone")
    private String phone;

    @Getter
    @Setter
    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private Set<Ord> ord;




}
