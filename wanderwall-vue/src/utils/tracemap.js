import echarts from "echarts";
import geoJson from "echarts/map/json/china";
import store from "@/store/index";
import request from '@/utils/request';

let myChart;

// 画中国地图
export function drawChinaMap(dialogVisible,mark_options,enterProvince,formObject) {
    if (myChart){
        myChart.dispose();
    }
    toggleBackIcon(false);
    myChart = echarts.init(document.getElementById("trace-map"));
    echarts.registerMap("china", geoJson);

    // 每个省份去过的次数（该省份下各个城市去过次数的总和）
    let provinceFreqData = [
        {name: '黑龙江', value: 12},
        {name: '北京', value: 10},
        {name: '上海', value: 10},
        {name: '重庆', value: 5},
    ];

    // 去过的地点数据，用于高亮
    let scatterData =  [
        { value: [116.407, 39.904], name: '北京' },//经纬度坐标 城市
        { value: [121.473, 31.230], name: '上海' },
        { value: [106.264, 29.56], name: '重庆' },
    ];

    // 去过的路线数据，按时间排序
    let routeData = [
        {
            coords: [[116.407, 39.904], [121.473, 31.230]],//起点经纬度坐标 终点经纬度坐标
        },
        {
            coords: [ [121.473, 31.230], [106.264, 29.56]],
        },
    ];

    // 发送POST请求，获取旅行记录数据
    console.log(formObject);
    console.log(mark_options);
    request.post('/user/getMapByUser', formObject)
    .then(response => {
        provinceFreqData = response.provinceFreqData;
        scatterData = response.scatterData;
        routeData = response.routeData;

        if (!mark_options.locPoint){
            scatterData = [];
        }
        if(!mark_options.routeArrow){
            routeData = [];
        }
        myChart.setOption({
            // 背景
            backgroundColor: "#47807e",
            // 配置项(组件)
            geo: {
                map: "china",
                // 地图的长宽比例
                aspectScale: 0.75,
                // 图层
                zoom: 1.1,
                roam: false,
                // 样式
                itemStyle: {
                    // 标准
                    normal: {
                        // 地图区域的颜色
                        areaColor: {
                            type: "radial",
                            x: 0.5,
                            y: 0.5,
                            r: 0.5,
                            // 颜色的步骤
                            colorStops: [
                                {
                                    offset: 0,
                                    color: "#09132c",
                                },
                                {
                                    offset: 1,
                                    color: "#274d68",
                                },
                            ],
                            // 延长作用域
                            globalCoord: true,
                        },
                        // 盒子的阴影
                        shadowColor: "#2b5172",
                        // 偏移
                        shadowOffsetX: 10,
                        shadowOffsetY: 11,
                    },
                },
                region: [
                    {
                        name: "南海诸岛",
                        itemStyle: {
                            opacity: 0,
                        },
                    },
                ],
            },
            visualMap: {
                min: 0,
                max: 30,
                left: 'left',
                top: 'bottom',
                text: ['高', '低'],
                inRange: {
                    color: ['#eff1f0', '#9f9f9f']
                },
                calculable: true,
                seriesIndex: [0]
            },
            series: [
                // 1. 配置基础地图相关的数据参数
                {
                    type: "map",
                    label: {
                        normal: {
                            show: mark_options.provinceName,
                            textStyle: {
                                color: "#617a74",
                            },
                        },
                        emphasis: {
                            show: true,
                            textStyle: {
                                color: "#48766c",
                            },
                        },
                    },
                    // 最外层
                    zoom: 1.1,
                    map: "china",
                    roam: false,
                    itemStyle: {
                        normal: {
                            // areaColor:"rgb(235, 233, 221)"
                            areaColor: "#eff1f0"
                        },
                        // 高亮效果
                        emphasis: {
                            areaColor: "#d2d2d2",
                            borderWidth: 0.1,
                        },
                    },
                    data: provinceFreqData,
                },
                //   // 2. 配置散点图的各项参数
                {
                    // 散点图（气泡图）
                    type: "effectScatter",
                    // 使用地理坐标系
                    coordinateSystem: "geo",
                    // 配置何时显示特效(绘制完成后显示特效)
                    showEffectOn: "render",
                    // 标记的大小(默认为10)
                    symbolSize: 10,
                    // 图层
                    zlevel: 1,
                    // 数据
                    data:scatterData,
                    // 涟漪特效相关配置
                    rippleEffect: {
                        // 动画周期
                        period: 5,
                        // 缩放比例
                        scale: 4,
                        // 波纹的绘制
                        brushType: "fill",
                    },
                    itemStyle: {
                        normal: {
                            color: '#c59ec3',  // 这里设置为红色
                        }
                    },
                },
                //   // 3. 配置线性图的各项参数
                {
                    // 线性图
                    type: "lines",
                    // 图层
                    zlevel: 2,
                    // 线特效的配置
                    effect: {
                        // 是否显示特效
                        show: true,
                        // 特效动画的时间
                        period: 4,
                        // 显示方式
                        symbol: "arrow",
                        // 特效的大小
                        symbolSize: 7,
                        // 拖尾的效果，取值范围 0-1，值越大效果越明显
                        trailLength: 0.6,
                    },
                    lineStyle: {
                        normal: {
                            color: "#6e6aab",
                            width: 1,
                            opacity: 0.4,
                            curveness: -0.6,
                        },
                    },
                    data: routeData,
                },
            ],
        });
    })
    .catch(error => {
        console.error('数据请求失败:', error);
    });


    // 设置缩放
    myChart.getZr().on('mousewheel', function (params) {
        let delta = params.wheelDelta || params.detail;
        let zoomStep = 0.1;
        let zoomFactor = 1 + zoomStep * (delta > 0 ? 1 : -1); // 缩放因子

        let currentZoom = myChart.getOption().geo[0].zoom;
        let newZoom = Math.max(0.5, Math.min(2, currentZoom * zoomFactor));

        // 更新缩放比例
        myChart.setOption({
            geo: [
                {
                    zoom: newZoom,
                },
            ],
            series: [
                {
                    zoom: newZoom,
                }
            ]
        });
    });

    myChart.resize();

    // 点击跳转
    myChart.on('click', function (params) {
        let provinces = ['shanghai', 'hebei','shanxi','neimenggu','liaoning','jilin','heilongjiang','jiangsu','zhejiang','anhui','fujian','jiangxi','shandong','henan','hubei','hunan','guangdong','guangxi','hainan','sichuan','guizhou','yunnan','xizang','shanxi1','gansu','qinghai','ningxia','xinjiang', 'beijing', 'tianjin', 'chongqing', 'xianggang', 'aomen', 'taiwan'];
        let provincesText = ['上海', '河北', '山西', '内蒙古', '辽宁', '吉林','黑龙江',  '江苏', '浙江', '安徽', '福建', '江西', '山东','河南', '湖北', '湖南', '广东', '广西', '海南', '四川', '贵州', '云南', '西藏', '陕西', '甘肃', '青海', '宁夏', '新疆', '北京', '天津', '重庆', '香港', '澳门', '台湾'];
        for(let  i= 0 ; i < provincesText.length ; i++ ){
            if(params.name === provincesText[i]){
                myChart.dispose();
                //省份标识和赋值
                if (enterProvince!=undefined){
                    enterProvince.value=true;
                }
                mark_options.pChinese=provincesText[i];
                formObject.province=provincesText[i];
                showProvince(provinces[i],provincesText[i],dialogVisible,mark_options);
                break ;
            }
        }
    });
}

