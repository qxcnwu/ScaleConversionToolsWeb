<script setup>
import {nextTick, ref} from "vue";
import * as echarts from 'echarts';
import axios from "axios";

const src_vis = ref('https://img2.imgtp.com/2024/03/09/N1DzAhG4.png')
const src_nir = ref('https://img2.imgtp.com/2024/03/09/sf2hG4Ws.png')
let canvas_vis = ref()
let ctx_vis = ref()
let canvas_nir = ref()
let ctx_nir = ref()
let rect_vis = []
let rect_nir = []
let nir_high = 0
let vis_high = 0
let nir_width = 0
let vis_width = 0
let cur_nir_high = 0
let cur_vis_high = 0
let cur_nir_width = 0
let cur_vis_width = 0
const name_ans = ref([]);
let index = 0
let scaleconvertion = {
  "wave": [],
  "vis": [],
  "nir": [],
  "1": [],
  "2": [],
  "3": [],
  "4": [],
  "ans": [],
}
let option = {
  xAxis: {
    name: "wave length(nm)",
    type: 'category',
    data: [],
    nameLocation: 'middle',
  },
  yAxis: {
    name: "reflectance",
    type: 'value',
    nameLocation: 'middle',
    min: 0,
    max: 1
  },
  title: {text: "ResTransformer深度学习模型地表反射率尺度转换结果"},
  legend: {},
  toolbox: {
    show: true,//显示工具箱
    feature: {
      dataZoom: {
        yAxisIndex: 'none'
      },//数据缩放
      dataView: {readOnly: false},//数据视图只读
      magicType: {type: ['line', 'bar']},//魔法类型
      restore: {},//重置
      saveAsImage: {}//保存图片
    }
  },
  series: []
};
let upload = {
  vis: null,
  nir: null,
  seds: {},
  vis_loc: [],
  nir_loc: []
}
let myChart = [];

//选择文件
function openVisFile() {
  document.getElementById("openvis").click();
}

// 选择近红外图像
function changeVisFile() {
  let files = event.target.files;
  upload.vis = files[0];
  const pathArr = Array.prototype.map.call(files, item => {
    return item;
  });
  const fr = new FileReader();
  const img = new Image();
  img.onload = () => {
    vis_width = img.naturalWidth; // 获取图像宽度
    vis_high = img.naturalHeight; // 获取图像高度
  };
  img.src = URL.createObjectURL(files[0]);
  fr.readAsDataURL(pathArr[0]);
  document.getElementById("vis_placer").placeholder = pathArr[0].name
  fr.onload = function (e) {
    document.getElementById('vis_img').src = this.result;
  }
}

//选择文件
function openNirFile() {
  document.getElementById("opennir").click();
}

// 选择近红外图像
function changeNirFile() {
  let files = event.target.files;
  upload.nir = files[0];
  const pathArr = Array.prototype.map.call(files, item => {
    return item;
  });
  const fr = new FileReader();
  const img = new Image();
  img.onload = () => {
    nir_width = img.naturalWidth; // 获取图像宽度
    nir_high = img.naturalHeight; // 获取图像高度
  };
  img.src = URL.createObjectURL(files[0]);
  fr.readAsDataURL(pathArr[0]);
  console.log(pathArr[0])
  document.getElementById("nir_placer").placeholder = pathArr[0].name
  fr.onload = function (e) {
    document.getElementById('nir_img').src = this.result;
  }
}

