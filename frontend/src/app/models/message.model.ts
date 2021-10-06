export interface Message {
  id: number,
  chat: string
  squad: number | null,
  sender: string,
  time: string,
  content: string
}
