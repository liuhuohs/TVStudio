package com.imooc.TVStudio.api;

import com.imooc.TVStudio.domain.JsonResponse;
import com.imooc.TVStudio.domain.Video;
import com.imooc.TVStudio.service.DemoService;
import com.imooc.TVStudio.service.ElasticSearchService;
import com.imooc.TVStudio.service.util.FastDFSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

//@RestController
public class DemoApi {
//    @Autowired
    private DemoService demoService;
    @Autowired
    private FastDFSUtil fastDFSUtil;
    @Autowired
    private ElasticSearchService elasticSearchService;

//    @Autowired
//    private MsDeclareService msDeclareService;
    @GetMapping("/query")
    public String query(Long id){
        return  "1又"+id;
    }
    @GetMapping("/slices")
    public void slices(MultipartFile file) throws Exception {
        System.out.println("a;oishdfpaq098oiwehdfjp0qaoiwehjfpo");
        fastDFSUtil.convertFileToSlices(file);
    }
    @GetMapping("/es-videos")
    public JsonResponse<Video> getEsVideos(@RequestParam String keyword){
        Video video = elasticSearchService.getVideos(keyword);
        return new JsonResponse<>(video);
    }
}
