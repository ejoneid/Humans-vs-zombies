import LatLng = google.maps.LatLng;

export interface Mission {
  name: string,
  description: string,
  latLng: LatLng | null,
  startTime: string | null,
  endTime: string | null,
  isHuman: boolean
}
