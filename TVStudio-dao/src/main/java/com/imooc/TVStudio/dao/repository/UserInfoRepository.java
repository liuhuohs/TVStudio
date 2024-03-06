package com.imooc.TVStudio.dao.repository;

import com.imooc.TVStudio.domain.UserInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserInfoRepository extends ElasticsearchRepository<UserInfo, Long> {

}
