const { shallowMount, mount } = require('@vue/test-utils');
import Gallery from '@/views/Gallery.vue';

describe('Gallery', () => {
    it('renders the main container with the correct classes and elements', () => {
        const wrapper = shallowMount(Gallery);
        expect(wrapper.find('.main-container').exists()).toBe(true);
        expect(wrapper.find('.gallery-heading').text()).toBe('画廊');
        expect(wrapper.find('.gallery-subheading').text()).toBe('旅行的点点滴滴');
        expect(wrapper.find('.paper-edge').exists()).toBe(true);
        expect(wrapper.find('.image-background').exists()).toBe(true);
        expect(wrapper.find('.gallery-overlay').exists()).toBe(true);
    });

    it('shows the edit button initially', () => {
        const wrapper = shallowMount(Gallery);
        expect(wrapper.find('.button .black-button').exists()).toBe(true);
        expect(wrapper.find('.button .black-button').text()).toContain('编辑');
    });

});