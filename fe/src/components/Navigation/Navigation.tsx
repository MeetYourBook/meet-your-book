import useSearchStore from "../../stores/search";
import { SearchOutlined } from "@ant-design/icons";

type HandleType =
    | React.FormEvent<HTMLFormElement>
    | React.MouseEvent<HTMLButtonElement, MouseEvent>;

const Navigation = () => {
    const { search, setSearch } = useSearchStore();

    const handleSearch = (e: HandleType) => {
        e.preventDefault();
        console.log(search);
    };

    return (
        <nav className="bg-white border-b border-gray-300 py-2 mb-10">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex items-center justify-between h-16">
                    <div className="flex items-center">
                        <div className="text-center">
                            <h1 className="text-3xl font-extrabold font-logo">
                                MYB
                            </h1>
                            <p className="text-xs font-sans italic text-gray-500">
                                Meet Your Book
                            </p>
                        </div>
                    </div>
                    <div className="flex-1 flex justify-center px-2 lg:ml-6 lg:justify-center">
                        <div className="w-full max-w-2xl">
                            <form onSubmit={handleSearch} className="relative">
                                <input
                                    type="text"
                                    className="block w-full pr-10 pl-4 py-2 border border-gray-300 rounded-lg focus:outline-none"
                                    placeholder="Search for books..."
                                    value={search}
                                    onChange={(e) => setSearch(e.target.value)}
                                />
                                <div className="absolute inset-y-0 right-0 flex items-center border-l border-gray-300">
                                    <button
                                        type="submit"
                                        className="px-4 flex items-center justify-center h-full"
                                    >
                                        <SearchOutlined className="text-gray-400" style={{ fontSize: '20px' }} />
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </nav>
    );
};

export default Navigation;
