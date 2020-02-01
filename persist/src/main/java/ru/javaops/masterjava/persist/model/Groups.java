package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Groups extends BaseEntity {
    private @NonNull String name;
    private @NonNull GroupFlag flag;
    @Column("project_id")
    private @NonNull Integer project_id;

    public Groups(Integer id, String name, GroupFlag flag, Integer project_id) {
        this(name,flag, project_id);
        this.id=id;
    }
}