import { createRouter, createWebHashHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
  {
    path: '/blog',
    name: 'blog',
    component: () => import('../views/blog.vue'),
  },
  {
    path: '/createblog',
    name: 'createblog',
    component: () => import('../views/createblog.vue')
  },
  {
    path: '/blog/:id',
    name: 'blog-detail',
    component: () => import('../views/blog-detail.vue'),
    props: true
  },
  {
    path: '/gallery',
    name: 'gallery',
    component: () => import('../views/gallery.vue'),
  },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
});

export default router
