'use client'

import { useEffect, useState } from 'react'
import { useRouter } from 'next/navigation'
import { Button, Card, Header, Loader } from '@/app/components'
import { obterFila, obterPosicao } from '@/app/services/api'
import type { Fila, PosicaoFila } from '@/app/types'

interface FilaPageProps {
  params: {
    id: string
  }
}

export default function FilaPage({ params }: FilaPageProps) {
  const router = useRouter()
  const [fila, setFila] = useState<Fila | null>(null)
  const [posicao, setPosicao] = useState<PosicaoFila | null>(null)
  const [loading, setLoading] = useState(true)
  const [erro, setErro] = useState('')
  const [usuarioId, setUsuarioId] = useState('')

  useEffect(() => {
    const uid = localStorage.getItem('usuarioId') || crypto.randomUUID()
    setUsuarioId(uid)
    localStorage.setItem('usuarioId', uid)
  }, [])

  useEffect(() => {
    if (!usuarioId) return

    const carregarDados = async () => {
      try {
        setLoading(true)
        const filaData = await obterFila(params.id)
        setFila(filaData)

        const token = localStorage.getItem('token') || 'demo-token'
        const posicaoData = await obterPosicao(params.id, usuarioId, token)
        setPosicao(posicaoData)
      } catch (err) {
        setErro('Erro ao carregar fila')
      } finally {
        setLoading(false)
      }
    }

    carregarDados()

    // Polling a cada 5 segundos
    const interval = setInterval(carregarDados, 5000)
    return () => clearInterval(interval)
  }, [params.id, usuarioId])

  if (loading) {
    return (
      <>
        <Header title="Carregando..." />
        <div className="flex-1 flex items-center justify-center">
          <Loader />
        </div>
      </>
    )
  }

  if (erro || !fila || !posicao) {
    return (
      <>
        <Header title="Erro" />
        <div className="flex-1 flex flex-col justify-center p-6">
          <Card className="mb-6">
            <p className="text-danger text-lg mb-6">{erro || 'Fila não encontrada'}</p>
            <Button onClick={() => router.push('/')}>Voltar</Button>
          </Card>
        </div>
      </>
    )
  }

  const ehVocadaVez = posicao.posicao === 1

  return (
    <>
      <Header title={fila.nome} subtitle={fila.local} />

      <div className="flex-1 flex flex-col justify-center p-6 gap-6">
        {ehVocadaVez ? (
          <Card className="bg-success text-white text-center py-12">
            <h2 className="text-4xl font-bold mb-4">🎉</h2>
            <p className="text-3xl font-bold">É a sua vez!</p>
          </Card>
        ) : (
          <>
            <Card className="text-center">
              <p className="text-base text-gray-600 mb-2">Sua posição</p>
              <p className="text-6xl font-bold text-primary mb-4">{posicao.posicao}</p>
              <p className="text-base text-gray-600">
                Tempo estimado: <strong>{posicao.tempoEstimadoMinutos} minutos</strong>
              </p>
            </Card>

            <Card>
              <p className="text-base">
                📊 Tempo médio de atendimento: <strong>{fila.tempoMedioAtendimento} min</strong>
              </p>
            </Card>
          </>
        )}

        <Button onClick={() => router.push('/')} variant="secondary">
          Voltar ao início
        </Button>
      </div>
    </>
  )
}
