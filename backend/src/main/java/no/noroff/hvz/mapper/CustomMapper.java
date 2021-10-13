package no.noroff.hvz.mapper;

import no.noroff.hvz.dto.kill.RegKillDTO;
import no.noroff.hvz.models.Kill;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomMapper {
    void updateKillFromDto(RegKillDTO dto, @MappingTarget Kill entity);
}
