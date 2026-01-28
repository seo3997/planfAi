/**
 * Admin Portal Common JavaScript
 */

// Axios Global Interceptor for Session handling
if (typeof axios !== 'undefined') {
    axios.interceptors.response.use(
        response => response,
        error => {
            if (error.response && error.response.status === 401) {
                alert('세션이 만료되었거나 권한이 없습니다. 로그인 페이지로 이동합니다.');
                location.href = '/admin/login.do';
            }
            return Promise.reject(error);
        }
    );
}

const AdminUtil = {
    /**
     * 포맷팅: 숫자에 콤마 추가
     */
    formatNumber: (num) => {
        return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    },

    /**
     * 공통 API 에러 메시지 처리
     */
    handleApiError: (err, defaultMsg = '오류가 발생했습니다.') => {
        const msg = err.response && err.response.data ? err.response.data : defaultMsg;
        alert(msg);
        console.error('API Error:', err);
    }
};
