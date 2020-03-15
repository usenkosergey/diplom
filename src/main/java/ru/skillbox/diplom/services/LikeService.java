package ru.skillbox.diplom.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.Mapper.Constant;
import ru.skillbox.diplom.api.requests.LikeRequest;
import ru.skillbox.diplom.entities.PostVotes;
import ru.skillbox.diplom.repositories.VotesRepositori;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

@Service
public class LikeService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private VotesRepositori votesRepositori;

    @Autowired
    private HttpServletRequest reg;

    //TODO можно как то переделать оба метода в один
    public ResponseEntity<Map> like(LikeRequest likeRequest) {
        logger.info("like");
        if (Constant.auth.size() == 0) {
            return new ResponseEntity<>(Constant.responseFalse(), HttpStatus.OK);
        }
        Optional<PostVotes> postVotes = votesRepositori.likeForPost(likeRequest.getPost_id()
                , Constant.auth.get(reg.getSession().getId()));

        if (postVotes.isPresent() && postVotes.get().getValue() == 1) {
            return new ResponseEntity<>(Constant.responseFalse(), HttpStatus.OK);
        } else if (postVotes.isPresent() && postVotes.get().getValue() == -1) {
            votesRepositori.deleteById(postVotes.get().getId());
            votesRepositori.save(createVotes(likeRequest.getPost_id(),
                    Constant.auth.get(reg.getSession().getId()),
                    1));
//            PostVotes upPostVotes = postVotes.get();//TODO почему то шлючит фронт
//            upPostVotes.setValue(1);
//            votesRepositori.save(upPostVotes);
        } else {
            votesRepositori.save(createVotes(likeRequest.getPost_id(),
                    Constant.auth.get(reg.getSession().getId()),
                    1));

        }
        return new ResponseEntity<>(Constant.responseTrue(), HttpStatus.OK);
    }

    //TODO можно как то переделать оба метода в один
    public ResponseEntity<Map> dislike(LikeRequest likeRequest) {
        logger.info("dislike");
        if (Constant.auth.size() == 0) {
            return new ResponseEntity<>(Constant.responseFalse(), HttpStatus.OK);
        }
        Optional<PostVotes> postVotes = votesRepositori.likeForPost(likeRequest.getPost_id()
                , Constant.auth.get(reg.getSession().getId()));

        if (postVotes.isPresent() && postVotes.get().getValue() == -1) {
            return new ResponseEntity<>(Constant.responseFalse(), HttpStatus.OK);
        } else if (postVotes.isPresent() && postVotes.get().getValue() == 1) {
            votesRepositori.deleteById(postVotes.get().getId());
            votesRepositori.save(createVotes(likeRequest.getPost_id(),
                    Constant.auth.get(reg.getSession().getId()),
                    -1));
//            PostVotes upPostVotes = postVotes.get();//TODO почему то шлючит фронт
//            upPostVotes.setValue(-1);
//            votesRepositori.save(upPostVotes);
        } else {
            votesRepositori.save(createVotes(likeRequest.getPost_id(),
                    Constant.auth.get(reg.getSession().getId()),
                    -1));
        }
        return new ResponseEntity<>(Constant.responseTrue(), HttpStatus.OK);
    }

    private static PostVotes createVotes(int post_id, int user_id, int value) {
        PostVotes newLike = new PostVotes();
        newLike.setPostId(post_id);
        newLike.setUserId(user_id);
        newLike.setValue(value);
        newLike.setTime(System.currentTimeMillis());
        return newLike;
    }
}
