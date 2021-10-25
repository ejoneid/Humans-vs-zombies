import {MapMarker} from "../../models/input/map-marker.model";
import {Kill} from "../../models/input/kill.model";
import {Mission} from "../../models/input/mission.model";
import {SquadCheckIn} from "../../models/input/squad-check-in.model";
import {MapBorder} from "../../models/input/map-border.model";

//Takes an array of kills and missions and turns them into map markers.
//Used in both the admin and game-info modules.
export function createMapMarkers(kills: Kill[], missions: Mission[], squadCheckIns: SquadCheckIn[], mapBorders: MapBorder): MapMarker[] {
  const markers: MapMarker[] = [];
  // Populating the marker list
  for (let mission of missions) {
    markers.push({
      id: mission.id,
      type: "MISSION",
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
        type: "KILL",
        description: kill.story,
        position: {lat: kill.lat, lng: kill.lng},
        label: {text: kill.victimName, color: "#B2BBBD"},
        options: {icon: "../assets/tombstone-icon.svg"},
        title: kill.killerName
      });
    }
  }
  for (let checkIn of squadCheckIns) {
    markers.push({
      id: checkIn.id,
      type: "CHECKIN",
      description: checkIn.time,
      position: {lat: checkIn.lat, lng: checkIn.lng},
      label: {text: checkIn.member.name, color: "#B2BBBD"},
      options: {icon: "../assets/check-in-icon.png"},
      title: checkIn.member.name
    })
  }
  if (mapBorders.nw_lat != null && mapBorders.nw_long != null && mapBorders.se_lat != null && mapBorders.se_long != null) {
    markers.push({
      description: "",
      id: 0,
      label: {color: "", text: ""},
      options: {icon: "../assets/map-borders-icon.png"},
      position: {lat: mapBorders.nw_lat, lng: mapBorders.nw_long},
      title: "topLeft",
      type: "BORDER"
    })
    markers.push({
      description: "",
      id: 0,
      label: {color: "", text: ""},
      options: {icon: "../assets/map-borders-icon.png"},
      position: {lat: mapBorders.nw_lat, lng: mapBorders.se_long},
      title: "topRight",
      type: "BORDER"
    })
    markers.push({
      description: "",
      id: 0,
      label: {color: "", text: ""},
      options: {icon: "../assets/map-borders-icon.png"},
      position: {lat: mapBorders.se_lat, lng: mapBorders.nw_long},
      title: "bottomLeft",
      type: "BORDER"
    })
    markers.push({
      description: "",
      id: 0,
      label: {color: "", text: ""},
      options: {icon: "../assets/map-borders-icon.png"},
      position: {lat: mapBorders.se_lat, lng: mapBorders.se_long},
      title: "bottomRight",
      type: "BORDER"
    })
  }
  return markers;
}
