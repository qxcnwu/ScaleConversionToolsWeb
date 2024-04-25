package com.qxc.scaleconvertiontools.Controller;

import com.qxc.scaleconvertiontools.Dao.ScaleConversion;
import com.qxc.scaleconvertiontools.Dao.UploadObject;
import com.qxc.scaleconvertiontools.Service.impl.ResponseDecode;
import com.qxc.scaleconvertiontools.Service.impl.ScaleConversionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * @Author qxc
 * @Date 2024 2024/3/10 17:27
 * @Version 1.0
 * @PACKAGE com.qxc.scaleconvertiontools.Controller
 */
@RestController
@RequestMapping("/sc")
public class ResponseGetHtml {

    @Autowired
    private ResponseDecode responseDecode;

    @Autowired
    private ScaleConversionService scaleConversionService;

    @RequestMapping("/upload")
    public ScaleConversion response(HttpServletRequest request, HttpServletResponse response, String data) {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        UploadObject uploadObject = new UploadObject();
        uploadObject = responseDecode.parse(multipartHttpServletRequest, uploadObject);
        response.addHeader("Access-Allow-Control-Origin","*");
        return scaleConversionService.patch(uploadObject);
    }

}
