import echarts from 'echarts';
import store from "@/store/index";

let timeTickId, timer, max;
export function drawGauge() {
    let chart_city = echarts.init(document.getElementById("city-gauge")); //初始化chart容器
    let chart_people = echarts.init(document.getElementById("people-gauge"));
    let city_data = { //显示的数据
        "name": 'Explored Cities',
        "num": 100
    };
    let people_data = { //显示的数据
        "name": 'Rank',
        "num": 100
    };
    timer = 1.5; //刷新频率
    max = 100; //最大值
    createPolarGauge(chart_city,city_data);
    createPolarGauge(chart_people,people_data)
}

function createPolarGauge(chart,data) {
    let option = {
        angleAxis: {
            show: false,
            max: max * 6 / 5, //这里将极坐标最大值转换成仪表盘的最大值，(360度除以240度)
            type: 'value',
            startAngle: 240, //极坐标初始角度，从第一象限算起，大约在7-8点钟角度之间
            splitLine: {
                show: false //隐藏坐标
            }
        },
        barMaxWidth: 20, //圆环宽度
        radiusAxis: { //隐藏坐标
            show: false,
            type: 'category',
        },
        polar: { //设置圆环位置和大小
            center: ['50%', '50%'],
            radius: '150'
        },
        series: [{
            type: 'bar',
            data: [{ //上层圆环，用于显示真实数据
                value: data.num,
                itemStyle: {
                    color: { //图形渐变颜色方法，四个数字分别代表，右，下，左，上，offset表示0%到100%
                        type: 'linear',
                        x: 0,
                        y: 0,
                        x2: 1, //从左到右 0-1
                        y2: 0,
                        colorStops: [{
                            offset: 0,
                            color: '#3884bb' // 0% 处的颜色
                        }, {
                            offset: 1,
                            color: '#2c3dfc' // 100% 处的颜色
                        }],
                        globalCoord: false // 缺省为 false
                    },
                    shadowColor: 'rgba(255, 255, 255, 0.2)', //加白色阴影产生高亮效果
                    shadowBlur: 10
                }
            }],
            barGap: '-100%', //柱间距离,用来将上下两层圆环重合
            coordinateSystem: 'polar', //类型，极坐标
            roundCap: true, //顶端圆角
            z: 2 //圆环层级，和zindex相似
        }, { //下层圆环，用于显示最大值
            type: 'bar',
            data: [{
                value: max,
                itemStyle: {
                    color: '#265195',
                    shadowColor: 'rgba(0, 0, 0, 0.2)', //加白色阴影产生高亮效果
                    shadowBlur: 5,
                    shadowOffsetY: 2
                }
            }],
            barGap: '-100%', //柱间距离,用来将上下两层圆环重合
            coordinateSystem: 'polar', //类型，极坐标
            roundCap: true, //顶端圆角
            z: 1 //圆环层级，和zindex相似
        },
            { //仪表盘
                type: 'gauge',
                radius: '95%',
                startAngle: 240, //起始角度，同极坐标
                endAngle: -60, //终止角度，同极坐标
                max: max,
                splitNumber: 5, //分割线个数（除原点外）
                axisLine: { // 坐标轴线
                    show: false
                },
                pointer: {
                    show: false
                },
                axisLabel: {
                    // 坐标轴数字
                    textStyle: {
                        fontSize: 10,
                        color: "#13B5FC"
                    },

                },
                axisTick: { // 坐标轴标记
                    length: 10,
                    lineStyle: {
                        color: "#13B5FC"
                    }
                },
                splitLine: { // 分隔线
                    length: 6,
                    lineStyle: {
                        width: 1,
                    }
                },
                title: { //标题
                    textStyle: {
                        color: '#fff',
                        shadowColor: '#fff',
                        fontSize: 16
                    },
                    offsetCenter: ["0", '-25%'] //位置偏移
                },
                detail: { //仪表盘数值
                    formatter: function (params) {
                        var name = data.num.toString()
                        var list = ''
                        for (var i = 0; i < name.length; i++) {
                            list += '{value|' + name[i] + '}' //每个数字用border隔开
                            if (i !== name.length - 1) {
                                list += '{margin|}' //添加margin值
                            }
                        }
                        return [list]
                    },
                    offsetCenter: ["0", '10%'],
                    rich: { //编辑富文本样式
                        value: {
                            width: 34,
                            height: 42,
                            borderColor: '#37a2ef',
                            borderWidth: 2,
                            borderRadius: 5,
                            lineHeight: 1000,
                            fontSize: 30,
                            padding: [0, 0, 4, 0],
                            color: '#fff',
                            shadowColor: 'rgb(92,76,234)',
                            shadowBlur: 5
                        },
                        margin: {
                            width: 8,
                            height: 42,
                        }
                    }

                },
                data: [{
                    value: data.num,
                    name: data.name
                }]
            }
        ]
    }
    setGaugeOption(chart,option,data);
}

function setGaugeOption(chart,option,data) { //更新数据
    let userId = store.state.user_id;

    // 发送POST请求，获取旅行记录数据
    // axios.post('/api/travel-rank', { userId })
    //     .then(response => {
    //         //let city_num = response.data.city_num;
    //         //let rank = response.data.rank;
    //     })
    //     .catch(error => {
    //         console.error('数据请求失败:', error);
    //     });

    data.num = parseInt(Math.random() * max);//TODO:后端写好后把这行注释
    option.series[2].data[0].value = data.num;
    option.series[0].data[0].value = data.num;
    //option.series[0].data[0].value = city_num;
    //option.series[0].data[0].value = rank;

    chart.setOption(option);
}
