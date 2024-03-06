package com.imooc.TVStudio.dao;

import com.imooc.TVStudio.domain.File;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileDao {

    Integer addFile(File file);

    File getFileByMD5(String md5);
}
