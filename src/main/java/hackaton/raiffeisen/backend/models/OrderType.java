package hackaton.raiffeisen.backend.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "order_type")
public class OrderType {

    @Id
    @Column(name = "order_type_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Setter
    @Column(name = "name")
    private String name;

    @Getter
    @Setter
    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
    private Set<Ord> orders;

}
