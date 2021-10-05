package no.noroff.hvz.repositories;

import no.noroff.hvz.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
