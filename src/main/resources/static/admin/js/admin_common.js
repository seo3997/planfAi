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
        // const cached = sessionStorage.getItem('topMenus');
        // if (cached) {
        //     this.state.topMenus = JSON.parse(cached);
        //     return this.state.topMenus;
        // }

        try {
            const res = await axios.get('/api/admin/menu/user-menus', { params: { menuLevel: 3 } });
            this.state.topMenus = res.data;
            // sessionStorage.setItem('topMenus', JSON.stringify(res.data));
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
        // const cacheKey = 'leftMenus_' + topMenuId;
        // const cached = sessionStorage.getItem(cacheKey);
        // if (cached) {
        //     this.state.leftMenus = JSON.parse(cached);
        //     return this.state.leftMenus;
        // }

        try {
            const res = await axios.get('/api/admin/menu/user-menus', { params: { parentMenuId: topMenuId } });
            const filtered = res.data.filter(m => m.menuTyCode !== '30');
            this.state.leftMenus = filtered;
            // sessionStorage.setItem(cacheKey, JSON.stringify(filtered));
            return this.state.leftMenus;
        } catch (err) {
            console.error('Failed to fetch left menus', err);
            return [];
        }
    }
};

// --- Common Components ---

const AdminProfileModal = {
    template: `
        <div class="profile-modal-overlay" @click.self="$emit('close')">
            <div class="profile-modal">
                <div class="profile-modal-header">
                    <h3>나의 정보</h3>
                    <button class="btn-close" @click="$emit('close')">&times;</button>
                </div>
                <div class="profile-modal-body">
                    <div class="profile-form-group">
                        <label>사용자ID</label>
                        <div class="form-control-static">{{ user.userId }}</div>
                    </div>
                    <div class="profile-form-group">
                        <label>비밀번호</label>
                        <input type="password" class="form-control" v-model="user.password" placeholder="비밀번호">
                    </div>
                    <div class="profile-form-group">
                        <label>이름</label>
                        <input type="text" class="form-control" v-model="user.userNm" placeholder="이름">
                    </div>
                    <div class="profile-form-group">
                        <label>이메일</label>
                        <input type="email" class="form-control" v-model="user.email" placeholder="이메일">
                    </div>
                    <div class="profile-form-group">
                        <label>연락처</label>
                        <div class="contact-inputs">
                            <input type="text" class="form-control" v-model="contact.part1">
                            <span>-</span>
                            <input type="text" class="form-control" v-model="contact.part2">
                            <span>-</span>
                            <input type="text" class="form-control" v-model="contact.part3">
                        </div>
                    </div>
                    <div class="profile-form-group">
                        <label>업체명</label>
                        <div class="form-control-static">{{ user.authorNm || '관리업체' }}</div>
                    </div>
                </div>
                <div class="profile-modal-footer">
                    <button class="btn-close-footer" @click="$emit('close')">
                        <i class="bi bi-x-square"></i> 닫기
                    </button>
                    <button class="btn-update" @click="updateProfile">수정</button>
                </div>
            </div>
        </div>
    `,
    setup(props, { emit }) {
        const user = Vue.ref({
            userId: '',
            password: '',
            userNm: '',
            email: '',
            cttpc: ''
        });
        const contact = Vue.ref({ part1: '', part2: '', part3: '' });

        const fetchProfile = () => {
            axios.get('/api/admin/user/profile')
                .then(res => {
                    user.value = res.data;
                    user.value.password = ''; // Don't show hash
                    if (user.value.cttpc) {
                        const parts = user.value.cttpc.split('-');
                        contact.value.part1 = parts[0] || '';
                        contact.value.part2 = parts[1] || '';
                        contact.value.part3 = parts[2] || '';
                    }
                });
        };

        const updateProfile = () => {
            user.value.cttpc = `${contact.value.part1}-${contact.value.part2}-${contact.value.part3}`;
            axios.put('/api/admin/user/profile', user.value)
                .then(res => {
                    alert('수정되었습니다.');
                    emit('close');
                })
                .catch(err => alert('수정 중 오류 발생'));
        };

        Vue.onMounted(fetchProfile);

        return { user, contact, updateProfile };
    }
};