// vis绘制
function VisDraw() {
  // 获取图像和画布元素
  const img = document.getElementById('vis_img');
  canvas_vis = document.getElementById('canvass_vis');
  ctx_vis = canvas_vis.getContext('2d');

  // 设置画布大小与图像相同
  canvas_vis.width = img.width;
  canvas_vis.height = img.height;

  cur_vis_width = img.width;
  cur_vis_high = img.height;

  // 初始化变量
  let isDrawing = false;
  let moving = false;
  let startX, startY, endX, endY;


  // 鼠标按下事件
  canvas_vis.addEventListener('mousedown', (e) => {
    if (e.button === 0) {
      isDrawing = true;
      startX = e.offsetX;
      startY = e.offsetY;
      console.log('矩形坐标vis:', startX, startY);
    }
  });

  // 鼠标移动事件
  canvas_vis.addEventListener('mousemove', (e) => {
    if (!isDrawing) return;
    moving = true;
    ctx_vis.clearRect(0, 0, canvas_vis.width, canvas_vis.height);
    endX = e.offsetX;
    endY = e.offsetY;
    drawRects()
    Rect(startX, startY, endX, endY);
  });

  // 鼠标松开事件
  canvas_vis.addEventListener('mouseup', () => {
    if (moving) {
      moving = false;
      isDrawing = false;
      // 绘制当前的矩形
      drawRect(startX, startY, endX, endY, true);
      // 绘制当前所有矩形
      drawRects()
      // 输出矩形的坐标
      console.log('矩形坐标vis:', startX, startY, endX, endY);
    }
  });


  canvas_vis.addEventListener('contextmenu', (e) => {
    e.preventDefault();
    if (rect_vis.length > 0) {
      ctx_vis.clearRect(0, 0, canvas_vis.width, canvas_vis.height);
      rect_vis = rect_vis.slice(0, rect_vis.length - 1);
      drawRects();
      while (name_ans.value.length > Math.min(rect_vis.length, rect_nir.length)) {
        delData();
      }
    }
  })

  // 绘制当前所有矩形
  function drawRects() {
    let idx = 0;
    rect_vis.forEach(rect => {
      Rect(rect[0], rect[1], rect[2], rect[3]);
      drawText(idx, (rect[0] + rect[2]) / 2, (rect[1] + rect[3]) / 2)
      idx += 1;
    });
  }

  // 绘制矩形的函数
  function Rect(x1, y1, x2, y2) {
    const width = x2 - x1;
    const height = y2 - y1;
    ctx_vis.strokeRect(x1, y1, width, height);
  }

  // 添加矩形的函数
  function drawRect(x1, y1, x2, y2, bools) {
    if (bools) {
      for (let i = 0; i < rect_vis.length; i++) {
        let rectVisKey = rect_vis[i];
        if (x1 === rectVisKey[0] && y1 === rectVisKey[1] && x2 === rectVisKey[2] && y2 === rectVisKey[3]) {
          return
        }
      }
      rect_vis.push([x1, y1, x2, y2])
      if (rect_vis.length >= rect_nir.length) {
        addData(rect_vis.length, "", x1, y1, x2, y2);
      }
    } else {
      Rect(x1, y1, x2, y2)
    }
  }

  function drawText(text, x, y) {
    ctx_vis.fillStyle = 'black'; // 设置文本颜色
    ctx_vis.font = '20px Arial'; // 设置字体样式和大小
    ctx_vis.fillText(text, x, y); // 在指定位置填写文本
  }
}

// nir绘制
function NirDraw() {
  // 获取图像和画布元素
  const img_nir = document.getElementById('nir_img');
  canvas_nir = document.getElementById('canvass_nir');
  ctx_nir = canvas_nir.getContext('2d');

  // 设置画布大小与图像相同
  canvas_nir.width = img_nir.width;
  canvas_nir.height = img_nir.height;

  cur_nir_width = img_nir.width;
  cur_nir_high = img_nir.height;

  // 初始化变量
  let isDrawing = false;
  let moving = false;
  let startX, startY, endX, endY;

  // 鼠标按下事件
  canvas_nir.addEventListener('mousedown', (e) => {
    if (e.button === 0) {
      isDrawing = true;
      startX = e.offsetX;
      startY = e.offsetY;
    }
  });

  // 鼠标移动事件
  canvas_nir.addEventListener('mousemove', (e) => {
    if (!isDrawing) return;
    ctx_nir.clearRect(0, 0, canvas_nir.width, canvas_nir.height);
    moving = true;
    endX = e.offsetX;
    endY = e.offsetY;
    drawRects()
    drawRect(startX, startY, endX, endY, false);
  });

  // 鼠标松开事件
  canvas_nir.addEventListener('mouseup', () => {
    if (moving) {
      moving = false;
      isDrawing = false;
      // 绘制当前的矩形
      drawRect(startX, startY, endX, endY, true);
      // 绘制当前所有矩形
      drawRects()
      // 输出矩形的坐标
      console.log('矩形坐标nir:', startX, startY, endX, endY);
    }
  });

  canvas_nir.addEventListener('contextmenu', (e) => {
    e.preventDefault();
    if (rect_nir.length > 0) {
      ctx_nir.clearRect(0, 0, canvas_nir.width, canvas_nir.height);
      rect_nir = rect_nir.slice(0, rect_nir.length - 1);
      drawRects();
      while (name_ans.value.length > Math.min(rect_vis.length, rect_nir.length)) {
        delData();
      }
    }
  })

  // 绘制当前所有矩形
  function drawRects() {
    let idx = 0;
    rect_nir.forEach(rect => {
      Rect(rect[0], rect[1], rect[2], rect[3]);
      drawText(idx, (rect[0] + rect[2]) / 2, (rect[1] + rect[3]) / 2)
      idx += 1;
    });
  }

  // 绘制矩形的函数
  function Rect(x1, y1, x2, y2) {
    const width = x2 - x1;
    const height = y2 - y1;
    ctx_nir.strokeRect(x1, y1, width, height);
  }

  // 绘制矩形的函数
  function drawRect(x1, y1, x2, y2, bools) {
    if (bools) {
      for (let i = 0; i < rect_nir.length; i++) {
        let rectVisKey = rect_nir[i];
        if (x1 === rectVisKey[0] && y1 === rectVisKey[1] && x2 === rectVisKey[2] && y2 === rectVisKey[3]) {
          return
        }
      }
      rect_nir.push([x1, y1, x2, y2])
      if (rect_vis.length >= rect_nir.length) {
        addData(rect_nir.length, "", x1, y1, x2, y2);
      }
    } else {
      Rect(x1, y1, x2, y2)
    }
  }

  function drawText(text, x, y) {
    ctx_nir.fillStyle = 'black'; // 设置文本颜色
    ctx_nir.font = '20px Arial'; // 设置字体样式和大小
    ctx_nir.fillText(text, x, y); // 在指定位置填写文本
  }

}

