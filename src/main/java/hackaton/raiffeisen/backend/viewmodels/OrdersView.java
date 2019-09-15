package hackaton.raiffeisen.backend.viewmodels;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdersView {

    @Getter
    @Setter
    private Long orderNumber;

    @Getter
    @Setter
    private Double paymentValue;

    @Getter
    @Setter
    private Double allPaymentValue;

    @Getter
    @Setter
    private boolean isOrderPaid;

    @Getter
    @Setter
    private LocalDate orderCreationDate;

    @Getter
    @Setter
    private LocalDate paymentDate;

}
