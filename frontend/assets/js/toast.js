// Toast simples global: showToast(mensagem, tipo)
// tipo: 'success' | 'error' | 'info'
(function () {
  function showToast(message, type) {
    var container = document.getElementById('toast-container');
    if (!container) return;

    var toast = document.createElement('div');
    toast.className = 'toast-item pointer-events-auto mb-2 w-full max-w-xs rounded-lg border text-xs shadow-lg transition-opacity duration-300 ';

    var baseClasses = 'px-3 py-2 flex items-start gap-2';
    var colorClasses;
    if (type === 'error') {
      colorClasses = 'bg-red-900/90 border-red-700 text-red-100';
    } else if (type === 'success') {
      colorClasses = 'bg-emerald-900/90 border-emerald-700 text-emerald-100';
    } else {
      colorClasses = 'bg-slate-900/90 border-slate-700 text-slate-100';
    }
    toast.className += baseClasses + ' ' + colorClasses;

    toast.innerHTML = '<div class="mt-0.5">' + message + '</div>';

    container.appendChild(toast);

    // Auto-remove after 4s
    setTimeout(function () {
      toast.style.opacity = '0';
      setTimeout(function () {
        if (toast.parentNode) {
          toast.parentNode.removeChild(toast);
        }
      }, 300);
    }, 4000);
  }

  window.showToast = showToast;
})();
