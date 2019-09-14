package hackaton.raiffeisen.backend.repository;

import hackaton.raiffeisen.backend.models.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
}