export function updateProvinceMap(mark_options,formObject)
{
    // 发送POST请求，获取旅行记录数据
    request.post('/user/getMapByProvince', {province:mark_options.pChinese,time:formObject.time,location:formObject.location})
    .then(response => {
        let dataList = response;
        //重新渲染
        let updatedOption = {
            geo: {
                map:mapID,
                roam: false,//不开启缩放和平移
                zoom: 1.1,//视角缩放比例
                label: {
                    normal: {
                        show: mark_options.provinceName,
                        fontSize: '12',
                        color: "#395b53",
                    },
                    emphasis: {
                        color: "#59837c",
                    }
                },
                itemStyle: {
                    normal: {
                        borderColor: 'rgba(0, 0, 0, 0.2)'
                    },
                    emphasis: {
                        areaColor: "#d2d2d2",//鼠标选择区域颜色
                        shadowOffsetX: 0,
                        shadowOffsetY: 0,
                        shadowBlur: 10,
                        borderWidth: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.4)'
                    }
                }
            },
            series: [
                {
                    name: '旅行情况',
                    type: 'map',
                    geoIndex: 0,
                    data: dataList
                }
            ]
        };
        myChart.setOption(updatedOption);
    })
    .catch(error => {
        console.error('数据请求失败:', error);
    });
}

