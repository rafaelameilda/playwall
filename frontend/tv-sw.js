'use strict';

const SHELL_CACHE = 'tv-wall-shell-v1';
const RUNTIME_CACHE = 'tv-wall-runtime-v1';
const TV_HTML_URL = new URL('tv.html', self.location.href).href;

self.addEventListener('install', function (event) {
    event.waitUntil(
        caches.open(SHELL_CACHE)
            .then(function (cache) { return cache.add(TV_HTML_URL); })
            .then(function () { return self.skipWaiting(); })
    );
});

self.addEventListener('activate', function (event) {
    event.waitUntil(self.clients.claim());
});

self.addEventListener('message', function (event) {
    const data = event.data || {};
    if (data.type !== 'CACHE_MEDIA' || !Array.isArray(data.urls)) return;

    const safeTvId = String(data.tvId || 'default').replace(/[^a-zA-Z0-9_-]/g, '_');
    event.waitUntil(
        caches.open('tv-wall-media-v1-' + safeTvId).then(function (cache) {
            return Promise.all(data.urls.filter(Boolean).map(function (url) {
                const absoluteUrl = new URL(url, self.location.href).href;
                return cache.match(absoluteUrl).then(function (cached) {
                    if (cached) return;
                    const isSameOrigin = new URL(absoluteUrl).origin === self.location.origin;
                    const request = new Request(absoluteUrl, {
                        mode: isSameOrigin ? 'same-origin' : 'no-cors',
                        credentials: isSameOrigin ? 'same-origin' : 'omit'
                    });
                    return fetch(request).then(function (response) {
                        if (!response || (!response.ok && response.type !== 'opaque')) return;
                        return cache.put(absoluteUrl, response.clone());
                    }).catch(function () {});
                });
            }));
        })
    );
});

function rangeResponse(request, cached) {
    const range = request.headers.get('range');
    if (!range || cached.type === 'opaque') return Promise.resolve(cached);

    return cached.clone().arrayBuffer().then(function (buffer) {
        const match = /bytes=(\d+)-(\d*)/.exec(range);
        if (!match) return cached;

        const start = Number(match[1]);
        const end = match[2] ? Number(match[2]) : buffer.byteLength - 1;
        const boundedEnd = Math.min(end, buffer.byteLength - 1);
        const headers = new Headers(cached.headers);
        headers.set('Content-Range', 'bytes ' + start + '-' + boundedEnd + '/' + buffer.byteLength);
        headers.set('Content-Length', String(boundedEnd - start + 1));
        headers.set('Accept-Ranges', 'bytes');

        return new Response(buffer.slice(start, boundedEnd + 1), {
            status: 206,
            statusText: 'Partial Content',
            headers: headers
        });
    });
}

self.addEventListener('fetch', function (event) {
    const request = event.request;
    if (request.method !== 'GET') return;

    if (request.mode === 'navigate') {
        event.respondWith(
            fetch(request).catch(function () { return caches.match(TV_HTML_URL); })
        );
        return;
    }

    if (request.destination !== 'image' && request.destination !== 'video') return;

    event.respondWith(
        caches.match(request.url).then(function (cached) {
            if (cached) return rangeResponse(request, cached);

            return fetch(request).then(function (response) {
                if (!response || (!response.ok && response.type !== 'opaque')) return response;
                const copy = response.clone();
                caches.open(RUNTIME_CACHE).then(function (cache) {
                    cache.put(request.url, copy).catch(function () {});
                });
                return response;
            });
        })
    );
});
