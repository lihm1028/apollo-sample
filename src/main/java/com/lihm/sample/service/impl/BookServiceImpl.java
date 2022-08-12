package com.lihm.sample.service.impl;

import com.lihm.sample.config.SampleProperties;
import com.lihm.sample.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private SampleProperties properties;

    @Override
    public SampleProperties getConfig() {
        return properties;
    }


}
