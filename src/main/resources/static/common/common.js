// Utility to format currency
function formatCurrency(value) {
    if (!value && value !== 0) return '-';
    return new Intl.NumberFormat('ko-KR', {
        style: 'currency',
        currency: 'KRW'
    }).format(value);
}

window.CommonUtils = {
    formatCurrency
};
