import { shallowMount } from '@vue/test-utils';
import Blog from '@/views/blog.vue';

describe('Blog', () => {
    it('renders properly', () => {
        const wrapper = shallowMount(Blog);
        expect(wrapper.exists()).toBe(true);
    });

    it('navigates to create blog page when "新建博客" is clicked', async () => {
        const mockRouter = {
            push: jest.fn(),
        };
        const wrapper = shallowMount(Blog, {
            global: {
                mocks: {
                    $router: mockRouter,
                },
            },
        });
        await wrapper.find('.create-frame').trigger('click');
        expect(mockRouter.push).toHaveBeenCalledWith('/createblog');
    });

});