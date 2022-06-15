package com.zeekr.es;

import com.zeekr.es.utils.EsUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class EsApplicationTests {
    @Autowired
    EsUtil util;

    @Test
    void create() {
        util.creat("test_20220523");
    }

    @Test
    void exists() {
        util.exists("test_20220523");
    }

}
