export interface MapMarker {
  id: number,
  isMission: boolean,
  description: string | null,
  position: {
    lat: number,
    lng: number
  },
  label: {
    text: string,
    color: string
  },
  options: {
    icon: string
  },
  title: string
}