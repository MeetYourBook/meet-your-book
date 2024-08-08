import type { Meta, StoryObj } from "@storybook/react";
import SearchInput from "@/components/Navigation/SearchInput/SearchInput";

const meta = {
    title: "Components/SearchInput",
    component: SearchInput,
    parameters: {
        layout: "centered",
    },
} as Meta<typeof SearchInput>;

export default meta;

type Story = StoryObj<typeof meta>;

export const Default: Story = {};
