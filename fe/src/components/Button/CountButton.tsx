import React, { useState } from 'react';

const CounterButton: React.FC = () => {
  const [count, setCount] = useState(0);

  const handleClick = () => {
    setCount(count + 1);
  };

  return (
    <button onClick={handleClick}>
      Count: {count}
    </button>
  );
};

export default CounterButton;
