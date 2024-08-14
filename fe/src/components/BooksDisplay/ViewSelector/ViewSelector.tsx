import { ViewType } from "@/types/View";
import { MenuOutlined, AppstoreOutlined } from "@ant-design/icons";
import * as S from "@/styles/ViewSelectorStyle"
interface ViewSelectorProps {
    setViewMode: React.Dispatch<React.SetStateAction<ViewType>>;
    viewMode: ViewType;
}

const ViewSelector = ({ viewMode, setViewMode }: ViewSelectorProps) => {
    return (
        <S.Container>
            <S.Title>Books</S.Title>
            <S.IconButtonGroup>
                <S.ListButton
                    $active={viewMode}
                    onClick={() => setViewMode("list")}
                >
                    <MenuOutlined />
                </S.ListButton>
                <S.GridButton
                    $active={viewMode}
                    onClick={() => setViewMode("grid")}
                >
                    <AppstoreOutlined />
                </S.GridButton>
            </S.IconButtonGroup>
        </S.Container>
    );
};

export default ViewSelector;
