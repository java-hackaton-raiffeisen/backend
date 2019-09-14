package hackaton.raiffeisen.backend.repository;

import hackaton.raiffeisen.backend.models.Ord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Ord, Long> {
}
