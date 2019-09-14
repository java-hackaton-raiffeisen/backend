package hackaton.raiffeisen.backend.repository;

import hackaton.raiffeisen.backend.models.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTypeRepository extends JpaRepository<OrderType, Long>{
}
