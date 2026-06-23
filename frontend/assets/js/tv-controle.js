// ── Módulo: Controle de TV ───────────────────────────────────────────────────
// Segue o mesmo padrão dos outros módulos do projeto (jQuery + API_BASE_URL).

function initTvControle() {
  var BASE = API_BASE_URL + '/tv';

  // Cache de dados carregados
  var _ambientes  = [];
  var _grupos     = [];
  var _conteudos  = [];
  var _playlists  = [];
  var _tvs        = [];

  // ── Helpers ────────────────────────────────────────────────────────────────

  function showToastOk(msg)  { if (typeof showToast === 'function') showToast(msg, 'success'); }
  function showToastErr(msg) { if (typeof showToast === 'function') showToast(msg, 'error');   }

  function ajaxErrorMessage(xhr, fallback) {
    if (!xhr || xhr.status === 0) {
      return 'Backend indisponível. Inicie a aplicação na porta 8090 e tente novamente.';
    }
    return (xhr.responseJSON && xhr.responseJSON.message) || fallback;
  }

  function badgeStatus(st) {
    var cor = st === 'A' ? 'bg-emerald-500/20 text-emerald-300' : 'bg-slate-700 text-slate-400';
    var label = st === 'A' ? 'Ativo' : 'Inativo';
    return '<span class="inline-flex items-center rounded-full px-2 py-0.5 text-[0.65rem] font-medium ' + cor + '">' + label + '</span>';
  }

  // ── Abas ───────────────────────────────────────────────────────────────────

  $('#tv-controle-root').on('click', '.tv-tab-btn', function () {
    var tab = $(this).data('tv-tab');

    $('.tv-tab-btn')
      .removeClass('bg-slate-800 border-b-2 border-emerald-400 text-slate-100')
      .addClass('text-slate-400');
    $(this)
      .addClass('bg-slate-800 border-b-2 border-emerald-400 text-slate-100')
      .removeClass('text-slate-400');

    $('.tv-tab-panel').addClass('hidden');
    $('#tv-tab-' + tab).removeClass('hidden');

    if (tab === 'ambientes') loadAmbientes();
    else if (tab === 'tvs')      loadTvs();
    else if (tab === 'grupos')   loadGrupos();
    else if (tab === 'conteudos') loadConteudos();
    else if (tab === 'playlists') loadPlaylists();
  });

  // ── AMBIENTES ──────────────────────────────────────────────────────────────

  function loadAmbientes() {
    $.ajax({ url: BASE + '/ambientes' })
      .done(function (data) {
        _ambientes = data || [];
        var html = '';
        if (!_ambientes.length) {
          html = '<tr><td colspan="3" class="px-3 py-4 text-center text-slate-500">Nenhum ambiente cadastrado.</td></tr>';
        } else {
          _ambientes.forEach(function (a) {
            html += '<tr>' +
              '<td class="px-3 py-2">' + (a.nmAmbiente || '') + '</td>' +
              '<td class="px-3 py-2">' + badgeStatus(a.stStatus) + '</td>' +
              '<td class="px-3 py-2 text-right space-x-2">' +
                '<button class="text-xs text-slate-300 hover:text-emerald-400" data-edit-ambiente="' + a.id + '"><i class="fa-solid fa-pen-to-square"></i></button>' +
                '<button class="text-xs text-slate-300 hover:text-red-400 ml-2" data-del-ambiente="' + a.id + '"><i class="fa-solid fa-trash"></i></button>' +
              '</td></tr>';
          });
        }
        $('#tv-ambientes-tbody').html(html);
      })
      .fail(function () { showToastErr('Erro ao carregar ambientes.'); });
  }

  $('#tv-controle-root').on('click', '#btn-novo-ambiente', function () {
    $('#amb-id').val('');
    $('#amb-nome').val('');
    $('#amb-status').val('A');
    $('#modal-ambiente-title').text('Novo Ambiente');
    $('#modal-ambiente-erro').addClass('hidden').text('');
    $('#modal-ambiente').removeClass('hidden');
  });

  $('#tv-controle-root').on('click', '[data-edit-ambiente]', function () {
    var id = $(this).data('edit-ambiente');
    var amb = _ambientes.find(function (a) { return a.id === id; });
    if (!amb) return;
    $('#amb-id').val(amb.id);
    $('#amb-nome').val(amb.nmAmbiente);
    $('#amb-status').val(amb.stStatus);
    $('#modal-ambiente-title').text('Editar Ambiente');
    $('#modal-ambiente-erro').addClass('hidden').text('');
    $('#modal-ambiente').removeClass('hidden');
  });

  $('#tv-controle-root').on('click', '[data-del-ambiente]', function () {
    var id = $(this).data('del-ambiente');
    if (!confirm('Inativar este ambiente?')) return;
    $.ajax({ url: BASE + '/ambientes/' + id, method: 'DELETE' })
      .done(function () { showToastOk('Ambiente inativado.'); loadAmbientes(); })
      .fail(function () { showToastErr('Erro ao inativar ambiente.'); });
  });

  $('#tv-controle-root').on('click', '#btn-cancelar-ambiente', function () {
    $('#modal-ambiente').addClass('hidden');
  });

  $('#tv-controle-root').on('click', '#btn-salvar-ambiente', function () {
    var id    = $('#amb-id').val();
    var nome  = $('#amb-nome').val().trim();
    var st    = $('#amb-status').val();
    if (!nome) { $('#modal-ambiente-erro').removeClass('hidden').text('Informe o nome do ambiente.'); return; }

    var url    = id ? BASE + '/ambientes/' + id : BASE + '/ambientes';
    var method = id ? 'PUT' : 'POST';
    $.ajax({ url: url, method: method, contentType: 'application/json',
      data: JSON.stringify({ nmAmbiente: nome, stStatus: st }) })
      .done(function () {
        $('#modal-ambiente').addClass('hidden');
        showToastOk('Ambiente salvo com sucesso.');
        loadAmbientes();
      })
      .fail(function (xhr) {
        var msg = ajaxErrorMessage(xhr, 'Erro ao salvar.');
        $('#modal-ambiente-erro').removeClass('hidden').text(msg);
      });
  });

  // ── GRUPOS ─────────────────────────────────────────────────────────────────

  function loadGrupos() {
    $.ajax({ url: BASE + '/grupos' })
      .done(function (data) {
        _grupos = data || [];
        var html = '';
        if (!_grupos.length) {
          html = '<tr><td colspan="4" class="px-3 py-4 text-center text-slate-500">Nenhum grupo cadastrado.</td></tr>';
        } else {
          _grupos.forEach(function (g) {
            html += '<tr>' +
              '<td class="px-3 py-2">' + (g.nmGrupo || '') + '</td>' +
              '<td class="px-3 py-2">' + (g.nrLinhas || 1) + 'x' + (g.nrColunas || 1) + '</td>' +
              '<td class="px-3 py-2">' + badgeStatus(g.stStatus) + '</td>' +
              '<td class="px-3 py-2 text-right space-x-2">' +
                '<button class="text-xs text-slate-300 hover:text-emerald-400" data-edit-grupo="' + g.id + '"><i class="fa-solid fa-pen-to-square"></i></button>' +
                '<button class="text-xs text-slate-300 hover:text-red-400 ml-2" data-del-grupo="' + g.id + '"><i class="fa-solid fa-trash"></i></button>' +
              '</td></tr>';
          });
        }
        $('#tv-grupos-tbody').html(html);
      })
      .fail(function () { showToastErr('Erro ao carregar grupos.'); });
  }

  $('#tv-controle-root').on('click', '#btn-novo-grupo', function () {
    $('#grupo-id').val('');
    $('#grupo-nome').val('');
    $('#grupo-colunas').val(2);
    $('#grupo-linhas').val(2);
    $('#modal-grupo-title').text('Novo Grupo (Video Wall)');
    $('#modal-grupo-erro').addClass('hidden').text('');
    $('#modal-grupo').removeClass('hidden');
  });

  $('#tv-controle-root').on('click', '[data-edit-grupo]', function () {
    var id = $(this).data('edit-grupo');
    var g  = _grupos.find(function (x) { return x.id === id; });
    if (!g) return;
    $('#grupo-id').val(g.id);
    $('#grupo-nome').val(g.nmGrupo);
    $('#grupo-colunas').val(g.nrColunas);
    $('#grupo-linhas').val(g.nrLinhas);
    $('#modal-grupo-title').text('Editar Grupo');
    $('#modal-grupo-erro').addClass('hidden').text('');
    $('#modal-grupo').removeClass('hidden');
  });

  $('#tv-controle-root').on('click', '[data-del-grupo]', function () {
    var id = $(this).data('del-grupo');
    if (!confirm('Inativar este grupo?')) return;
    $.ajax({ url: BASE + '/grupos/' + id, method: 'DELETE' })
      .done(function () { showToastOk('Grupo inativado.'); loadGrupos(); })
      .fail(function () { showToastErr('Erro ao inativar grupo.'); });
  });

  $('#tv-controle-root').on('click', '#btn-cancelar-grupo', function () {
    $('#modal-grupo').addClass('hidden');
  });

  $('#tv-controle-root').on('click', '#btn-salvar-grupo', function () {
    var id   = $('#grupo-id').val();
    var nome = $('#grupo-nome').val().trim();
    var cols = parseInt($('#grupo-colunas').val()) || 1;
    var rows = parseInt($('#grupo-linhas').val()) || 1;
    if (!nome) { $('#modal-grupo-erro').removeClass('hidden').text('Informe o nome do grupo.'); return; }

    var url    = id ? BASE + '/grupos/' + id : BASE + '/grupos';
    var method = id ? 'PUT' : 'POST';
    $.ajax({ url: url, method: method, contentType: 'application/json',
      data: JSON.stringify({ nmGrupo: nome, nrColunas: cols, nrLinhas: rows }) })
      .done(function () {
        $('#modal-grupo').addClass('hidden');
        showToastOk('Grupo salvo com sucesso.');
        loadGrupos();
      })
      .fail(function (xhr) {
        var msg = ajaxErrorMessage(xhr, 'Erro ao salvar.');
        $('#modal-grupo-erro').removeClass('hidden').text(msg);
      });
  });

  // ── CONTEÚDOS ──────────────────────────────────────────────────────────────

  function loadConteudos() {
    $.ajax({ url: BASE + '/conteudos' })
      .done(function (data) {
        _conteudos = data || [];
        var html = '';
        if (!_conteudos.length) {
          html = '<tr><td colspan="4" class="px-3 py-4 text-center text-slate-500">Nenhum conteúdo cadastrado.</td></tr>';
        } else {
          _conteudos.forEach(function (c) {
            var icon = c.tpConteudo === 'VIDEO'
              ? '<i class="fa-solid fa-film text-emerald-400 mr-1"></i>'
              : '<i class="fa-solid fa-image text-sky-400 mr-1"></i>';
            var urlShort = (c.dsUrlArquivo || '').substring(0, 50);
            if ((c.dsUrlArquivo || '').length > 50) urlShort += '…';
            html += '<tr>' +
              '<td class="px-3 py-2">' + (c.nmConteudo || '') + '</td>' +
              '<td class="px-3 py-2">' + icon + c.tpConteudo + '</td>' +
              '<td class="px-3 py-2 hidden md:table-cell text-slate-500">' + urlShort + '</td>' +
              '<td class="px-3 py-2 text-right space-x-2">' +
                '<button class="text-xs text-slate-300 hover:text-emerald-400" data-edit-conteudo="' + c.id + '"><i class="fa-solid fa-pen-to-square"></i></button>' +
                '<button class="text-xs text-slate-300 hover:text-red-400 ml-2" data-del-conteudo="' + c.id + '"><i class="fa-solid fa-trash"></i></button>' +
              '</td></tr>';
          });
        }
        $('#tv-conteudos-tbody').html(html);
      })
      .fail(function () { showToastErr('Erro ao carregar conteúdos.'); });
  }

  $('#tv-controle-root').on('click', '#btn-novo-conteudo', function () {
    $('#conteudo-id').val('');
    $('#conteudo-nome').val('');
    $('#conteudo-tipo').val('IMAGEM');
    $('#conteudo-url').val('');
    $('#modal-conteudo-title').text('Novo Conteúdo');
    $('#modal-conteudo-erro').addClass('hidden').text('');
    $('#modal-conteudo').removeClass('hidden');
  });

  $('#tv-controle-root').on('click', '[data-edit-conteudo]', function () {
    var id = $(this).data('edit-conteudo');
    var c  = _conteudos.find(function (x) { return x.id === id; });
    if (!c) return;
    $('#conteudo-id').val(c.id);
    $('#conteudo-nome').val(c.nmConteudo);
    $('#conteudo-tipo').val(c.tpConteudo);
    $('#conteudo-url').val(c.dsUrlArquivo);
    $('#modal-conteudo-title').text('Editar Conteúdo');
    $('#modal-conteudo-erro').addClass('hidden').text('');
    $('#modal-conteudo').removeClass('hidden');
  });

  $('#tv-controle-root').on('click', '[data-del-conteudo]', function () {
    var id = $(this).data('del-conteudo');
    if (!confirm('Excluir este conteúdo?')) return;
    $.ajax({ url: BASE + '/conteudos/' + id, method: 'DELETE' })
      .done(function () { showToastOk('Conteúdo excluído.'); loadConteudos(); })
      .fail(function () { showToastErr('Erro ao excluir conteúdo.'); });
  });

  $('#tv-controle-root').on('click', '#btn-cancelar-conteudo', function () {
    $('#modal-conteudo').addClass('hidden');
  });

  $('#tv-controle-root').on('click', '#btn-salvar-conteudo', function () {
    var id   = $('#conteudo-id').val();
    var nome = $('#conteudo-nome').val().trim();
    var tipo = $('#conteudo-tipo').val();
    var url  = $('#conteudo-url').val().trim();
    if (!nome) { $('#modal-conteudo-erro').removeClass('hidden').text('Informe o nome.'); return; }
    if (!url)  { $('#modal-conteudo-erro').removeClass('hidden').text('Informe a URL.'); return; }

    var apiUrl = id ? BASE + '/conteudos/' + id : BASE + '/conteudos';
    var method = id ? 'PUT' : 'POST';
    $.ajax({ url: apiUrl, method: method, contentType: 'application/json',
      data: JSON.stringify({ nmConteudo: nome, tpConteudo: tipo, dsUrlArquivo: url }) })
      .done(function () {
        $('#modal-conteudo').addClass('hidden');
        showToastOk('Conteúdo salvo com sucesso.');
        loadConteudos();
      })
      .fail(function (xhr) {
        var msg = ajaxErrorMessage(xhr, 'Erro ao salvar.');
        $('#modal-conteudo-erro').removeClass('hidden').text(msg);
      });
  });

  // ── PLAYLISTS ──────────────────────────────────────────────────────────────

  function loadPlaylists() {
    $.ajax({ url: BASE + '/playlists' })
      .done(function (data) {
        _playlists = data || [];
        var html = '';
        if (!_playlists.length) {
          html = '<tr><td colspan="4" class="px-3 py-4 text-center text-slate-500">Nenhuma playlist cadastrada.</td></tr>';
        } else {
          _playlists.forEach(function (p) {
            var qtItens = (p.itens && p.itens.length) ? p.itens.length : '—';
            html += '<tr>' +
              '<td class="px-3 py-2">' + (p.nmPlaylist || '') + '</td>' +
              '<td class="px-3 py-2">' + qtItens + '</td>' +
              '<td class="px-3 py-2">' + badgeStatus(p.stStatus) + '</td>' +
              '<td class="px-3 py-2 text-right space-x-2">' +
                '<button class="text-xs text-slate-300 hover:text-emerald-400" data-edit-playlist="' + p.id + '"><i class="fa-solid fa-pen-to-square"></i></button>' +
                '<button class="text-xs text-slate-300 hover:text-red-400 ml-2" data-del-playlist="' + p.id + '"><i class="fa-solid fa-trash"></i></button>' +
              '</td></tr>';
          });
        }
        $('#tv-playlists-tbody').html(html);
      })
      .fail(function () { showToastErr('Erro ao carregar playlists.'); });
  }

  function buildPlaylistItemRow(item) {
    var opts = _conteudos.map(function (c) {
      var sel = (item && item.idConteudo === c.id) ? 'selected' : '';
      return '<option value="' + c.id + '" ' + sel + '>' + c.nmConteudo + ' (' + c.tpConteudo + ')</option>';
    }).join('');
    return '<div class="flex items-center gap-2 playlist-item-row">' +
      '<select class="flex-1 rounded-lg border border-slate-700 bg-slate-950 text-slate-100 px-2 py-1.5 text-xs pi-conteudo"><option value="">-- Conteúdo --</option>' + opts + '</select>' +
      '<input type="number" min="1" value="' + (item ? item.qtSegundos : 10) + '" class="w-16 rounded-lg border border-slate-700 bg-slate-950 text-slate-100 px-2 py-1.5 text-xs pi-segundos" placeholder="seg" />' +
      '<button type="button" class="text-red-400 hover:text-red-300 pi-remover"><i class="fa-solid fa-xmark"></i></button>' +
    '</div>';
  }

  $('#tv-controle-root').on('click', '#btn-nova-playlist', function () {
    $('#playlist-id').val('');
    $('#playlist-nome').val('');
    $('#playlist-itens-container').empty();
    $('#modal-playlist-title').text('Nova Playlist');
    $('#modal-playlist-erro').addClass('hidden').text('');
    $('#modal-playlist').removeClass('hidden');
  });

  $('#tv-controle-root').on('click', '[data-edit-playlist]', function () {
    var id = $(this).data('edit-playlist');
    $.ajax({ url: BASE + '/playlists/' + id })
      .done(function (p) {
        $('#playlist-id').val(p.id);
        $('#playlist-nome').val(p.nmPlaylist);
        $('#playlist-itens-container').empty();
        (p.itens || []).forEach(function (item) {
          $('#playlist-itens-container').append(buildPlaylistItemRow(item));
        });
        $('#modal-playlist-title').text('Editar Playlist');
        $('#modal-playlist-erro').addClass('hidden').text('');
        $('#modal-playlist').removeClass('hidden');
      })
      .fail(function () { showToastErr('Erro ao carregar playlist.'); });
  });

  $('#tv-controle-root').on('click', '[data-del-playlist]', function () {
    var id = $(this).data('del-playlist');
    if (!confirm('Excluir esta playlist?')) return;
    $.ajax({ url: BASE + '/playlists/' + id, method: 'DELETE' })
      .done(function () { showToastOk('Playlist excluída.'); loadPlaylists(); })
      .fail(function () { showToastErr('Erro ao excluir playlist.'); });
  });

  $('#tv-controle-root').on('click', '#btn-add-item-playlist', function () {
    $('#playlist-itens-container').append(buildPlaylistItemRow(null));
  });

  $('#tv-controle-root').on('click', '.pi-remover', function () {
    $(this).closest('.playlist-item-row').remove();
  });

  $('#tv-controle-root').on('click', '#btn-cancelar-playlist', function () {
    $('#modal-playlist').addClass('hidden');
  });

  $('#tv-controle-root').on('click', '#btn-salvar-playlist', function () {
    var id   = $('#playlist-id').val();
    var nome = $('#playlist-nome').val().trim();
    if (!nome) { $('#modal-playlist-erro').removeClass('hidden').text('Informe o nome da playlist.'); return; }

    var itens = [];
    var ordem = 0;
    var valido = true;
    $('.playlist-item-row').each(function () {
      var idConteudo = parseInt($(this).find('.pi-conteudo').val());
      var secs       = parseInt($(this).find('.pi-segundos').val()) || 10;
      if (!idConteudo) { valido = false; return false; }
      itens.push({ idConteudo: idConteudo, qtSegundos: secs, nrOrdem: ordem++ });
    });

    if (!valido) { $('#modal-playlist-erro').removeClass('hidden').text('Selecione o conteúdo em todos os itens.'); return; }

    var apiUrl = id ? BASE + '/playlists/' + id : BASE + '/playlists';
    var method = id ? 'PUT' : 'POST';
    $.ajax({ url: apiUrl, method: method, contentType: 'application/json',
      data: JSON.stringify({ nmPlaylist: nome, stStatus: 'A', itens: itens }) })
      .done(function () {
        $('#modal-playlist').addClass('hidden');
        showToastOk('Playlist salva com sucesso.');
        loadPlaylists();
      })
      .fail(function (xhr) {
        var msg = ajaxErrorMessage(xhr, 'Erro ao salvar.');
        $('#modal-playlist-erro').removeClass('hidden').text(msg);
      });
  });

  // ── TVs ────────────────────────────────────────────────────────────────────

  function loadTvs() {
    $.ajax({ url: BASE + '/tvs' })
      .done(function (data) {
        _tvs = data || [];
        var html = '';
        if (!_tvs.length) {
          html = '<tr><td colspan="6" class="px-3 py-4 text-center text-slate-500">Nenhuma TV cadastrada.</td></tr>';
        } else {
          _tvs.forEach(function (t) {
            var idShort = t.id ? t.id.substring(0, 8) + '…' : '—';
            html += '<tr>' +
              '<td class="px-3 py-2">' + (t.nmTv || '') + '</td>' +
              '<td class="px-3 py-2 hidden md:table-cell">' + (t.nmAmbiente || '—') + '</td>' +
              '<td class="px-3 py-2 hidden md:table-cell">' + (t.nmPlaylist || '—') + '</td>' +
              '<td class="px-3 py-2 hidden md:table-cell">' +
                '<span class="font-mono text-slate-500 text-[0.6rem] cursor-pointer" title="' + t.id + '" onclick="navigator.clipboard && navigator.clipboard.writeText(\'' + t.id + '\')">' + idShort + '</span>' +
              '</td>' +
              '<td class="px-3 py-2">' + badgeStatus(t.stStatus) + '</td>' +
              '<td class="px-3 py-2 text-right space-x-1">' +
                '<a href="tv.html?id=' + t.id + '" target="_blank" class="text-xs text-sky-400 hover:text-sky-300 mr-1" title="Abrir player"><i class="fa-solid fa-external-link"></i></a>' +
                '<button class="text-xs text-slate-300 hover:text-emerald-400" data-edit-tv="' + t.id + '"><i class="fa-solid fa-pen-to-square"></i></button>' +
                '<button class="text-xs text-slate-300 hover:text-red-400 ml-2" data-del-tv="' + t.id + '"><i class="fa-solid fa-trash"></i></button>' +
              '</td></tr>';
          });
        }
        $('#tv-tvs-tbody').html(html);
      })
      .fail(function () { showToastErr('Erro ao carregar TVs.'); });
  }

  function preencherSelectsTv() {
    var ambOpts = '<option value="">-- Selecione --</option>' +
      _ambientes.filter(function (a) { return a.stStatus === 'A'; })
        .map(function (a) { return '<option value="' + a.id + '">' + a.nmAmbiente + '</option>'; }).join('');
    $('#tv-ambiente-select').html(ambOpts);

    var grpOpts = '<option value="">-- Nenhum (TV individual) --</option>' +
      _grupos.filter(function (g) { return g.stStatus === 'A'; })
        .map(function (g) { return '<option value="' + g.id + '">' + g.nmGrupo + ' (' + g.nrLinhas + 'x' + g.nrColunas + ')</option>'; }).join('');
    $('#tv-grupo-select').html(grpOpts);

    var plOpts = '<option value="">-- Nenhuma --</option>' +
      _playlists.filter(function (p) { return p.stStatus === 'A'; })
        .map(function (p) { return '<option value="' + p.id + '">' + p.nmPlaylist + '</option>'; }).join('');
    $('#tv-playlist-select').html(plOpts);
  }

  function openModalTv(tv) {
    // Garante que listas estão carregadas
    var deps = [
      $.ajax({ url: BASE + '/ambientes' }).done(function (d) { _ambientes = d || []; }),
      $.ajax({ url: BASE + '/grupos' }).done(function (d) { _grupos = d || []; }),
      $.ajax({ url: BASE + '/playlists' }).done(function (d) { _playlists = d || []; })
    ];
    $.when.apply($, deps).then(function () {
      preencherSelectsTv();

      if (tv) {
        $('#tv-id-field').val(tv.id);
        $('#tv-nome').val(tv.nmTv);
        $('#tv-ambiente-select').val(tv.idAmbiente || '');
        $('#tv-grupo-select').val(tv.idGrupo || '');
        $('#tv-posicao').val(tv.nrPosicaoGrupo || 0);
        $('#tv-playlist-select').val(tv.idPlaylist || '');
        $('#modal-tv-title').text('Editar TV');
      } else {
        $('#tv-id-field').val('');
        $('#tv-nome').val('');
        $('#tv-ambiente-select').val('');
        $('#tv-grupo-select').val('');
        $('#tv-posicao').val(0);
        $('#tv-playlist-select').val('');
        $('#modal-tv-title').text('Nova TV');
      }
      $('#modal-tv-erro').addClass('hidden').text('');
      $('#modal-tv').removeClass('hidden');
    });
  }

  $('#tv-controle-root').on('click', '#btn-nova-tv', function () { openModalTv(null); });

  $('#tv-controle-root').on('click', '[data-edit-tv]', function () {
    var id = $(this).data('edit-tv');
    var tv = _tvs.find(function (t) { return t.id === id; });
    if (tv) openModalTv(tv);
  });

  $('#tv-controle-root').on('click', '[data-del-tv]', function () {
    var id = $(this).data('del-tv');
    if (!confirm('Inativar esta TV?')) return;
    $.ajax({ url: BASE + '/tvs/' + id, method: 'DELETE' })
      .done(function () { showToastOk('TV inativada.'); loadTvs(); })
      .fail(function () { showToastErr('Erro ao inativar TV.'); });
  });

  $('#tv-controle-root').on('click', '#btn-cancelar-tv', function () {
    $('#modal-tv').addClass('hidden');
  });

  $('#tv-controle-root').on('click', '#btn-salvar-tv', function () {
    var id       = $('#tv-id-field').val();
    var nome     = $('#tv-nome').val().trim();
    var ambId    = $('#tv-ambiente-select').val() || null;
    var grpId    = $('#tv-grupo-select').val() || null;
    var pos      = parseInt($('#tv-posicao').val()) || 0;
    var playId   = $('#tv-playlist-select').val() || null;

    if (!nome) { $('#modal-tv-erro').removeClass('hidden').text('Informe o nome da TV.'); return; }

    var payload = {
      nmTv: nome,
      idAmbiente: ambId ? parseInt(ambId) : null,
      idGrupo: grpId ? parseInt(grpId) : null,
      nrPosicaoGrupo: pos,
      idPlaylist: playId ? parseInt(playId) : null
    };

    var apiUrl = id ? BASE + '/tvs/' + id : BASE + '/tvs';
    var method = id ? 'PUT' : 'POST';
    $.ajax({ url: apiUrl, method: method, contentType: 'application/json',
      data: JSON.stringify(payload) })
      .done(function () {
        $('#modal-tv').addClass('hidden');
        showToastOk('TV salva com sucesso.');
        loadTvs();
      })
      .fail(function (xhr) {
        var msg = ajaxErrorMessage(xhr, 'Erro ao salvar.');
        $('#modal-tv-erro').removeClass('hidden').text(msg);
      });
  });

  // ── Inicialização ──────────────────────────────────────────────────────────
  loadAmbientes();
  // Pré-carrega conteúdos para o builder de playlists
  $.ajax({ url: BASE + '/conteudos' }).done(function (d) { _conteudos = d || []; });
}