const AdminHeader = {
    template: `
        <div>
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
                    <i class="bi bi-person-fill me-1"></i> 
                    <span @click="showProfileModal = true" style="cursor:pointer;">{{ loginId }}</span>
                    <a href="/admin/logout.do" class="ms-3"><i class="bi bi-unlock-fill me-1"></i> Logout</a>
                </div>
            </header>
            <admin-profile-modal v-if="showProfileModal" @close="showProfileModal = false"></admin-profile-modal>
        </div>
    `,
    setup() {
        const menuState = AdminMenu.state;
        const showProfileModal = Vue.ref(false);
        const loginId = Vue.ref('admin');

        const fetchLoginId = () => {
             axios.get('/api/admin/user/profile')
                .then(res => {
                    loginId.value = res.data.userId;
                });
        };

        const changeTop = async (id) => {
            const leftMenus = await AdminMenu.fetchLeftMenus(id);
            const firstMenu = leftMenus.find(m => m.menuTyCode === '20');
            if (firstMenu && firstMenu.url) {
                location.href = firstMenu.url;
            }
        };
        const goHome = () => location.href = '/admin/main.do';

        Vue.onMounted(fetchLoginId);

        return { menuState, changeTop, goHome, showProfileModal, loginId };
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

const AdminFooter = {
    template: `
        <footer class="admin-footer">
            Copyright 2026. <strong>planF</strong>. All rights reserved.
        </footer>
    `
};

const AdminCode = {
    // 중분류 코드 조회 (groupId 기준)
    async getCodes(groupId) {
        try {
            const res = await axios.get(`/api/admin/code/sub/${groupId}`);
            return res.data.map(item => ({
                CODE: item.code,
                CODE_NM: item.codeNm
            }));
        } catch (err) {
            console.error('AdminCode.getCodes error:', err);
            return [];
        }
    },
    // 소분류 코드 조회 (groupId, 중분류 code 기준)
    async getSclasCodes(groupId, code) {
        try {
            const res = await axios.get(`/api/admin/code/sclas/${groupId}/${code}`);
            return res.data.map(item => ({
                CODE: item.sclasCode,
                CODE_NM: item.sclasNm
            }));
        } catch (err) {
            console.error('AdminCode.getSclasCodes error:', err);
            return [];
        }
    }
};

const AdminPagination = {
    props: {
        totalCount: { type: Number, default: 0 },
        currentPage: { type: Number, default: 1 },
        pageSize: { type: Number, default: 10 }
    },
    template: `
        <nav class="mt-4" v-if="totalPages > 0">
            <ul class="pagination pagination-sm justify-content-center">
                <li class="page-item" :class="{ disabled: currentPage === 1 }">
                    <a class="page-link" href="#" @click.prevent="changePage(currentPage - 1)">
                        <i class="bi bi-chevron-left"></i>
                    </a>
                </li>
                <li v-for="p in totalPages" :key="p" class="page-item" :class="{ active: p === currentPage }">
                    <a class="page-link" href="#" @click.prevent="changePage(p)">{{ p }}</a>
                </li>
                <li class="page-item" :class="{ disabled: currentPage === totalPages }">
                    <a class="page-link" href="#" @click.prevent="changePage(currentPage + 1)">
                        <i class="bi bi-chevron-right"></i>
                    </a>
                </li>
            </ul>
        </nav>
    `,
    setup(props, { emit }) {
        const totalPages = Vue.computed(() => Math.ceil(props.totalCount / props.pageSize) || 0);
        const changePage = (p) => {
            if (p < 1 || p > totalPages.value) return;
            emit('change-page', p);
        };
        return { totalPages, changePage };
    }
};

const AdminLayout = {
    register(app) {
        app.component('admin-header', AdminHeader);
        app.component('admin-sidebar', AdminSidebar);
        app.component('admin-footer', AdminFooter);
        app.component('admin-profile-modal', AdminProfileModal);
        app.component('admin-pagination', AdminPagination);
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
