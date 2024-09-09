import { vi } from "vitest";
import * as utils from '@/utils';
import { fireEvent, render } from "@testing-library/react";
import FavoriteBtn from "@/components/FavoriteBtn/FavoriteBtn";
const mockLocalStorage = (() => {
    let store: { [key: string]: string } = {};
    return {
        getItem: (key: string) => store[key] || null,
        setItem: (key: string, value: string) => { store[key] = value.toString() },
        clear: () => { store = {}},
    };
})();

Object.defineProperty(window, "localStorage", {value: mockLocalStorage})

describe("FavoriteBtn 컴포넌트 테스트", () => {
    const mockItem = { id: "1", name: "Test Item" };
    const mockStorageName = "libraries";

    beforeEach(() => {
        mockLocalStorage.clear();
        vi.spyOn(utils, 'getStorage').mockReturnValue([]);
        vi.spyOn(utils, 'hasFavoriteItem').mockReturnValue(false);
    });
    
    test("버튼 클릭시 로컬스토리지에 데이터가 저장되는지 확인.", () => {
        const {container} = render(<FavoriteBtn item={mockItem} storageName={mockStorageName}/>)
        const button = container.firstChild as HTMLElement;
        fireEvent.click(button);
        const storedData = JSON.parse(localStorage.getItem(mockStorageName) || '[]');
        expect(storedData).toContainEqual(mockItem);
    })

    test('이미 저장되어 있는 데이터를 클릭 시 로컬스토리지에서 없어지는지 확인.', () => {
        vi.spyOn(utils, 'hasFavoriteItem').mockReturnValue(true);
        localStorage.setItem(mockStorageName, JSON.stringify([mockItem]));
    
        const { container } = render(<FavoriteBtn item={mockItem} storageName={mockStorageName as "libraries" | "books"} />);
        
        const button = container.firstChild as HTMLElement;
        fireEvent.click(button);
    
        const storedData = JSON.parse(localStorage.getItem(mockStorageName) || '[]');
        expect(storedData).not.toContainEqual(mockItem);
    });
})