function InitCharts() {
  myChart = echarts.init(document.getElementById('chartsmain'));
  option && myChart.setOption(option);
}

function Res2Option() {
  option.series = []
  option.xAxis.data = scaleconvertion.wave
  Object.keys(scaleconvertion).forEach((key) => {
    if (key !== "wave") {
      option.series.push({
        name: key,
        data: scaleconvertion[key],
        type: 'line'
      })
    }
  })
  console.log(option)
}

// 初始化画布
nextTick(() => {
  VisDraw();
  NirDraw();
  Res2Option();
  InitCharts();
})

const addData = (id, name, x, y, x1, y1) => {
  name_ans.value.push({
    id: id,
    name: name,
    data: null,
    x: x,
    y: y,
    x1: x1,
    y1: y1
  });
};

const delData = () => {
  name_ans.value.pop();
};

// 选取sed
function ChooseFile(idx) {
  index = idx;
  document.getElementById("sedfile").click();
}

function handleFileChange(event) {
  let files = event.target.files;
  const pathArr = Array.prototype.map.call(files, item => {
    return item;
  });
  upload.seds[index] = files[0];
  const fr = new FileReader();
  fr.readAsDataURL(pathArr[0]);
  console.log(name_ans.value[index])
  name_ans.value[index].name = pathArr[0].name
  fr.onload = function (e) {
    name_ans.value[index].data = this.result;
  }
}

// 初始化尺度转换的结果
function init_scale_conversion() {
  for (let i = 350; i < 2151; i++) {
    Object.keys(scaleconvertion).forEach((key) => {
      if (key === "wave") {
        scaleconvertion[key].push(i);
      } else {
        scaleconvertion[key].push(Math.random());
      }
    })
  }
}

// 下载结果
function downloadAns() {
  // 将对象转换为 JSON 字符串
  const jsonString = JSON.stringify(scaleconvertion);

  // 创建一个新的 Blob 对象
  const blob = new Blob([jsonString], {type: 'application/json'});

  // 创建一个链接元素
  const link = document.createElement('a');
  link.href = URL.createObjectURL(blob);
  link.download = 'answer.json'; // 设置文件名

  // 将链接元素插入到文档中，并触发点击事件
  document.body.appendChild(link);
  link.click();

// 删除链接元素
  document.body.removeChild(link);
}

// 上传文件
function initUpload() {
  upload.vis_loc = []
  upload.nir_loc = []

  // rect初始化
  for (let i = 0; i < rect_vis.length; i++) {
    let rectVisKey = rect_vis[i]
    upload.vis_loc.push([Math.round(rectVisKey[0] / cur_vis_high * vis_high),
      Math.round(rectVisKey[1] / cur_vis_width * vis_width),
      Math.round(rectVisKey[2] / cur_vis_high * vis_high),
      Math.round(rectVisKey[3] / cur_vis_width * vis_width)])
  }

  // loc初始化
  for (let i = 0; i < rect_nir.length; i++) {
    let rectVisKey = rect_nir[i]
    upload.nir_loc.push([Math.round(rectVisKey[0] / cur_nir_high * nir_high),
      Math.round(rectVisKey[1] / cur_nir_width * nir_width),
      Math.round(rectVisKey[2] / cur_nir_high * nir_high),
      Math.round(rectVisKey[3] / cur_nir_width * nir_width)])
  }
}

// 初始化对象
function Upload() {
  initUpload();
  let param = new FormData(); //创建form对象
  param.append('vis_pic', upload.vis);
  param.append('nir_pic', upload.vis);
  // 添加sed文件
  for (let i = 0; i < Object.keys(upload.seds).length; i++) {
    param.append('sed' + i.toString(), upload.seds[i]);
  }
  param.append('vis_loc', upload.vis_loc);
  param.append('nir_loc', upload.vis_loc);
  let config = {
    headers: {'Content-Type': 'multipart/form-data'} //这里是重点，需要和后台沟通好请求头，Content-Type不一定是这个值
  }; //添加请求头
  console.log(upload)
  console.log(param)
  axios.post('http://81.70.190.126:8080/sc/upload', param, config)
      .then(response => {
        scaleconvertion.wave = response.data.wave;
        scaleconvertion.vis = response.data.vis;
        scaleconvertion.nir = response.data.nir;
        scaleconvertion.ans = response.data.ans;
        for (let i = 0; i < response.data.seds.length; i++) {
          scaleconvertion[i] = response.data.seds[i]
        }
        myChart.clear();
        myChart.dispose();
        Res2Option();
        InitCharts();

        console.log(option)
      })
}

