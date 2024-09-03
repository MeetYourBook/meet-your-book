import styled from "styled-components";

const ListItem = styled.li`
    width: 100%;
    display: flex;
    align-items: center;
    margin-bottom: 0.5rem;
`;

const Checkbox = styled.input`
    margin-right: 0.5rem;
    flex-shrink: 0;
    appearance: none;
    width: 1rem;
    height: 1rem;
    border: 1px solid gainsboro;
    border-radius: 0.35rem;
    &:checked {
        border-color: transparent;
        background-image: url("data:image/svg+xml,%3csvg viewBox='0 0 16 16' fill='white' xmlns='http://www.w3.org/2000/svg'%3e%3cpath d='M5.707 7.293a1 1 0 0 0-1.414 1.414l2 2a1 1 0 0 0 1.414 0l4-4a1 1 0 0 0-1.414-1.414L7 8.586 5.707 7.293z'/%3e%3c/svg%3e");
        background-size: 100% 100%;
        background-position: 50%;
        background-repeat: no-repeat;
        background-color: #0064ff;
    }
`;

const Label = styled.label`
    flex-grow: 1;
    font-size: 0.9rem;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
`;

export { ListItem, Checkbox, Label };
