import type { Meta, StoryObj } from "@storybook/react";
import Login from "@/pages/Login";

const meta = {
    title: "pages/Login",
    component: Login,
    parameters: {
        layout: "center",
    },
} as Meta<typeof Login>;

export default meta;

type Story = StoryObj<typeof meta>;

export const Default: Story = {};




