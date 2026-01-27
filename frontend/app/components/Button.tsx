'use client'

import React from 'react'

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'danger'
  size?: 'sm' | 'md' | 'lg'
  loading?: boolean
  children: React.ReactNode
}

export function Button({
  variant = 'primary',
  size = 'lg',
  loading = false,
  disabled = false,
  children,
  className = '',
  ...props
}: ButtonProps) {
  const baseStyles = 'font-semibold rounded-lg transition-colors w-full'

  const variantStyles = {
    primary: 'bg-primary text-white hover:bg-blue-700 disabled:bg-gray-400',
    secondary: 'bg-secondary text-white hover:bg-lime-500 disabled:bg-gray-400',
    danger: 'bg-danger text-white hover:bg-red-600 disabled:bg-gray-400',
  }

  const sizeStyles = {
    sm: 'px-3 py-2 text-base',
    md: 'px-4 py-3 text-lg',
    lg: 'px-6 py-4 text-2xl',
  }

  return (
    <button
      disabled={disabled || loading}
      className={`${baseStyles} ${variantStyles[variant]} ${sizeStyles[size]} ${className}`}
      {...props}
    >
      {loading ? '...' : children}
    </button>
  )
}
