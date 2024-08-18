import { useEffect, useRef } from "react";

const useInfiniteScroll = (callback: () => void) => {
    const observer = useRef<IntersectionObserver | null>(null);

    const observe = (element: Element) => {
        if (observer.current) observer.current.disconnect();

        observer.current = new IntersectionObserver((entries) => {
            const entry = entries[0];
            if (entry.isIntersecting) {
                callback();
            }
        }, { threshold: 0.1 });

        if (element) observer.current.observe(element);
    };

    useEffect(() => {
        return () => {
            if (observer.current) observer.current.disconnect();
        };
    }, []);

    return { observe };
};

export default useInfiniteScroll;
