'use client'

import { Fila, PosicaoFila, TokenResponse } from '@/app/types'

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api'

class ApiError extends Error {
  constructor(public status: number, message: string) {
    super(message)
    this.name = 'ApiError'
  }
}

async function request<T>(
  endpoint: string,
  options: RequestInit & { token?: string } = {}
): Promise<T> {
  const { token, ...fetchOptions } = options
  const url = `${API_URL}${endpoint}`

  const headers: Record<string, string> = {
    'Content-Type': 'application/json',
  }

  if (token) {
    headers['Authorization'] = `Bearer ${token}`
  }

  try {
    const response = await fetch(url, {
      ...fetchOptions,
      headers,
    })

    if (!response.ok) {
      throw new ApiError(response.status, `HTTP ${response.status}`)
    }

    const data = await response.json()
    return data
  } catch (error) {
    if (error instanceof ApiError) throw error
    throw new Error(`Erro ao conectar com servidor: ${error}`)
  }
}

// Auth
export async function login(usuarioId: string, role: string): Promise<TokenResponse> {
  return request<TokenResponse>('/auth/login', {
    method: 'POST',
    body: JSON.stringify({ usuarioId, role }),
  })
}

// Filas
export async function criarFila(
  nome: string,
  local: string,
  tempoMedioAtendimento: number,
  token: string
): Promise<Fila> {
  return request<Fila>('/filas', {
    method: 'POST',
    token,
    body: JSON.stringify({
      nome,
      local,
      tempoMedioAtendimento,
    }),
  })
}

export async function obterFila(id: string): Promise<Fila> {
  return request<Fila>(`/filas/${id}`)
}

export async function entrarNaFila(filaId: string, usuarioId: string, token: string): Promise<void> {
  return request<void>(`/filas/${filaId}/entrar?usuarioId=${usuarioId}`, {
    method: 'POST',
    token,
  })
}

export async function obterPosicao(
  filaId: string,
  usuarioId: string,
  token: string
): Promise<PosicaoFila> {
  return request<PosicaoFila>(`/filas/${filaId}/posicao/${usuarioId}`, {
    token,
  })
}

export async function chamarProximo(filaId: string, token: string): Promise<void> {
  return request<void>(`/filas/${filaId}/chamar-proximo`, {
    method: 'POST',
    token,
  })
}
