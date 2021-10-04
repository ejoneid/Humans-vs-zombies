package no.noroff.hvz.repositories;

import no.noroff.hvz.models.SquadMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SquadMemberRepository extends JpaRepository<SquadMember, Long> {
}
