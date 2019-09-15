package hackaton.raiffeisen.backend.restresponses;

import hackaton.raiffeisen.backend.viewmodels.ClientsView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class ClientList {

    @Getter
    @Setter
    private List<ClientsView> clients;

}
