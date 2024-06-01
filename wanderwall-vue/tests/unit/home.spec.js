import { shallowMount } from '@vue/test-utils';
import AnnualReport from '@/views/AnnualReport.vue';


describe('AnnualReport', () => {
    // it('renders correctly', () => {
    //     const wrapper = shallowMount(AnnualReport);
    //     expect(wrapper.exists()).toBe(true);
    // });

    it('highlights word correctly', () => {
        const wrapper = shallowMount(AnnualReport);
        const highlightedContent = wrapper.vm.highlightWord("This is a test sentence.", "test");
        expect(highlightedContent).toBe("This is a <span class=\"highlight\">test</span> sentence.");
    });

    it('correctly sets data properties', () => {
        const wrapper = shallowMount(AnnualReport);
        expect(wrapper.vm.year).toBe(2024);
        expect(wrapper.vm.city_num).toBe(5);
        expect(wrapper.vm.exceed_friend_rate).toBe("50%");
        // Add more expectations for other data properties
    });

    // You can add more tests for other methods and data properties as needed
});