const { shallowMount } = require('@vue/test-utils');
const HomeView = require('@/views/HomeView.vue');

describe('HomeView', () => {
    it('renders properly', () => {
        const wrapper = shallowMount(HomeView.default);
        expect(wrapper.exists()).toBe(true);
    });
});