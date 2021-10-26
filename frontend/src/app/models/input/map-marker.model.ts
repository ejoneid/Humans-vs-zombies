export interface MapMarker {
  id: number,
  type: string,
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
    icon: string,
    opacity: number
  },
  title: string
}
