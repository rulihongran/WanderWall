const { mount } = require('@vue/test-utils');
const RelationGraph = require('@/views/RelationGraph.vue');
const { drawRelationGraph } = require('../../src/utils/relation-graph/relation-graph');

jest.mock('../../src/utils/relation-graph/relation-graph', () => ({
    drawRelationGraph: jest.fn(),
}));

describe('RelationGraph.vue', () => {
    it('renders correctly', () => {
        const wrapper = mount(RelationGraph);
        expect(wrapper.exists()).toBe(true);
        // 这个测试确保组件能够被正确渲染
    });

    it('contains the relation-background and relation-container elements', () => {
        const wrapper = mount(RelationGraph);
        expect(wrapper.find('.relation-background').exists()).toBe(true);
        expect(wrapper.find('#relation-container').exists()).toBe(true);
    });

    it('displays the title correctly', () => {
        const wrapper = mount(RelationGraph);
        expect(wrapper.find('.card-title').text()).toBe('Travel Relation Graph');
        // 这个测试确保标题被正确显示
    });

});