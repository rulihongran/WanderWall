import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import TraceMap from "../views/TraceMap";
import Gallery from "../views/Gallery";

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/tracemap',
    name: 'tracemap',
    component: TraceMap
  },
  {
    path: '/blog',
    name: 'blog',
    component: () => import('../views/blog.vue'),
  },
  {
    path: '/createblog',
    name: 'createblog',
    component: () => import('../views/CreateBlog.vue')
  },
  {
    path: '/blog/:id',
    name: 'blog-detail',
    component: () => import('../views/BlogDetail.vue'),
    props: true
  },
  {
    path: '/gallery',
    name: 'gallery',
    component: Gallery,
  },
  {
    path: '/about',
    name: 'about',
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
