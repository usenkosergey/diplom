package ru.skillbox.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.entities.Tag;

@Repository
public interface TagsRepositori extends JpaRepository<Tag, Integer> {

    @Query(nativeQuery = true,
            value = "INSERT INTO new.tags (name) values (:tagNew) ON CONFLICT (name) DO UPDATE SET name = EXCLUDED.name RETURNING id;")
    Integer addNewTag(@Param("tagNew") String tag);

}
