package com.leyou.goods.service;

import com.leyou.goods.util.ThreadUtils;
import com.leyou.item.pojo.Spu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author zhang
 * @date 2019年04月22日 17:36
 */
@Service
public class GoodsHtmlService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private TemplateEngine templateEngine;

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsHtmlService.class);

    /**
     * 创建html页面
     *
     * @param map
     * @throws Exception
     */
    public void createHtml(Map<String, Object> map) {

        PrintWriter writer = null;
        try {
            // 获取页面数据
            // 把数据放入上下文对象
            // 创建thymeleaf上下文对象
            Context context = new Context();
            context.setVariables(map);


            // 创建输出流
            File file = new File("C:\\Users\\zpf\\Desktop\\nginx-1.8.0\\html\\item\\" + ((Spu)map.get("spu")).getId() + ".html");
            writer = new PrintWriter(file);

            // 执行页面静态化方法
            templateEngine.process("item", context, writer);
        } catch (Exception e) {
            LOGGER.error("页面静态化出错：{}，"+ e, ((Spu)map.get("spu")).getId());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * 新建线程处理页面静态化
     * @param spuId
     */
    public void asyncExcute(Map<String, Object> spuId) {
        ThreadUtils.execute(()->createHtml(spuId));
        /*ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                createHtml(spuId);
            }
        });*/
    }
    public void deleteHtml(Long id) {
        File file = new File("C:\\Users\\zpf\\Desktop\\nginx-1.8.0\\html\\item\\", id + ".html");
        file.deleteOnExit();
    }
}
