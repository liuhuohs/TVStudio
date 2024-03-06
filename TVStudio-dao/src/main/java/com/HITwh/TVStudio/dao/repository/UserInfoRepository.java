package com.HITwh.TVStudio.dao.repository;

import com.HITwh.TVStudio.domain.UserInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserInfoRepository extends ElasticsearchRepository<UserInfo, Long> {

}
