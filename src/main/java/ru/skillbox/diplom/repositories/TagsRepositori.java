package ru.skillbox.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.entities.Tag;

@Repository
public interface TagsRepositori extends JpaRepository<Tag, Integer> {

    @Query(nativeQuery = true,
            value = "INSERT INTO tags (name) values (:tagNew) ON CONFLICT (name) DO UPDATE SET name = EXCLUDED.name RETURNING id;")
    Integer addNewTag(@Param("tagNew") String tag);

    /*
    select new.tags.name, count(*)
From new.tag2post JOIN new.tags
on new.tags.id = new.tag2post.tag_id GROUP BY new.tags.name;
     */
}
