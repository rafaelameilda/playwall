
const origin = window.location.origin; // ex.: http://localhost:5500

let API_BASE_URL;

if (window.location.protocol === 'file:') {
  API_BASE_URL = 'http://localhost:8090/tv-wall';
} else if (origin.includes('localhost') || origin.includes('127.0.0.1')) {
  // Quando servido pelo Spring usa a origem atual; em servidor frontend separado usa o backend local.
  API_BASE_URL = window.location.port === '8090' ? `${origin}/tv-wall` : 'http://localhost:8090/tv-wall';
} else {
  // Fallback genérico: usa mesma origem da página
  API_BASE_URL = `${origin}/tv-wall`;
}


// Máscara de telefone reutilizável
function formatTelefoneBr(value) {
  if (!value) return '';

  // Mantém apenas dígitos
  const digits = value.toString().replace(/\D/g, '');
  const len = digits.length;

  if (len === 10) {
    // (44) 3537-1008
    const ddd = digits.slice(0, 2);
    const parte1 = digits.slice(2, 6);
    const parte2 = digits.slice(6);
    return `(${ddd}) ${parte1}-${parte2}`;
  }

  if (len === 11 && digits.startsWith('0800')) {
    // 0800 486 2521
    const parte1 = digits.slice(0, 4);
    const parte2 = digits.slice(4, 7);
    const parte3 = digits.slice(7);
    return `${parte1} ${parte2} ${parte3}`;
  }

  if (len === 11) {
    // (44) 9 9876-5432
    const ddd = digits.slice(0, 2);
    const digito9 = digits.slice(2, 3);
    const parte1 = digits.slice(3, 7);
    const parte2 = digits.slice(7);
    return `(${ddd}) ${digito9} ${parte1}-${parte2}`;
  }

  // Fallback: retorna só os dígitos se o tamanho não bater com os formatos esperados
  return digits;
}

// Máscara de CPF/CNPJ reutilizável
function formatCpfCnpjBr(value) {
  if (!value) return '';

  // Mantém apenas dígitos e letras (para CNPJ alfanumérico)
  const clean = value.toString().replace(/[^0-9A-Za-z]/g, '');
  const len = clean.length;

  if (len === 11) {
    // CPF: 066.703.419-62
    const p1 = clean.slice(0, 3);
    const p2 = clean.slice(3, 6);
    const p3 = clean.slice(6, 9);
    const p4 = clean.slice(9);
    return `${p1}.${p2}.${p3}-${p4}`;
  }

  if (len === 14) {
    // CNPJ (numérico ou alfanumérico): 20.331.841/0001-99 ou A1.B2C.3D4/56EF-90
    const p1 = clean.slice(0, 2);
    const p2 = clean.slice(2, 5);
    const p3 = clean.slice(5, 8);
    const p4 = clean.slice(8, 12);
    const p5 = clean.slice(12);
    return `${p1}.${p2}.${p3}/${p4}-${p5}`;
  }

  // Fallback: retorna só os caracteres limpos se não bater tamanho
  return clean;
}

// Máscara de CEP reutilizável
function formatCepBr(value) {
  if (!value) return '';

  // Mantém apenas dígitos
  const digitsOnly = value.toString().replace(/\D/g, '');

  if (!digitsOnly) return '';

  // Se vier com 7 dígitos (ex.: 2580000), completa à esquerda com zero
  let digits = digitsOnly;
  if (digits.length === 7) {
    digits = '0' + digits;
  }

  if (digits.length !== 8) {
    // Fallback: retorna só os dígitos se não tiver 8
    return digits;
  }

  // Formato: 87.270-000
  const p1 = digits.slice(0, 2);
  const p2 = digits.slice(2, 5);
  const p3 = digits.slice(5);
  return `${p1}.${p2}-${p3}`;
}

