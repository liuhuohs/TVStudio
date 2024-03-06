package com.HITwh.TVStudio.dao.repository;

import com.HITwh.TVStudio.domain.Video;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface VideoRepository extends ElasticsearchRepository<Video, Long> {

    Video findByTitleLike(String keyword);
}
