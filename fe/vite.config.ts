import { defineConfig, loadEnv } from 'vite';
import react from '@vitejs/plugin-react';
import path from 'path';

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');
  
  return {
    plugins: [react()],
    server: {
      proxy: {
        '/dev': {
          target: env.VITE_ORIGINAL_API_URL,
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/dev/, ''),
          secure: false,
          ws: true
        }
      }
    },
    resolve: {
      alias: [
        { find: '@', replacement: path.resolve(__dirname, 'src') },
      ],
    },
    build: {
      target: 'esnext',
      rollupOptions: {
        external: ['msw-storybook-addon'],
      },
    },
    test: {
      globals: true,
      environment: 'jsdom',
      setupFiles: './src/tests/setup.ts',
      coverage: {
        reporter: ['text', 'lcov'],
        reportsDirectory: 'coverage',
        include: [
          'src/pages/**/*.{ts,tsx}',
          'src/hooks/**/*.{ts,tsx}',
          'src/components/**/*.{ts,tsx}',
        ],
      },
    },
  };
});