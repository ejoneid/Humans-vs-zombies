package no.noroff.hvz.repositories;

import no.noroff.hvz.models.Kill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KillerRepository extends JpaRepository<Kill, Long> {
}