</script>

<template>
  <el-menu
      class="el-menu-demo"
      mode="horizontal"
      :ellipsis="false"
  >
    <el-menu-item index="0" disabled="true">
      <img
          src="./images/logger.svg"
          alt="scale conversion tools logo"
      />
    </el-menu-item>
  </el-menu>
  <el-container>
    <el-aside width="25%">
      <el-row style="margin-top: 5%">
        <el-col :span="4">
          <el-button size="large" type="primary" plain style="font-size: medium;color: #181818;width: 100%"
                     @click="openVisFile">
            选择文件
          </el-button>
        </el-col>
        <el-col :offset="1" :span="19">
          <el-input
              id="vis_placer"
              size="large"
              style="width: 100%"
              disabled
              placeholder="可见光图像(一定要输入)"
          />
          <input
              type="file"
              name="filename"
              id="openvis"
              style="display: none"
              @change="changeVisFile"
          />
        </el-col>
      </el-row>
      <el-row style="margin-top: 5%">
        <el-col :span="4">
          <el-button size="large" type="primary" plain style="font-size: medium;color: #181818;width: 100%"
                     @click="openNirFile" disabled>
            选择文件
          </el-button>
        </el-col>
        <el-col :offset="1" :span="19">
          <el-input
              id="nir_placer"
              size="large"
              style="width: 100%"
              disabled
              placeholder="近红外图像(可以不输入)"
          />
          <input
              type="file"
              name="filename"
              id="opennir"
              style="display: none"
              @change="changeNirFile"
          />
        </el-col>
      </el-row>
      <el-divider/>
      <el-row>
        <el-table :data="name_ans" style="width: 100%">
          <el-table-column label="Date" width="60">
            <template #default="scope">
              <div style="display: flex; align-items: center">
                <span style="margin-left: 10px">{{ scope.row.id }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="Name">
            <template #default="scope">
              <el-tag>{{ scope.row.name }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="选择" width="100%">
            <template #default="scope">
              <input type="file" id="sedfile" ref="fileInput" style="display: none"
                     @change="handleFileChange">
              <el-button size="small" @click="ChooseFile(scope.$index)">选择文件</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-row>
      <el-divider/>
      <el-row>
        <el-col span="24" width="100%" style="width: 100%;text-align: center;">
          <el-button size="large" style="width: 300px; text-align: center;" type="success" @click="Upload">
            执行尺度转换
          </el-button>
        </el-col>
      </el-row>
      <el-row style="margin-top: 5%">
        <el-col span="24" width="100%" style="width: 100%;text-align: center;">
          <el-button size="large" style="width: 300px; text-align: center;" type="primary" @click="downloadAns">
            下载结果
          </el-button>
        </el-col>
      </el-row>
      <el-row style="margin-top: 5%">
        <el-col span="24" width="100%" style="width: 100%;text-align: center;">
          <div>
            1.当前服务器仅有2核2G资源，执行尺度转换操作需要大约1分钟，请耐心等待<br>
            2.样方地表反射率输入示例：<a href="http://81.70.190.126/1.xlsx">下载地址</a>
          </div>
        </el-col>
      </el-row>
    </el-aside>
    <el-main>
      <el-row style="margin-top: 0">
        <el-col :span="11">
          <div class="block" style="position: relative;">
            <img id="vis_img" :src="src_vis" style="width: 100%; height: 100%" alt="" @load="VisDraw"/>
            <canvas id="canvass_vis" class="canvas"
                    style="position: absolute; top: 0; left: 0;width: 100%;height: 100%"></canvas>
          </div>
        </el-col>

        <el-col :offset="2" :span="11">
          <div class="block" style="position: relative;">
            <img id="nir_img" :src="src_nir" style="width: 100%; height: 100%" alt="" @load="NirDraw"/>
            <canvas id="canvass_nir" class="canvas"
                    style="position: absolute; top: 0; left: 0;width: 100%;height: 100%"></canvas>
          </div>
        </el-col>
      </el-row>
      <el-divider/>
      <el-row>
        <el-col>
          <div id="chartsmain" style="width: 70vw;height: 50vh"></div>
        </el-col>
      </el-row>
    </el-main>
  </el-container>

</template>
