import { ViewType } from "../BookDisplay";
import { MenuOutlined, AppstoreOutlined } from "@ant-design/icons";

interface ViewSelectorProps {
    setViewMode: React.Dispatch<React.SetStateAction<ViewType>>;
    viewMode: ViewType;
}

const ViewSelector = ({ viewMode, setViewMode }: ViewSelectorProps) => {
    return (
        <div className="flex justify-between mb-4">
            <h1 className="text-3xl font-extrabold font-logo">Books</h1>
            <div className="flex gap-3 mr-2">
                <button onClick={() => setViewMode("list")}>
                    <MenuOutlined className={`${viewMode === "list" ? "text-purple-500" : "text-gray-400"}`} />
                </button>
                <button onClick={() => setViewMode("grid")}>
                    <AppstoreOutlined className={`${viewMode === "grid" ? "text-purple-500" : "text-gray-400"}`} />
                </button>
            </div>
        </div>
    );
};

export default ViewSelector;
