package com.qxc.scaleconvertiontools.Service.impl;

import com.qxc.scaleconvertiontools.Config.ScaleConversionToolsConfiguration;
import com.qxc.scaleconvertiontools.Dao.ScaleConversion;
import com.qxc.scaleconvertiontools.Dao.UploadObject;
import com.qxc.scaleconvertiontools.Utiles.JsonUtiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Author qxc
 * @Date 2024 2024/3/10 20:20
 * @Version 1.0
 * @PACKAGE com.qxc.scaleconvertiontools.Service.impl
 */
@Service
public class ScaleConversionService {

    @Autowired
    private ScaleConversionToolsConfiguration configuration;

    public ScaleConversion patch(UploadObject uploadObject) {
        final String sPath = JsonUtiles.object2Json(uploadObject, configuration.getSaveDir());
        run(sPath);
        return JsonUtiles.readJson(uploadObject.getSavePath());
    }

    public void run(String path) {
        try {
            // 创建一个ProcessBuilder对象，指定要执行的命令
            ProcessBuilder processBuilder = new ProcessBuilder("python", configuration.getPy(), path);
            // 启动外部进程
            Process process = processBuilder.start();
            // 获取进程的输入流
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            // 读取进程的输出
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            // 等待进程执行完成
            int exitCode = process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
