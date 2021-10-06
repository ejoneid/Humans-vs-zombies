export interface Message {
  id: number,
  squad: number,
  human: boolean,
  zombie: boolean,
  sender: string,
  time: string,
  content: string
}
