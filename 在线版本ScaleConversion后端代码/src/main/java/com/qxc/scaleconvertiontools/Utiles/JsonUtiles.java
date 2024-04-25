package com.qxc.scaleconvertiontools.Utiles;

import com.google.gson.Gson;
import com.qxc.scaleconvertiontools.Dao.ScaleConversion;
import com.qxc.scaleconvertiontools.Dao.UploadObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.UUID;

/**
 * @Author qxc
 * @Date 2024 2024/3/10 20:16
 * @Version 1.0
 * @PACKAGE com.qxc.scaleconvertiontools.Utiles
 */
public class JsonUtiles {
    public static @NotNull String object2Json(UploadObject uploadObject, String saveDir) {
        // 将Person对象转换为JSON格式的字符串
        Gson gson = new Gson();
        String json = gson.toJson(uploadObject);
        String name = UUID.randomUUID().toString() + ".json";
        File file = new File(saveDir, name);
        // 将JSON字符串写入文件
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    /**
     * 读取json文件并转化为对象
     *
     * @param path
     * @return
     */
    @Contract(pure = true)
    public static ScaleConversion readJson(String path) {
        Reader reader;
        try {
            reader = new FileReader(path);
            // 创建Gson对象
            Gson gson = new Gson();
            // 从JSON文件中读取并转换为Person对象
            ScaleConversion scaleConversion = gson.fromJson(reader, ScaleConversion.class);
            reader.close();
            return scaleConversion;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
