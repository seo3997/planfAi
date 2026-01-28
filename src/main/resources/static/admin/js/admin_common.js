/**
 * Admin Portal Common JavaScript
 */

// Axios Global Interceptor for Session handling
if (typeof axios !== 'undefined') {
    axios.interceptors.response.use(
        response => response,
        error => {
            // 로그인 API 요청인 경우 인터셉터에서 처리하지 않고 인터페이스에서 직접 처리
            if (error.config && error.config.url.includes('/api/admin/login')) {
                return Promise.reject(error);
            }

            if (error.response && error.response.status === 401) {
                alert('세션이 만료되었거나 권한이 없습니다. 로그인 페이지로 이동합니다.');
                location.href = '/admin/login.do';
            }
            return Promise.reject(error);
        }
    );
}

const AdminMenu = {
    state: Vue.reactive({
        topMenus: [],
        leftMenus: [],
        activeTopId: localStorage.getItem('activeTopId') || '',
        currentPath: window.location.pathname
    }),

    async init() {
        await this.fetchTopMenus();
        if (this.state.activeTopId) {
            await this.fetchLeftMenus(this.state.activeTopId);
        } else if (this.state.topMenus.length > 0) {
            await this.fetchLeftMenus(this.state.topMenus[0].menuId);
        }
    },

    async fetchTopMenus() {
        try {
            const res = await axios.get('/api/admin/menu/user-menus', { params: { menuLevel: 3 } });
            this.state.topMenus = res.data;
            return res.data;
        } catch (err) {
            console.error('Failed to fetch top menus', err);
            return [];
        }
    },

    async fetchLeftMenus(topMenuId) {
        try {
            this.state.activeTopId = topMenuId;
            localStorage.setItem('activeTopId', topMenuId);
            const res = await axios.get('/api/admin/menu/user-menus', { params: { parentMenuId: topMenuId } });
            this.state.leftMenus = res.data.filter(m => m.menuTyCode !== '30');
            return this.state.leftMenus;
        } catch (err) {
            console.error('Failed to fetch left menus', err);
            return [];
        }
    }
};

// --- Common Components ---

const AdminHeader = {
    template: `
        <header class="admin-header">
            <div class="logo-area" @click="goHome" style="cursor:pointer">planF</div>
            <nav class="nav-area">
                <div v-for="menu in menuState.topMenus" 
                     :key="menu.menuId" 
                     class="nav-item" 
                     :class="{ active: menuState.activeTopId === menu.menuId }"
                     @click="changeTop(menu.menuId)">
                    {{ menu.menuNm }}
                </div>
            </nav>
            <div class="user-info">
                <i class="bi bi-person-fill me-1"></i> admin
                <a href="/admin/logout.do" class="ms-3"><i class="bi bi-unlock-fill me-1"></i> Logout</a>
            </div>
        </header>
    `,
    setup() {
        const menuState = AdminMenu.state;
        const changeTop = (id) => AdminMenu.fetchLeftMenus(id);
        const goHome = () => location.href = '/admin/main.do';
        return { menuState, changeTop, goHome };
    }
};

const AdminSidebar = {
    template: `
        <aside class="sidebar">
            <div v-for="menu in menuState.leftMenus" :key="menu.menuId">
                <div v-if="menu.menuTyCode === '10'" class="nav-link category" @click="menu.isExpanded = !menu.isExpanded">
                    <i class="bi" :class="menu.isExpanded ? 'bi-chevron-down' : 'bi-chevron-right'"></i>
                    {{ menu.menuNm }}
                </div>
                <a v-else-if="menu.menuTyCode === '20'" 
                   :href="menu.url" 
                   class="nav-link" 
                   :class="{ active: currentPath === menu.url }">
                    {{ menu.menuNm }}
                </a>
            </div>
        </aside>
    `,
    setup() {
        const menuState = AdminMenu.state;
        const currentPath = window.location.pathname;
        return { menuState, currentPath };
    }
};

const AdminLayout = {
    register(app) {
        app.component('admin-header', AdminHeader);
        app.component('admin-sidebar', AdminSidebar);
    }
};

const AdminUtil = {
    formatNumber: (num) => {
        if (!num) return "0";
        return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    },
    handleApiError: (err, defaultMsg = '오류가 발생했습니다.') => {
        const msg = err.response && err.response.data ? err.response.data : defaultMsg;
        alert(msg);
        console.error('API Error:', err);
    }
};
