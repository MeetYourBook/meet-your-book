# Meet Your Book

- 라이브러리 필터 컴포넌트: 성능 문제와 최적화

- 팀 프로젝트로 진행중인 Meet Your Book 사이트에서 라이브러리 필터 컴포넌트에서 마주친 성능 문제와 그 해결 방안에 대해 이야기해보려 합니다.

문제 상황

- 먼저, 제가 구현한 LibraryFilter 컴포넌트의 초기 코드를 간단히 살펴보겠습니다.

```
import { useLibraryFilter } from "@/hooks/useLibraryFilter";
import { DownOutlined, UpOutlined } from "@ant-design/icons";
import * as S from "@/styles/LibraryFilterStyle";
import LibraryList from "../LibraryList/LibraryList";
import LoadingFallBack from "@/components/LoadingFallBack/LoadingFallBack";
import { LibrariesType } from "@/types/Libraries";

const LibraryFilter = () => {
    const {
        isOpen,
        searchValue,
        librariesFilter,
        handleSelectLibrary,
        toggleFilter,
        handleSearch,
        isLoading,
        getDisplayLibraries,
        observerRef
    } = useLibraryFilter();
    
    return (
        <S.Container>
            <S.Header onClick={toggleFilter}>
                <S.Title>도서관 필터</S.Title>
                {isOpen ? <UpOutlined /> : <DownOutlined />}
            </S.Header>
            <S.ListWrap $isOpen={isOpen}>
                <S.Input
                    value={searchValue}
                    onChange={handleSearch}
                    placeholder="도서관 검색..."
                />
                {isLoading && <LoadingFallBack/>}
                <LibraryList
                    libraries={getDisplayLibraries as LibrariesType[]}
                    librariesFilter={librariesFilter}
                    handleSelectLibrary={handleSelectLibrary}
                    />
                <div ref={observerRef} style={{ height: "10px" }} />
            </S.ListWrap>
        </S.Container>
    );
};

export default LibraryFilter;

```

- 이 컴포넌트는 도서관 목록을 필터링하고 검색하는 기능을 제공합니다. 처음에는 잘 작동하는 것처럼 보였지만, 실제 사용 중에 성능 문제가 발생했습니다.

## 초기 최적화 시도와 한계
- 성능 문제를 해결하기 위해 처음에는 렌더링 최적화에 초점을 맞췄습니다:

1. 데이터 분할: 5000개의 도서관 데이터를 한 번에 렌더링하는 대신, 20개씩 나누어 보여주도록 구현했습니다.
2. 무한 스크롤: 사용자가 스크롤을 내릴 때마다 추가 데이터를 로드하는 무한 스크롤 기능을 구현했습니다.

- 이러한 최적화를 통해 초기 렌더링 속도는 개선되었습니다. 하지만 여전히 사용자 입력에 대한 반응이 느렸고, 특히 검색 기능을 사용할 때 현저한 지연이 발생했습니다.

## 트러블 슈팅
- 성능 문제의 실제 원인은 다음과 같았습니다:

1. 대량의 데이터 로딩: 약 120KB 크기의 데이터를 한 번에 모두 메모리에 불러왔습니다. 이 데이터에는 5000개의 도서관 정보가 포함되어 있었습니다.
2. 전체 데이터에 대한 필터링 연산: 사용자가 검색창에 입력할 때마다 5000개의 도서관 데이터 전체에 대해 필터링 연산을 수행했습니다.

- 초기에 구현한 렌더링 최적화(20개씩 나누어 보여주기, 무한 스크롤)는 화면에 표시되는 항목의 수를 줄이는 데는 효과적이었습니다. 하지만 이는 근본적인 문제를 해결하지 못했습니다. 실제로 사용자가 검색어를 입력할 때마다 백그라운드에서는 여전히 5000개의 전체 데이터를 대상으로 필터링 연산이 수행되고 있었던 것입니다.

## 최종 해결 방안
- 문제의 근본적인 원인을 파악한 후, 다음과 같은 해결 방안을 적용했습니다:

### 입력 컴포넌트 분리 및 디바운스 적용:
- 검색 입력 컴포넌트를 별도로 분리하고, 디바운스 기능을 추가했습니다. 이를 통해 사용자가 타이핑을 멈춘 후 일정 시간(예: 300ms)이 지난 후에만 검색 쿼리를 서버로 전송하도록 구현했습니다.

#### input 분리

```
import { useLibraryFilter } from "@/hooks/useLibraryFilter";
import { DownOutlined, UpOutlined } from "@ant-design/icons";
import * as S from "@/styles/LibraryFilterStyle";
import LibraryList from "../LibraryList/LibraryList";
import LoadingFallBack from "@/components/LoadingFallBack/LoadingFallBack";
import { LibrariesType } from "@/types/Libraries";
import FilterInput from "../FilterInput/FilterInput";
const LibraryFilter = () => {
    const {
        isOpen,
        setDebounceValue,
        toggleFilter,
    } = useLibraryFilter();
    
    return (
        <S.Container>
            <S.Header onClick={toggleFilter}>
                <S.Title>도서관 필터</S.Title>
                {isOpen ? <UpOutlined /> : <DownOutlined />}
            </S.Header>
            <S.ListWrap $isOpen={isOpen}>
                <FilterInput setDebounceValue={setDebounceValue}/>
                {isLoading && <LoadingFallBack/>}
                <LibraryList
                    libraries={getDisplayLibraries as LibrariesType[]}
                    librariesFilter={librariesFilter}
                    handleSelectLibrary={handleSelectLibrary}
                    />
                <div ref={observerRef} style={{ height: "10px" }} />
            </S.ListWrap>
        </S.Container>
    );
};

export default LibraryFilter;

```

#### 분리한 인풋에 디바운스 적용
```
import React, { Dispatch, SetStateAction, useEffect, useState } from "react";
import styled from "styled-components";
import { DEBOUNCE_TIME } from "@/constants";

interface FilterInputProps {
    setDebounceValue: Dispatch<SetStateAction<string>>;
}
const FilterInput = ({ setDebounceValue }: FilterInputProps) => {
    const [searchValue, setSearchValue] = useState("");

    const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) =>
        setSearchValue(e.target.value);

    useEffect(() => {
        const timer = setTimeout(() => setDebounceValue(searchValue), DEBOUNCE_TIME); // 3초뒤에 debounceValue를 변경해 debounceValue이 변경될때만 서버에 데이터 전송
        
        return () => clearTimeout(timer);
    }, [searchValue, setDebounceValue]);

    return (
        <Input
            value={searchValue}
            onChange={handleSearch}
            placeholder="도서관 검색..."
        />
    );
};

export default FilterInput;

const Input = styled.input`
    width: 90%;
    height: 2rem;
    border: ${({ theme }) => theme.border};
    border-radius: 8px;
    margin: 0px auto 1rem;
    padding: 0 0.6rem;
    outline: none;
    background-color: ${({ theme }) => theme.input};
    color: ${({ theme }) => theme.text};
`;

```

다음과 같이 잘 적용되는 모습입니다.


### 서버 사이드 필터링 구현:
- 클라이언트에서 전체 데이터를 필터링하는 대신, 서버에 검색 쿼리를 보내고 필터링된 결과만 받아오는 방식으로 변경했습니다. 이를 통해 클라이언트의 메모리 부담과 연산량을 크게 줄일 수 있었습니다.