package no.noroff.hvz.mapper;

import no.noroff.hvz.dto.kill.KillDTOReg;
import no.noroff.hvz.dto.mission.MissionDTOReg;
import no.noroff.hvz.dto.player.PlayerDTOUpdate;
import no.noroff.hvz.models.Kill;
import no.noroff.hvz.models.Mission;
import no.noroff.hvz.models.Player;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomMapper {
    void updateKillFromDto(KillDTOReg dto, @MappingTarget Kill entity);
    void updateMissionFromDto(MissionDTOReg dto, @MappingTarget Mission mission);
    void updatePlayerFromDto(PlayerDTOUpdate dto, @MappingTarget Player player);
}
