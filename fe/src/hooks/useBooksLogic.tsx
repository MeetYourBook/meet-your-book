import { useCallback, useEffect, useRef, useState } from "react";
import useGenerateQuery from "./useGenerateQuery";
import useQueryData from "./useQueryData";
import useQueryStore from "@/stores/queryStore";
import useInfiniteScroll from "./useInfiniteScroll";
import { FIRST_PAGE } from "@/constants";

const useBooksLogic = () => {
    const query = useGenerateQuery();
    const { data: books, isLoading, isFetching } = useQueryData(query);
    const { booksItem, setBooksItem, page, setPage } = useQueryStore();
    const [lastPageNum, setLastPageNum] = useState(FIRST_PAGE);
    const observerRef = useRef<HTMLDivElement | null>(null);
    const loadingMore = useRef(false);

    const handleLoadMore = useCallback(() => {
        if (!loadingMore.current && !isFetching && page < lastPageNum) {
            loadingMore.current = true;
            setPage(page + 1);
        }
    }, [isFetching, page, lastPageNum, setPage]);

    const { observe } = useInfiniteScroll(handleLoadMore);

    useEffect(() => {
        if (observerRef.current) {
            observe(observerRef.current);
        }
    }, [observe]);

    useEffect(() => {
        if (books && books.content) {
            if (page === FIRST_PAGE) {
                setBooksItem(books.content);
            } else {
                setBooksItem([...booksItem, ...books.content]);
            }
            setLastPageNum(books.totalPages);
            loadingMore.current = false;
        }
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [books]);

    return {
        booksItem,
        observerRef,
        lastPageNum,
        page,
        isLoading: isLoading || isFetching,
    };
};

export default useBooksLogic;

