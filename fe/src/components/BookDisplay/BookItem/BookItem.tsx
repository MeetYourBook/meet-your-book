import { useEffect, useState } from "react";
import { BookType, ViewType } from "../BookDisplay";
import '../../../index.css';

interface BookItemProps {
    bookData: BookType;
    viewMode: ViewType;
}

const BookItem = ({ bookData, viewMode }: BookItemProps) => {
    const [isVisible, setIsVisible] = useState(false);

    useEffect(() => {
        const timer = setTimeout(() => {
            setIsVisible(true);
        }, 100);

        return () => clearTimeout(timer);
    }, []);

    return (
        <>
            {viewMode === "grid" ? (
                <div className={`card-animation w-[150px] ${isVisible ? 'opacity-100' : 'opacity-0'}`}>
                    <img
                        src={`http://${bookData.image_url}`}
                        alt={bookData.title}
                        className="object-contain h-48 w-full rounded-md mx-auto mb-2"
                    />
                    <div className="text-center w-full">
                        <h3 className="font-bold text-sm mb-1 truncate w-full">
                            {bookData.title}
                        </h3>
                        <p className="text-xs truncate w-full">
                            {bookData.author}
                        </p>
                    </div>
                </div>
            ) : (
                <div className={`card-animation w-full flex mb-4 ${isVisible ? 'opacity-100' : 'opacity-0'}`}>
                    <img
                        src={`http://${bookData.image_url}`}
                        alt={bookData.title}
                        className="object-contain h-48 rounded-md mx-auto w-[132px]"
                    />
                    <div className="w-full text-left pl-4 my-auto">
                        <div>
                            <h3 className="font-bold text-xl mb-2">
                                {bookData.title}
                            </h3>
                            <p className="text-sm text-gray-600 mb-1">
                                {bookData.author}
                            </p>
                        </div>
                        <div className="mt-2">
                            <p className="text-xs text-gray-500">
                                {bookData.provider}
                            </p>
                            <p className="text-xs text-gray-500">
                                {bookData.publisher}
                            </p>
                            <p className="text-xs text-gray-500">
                                {bookData.publish_date}
                            </p>
                        </div>
                    </div>
                </div>
            )}
        </>
    );
};

export default BookItem;
