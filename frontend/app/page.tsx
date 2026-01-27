'use client'

import { useState } from 'react'
import { useRouter } from 'next/navigation'
import { Button, Card, Header, Loader } from '@/app/components'
import { entrarNaFila, obterFila } from '@/app/services/api'

export default function Home() {
  const router = useRouter()
  const [codigoFila, setCodigoFila] = useState('')
  const [loading, setLoading] = useState(false)
  const [erro, setErro] = useState('')
  const [usuarioId] = useState(() => {
    if (typeof window !== 'undefined') {
      const stored = localStorage.getItem('usuarioId')
      if (stored) return stored
      const novo = crypto.randomUUID()
      localStorage.setItem('usuarioId', novo)
      return novo
    }
    return ''
  })

  const handleEntrar = async () => {
    if (!codigoFila.trim()) {
      setErro('Digite o código da fila')
      return
    }

    setLoading(true)
    setErro('')

    try {
      // Verificar se fila existe
      await obterFila(codigoFila)

      // Obter token
      const tokenData = localStorage.getItem('token')
      if (!tokenData) {
        // Para demo, usamos um token fake
        localStorage.setItem('token', 'demo-token-' + Date.now())
      }

      router.push(`/fila/${codigoFila}`)
    } catch (err) {
      setErro('Fila não encontrada')
    } finally {
      setLoading(false)
    }
  }

  return (
    <>
      <Header title="Kuilu" subtitle="Sua vez, sem perder tempo" />

      <div className="flex-1 flex flex-col justify-center p-6 gap-6">
        <Card>
          <h2 className="text-2xl font-bold mb-4">Entrar na fila</h2>
          <label className="block mb-3">
            <span className="text-lg font-semibold mb-2 block">Código da fila:</span>
            <input
              type="text"
              value={codigoFila}
              onChange={(e) => setCodigoFila(e.target.value)}
              placeholder="Ex: 550e8400"
              className="w-full px-4 py-3 text-lg border-2 border-gray-300 rounded-lg focus:border-primary"
              disabled={loading}
            />
          </label>

          {erro && <p className="text-danger text-base mb-4">{erro}</p>}

          <Button onClick={handleEntrar} loading={loading}>
            Entrar
          </Button>
        </Card>

        <Card>
          <p className="text-base text-gray-600">
            📱 <strong>Dica:</strong> Cada fila tem um código único. Peça ao balcão.
          </p>
        </Card>

        <Button
          variant="secondary"
          onClick={() => router.push('/criar-fila')}
          disabled={loading}
        >
          Criar fila (Admin)
        </Button>
      </div>
    </>
  )
}
