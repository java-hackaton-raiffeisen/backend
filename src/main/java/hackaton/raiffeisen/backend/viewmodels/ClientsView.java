package hackaton.raiffeisen.backend.viewmodels;

import hackaton.raiffeisen.backend.models.Client;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientsView {

    @Getter
    @Setter
    private Client client;

    @Getter
    @Setter
    private int paymentPercentage;

    @Getter
    @Setter
    private double income;

}
