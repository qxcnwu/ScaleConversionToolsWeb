package com.qxc.scaleconvertiontools.Service.impl;

import com.qxc.scaleconvertiontools.Config.ScaleConversionToolsConfiguration;
import com.qxc.scaleconvertiontools.Dao.UploadObject;
import com.qxc.scaleconvertiontools.Utiles.FileUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.*;
import java.io.*;

/**
 * @Author qxc
 * @Date 2024 2024/3/10 18:56
 * @Version 1.0
 * @PACKAGE com.qxc.scaleconvertiontools.Service.impl
 */
@Service
public class ResponseDecode {

    @Autowired
    private ScaleConversionToolsConfiguration configuration;

    /**
     * 解码sed文件
     *
     * @param multipartHttpServletRequest
     * @param uploadObject
     * @return
     */
    @Contract("_, _ -> param2")
    private UploadObject decodeSed(@NotNull MultipartHttpServletRequest multipartHttpServletRequest, UploadObject uploadObject) {
        multipartHttpServletRequest.getFileNames().forEachRemaining(fileName -> {
            if (configuration.getNirPic().equals(fileName) || configuration.getVisPic().equals(fileName)) {
                return;
            }
            Integer idx = Integer.parseInt(fileName.substring(3));
            String paths = FileUtil.saveFile(Objects.requireNonNull(multipartHttpServletRequest.getFile(fileName)), configuration.getSaveDir());
            uploadObject.getSeds().put(idx, paths);
        });
        return uploadObject;
    }

    /**
     * 解码图像
     *
     * @param multipartHttpServletRequest
     * @param uploadObject
     * @return
     */
    @Contract(pure = true)
    private UploadObject decodePicture(@NotNull MultipartHttpServletRequest multipartHttpServletRequest, UploadObject uploadObject) {
        multipartHttpServletRequest.getFileNames().forEachRemaining(fileName -> {
            if (configuration.getNirPic().equals(fileName)) {
                String paths = FileUtil.saveFile(Objects.requireNonNull(multipartHttpServletRequest.getFile(fileName)), configuration.getSaveDir());
                uploadObject.setNirPic(paths);
            } else if (configuration.getVisPic().equals(fileName)) {
                String paths = FileUtil.saveFile(Objects.requireNonNull(multipartHttpServletRequest.getFile(fileName)), configuration.getSaveDir());
                uploadObject.setVisPic(paths);
            }
        });
        return uploadObject;
    }

    /**
     * 解码位置
     *
     * @param multipartHttpServletRequest
     * @param uploadObject
     * @return
     */
    @Contract("_, _ -> param2")
    private @NotNull UploadObject decodeLoc(@NotNull MultipartHttpServletRequest multipartHttpServletRequest, @NotNull UploadObject uploadObject) {
        String parameter = multipartHttpServletRequest.getParameter(configuration.getVisLoc());
        uploadObject.setVisLoc(parseString2List(parameter));
        parameter = multipartHttpServletRequest.getParameter(configuration.getNirLoc());
        uploadObject.setNirLoc(parseString2List(parameter));
        return uploadObject;
    }


    private static @NotNull List<List<Integer>> parseString2List(@NotNull String s) {
        List<List<Integer>> ans = new ArrayList<>();
        List<Integer> tmp = new ArrayList<>();
        Arrays.stream(s.split(",")).forEach(s1 -> {
            tmp.add(Integer.parseInt(s1));
        });
        int size = tmp.size() / 4;
        for (int i = 0; i < size; i++) {
            ans.add(new ArrayList<>(4));
        }
        int idx = 0;
        for (int i = 0; i < tmp.size(); i++) {
            ans.get(idx).add(tmp.get(i));
            if (i % 4 == 3) {
                idx++;
            }
        }
        return ans;
    }

    public UploadObject parse(@NotNull MultipartHttpServletRequest multipartHttpServletRequest, @NotNull UploadObject uploadObject) {
        uploadObject.setSavePath(new File(configuration.getSaveDir(), UUID.randomUUID().toString() + ".json").getAbsolutePath());
        uploadObject = decodeSed(multipartHttpServletRequest, uploadObject);
        uploadObject = decodePicture(multipartHttpServletRequest, uploadObject);
        uploadObject = decodeLoc(multipartHttpServletRequest, uploadObject);
        return uploadObject;
    }
}
