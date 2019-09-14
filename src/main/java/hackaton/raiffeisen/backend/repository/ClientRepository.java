package hackaton.raiffeisen.backend.repository;

import hackaton.raiffeisen.backend.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{
}
