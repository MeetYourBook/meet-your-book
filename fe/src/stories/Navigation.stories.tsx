import type { Meta, StoryObj } from "@storybook/react";
import DefaultLayout from "@/components/Layout/DefaultLayout";

const meta = {
    title: "Components/DefaultLayout",
    component: DefaultLayout,
    parameters: {
        layout: "center",
    },
} as Meta<typeof DefaultLayout>;

export default meta;

type Story = StoryObj<typeof meta>;

export const Default: Story = {};




