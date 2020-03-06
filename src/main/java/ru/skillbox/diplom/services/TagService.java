package ru.skillbox.diplom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.api.responses.TagForTopicOne;
import ru.skillbox.diplom.api.responses.TagsForTopicResponse;
import ru.skillbox.diplom.repositories.PostRepositori;
import ru.skillbox.diplom.repositories.TagsRepositori;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TagService {

    @Autowired
    private TagsRepositori tagsRepositori;

//    @Autowired
//    private PostRepositori postRepositori;


    public TagsForTopicResponse getTagsForTopic(){
        TagsForTopicResponse tagsForTopicResponse = new TagsForTopicResponse();
        List<TagForTopicOne> listTags = new ArrayList<>();

        List<Object[]> objectsTags = tagsRepositori.tagsForTopic(System.currentTimeMillis());
        int maxTagWeight = 0;
        for (Object[] obj : objectsTags) {
            TagForTopicOne tagForTopicOne = new TagForTopicOne();
            BigInteger b = (BigInteger) obj[1];
            if (maxTagWeight <  b.intValue()) maxTagWeight = b.intValue();
            tagForTopicOne.setName((String) obj[0]);
            tagForTopicOne.setWeight(new BigDecimal( b.doubleValue() / maxTagWeight).setScale(2, RoundingMode.UP).doubleValue());
            listTags.add(tagForTopicOne);
        }
        tagsForTopicResponse.setTags(listTags);

        return tagsForTopicResponse;
    }


}
