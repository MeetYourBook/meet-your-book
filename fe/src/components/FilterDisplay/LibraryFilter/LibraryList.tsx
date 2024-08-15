import React from "react";
import { LibrariesType } from "@/types/Libraries";
import * as S from "@/styles/LibraryListStyle"

interface LibraryListProps {
    libraries: LibrariesType[];
    librariesFilter: string[];
    handleSelectLibrary: (id: string) => void;
}

const LibraryList = React.memo(
    ({ libraries, librariesFilter, handleSelectLibrary }: LibraryListProps) => (
        <>
            {libraries.map((library, index) => (
                <S.ListItem key={library.id}>
                    <S.Checkbox
                        type="checkbox"
                        id={`library-${index}`}
                        checked={librariesFilter.includes(library.id)}
                        onChange={() => handleSelectLibrary(library.id)}
                    />
                    <S.Label htmlFor={`library-${index}`}>
                        {library.name}
                    </S.Label>
                </S.ListItem>
            ))}
        </>
    )
);

export default LibraryList