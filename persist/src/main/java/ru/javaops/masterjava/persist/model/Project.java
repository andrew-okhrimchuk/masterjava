package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Project extends BaseEntity {
    private @NonNull String name;
    private @NonNull String description;
    @Column("groups_id")
    private @NonNull Integer groups_id;

    public Project(Integer id, String name, String description, Integer groups_id) {
        this(name, description, groups_id);
        this.id=id;
    }

}