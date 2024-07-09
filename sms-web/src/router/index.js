import VueRouter from "vue-router";

const routes = [
    {
        path:'/',
        name:'login',
        component:()=>import('../components/Login')
    },
    {
        path:'/Register',
        name:'register',
        component:()=>import('../components/Register')
    },
    {
        path:'/FindPwd',
        name:'findPwd',
        component:()=>import('../components/FindPwd')
    },
    {
        path:'/Index',
        name:'index',
        component:()=>import('../components/Index'),
        children:[
            {
                path:'/Home',
                name:'home',
                meta:{
                    title:'��ҳ'
                },
                component:()=>import('../components/Home'),
            },
            // {
            //     path:'/Admin',
            //     name:'admin',
            //     meta:{
            //         title:'����Ա����'
            //     },
            //     component:()=>import('../components/admin/AdminManage.vue'),
            // },
            // {
            //     path:'/User',
            //     name:'user',
            //     meta:{
            //         title:'�û�����'
            //     },
            //     component:()=>import('../components/user/UserManage.vue'),
            // },
        ]
    }
]

const router = new VueRouter({
    mode:'history',
    routes
})

export  function resetRouter(){
    router.matcher = new VueRouter({
        mode:'history',
        routes:[]
    }).matcher
}

const VueRouterPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (to){
    return VueRouterPush.call(this,to).catch(err=>err)
}
export default router;