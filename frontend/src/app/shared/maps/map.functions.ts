import {MapMarker} from "../../models/input/map-marker.model";
import {Kill} from "../../models/input/kill.model";
import {Mission} from "../../models/input/mission.model";

//Takes an array of kills and missions and turns them into map markers.
//Used in both the admin and game-info modules.
export function createMapMarkers(kills: Kill[], missions: Mission[]): MapMarker[] {
  const markers: MapMarker[] = [];
  // Populating the marker list
  for (let mission of missions) {
    markers.push({
      id: mission.id,
      isMission: true,
      description: mission.description,
      position: {lat: mission.lat, lng: mission.lng},
      label: {text: mission.name, color: "#B2BBBD"},
      options: {icon: "../assets/mission-icon.svg"},
      title: mission.name
    });
  }
  for (let kill of kills) {
    if (kill.lat != null && kill.lng != null) { //Position data for kills is optional.
      markers.push({
        id: kill.id,
        isMission: false,
        description: kill.story,
        position: {lat: kill.lat, lng: kill.lng},
        label: {text: kill.killerName, color: "#B2BBBD"},
        options: {icon: "../assets/tombstone-icon.svg"},
        title: "Kill"
      });
    }
  }
  return markers;
}
