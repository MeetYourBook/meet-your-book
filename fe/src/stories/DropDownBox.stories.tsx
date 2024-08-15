import type { Meta, StoryObj } from "@storybook/react";
import DropDownBox from "@/components/Navigation/DropDownBox/DropDownBox";

const meta = {
    title: "Components/DropDownBox",
    component: DropDownBox,
    parameters: {
        layout: "centered",
    },
} as Meta<typeof DropDownBox>;

export default meta;

type Story = StoryObj<typeof meta>;

export const Default: Story = {};




