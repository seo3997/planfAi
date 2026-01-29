/**
 * Admin Portal Common JavaScript
 */

// Axios Global Interceptor for Session handling and Loading Mask
if (typeof axios !== 'undefined') {
    axios.interceptors.request.use(
        config => {
            // 로그인 요청 등 특정 요청에서는 로딩바 제외하고 싶을 경우 조건 추가 가능
            wrapLoadingMask('show');
            return config;
        },
        error => {
            wrapLoadingMask('hide');
            return Promise.reject(error);
        }
    );

    axios.interceptors.response.use(
        response => {
            wrapLoadingMask('hide');
            return response;
        },
        error => {
            wrapLoadingMask('hide');
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
        const topMenus = await this.fetchTopMenus();
        if (window.location.pathname.includes('/admin/main.do')) {
            // Dashboard doesn't show sidebar, but we still need activeTopId for GNB highlights
            if (!this.state.activeTopId && topMenus.length > 0) {
                this.state.activeTopId = topMenus[0].menuId;
            }
            return;
        }

        if (this.state.activeTopId) {
            await this.fetchLeftMenus(this.state.activeTopId);
        } else if (topMenus.length > 0) {
            await this.fetchLeftMenus(topMenus[0].menuId);
        }
    },

    async fetchTopMenus() {
        // Try to get from session cache first
        const cached = sessionStorage.getItem('topMenus');
        if (cached) {
            this.state.topMenus = JSON.parse(cached);
            return this.state.topMenus;
        }

        try {
            const res = await axios.get('/api/admin/menu/user-menus', { params: { menuLevel: 3 } });
            this.state.topMenus = res.data;
            sessionStorage.setItem('topMenus', JSON.stringify(res.data));
            return res.data;
        } catch (err) {
            console.error('Failed to fetch top menus', err);
            return [];
        }
    },

    async fetchLeftMenus(topMenuId) {
        this.state.activeTopId = topMenuId;
        localStorage.setItem('activeTopId', topMenuId);

        // Try cache
        const cacheKey = 'leftMenus_' + topMenuId;
        const cached = sessionStorage.getItem(cacheKey);
        if (cached) {
            this.state.leftMenus = JSON.parse(cached);
            return this.state.leftMenus;
        }

        try {
            const res = await axios.get('/api/admin/menu/user-menus', { params: { parentMenuId: topMenuId } });
            const filtered = res.data.filter(m => m.menuTyCode !== '30');
            this.state.leftMenus = filtered;
            sessionStorage.setItem(cacheKey, JSON.stringify(filtered));
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
        const changeTop = async (id) => {
            const leftMenus = await AdminMenu.fetchLeftMenus(id);
            // After loading left menus, navigate to the first menu item if it exists
            const firstMenu = leftMenus.find(m => m.menuTyCode === '20');
            if (firstMenu && firstMenu.url) {
                location.href = firstMenu.url;
            }
        };
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

/**
 * 전역 로딩 마스크 제어 함수
 */
function wrapLoadingMask(stat) {
    let dimmed = document.querySelector('.dimmed');
    
    // 마스크 엘리먼트가 없으면 생성하여 body에 추가
    if (!dimmed) {
        const loadingWrap = `
            <div class="dimmed">
                <div class="loading-bar">
                    <div class="lds-default">
                        <div></div><div></div><div></div><div></div>
                        <div></div><div></div><div></div><div></div>
                        <div></div><div></div><div></div><div></div>
                    </div>
                </div>
            </div>`;
        document.body.insertAdjacentHTML('beforeend', loadingWrap);
        dimmed = document.querySelector('.dimmed');
    }

    if (stat === 'show') {
        document.body.style.overflow = 'hidden';
        dimmed.style.display = 'flex';
    } else {
        document.body.style.overflow = '';
        dimmed.style.display = 'none';
    }
}

// 페이지 표시/숨김 이벤트 처리 (BFCache 대응)
window.addEventListener('pageshow', function (e) {
    wrapLoadingMask('hide');
    if (e.persisted) console.log('[BFCache] restored');
});

window.addEventListener('pagehide', function () {
    wrapLoadingMask('hide');
});

// 페이지 이동 전 로딩바 표시
window.onbeforeunload = function(e) {
    // 특정 예외 처리 (예: 파일 다운로드 등)가 필요한 경우 여기에 논리 추가
    // 브라우저에 따라 onbeforeunload에서 무거운 작업을 하면 차단될 수 있으므로 주의
    wrapLoadingMask('show');
};
