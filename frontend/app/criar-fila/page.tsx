'use client'

import { useState } from 'react'
import { useRouter } from 'next/navigation'
import { Button, Card, Header, Loader } from '@/app/components'
import { criarFila } from '@/app/services/api'

export default function CriarFila() {
  const router = useRouter()
  const [nome, setNome] = useState('')
  const [local, setLocal] = useState('')
  const [tempo, setTempo] = useState('')
  const [loading, setLoading] = useState(false)
  const [erro, setErro] = useState('')

  const handleCriar = async () => {
    setErro('')

    if (!nome.trim() || !local.trim() || !tempo.trim()) {
      setErro('Preencha todos os campos')
      return
    }

    if (parseInt(tempo) <= 0) {
      setErro('Tempo deve ser maior que 0')
      return
    }

    setLoading(true)

    try {
      // Demo token
      const token = 'demo-admin-token'
      const fila = await criarFila(nome, local, parseInt(tempo), token)
      router.push('/')
    } catch (err) {
      setErro('Erro ao criar fila. Tente novamente.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <>
      <Header
        title="Criar fila"
        showBack
        onBack={() => router.push('/')}
      />

      <div className="flex-1 flex flex-col justify-center p-6">
        <Card className="mb-6">
          <h2 className="text-2xl font-bold mb-6">Dados da fila</h2>

          <label className="block mb-4">
            <span className="text-lg font-semibold mb-2 block">Nome da fila:</span>
            <input
              type="text"
              value={nome}
              onChange={(e) => setNome(e.target.value)}
              placeholder="Ex: Atendimento"
              className="w-full px-4 py-3 text-lg border-2 border-gray-300 rounded-lg focus:border-primary"
              disabled={loading}
            />
          </label>

          <label className="block mb-4">
            <span className="text-lg font-semibold mb-2 block">Local:</span>
            <input
              type="text"
              value={local}
              onChange={(e) => setLocal(e.target.value)}
              placeholder="Ex: Balcão 1"
              className="w-full px-4 py-3 text-lg border-2 border-gray-300 rounded-lg focus:border-primary"
              disabled={loading}
            />
          </label>

          <label className="block mb-6">
            <span className="text-lg font-semibold mb-2 block">Tempo médio (min):</span>
            <input
              type="number"
              value={tempo}
              onChange={(e) => setTempo(e.target.value)}
              placeholder="Ex: 15"
              className="w-full px-4 py-3 text-lg border-2 border-gray-300 rounded-lg focus:border-primary"
              disabled={loading}
              min="1"
            />
          </label>

          {erro && <p className="text-danger text-base mb-4">{erro}</p>}

          <Button onClick={handleCriar} loading={loading}>
            Criar fila
          </Button>
        </Card>
      </div>
    </>
  )
}
