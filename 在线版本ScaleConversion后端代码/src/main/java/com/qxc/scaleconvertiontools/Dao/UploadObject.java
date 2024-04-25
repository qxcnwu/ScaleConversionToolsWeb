package com.qxc.scaleconvertiontools.Dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author qxc
 * @Date 2024 2024/3/10 18:49
 * @Version 1.0
 * @PACKAGE com.qxc.scaleconvertiontools.Dao
 */
@Getter
@ToString
@Setter
public class UploadObject {

    private String visPic;
    private String nirPic;

    private List<List<Integer>> visLoc;
    private List<List<Integer>> nirLoc;

    private HashMap<Integer, String> seds;

    private String savePath;

    @Contract(pure = true)
    public UploadObject(String visPic, String nirPic, List<List<Integer>> visLoc, List<List<Integer>> nirLoc, HashMap<Integer, String> seds) {
        this.visPic = visPic;
        this.nirPic = nirPic;
        this.visLoc = visLoc;
        this.nirLoc = nirLoc;
        this.seds = seds;
    }

    public UploadObject() {
        this.seds = new HashMap<>(8);
        this.visLoc = new ArrayList<>();
        this.nirLoc = new ArrayList<>();
    }
}
