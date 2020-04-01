package ru.skillbox.diplom.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tag2post")
public class Tag2post implements Serializable {

    @Id
    @Column(name = "post_id")
    private int postId;

    @Id
    @Column(name = "tag_id")
    private int tagId;

    @ManyToOne(optional=false, cascade= CascadeType.ALL)
    @JoinColumn(name="post_id", updatable = false, insertable = false)
    private Post post;

    @ManyToOne(optional=false, cascade= CascadeType.ALL)
    @JoinColumn(name = "tag_id", updatable = false, insertable = false)
    private Tag tag;
}
