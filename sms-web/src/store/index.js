import vue from 'vue'
import Vuex from 'vuex'
import router,{resetRouter} from '../router'
import createPersistedState from 'vuex-persistedstate'
vue.use(Vuex)

function addNewRoute(menuList) {
    let routes = router.options.routes
    console.log(menuList)
    console.log(routes)
    routes.forEach(routeItem=>{
        if (routeItem.path=="/Index"){
            menuList.forEach(menu=>{
                let childRoute = {
                    path:'/'+menu.menuclick,
                    name:menu.menuclick,
                    meta:{
                        title:menu.menuname
                    },
                    component:()=>import('../components/'+menu.menucomponent)
                }

                routeItem.children.push(childRoute)
            })
        }
    })
    resetRouter()
    router.addRoutes(routes)
}

export default new Vuex.Store({
    state:{
        menu:[]
    },
    mutations:{
        setMenu(state,menuList) {
            state.menu = menuList
            addNewRoute(menuList)
        },

    },

    getters:{
        getMenu(state){
            return state.menu
        }
    },

    plugins:[createPersistedState()]

})


//
// 由于 store 中的状态是响应式的，在组件中调用 store 中的状态简单到仅需要在计算属
// 性中返回即可。触发变化也仅仅是在组件的 methods 中提交 mutation。
