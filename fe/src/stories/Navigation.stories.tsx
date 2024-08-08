import type { Meta, StoryObj } from "@storybook/react";
import Navigation from "@/components/Navigation/Navigation";

const meta = {
    title: "Components/Navigation",
    component: Navigation,
    parameters: {
        layout: "centered",
    },
} as Meta<typeof Navigation>;

export default meta;

type Story = StoryObj<typeof meta>;

export const Default: Story = {};
