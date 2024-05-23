import "./d3.v3.min.js"
import "./jquery-3.4.1.min.js"
import "./materialize.min.js"
import {initKG} from "./graph-render.js"

export function drawRelationGraph()
{
    let graphNodesArray = {
        "1": {"name": "欲盖弥彰", "type": "User"},
        "8": {"name": "道德天尊", "type": "Friend1"},
        "9": {"name": "云海", "type": "Friend2"},
        "10": {"name": "史彦彬", "type": "Friend3"},
        "11": {"name": "(´-ι_-｀)", "type": "Friend4"},
        "2": {"name": "上海", "type": "City"},
        "3": {"name": "重庆", "type": "City"},
        "4": {"name": "北京", "type": "City"},
        "5": {"name": "扬州",  "type": "City"},
        "6": {"name": "杭州",  "type": "City"},
        "7": {"name": "百色",  "type": "City"},
    };

    let graphLinksArray = [
        {"source": 1, "target": 2, "rela": "居住"},
        {"source": 1, "target": 3, "rela": "家乡"},
        {"source": 1, "target": 4, "rela": "去过"},
        {"source": 1, "target": 5, "rela": "去过"},
        {"source": 1, "target": 6, "rela": "去过"},
        {"source": 8, "target": 2, "rela": "居住"},
        {"source": 8, "target": 4, "rela": "去过"},
        {"source": 8, "target": 5, "rela": "去过"},
        {"source": 8, "target": 7, "rela": "家乡"},
        {"source": 9, "target": 2, "rela": "居住"},
        {"source": 9, "target": 6, "rela": "去过"},
        {"source": 10, "target": 5, "rela": "去过"},
        {"source": 10, "target": 2, "rela": "家乡"},
        {"source": 11, "target": 6, "rela": "去过"}
    ];

    let data = {};
    data.nodes = graphNodesArray;
    data.links = graphLinksArray;
    let config = {
        content: null,
        contentHook: null,
        nodeColor: null,
        linkColor:null,
        width: document.getElementById("relation-container").clientWidth,
        height: 600
    }
    initKG(data, config, "#relation-container");
}