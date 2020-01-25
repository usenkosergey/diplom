package ru.skillbox.diplom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.entities.PostVotes;
import ru.skillbox.diplom.repositories.VotesRepositori;

@Service
public class VotesService {
    @Autowired
    private VotesRepositori votesRepositori;

    public boolean addVote(int postId, byte vote){
//        PostVotes postVotes = new PostVotes();
//        postVotes.setPostId(postId);
//        postVotes.setUserId(1);
//        postVotes.setValue(vote);
//        postVotes.setTime(System.currentTimeMillis());
        try {
//            votesRepositori.save(postVotes);
            return true; //TODO нужно переделать
        } catch (Exception e) {
            return false; //TODO нужно переделать
        }

    }
}
