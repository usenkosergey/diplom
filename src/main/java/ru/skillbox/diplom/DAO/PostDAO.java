package ru.skillbox.diplom.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import ru.skillbox.diplom.entities.Comment;
import ru.skillbox.diplom.entities.Post;
import ru.skillbox.diplom.entities.Tag;
import ru.skillbox.diplom.entities.User;
import ru.skillbox.diplom.repositories.CommentRepositori;
import ru.skillbox.diplom.repositories.PostRepositori;
import ru.skillbox.diplom.repositories.VotesRepositori;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

public class PostDAO {
    private int postId;
    private LocalDate time;
    private User user;
    private String title;
    private String text;
    private int likeCount;
    private int dislikeCount;
    private int viewCount;
    private List<Comment> comments;
    private List<Tag> tags;

    @Autowired
    private PostRepositori postRepositori;

    @Autowired
    private VotesRepositori votesRepositori;

    @Autowired
    private CommentRepositori commentRepositori;

    public PostDAO(int postId) {
        Optional<Post> currentPost = postRepositori.findById(postId);
        this.postId = postId;
        this.time = Instant.ofEpochMilli(currentPost.get().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        this.user = currentPost.get().getUser();
        this.title = currentPost.get().getTitle();
        this.text = currentPost.get().getText();
        this.likeCount = votesRepositori.likeCount(postId);
        this.dislikeCount = votesRepositori.dislikeCount(postId);
        this.viewCount = currentPost.get().getViewCount();
        this.comments = commentRepositori.findBypostId(postId);
        this.tags = (List<Tag>) currentPost.get().getTags();

    }
}
