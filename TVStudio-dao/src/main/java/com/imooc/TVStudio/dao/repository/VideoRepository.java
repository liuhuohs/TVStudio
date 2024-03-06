package com.imooc.TVStudio.dao.repository;

import com.imooc.TVStudio.domain.Video;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface VideoRepository extends ElasticsearchRepository<Video, Long> {

    Video findByTitleLike(String keyword);
}
