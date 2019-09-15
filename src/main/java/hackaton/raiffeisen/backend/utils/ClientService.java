package hackaton.raiffeisen.backend.utils;

import hackaton.raiffeisen.backend.models.Client;
import hackaton.raiffeisen.backend.models.Ord;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ClientService {

    public int calculatePaymentPercentage(Client client) {
        Set<Ord> orders = client.getOrd();
        double expectedValue = orders
                .stream()
                .filter(order -> order.getContract() != null)
                .mapToDouble(order -> order.getContract().getAllPaymentValue())
                .sum();
        double currentValue = calculateIncome(client);
        double result = currentValue / expectedValue * 100;
        return (int) result;
    }

    public double calculateIncome(Client client) {
        Set<Ord> orders = client.getOrd();
        double income = orders
                .stream()
                .filter(order -> order.getContract() != null && order.getContract().getPayment() != null)
                .mapToDouble(order -> order.getContract().getPayment().getPaymentValue())
                .sum();
        return income;
    }

}
