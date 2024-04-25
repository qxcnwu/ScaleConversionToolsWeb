package com.qxc.scaleconvertiontools.Utiles;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author qxc
 * @Date 2024 2024/3/10 19:12
 * @Version 1.0
 * @PACKAGE com.qxc.scaleconvertiontools.Utiles
 */
public class FileUtil {
    /**
     * 设置保存文件
     *
     * @param files
     * @param saveDir
     * @return
     */
    @Contract(pure = true)
    public static @NotNull List<String> saveFiles(@NotNull List<MultipartFile> files, String saveDir) {
        List<String> ans = new ArrayList<>(files.size());
        for (MultipartFile file : files) {
            ans.add(saveFile(file, saveDir));
        }
        return ans;
    }

    @Contract(pure = true)
    public static @NotNull String saveFile(@NotNull MultipartFile file, String saveDir) {
        String uuid = UUID.randomUUID().toString();
        // 得到上传文件后缀
        String originalName = file.getOriginalFilename();
        String ext = "." + FilenameUtils.getExtension(originalName);
        // 新生成的文件名称
        String fileName = uuid + ext;
        // 得到新的文件File对象
        File targetFile = new File(saveDir, fileName);
        // 开始复制文件
        try {
            FileUtils.writeByteArrayToFile(targetFile, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return targetFile.getAbsolutePath();
    }
}
