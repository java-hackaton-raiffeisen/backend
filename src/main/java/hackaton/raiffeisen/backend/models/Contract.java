package hackaton.raiffeisen.backend.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "contract")
public class Contract {

    @Id
    @Column(name = "contract_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Setter
    @Column(name = "contract_create_date")
    private LocalDate contractCreateDate;

    @Getter
    @Setter
    @Column(name = "all_payment_value")
    private Double allPaymentValue;

    @Getter
    @Setter
    @Column(name = "payment_last_date")
    private LocalDate paymentLastDate;

    @Getter
    @Setter
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ord_id", referencedColumnName = "ord_id")
    private Ord ord;

    @Getter
    @Setter
    @OneToOne(mappedBy = "contract", fetch = FetchType.EAGER)
    private Payment payment;

}
