import type { Metadata, Viewport } from 'next'
import './globals.css'

export const metadata: Metadata = {
  title: 'Kuilu - Sua vez, sem perder tempo',
  description: 'Sistema digital de gestão de filas',
  manifest: '/manifest.json',
  appleWebApp: {
    capable: true,
    statusBarStyle: 'black-translucent',
    title: 'Kuilu',
  },
}

export const viewport: Viewport = {
  width: 'device-width',
  initialScale: 1,
  maximumScale: 1,
  themeColor: '#1e3a8a',
}

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="pt-AO">
      <head>
        <meta name="theme-color" content="#1e3a8a" />
        <link rel="icon" href="/icon-192.png" />
      </head>
      <body className="bg-gray-100">
        <div className="max-w-md mx-auto bg-white min-h-screen flex flex-col">
          {children}
        </div>
      </body>
    </html>
  )
}
