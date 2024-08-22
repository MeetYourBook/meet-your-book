import type { Meta, StoryObj } from "@storybook/react";
import SignUp from "@/pages/SignUp";

const meta = {
    title: "pages/SignUp",
    component: SignUp,
    parameters: {
        layout: "center",
    },
} as Meta<typeof SignUp>;

export default meta;

type Story = StoryObj<typeof meta>;

export const Default: Story = {};