// 画省份地图
let mapID="";
export function showProvince(pName, pChinese,dialogVisible,mark_options,formObject){
    toggleBackIcon(true);
    // 动态加载地图的json文件
    mapID=pName+'JS';
    fetch('/province/'+pName+'.json')
        .then(response => response.json())
        .then(geoJson => {
            // 注册地图
            myChart = echarts.init(document.getElementById("trace-map"));
            echarts.registerMap(mapID, geoJson);

            // 请求后端旅行数据，并渲染省地图
            getTravelData(pName, pChinese,dialogVisible,mark_options,formObject);

            myChart.on('click', function (params) {
                dialogVisible.value=true;
            });
        })
        .catch(error => console.error('Error loading geoJson:', error));
}

// 获取旅行数据
function getTravelData(pName, pChinese,dialogVisible,mark_options,formObject)
{
    //某个省份各个城市的名称，去过的次数，发帖的次数
    let dataList = [
        { name: "黄山市", value: 1, postCount: "10" },
        { name: "宣城市", value: 10, postCount: "10" },
        { name: "滁州市", value: 1, postCount: "9" },
        { name: "六安市", value: 8, postCount: "8" },
        { name: "池州市", value: 1, postCount: "7" },
        { name: "合肥市", value: 7, postCount: "7" },
        { name: "阜阳市", value: 0, postCount: "0" },
        { name: "芜湖市", value: 0, postCount: "0" },
        { name: "马鞍山市", value: 0, postCount: "5" },
        { name: "安庆市", value: 4, postCount: "5" },
        { name: "蚌埠市", value: 1, postCount: "5" },
        { name: "亳州市", value: 1, postCount: "4" },
        { name: "铜陵市", value: 5, postCount: "3" },
        { name: "淮南市", value: 0, postCount: "2" },
        { name: "淮北市", value: 1, postCount: "2" },
        { name: "宿州市", value: 1, postCount: "1" }
    ]

    // 发送POST请求，获取旅行记录数据
    request.post('/user/getMapByProvince', {province:pChinese,formObject:formObject})
    .then(response => {
        dataList = response;
        //重新渲染
        let option = {
            tooltip: {
                formatter(params) {
                    if (params.name === '南海诸岛') {
                        return;
                    }
                    if (params.data === undefined){
                        return `
                                  ${params.name}</br>
                                  去过次数：0</br>
                                  发帖数量：0
                                  `;
                    }else {
                        return `
                                  ${params.data.name}</br>
                                  去过次数：${params.data.value}</br>
                                  发帖数量：${params.data.postCount}
                                  `;
                    }

                },
            },
            visualMap: {
                min: 0,
                max: 10,
                left: 'left',
                top: 'bottom',
                text: ['高', '低'],//取值范围的文字
                textStyle: {
                    color: '#ffffff',
                },
                inRange: {
                    color: ['#eff1f0','#acadad']//取值范围的颜色
                },
                show: true//图注
            },
            geo: {
                map:mapID,
                roam: false,//不开启缩放和平移
                zoom: 1.1,//视角缩放比例
                label: {
                    normal: {
                        show: mark_options.provinceName,
                        fontSize: '12',
                        color: "#395b53",
                    },
                    emphasis: {
                        color: "#59837c",
                    }
                },
                itemStyle: {
                    normal: {
                        borderColor: 'rgba(0, 0, 0, 0.2)'
                    },
                    emphasis: {
                        areaColor: "#d2d2d2",//鼠标选择区域颜色
                        shadowOffsetX: 0,
                        shadowOffsetY: 0,
                        shadowBlur: 10,
                        borderWidth: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.4)'
                    }
                }
            },
            series: [
                {
                    name: '旅行情况',
                    type: 'map',
                    geoIndex: 0,
                    data: dataList
                }
            ]
        };
        myChart.setOption(option);
    })
    .catch(error => {
        console.error('数据请求失败:', error);
    });

    return dataList;
}

// 显示/隐藏左上方从省份返回国家地图的按钮
function toggleBackIcon(show){
    const svgElement = document.getElementById('back-to-china-icon');
    if (show) {
        svgElement.style.display = 'block'; // 显示 svg 元素
    } else {
        svgElement.style.display = 'none'; // 隐藏 svg 元素
    }
}