// Formatação de valores em formato brasileiro (duas casas, vírgula decimal, ponto milhar)
function formatValorBr(value) {
  if (value === null || value === undefined || value === '') return '';

  let str = value.toString().trim();
  if (!str) return '';

  let sign = '';
  if (str[0] === '-') {
    sign = '-';
    str = str.slice(1);
  }

  // Mantém apenas dígitos, vírgula e ponto
  str = str.replace(/[^0-9.,]/g, '');
  if (!str) return sign + '0,00';

  const lastComma = str.lastIndexOf(',');
  const lastDot = str.lastIndexOf('.');
  let decimalSepIndex = Math.max(lastComma, lastDot);

  let integerPart = '';
  let decimalPart = '';

  if (decimalSepIndex >= 0) {
    integerPart = str.slice(0, decimalSepIndex).replace(/[^0-9]/g, '');
    decimalPart = str.slice(decimalSepIndex + 1).replace(/[^0-9]/g, '');
  } else {
    integerPart = str.replace(/[^0-9]/g, '');
    decimalPart = '';
  }

  if (!integerPart) integerPart = '0';

  if (decimalPart.length === 0) {
    decimalPart = '00';
  } else if (decimalPart.length === 1) {
    decimalPart = decimalPart + '0';
  } else {
    decimalPart = decimalPart.slice(0, 2);
  }

  // Remove zeros à esquerda
  integerPart = integerPart.replace(/^0+(?=\d)/, '');
  if (!integerPart) integerPart = '0';

  // Aplica separador de milhar
  const integerWithThousands = integerPart.replace(/\B(?=(\d{3})+(?!\d))/g, '.');

  return sign + integerWithThousands + ',' + decimalPart;
}

// Formatação de quantidades em formato brasileiro (até três casas decimais)
// Regras desejadas (exemplos):
// 1        -> "1"
// 1.2      -> "1,200"
// 1.05     -> "1,050"
// 1000     -> "1.000"
// 1000,005 -> "1.000,005"
// 1000,5   -> "1.000,50"
// .5       -> "0,500"
// 1,5      -> "1,500"
// -1       -> "-1"
// -5,05    -> "-5,050"
function formatQuantidadeBr(value) {
  if (value === null || value === undefined || value === '') return '';

  let str = value.toString().trim();
  if (!str) return '';

  let sign = '';
  if (str[0] === '-') {
    sign = '-';
    str = str.slice(1);
  }

  // Mantém apenas dígitos, vírgula e ponto
  str = str.replace(/[^0-9.,]/g, '');
  if (!str) return sign + '0';

  const lastComma = str.lastIndexOf(',');
  const lastDot = str.lastIndexOf('.');
  let decimalSepIndex = Math.max(lastComma, lastDot);

  let integerPart = '';
  let decimalPart = '';

  if (decimalSepIndex >= 0) {
    integerPart = str.slice(0, decimalSepIndex).replace(/[^0-9]/g, '');
    decimalPart = str.slice(decimalSepIndex + 1).replace(/[^0-9]/g, '');
  } else {
    integerPart = str.replace(/[^0-9]/g, '');
    decimalPart = '';
  }

  if (!integerPart) integerPart = '0';

  // Regras para casas decimais:
  // - se não houver parte decimal => não mostra vírgula
  // - se houver, até 3 casas
  // - padding de zeros com pequeno ajuste para reproduzir exemplos
  if (decimalPart.length === 0) {
    decimalPart = '';
  } else {
    // até 3 dígitos relevantes
    if (decimalPart.length > 3) {
      decimalPart = decimalPart.slice(0, 3);
    }

    // Heurística para bater com os exemplos do usuário:
    // - se parte inteira tiver mais de 3 dígitos e decimal tiver 1 dígito, usar 2 casas
    // - caso contrário, usar 3 casas
    if (integerPart.length > 3 && decimalPart.length === 1) {
      // ex.: 1000,5 => "1.000,50"
      decimalPart = decimalPart + '0'; // 1 dígito vira 2
    } else {
      // demais casos: preencher até 3 dígitos
      if (decimalPart.length === 1) {
        decimalPart = decimalPart + '00';
      } else if (decimalPart.length === 2) {
        decimalPart = decimalPart + '0';
      }
    }

    // se depois do ajuste todas casas decimais forem zero, remover parte decimal
    if (/^0+$/.test(decimalPart)) {
      decimalPart = '';
    }
  }

  // Remove zeros à esquerda na parte inteira
  integerPart = integerPart.replace(/^0+(?=\d)/, '');
  if (!integerPart) integerPart = '0';

  // Aplica separador de milhar
  const integerWithThousands = integerPart.replace(/\B(?=(\d{3})+(?!\d))/g, '.');

  if (!decimalPart) {
    return sign + integerWithThousands;
  }

  return sign + integerWithThousands + ',' + decimalPart;
}

