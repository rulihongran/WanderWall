import echarts from "echarts";
import geoJson from "echarts/map/json/china";

let myChart;

export function drawChinaMap(dialogVisible) {
    if (myChart){
        myChart.dispose();
    }
    toggleBackIcon(false);
    myChart = echarts.init(document.getElementById("trace-map"));
    echarts.registerMap("china", geoJson);

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
            roam:false,
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
        series: [
            // 1. 配置基础地图相关的数据参数
            {
                type: "map",
                label: {
                    normal: {
                        show: false,
                        textStyle: {
                            color: "#1DE9B6",
                        },
                    },
                    emphasis: {
                        show:true,
                        textStyle: {
                            color: "#48766c",
                        },
                    },
                },
                // 最外层
                zoom: 1.1,
                map: "china",
                roam:false,
                itemStyle: {
                    normal: {
                        // areaColor:"rgb(235, 233, 221)"
                        areaColor:"#eff1f0"
                    },
                    // 高亮效果
                    emphasis: {
                        areaColor: "#d2d2d2",
                        borderWidth: 0.1,
                    },
                },
                data: [
                    {
                        name: '黑龙江',
                        itemStyle: {
                            normal: {
                                areaColor: "#d2d2d2"
                            }
                        }
                    }
                ]
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
                data: [
                    { value: [116.407, 39.904], name: '北京' },
                    { value: [121.473, 31.230], name: '上海' },
                    { value: [106.264, 29.56], name: '重庆' },
                    // 其他散点数据
                ],
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
                    color: '#c59ec3'  // 这里设置为红色
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
                data: [
                    {
                        coords: [[116.407, 39.904], [121.473, 31.230]],
                    },
                    {
                        coords: [ [121.473, 31.230], [106.264, 29.56]],
                    },
                ],
            },
        ],
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

    // 点击跳转
    myChart.on('click', function (params) {
        let provinces = ['shanghai', 'hebei','shanxi','neimenggu','liaoning','jilin','heilongjiang','jiangsu','zhejiang','anhui','fujian','jiangxi','shandong','henan','hubei','hunan','guangdong','guangxi','hainan','sichuan','guizhou','yunnan','xizang','shanxi1','gansu','qinghai','ningxia','xinjiang', 'beijing', 'tianjin', 'chongqing', 'xianggang', 'aomen', 'taiwan'];
        let provincesText = ['上海', '河北', '山西', '内蒙古', '辽宁', '吉林','黑龙江',  '江苏', '浙江', '安徽', '福建', '江西', '山东','河南', '湖北', '湖南', '广东', '广西', '海南', '四川', '贵州', '云南', '西藏', '陕西', '甘肃', '青海', '宁夏', '新疆', '北京', '天津', '重庆', '香港', '澳门', '台湾'];
        for(let  i= 0 ; i < provincesText.length ; i++ ){
            if(params.name === provincesText[i]){
                myChart.dispose();
                showProvince(provinces[i],provincesText[i],dialogVisible)
                break ;
            }
        }
    });
}

export function showProvince(pName, pChinese,dialogVisible){
    toggleBackIcon(true);
    // 动态加载地图的json文件
    let mapID=pName+'JS';
    fetch('/province/'+pName+'.json')
        .then(response => response.json())
        .then(geoJson => {
            // 注册地图
            myChart = echarts.init(document.getElementById("trace-map"));
            echarts.registerMap(mapID, geoJson);
            let dataList = getTravelData(pName);
            let option = {
                tooltip: {
                    formatter(params) {
                        if (params.name === '南海诸岛') {
                            return;
                        }
                        return `
                      ${params.data.name}</br>
                      去过次数：${params.data.value}</br>
                      发帖数量：${params.data.PostCount}
                      `;
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
                            show: true,
                            fontSize: '12',
                            color: "#395b53",
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

            myChart.on('click', function (params) {
                dialogVisible.value=true;
            });
        })
        .catch(error => console.error('Error loading geoJson:', error));
}

// 获取旅行数据
function getTravelData(place)
{
    let dataList = [{ name: "黄山市", value: 1, PostCount: "10" },
        { name: "宣城市", value: 10, PostCount: "10" },
        { name: "滁州市", value: 1, PostCount: "9" },
        { name: "六安市", value: 8, PostCount: "8" },
        { name: "池州市", value: 1, PostCount: "7" },
        { name: "合肥市", value: 7, PostCount: "7" },
        { name: "阜阳市", value: 0, PostCount: "6" },
        { name: "芜湖市", value: 0, PostCount: "5" },
        { name: "马鞍山市", value: 0, PostCount: "5" },
        { name: "安庆市", value: 4, PostCount: "5" },
        { name: "蚌埠市", value: 1, PostCount: "5" },
        { name: "亳州市", value: 1, PostCount: "4" },
        { name: "铜陵市", value: 5, PostCount: "3" },
        { name: "淮南市", value: 0, PostCount: "2" },
        { name: "淮北市", value: 1, PostCount: "2" },
        { name: "宿州市", value: 1, PostCount: "1" }]

    return dataList;
}

function toggleBackIcon(show){
    const svgElement = document.getElementById('back-to-china-icon');
    if (show) {
        svgElement.style.display = 'block'; // 显示 svg 元素
    } else {
        svgElement.style.display = 'none'; // 隐藏 svg 元素
    }
}