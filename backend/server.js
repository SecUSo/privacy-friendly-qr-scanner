// Product lookup service. Barcode values are intentionally not logged.
const http = require('node:http');

const providers = [
  'https://world.openfoodfacts.org',
  'https://world.openbeautyfacts.org',
];

function expandUpce(code) {
  const d = code.slice(1, 7);
  let body;
  switch (d[5]) {
    case '0': case '1': case '2':
      body = d.slice(0, 2) + d[5] + '0000' + d.slice(2, 5);
      break;
    case '3':
      body = d.slice(0, 3) + '00000' + d.slice(3, 5);
      break;
    case '4':
      body = d.slice(0, 4) + '00000' + d[4];
      break;
    default:
      body = d.slice(0, 5) + '0000' + d[5];
  }
  return code[0] + body + code[7];
}

async function lookup(code, fetchImpl = fetch) {
  let failed = false;
  for (const provider of providers) {
    try {
      const url = `${provider}/api/v2/product/${code}.json?fields=product_name,categories,ingredients_text`;
      const response = await fetchImpl(url, {
        headers: { 'User-Agent': 'PersonalInventoryScanner/1.0 (product lookup)' },
        signal: AbortSignal.timeout(6000),
      });
      if (response.status === 404) continue;
      if (!response.ok) {
        failed = true;
        continue;
      }
      const data = await response.json();
      if (data.status === 0) continue;
      if (data.status !== 1 || !data.product || typeof data.product !== 'object') {
        failed = true;
        continue;
      }
      const product = data.product;
      if (typeof product.product_name !== 'string' || !product.product_name.trim()) continue;
      return {
        status: 200,
        product: {
          name: product.product_name.trim(),
          category: typeof product.categories === 'string' ? product.categories.trim() : '',
          ingredients: typeof product.ingredients_text === 'string' ? product.ingredients_text.trim() : '',
        },
      };
    } catch (error) {
      console.error('Product provider unavailable:', error.name);
      failed = true;
    }
  }
  return { status: failed ? 503 : 404 };
}

function createServer(lookupProduct = lookup) {
  return http.createServer(async (req, res) => {
    const url = new URL(req.url, 'http://localhost');
    res.setHeader('Cache-Control', 'no-store');
    res.setHeader('Content-Type', 'application/json; charset=utf-8');
    if (req.method === 'GET' && url.pathname === '/health') {
      res.writeHead(200).end('{"status":"ok"}');
      return;
    }
    if (req.method !== 'GET' || url.pathname !== '/scan') {
      res.writeHead(404).end('{"error":"not found"}');
      return;
    }
    const codes = url.searchParams.getAll('code');
    if (codes.length !== 1 || !/^(?:\d{8}|\d{12}|\d{13})$/.test(codes[0])) {
      res.writeHead(400).end('{"error":"invalid barcode"}');
      return;
    }
    const formats = url.searchParams.getAll('format');
    if (formats.length > 1 || (formats.length && (formats[0] !== 'UPC_E' || !/^[01]\d{7}$/.test(codes[0])))) {
      res.writeHead(400).end('{"error":"invalid format"}');
      return;
    }
    try {
      const result = await lookupProduct(formats.length ? expandUpce(codes[0]) : codes[0]);
      res.writeHead(result.status).end(JSON.stringify(result.product || { error: 'product unavailable' }));
    } catch (error) {
      console.error('Product lookup failed:', error.name);
      res.writeHead(503).end('{"error":"product unavailable"}');
    }
  });
}

if (require.main === module) {
  createServer().listen(process.env.PORT || 3000, '0.0.0.0');
}

module.exports = { createServer, lookup };
