import type { Meta, StoryObj } from "@storybook/react";
import LibraryFilter from "@/components/FilterDisplay/LibraryFilter/LibraryFilter";

const meta = {
    title: "Components/LibraryFilter",
    component: LibraryFilter,
    parameters: {
        layout: "centered",
    },
} as Meta<typeof LibraryFilter>;

export default meta;

type Story = StoryObj<typeof meta>;

export const Default: Story = {};




