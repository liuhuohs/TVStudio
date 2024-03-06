package com.imooc.TVStudio.service;

import com.imooc.TVStudio.dao.DemoDao;
import org.springframework.beans.factory.annotation.Autowired;

//@Service
public class DemoService {
    @Autowired
    private DemoDao demoDao;

    public Long query(Long id){
        return demoDao.query(id);
    }
}
