export type Role = 'ADMIN' | 'CLIENTE'

export interface Fila {
  id: string
  nome: string
  local: string
  ativa: boolean
  tempoMedioAtendimento: number
  criadaEm: string
}

export interface Usuario {
  id: string
  nome: string
  telefone: string
  role: Role
  criadoEm: string
}

export interface PosicaoFila {
  usuarioId: string
  filaId: string
  posicao: number
  tempoEstimadoMinutos: number
  ativo: boolean
}

export interface TokenResponse {
  token: string
  type: string
  usuarioId: string
  role: Role
}
