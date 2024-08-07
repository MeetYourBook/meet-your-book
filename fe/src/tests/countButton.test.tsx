import { render, screen, fireEvent } from '@testing-library/react';
import CounterButton from '@/components/Button/CountButton';
import { describe, it, expect, test } from 'vitest';

describe('CounterButton', () => {
  test('should render the button with initial count', () => {
    render(<CounterButton />);
    const button = screen.getByRole('button', { name: /Count: 0/i });
    expect(button).toBeInTheDocument();
  });

  it('should increment the count when clicked', () => {
    render(<CounterButton />);
    const button = screen.getByRole('button', { name: /Count: 0/i });
    fireEvent.click(button);
    expect(button).toHaveTextContent('Count: 1');
  });
});
