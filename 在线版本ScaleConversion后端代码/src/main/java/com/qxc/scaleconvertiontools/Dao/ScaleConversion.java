package com.qxc.scaleconvertiontools.Dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author qxc
 * @Date 2024 2024/3/10 20:21
 * @Version 1.0
 * @PACKAGE com.qxc.scaleconvertiontools.Dao
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScaleConversion {
    private float[] wave;
    private float[] vis;
    private float[] nir;
    private float[] ans;

    private float[][] seds;
}
