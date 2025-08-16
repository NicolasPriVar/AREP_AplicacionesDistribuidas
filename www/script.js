function $(id){ return document.getElementById(id); }
function j(pretty){ return JSON.stringify(pretty, null, 2); }

window.addEventListener('DOMContentLoaded', () => {
    $('btnHello').addEventListener('click', async () => {
        const name = encodeURIComponent($('name').value || 'desconocido');
        const res = await fetch(`/app/hello?name=${name}`);
        const data = await res.json();
        $('outHello').textContent = j(data);
    });

    $('btnTime').addEventListener('click', async () => {
        const res = await fetch(`/app/time`);
        const data = await res.json();
        $('outTime').textContent = j(data);
    });

    $('btnEcho').addEventListener('click', async () => {
        const msg = $('msg').value || '';
        // application/x-www-form-urlencoded
        const body = new URLSearchParams({ mensaje: msg }).toString();
        const res = await fetch(`/app/echo?source=frontend`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body
        });
        const data = await res.json();
        $('outEcho').textContent = j(data);
    });
});