// Converte string em formato brasileiro para número JS (ponto decimal)
function parseValorBrToNumber(value) {
  if (value === null || value === undefined) return null;
  let str = value.toString().trim();
  if (!str) return null;

  let sign = 1;
  if (str[0] === '-') {
    sign = -1;
    str = str.slice(1);
  }

  str = str.replace(/[^0-9.,]/g, '');
  if (!str) return null;

  const lastComma = str.lastIndexOf(',');
  const lastDot = str.lastIndexOf('.');
  let decimalSepIndex = Math.max(lastComma, lastDot);

  let integerPart = '';
  let decimalPart = '';

  if (decimalSepIndex >= 0) {
    integerPart = str.slice(0, decimalSepIndex).replace(/[^0-9]/g, '');
    decimalPart = str.slice(decimalSepIndex + 1).replace(/[^0-9]/g, '');
  } else {
    integerPart = str.replace(/[^0-9]/g, '');
  }

  if (!integerPart) integerPart = '0';
  if (!decimalPart) decimalPart = '0';

  const numStr = integerPart + '.' + decimalPart;
  const num = parseFloat(numStr);
  if (Number.isNaN(num)) return null;
  return sign * num;
}

// Formata data ISO (yyyy-MM-dd ou yyyy-MM-ddTHH:mm:ss) para dd/MM/yyyy
function formatDateBr(value) {
  if (!value) return '';

  let str = value.toString().trim();
  if (!str) return '';

  // Se vier com hora (datetime), pega só a parte da data
  const parts = str.split('T');
  const datePart = parts[0];

  const comps = datePart.split('-');
  if (comps.length !== 3) return str;

  const ano = comps[0];
  const mes = comps[1];
  const dia = comps[2];

  if (!ano || !mes || !dia) return str;

  return `${dia.padStart(2, '0')}/${mes.padStart(2, '0')}/${ano}`;
}

// Formata data/hora ISO (yyyy-MM-ddTHH:mm ou yyyy-MM-ddTHH:mm:ss) para dd/MM/yyyy HH:mm
function formatDateTimeBr(value) {
  if (!value) return '';

  let str = value.toString().trim();
  if (!str) return '';

  const [datePart, timePartRaw] = str.split('T');
  if (!datePart) return '';

  const comps = datePart.split('-');
  if (comps.length !== 3) return str;

  const ano = comps[0];
  const mes = comps[1];
  const dia = comps[2];

  if (!ano || !mes || !dia) return str;

  let timePart = timePartRaw || '';
  // timePart pode vir "HH:mm" ou "HH:mm:ss"; queremos só HH:mm
  if (timePart) {
    const [hhmm] = timePart.split(':').length >= 2
      ? [timePart.split(':')[0] + ':' + timePart.split(':')[1]]
      : ['00:00'];
    timePart = hhmm;
  } else {
    timePart = '00:00';
  }

  return `${dia.padStart(2, '0')}/${mes.padStart(2, '0')}/${ano} ${timePart}`;
}
