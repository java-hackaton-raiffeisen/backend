package hackaton.raiffeisen.backend.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "recommendation")
public class Recommendation {

    @Id
    @Column(name = "recommendation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Setter
    @Getter
    @Column(name = "name")
    private String name;

    @Setter
    @Getter
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_recommendation",
            joinColumns = @JoinColumn(name = "recommendation_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users;

}
