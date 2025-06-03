package ru.job4j.lombok;

import lombok.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public class Category {
    @Getter
    @NonNull
    @EqualsAndHashCode.Include
    private Long id;

    @Getter
    @Setter
    private String name;
}
