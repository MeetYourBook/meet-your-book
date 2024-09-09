import { useCallback, useEffect, useRef, useState } from "react";
import useGenerateQuery from "./useGenerateQuery";
import useQueryStore from "@/stores/queryStore";
import useInfiniteScroll from "./useInfiniteScroll";
import { FIRST_PAGE } from "@/constants";
import useBooksQuery from "./queries/useBooksQuery";

const useBooksLogic = () => {
    const query = useGenerateQuery();
    const { data: books, isLoading, isFetching } = useBooksQuery(query);
    const { booksItem, setBooksItem, page, setPage } = useQueryStore();
    const [lastPageNum, setLastPageNum] = useState(FIRST_PAGE);
    const loadingMore = useRef(false);

    const handleLoadMore = useCallback(() => {
        if (!loadingMore.current && !isFetching && page < lastPageNum) {
            loadingMore.current = true;
            setPage(page + 1);
        }
    }, [isFetching, page, lastPageNum, setPage]);

    const { observe } = useInfiniteScroll(handleLoadMore);

    const observerRefCallback = useCallback((node: HTMLDivElement) => {
        if (node) observe(node);
    }, [observe]);


    useEffect(() => {
        if (books && books.content) {
            setBooksItem(page === FIRST_PAGE ? books.content : [...booksItem, ...books.content])
            setLastPageNum(books.totalPages);
            loadingMore.current = false;
        }
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [books]);

    return {
        booksItem,
        observerRefCallback,
        lastPageNum,
        page,
        isLoading: isLoading || isFetching,
    };
};

export default useBooksLogic;

