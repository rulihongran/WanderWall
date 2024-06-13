import "./d3.v3.min.js"
import "./jquery-3.4.1.min.js"
import "./materialize.min.js"
import {initKG} from "./graph-render.js"
import request from '@/utils/request'
import store from "./../../store/index"


export async function drawRelationGraph()
{
    const username=JSON.parse(localStorage.getItem("username"))?JSON.parse(localStorage.getItem("username")):"Suicidal Capybara";
    let graphNodesArray = //graphNodesArray1;
    {
        "das": {"name": "李永恒","type": "User"},
        "2": {"name": "谢奔",  "type": "Friend1"},
        "3": {"name": "苏子言","type": "Friend2"},
        "4": {"name": "史彦彬","type": "Friend3"},
        "5": {"name": "张婧璇","type": "Friend4"},
        "上海": {"name": "上海",  "type": "City"},
        "重庆": {"name": "重庆",  "type": "City"},
        "北京": {"name": "北京",  "type": "City"},
        "扬州": {"name": "扬州",  "type": "City"},
        "杭州": {"name": "杭州", "type": "City"},
        "百色": {"name": "百色", "type": "City"},
    };

    let graphLinksArray = //graphLinksArray2;
    [
        {"source": "das", "target": "上海", "rela": "居住"},
        {"source": "das", "target": "重庆", "rela": "家乡"},
        {"source": "das", "target": "北京", "rela": "去过"},
        {"source": "das", "target": "扬州", "rela": "去过"},
        {"source": "das", "target": "杭州", "rela": "去过"},
        {"source": "2", "target": "上海", "rela": "居住"},
        {"source": "2", "target": "上海", "rela": "去过"},
        {"source": "2", "target": "上海", "rela": "去过"},
        {"source": "2", "target": "上海", "rela": "家乡"},
    ];
    // request({
    //     url: '/user/friend/rela',
    //     method: 'post',
    //     params: { username:username },
    //   }).then(res =>  {
    //    graphNodesArray=res.data.nodes;
    //    graphLinksArray=res.data.links;
    //    console.log(graphNodesArray);
    //    console.log(graphLinksArray);
    //    });
    const userID = store.state.user_id;
    let data = {};
    try {
        await request({
            url: '/user/friend/rela',
            method: 'post',
            params: { username:username },
          }).then(res =>  {
            data.nodes=res.data.nodes;
            data.links=res.data.links;
           });
    } catch (error) {
        console.error("Error fetching the relation graph:", error);
    }
    // data.nodes = graphNodesArray;
    // data.links = graphLinksArray;
    let config = {
        content: null,
        contentHook: null,
        nodeColor: null,
        linkColor:null,
        width: document.getElementById("relation-container").clientWidth,
        height: 600
    }
    initKG(data, config, "#relation-container");
    //console.log(graphNodesArray.das);
}