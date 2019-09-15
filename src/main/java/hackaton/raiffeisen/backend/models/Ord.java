package hackaton.raiffeisen.backend.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ord")
public class Ord {

    @Id
    @Column(name = "ord_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Setter
    @Column(name = "order_type")
    private String type;

    @Getter
    @Setter
    @Column(name = "order_deleted")
    private boolean orderDeleted;

    @Getter
    @Setter
    @Column(name = "order_creation_date")
    private LocalDate creationDate;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @Getter
    @Setter
    @OneToOne(mappedBy = "ord", fetch = FetchType.EAGER)
    private Contract contract;


}
