import type { Meta, StoryObj } from "@storybook/react";
import SearchInput from "@/components/Navigation/SearchInput/SearchInput";
import styled from "styled-components";

const StoryContainer = styled.div`
    width: 100%;
    max-width: 1200px;
    margin: 0 auto;
    display: flex;
    border: 1px solid #e9e7e7;
    border-radius: 8px;
    padding: 5px;
    align-items: center;
`;

const meta = {
    title: "Components/SearchInput",
    component: SearchInput,
    decorators: [
        (Story) => (
            <StoryContainer>
                <Story />
            </StoryContainer>
        ),
    ],
    parameters: {
        layout: "centered",
    },
} as Meta<typeof SearchInput>;

export default meta;

type Story = StoryObj<typeof meta>;

export const Default: Story = {};
