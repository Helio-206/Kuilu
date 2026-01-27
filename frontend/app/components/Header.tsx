'use client'

import React from 'react'

interface HeaderProps {
  title: string
  subtitle?: string
  showBack?: boolean
  onBack?: () => void
}

export function Header({ title, subtitle, showBack = false, onBack }: HeaderProps) {
  return (
    <div className="bg-primary text-white p-6 rounded-b-lg">
      <div className="max-w-md mx-auto">
        {showBack && onBack && (
          <button
            onClick={onBack}
            className="mb-4 text-lg font-semibold hover:opacity-80"
            aria-label="Voltar"
          >
            ← Voltar
          </button>
        )}
        <h1 className="text-3xl font-bold">{title}</h1>
        {subtitle && <p className="text-base opacity-90 mt-2">{subtitle}</p>}
      </div>
    </div>
  )
}
