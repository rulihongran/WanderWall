import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import TraceMap from "../views/TraceMap";
import Gallery from "../views/Gallery";
import RelationGraph from "@/views/RelationGraph";
import AnnualReport from "@/views/AnnualReport";
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
  {
    path:'/relation-graph',
    name:'relation-graph',
    component: RelationGraph,
  },
  {
    path: '/report',
    name: 'report',
    component: AnnualReport
  },
  {
    path: '/login',
    name: 'LoginView',
    component: () => import('../views/LoginView.vue')
  },
  {
    path: '/register',
    name: 'RegisterView',
    component: () => import('../views/RegisterView.vue')
  },
  {
    path: '/findpass',
    name: 'FindpassView',
    component: () => import('../views/FindpassView.vue')
  },

  {
    path: '/userinfo',
    name: 'UserView',
    component: () => import('@/views/UserInfoView/UserView.vue'),
    children: [
      {
        path: '/work',
        name: 'workView',
        component: () => import('@/views/UserInfoView/workView.vue')
      },
      {
        path: '/friend',
        name: 'friendView',
        component: () => import('@/views/UserInfoView/friendView.vue')
      },
      {
        path: '/addfriend',
        name: 'addfriView',
        component: () => import('@/views/UserInfoView/addfriView.vue')
      },
    ]
  },

  {
    path:'/edit',
    name:'EditView',
    component: () => import('../views/UserInfoView/EditView.vue'),
    children: [
      {
        // path: '/personal/info/:id',
        path: '/infoView',
        name:'infoView',
        component: () => import('@/views/UserInfoView/infoView.vue')
      },
    ]
  },
  {
    path:'/testView',
    name:'testView',
    component: () => import( '../views/UserInfoView/testView.vue'),
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
