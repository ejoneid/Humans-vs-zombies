export interface Mission {
  name: string,
  description: string,
  lat: number | null,
  lng: number | null,
  startTime: string | null,
  endTime: string | null,
  isHuman: boolean
}
