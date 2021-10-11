export interface Kill {
  timeOfDeath: string,
  story: string | null,
  lat: number | null,
  lng: number | null,
  killerURL: string,
  victimURL: string
}
