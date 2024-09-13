import { ActiveType } from "@/types/Admin";

export const DROP_DOWN_INITIAL_ITEMS = [
    { value: "all", label: "통합 검색" },
    { value: "title", label: "제목" },
    { value: "author", label: "저자" },
    { value: "publisher", label: "출판사" },
];

export const ERROR_MESSAGE = {
    LOGIN_ERROR: "로그인에 실패했습니다! 아이디, 비밀번호를 다시 확인해주세요!",
    LOGIN_REQUIRED_MESSAGE: "로그인이 필요합니다.",
    EMPTY_INPUT: "검색어를 입력해주세요."
}

export const SUCCESS_MESSAGE = {
    LOGIN_SUCCESS: "로그인 성공!"
}

export const ADMIN_MENU_VALUE: ActiveType[] = [
    "books",
    "libraries",
    "crawling",
    "LibraryBooksCount",
];

export const BOOKS_TABLE_ITEM = ["title" , "author", "publisher", "publishDate", ]

export const PAGINATION_FIRST_PAGE = 1
export const FIRST_PAGE = 0;
export const ANIMATION_TIME = 100;
export const LIBRARIES_PER_PAGE = 5;
export const LIBRARIES_FILTER_PER_PAGE = 20;
export const DEBOUNCE_TIME = 300;