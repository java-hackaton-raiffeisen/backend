package hackaton.raiffeisen.backend.repository;

import hackaton.raiffeisen.backend.models.Ord;
import hackaton.raiffeisen.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//    public List<Ord> ge
}
