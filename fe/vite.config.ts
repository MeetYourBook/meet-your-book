import { defineConfig } from 'vitest/config';
import react from '@vitejs/plugin-react';
import path from 'path';

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: [
      { find: '@', replacement: path.resolve(__dirname, 'src') },
    ],
  },
  build: {
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
});
