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
        try {
            return true; //TODO нужно переделать
        } catch (Exception e) {
            return false; //TODO нужно переделать
        }

    }
}
