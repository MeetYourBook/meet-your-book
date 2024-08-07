import type { Meta, StoryObj } from "@storybook/react";
import CounterButton from "@/components/Button/CountButton";

const meta = {
    title: "UI/Button",
    component: CounterButton,
    parameters: {
        layout: "centered",
    },
} as Meta<typeof CounterButton>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {};
