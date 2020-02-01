package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Groups extends BaseEntity {
    private @NonNull String name;

    public Groups(Integer id, String name) {
        this(name);
        this.id=id;
    }
}