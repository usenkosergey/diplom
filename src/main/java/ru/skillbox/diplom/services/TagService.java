package ru.skillbox.diplom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.api.responses.TagForTopicOne;
import ru.skillbox.diplom.api.responses.TagsForTopicResponse;
import ru.skillbox.diplom.repositories.TagsRepositori;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TagService {

    @Autowired
    private TagsRepositori tagsRepositori;

    public TagsForTopicResponse getTagsForTopic() {
        TagsForTopicResponse tagsForTopicResponse = new TagsForTopicResponse();
        List<TagForTopicOne> listTags = new ArrayList<>();
        List<Object[]> objectsTags = tagsRepositori.tagsForTopic(System.currentTimeMillis());

        long maxTagWeight = 0;
        for (Object[] obj : objectsTags) {
            TagForTopicOne tagForTopicOne = new TagForTopicOne();
            long b = (long) obj[1];
            if (maxTagWeight < b) maxTagWeight = b;
            tagForTopicOne.setName((String) obj[0]);
            tagForTopicOne.setWeight(new BigDecimal((double) b / maxTagWeight).setScale(2, RoundingMode.UP).doubleValue());
            listTags.add(tagForTopicOne);
        }
        tagsForTopicResponse.setTags(listTags);

        return tagsForTopicResponse;
    }
}
