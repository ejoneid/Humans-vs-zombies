export interface MapMarker {
  name: string,
  description: string | null,
  position: {
    lat: number,
    lng: number
  },
  options: {
    icon: string
  }
}
