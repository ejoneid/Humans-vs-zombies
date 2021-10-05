export interface ActiveGame {
  id: number,
  name: string,
  state: string,
  nw_lat: string | null,
  se_lat: string | null,
  nw_long: string | null,
  se_long: string | null
}
