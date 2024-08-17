import type { Meta, StoryObj } from "@storybook/react";
import BooksDisplay from "@/components/BooksDisplay/BooksDisplay";

const meta = {
    title: "Components/BooksDisplay",
    component: BooksDisplay,
    parameters: {
        layout: "centered",
    },
} as Meta<typeof BooksDisplay>;

export default meta;

type Story = StoryObj<typeof meta>;

export const Default: Story = {};




