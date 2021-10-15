export interface GameOutput {
  name: string,
  gameState: string,
  description: string | null,
  nw_lat: number | undefined,
  nw_long: number | undefined,
  se_lat: number | undefined,
  se_long: number | undefined
}
