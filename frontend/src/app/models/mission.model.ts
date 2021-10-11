export interface Mission {
  name: string,
  description: string,
  lat: number,
  lng: number,
  startTime: string | null,
  endTime: string | null,
  isHuman: boolean
}
