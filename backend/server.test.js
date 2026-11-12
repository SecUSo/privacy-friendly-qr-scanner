const { test } = require('node:test');
const assert = require('node:assert/strict');
const { createServer, lookup } = require('./server');

const found = product => ({ ok: true, status: 200, json: async () => ({ status: 1, product }) });
const missing = { ok: true, status: 200, json: async () => ({ status: 0 }) };

test('food products are normalized', async () => {
  const result = await lookup('12345678', async () => found({
    product_name: ' Soap ', categories: ' Home ', ingredients_text: ' Water ',
  }));
  assert.deepEqual(result, { status: 200, product: { name: 'Soap', category: 'Home', ingredients: 'Water' } });
});

test('tries beauty after food and tolerates missing optional fields', async () => {
  let calls = 0;
  const result = await lookup('12345678', async () => ++calls === 1 ? missing : found({ product_name: 'Cream' }));
  assert.equal(calls, 2);
  assert.deepEqual(result.product, { name: 'Cream', category: '', ingredients: '' });
});

test('distinguishes missing products from provider failures', async () => {
  assert.equal((await lookup('12345678', async () => missing)).status, 404);
  let calls = 0;
  assert.equal((await lookup('12345678', async () => ++calls === 1 ? { ok: false, status: 500 } : missing)).status, 503);
});

test('HTTP endpoint validates inputs and returns product data', async t => {
  const server = createServer(async code => ({ status: 200, product: { name: code, category: '', ingredients: '' } }));
  await new Promise(resolve => server.listen(0, '127.0.0.1', resolve));
  t.after(() => server.close());
  const base = `http://127.0.0.1:${server.address().port}`;
  assert.equal((await fetch(`${base}/health`)).status, 200);
  assert.equal((await fetch(`${base}/scan?code=not-a-barcode`)).status, 400);
  assert.equal((await fetch(`${base}/scan?code=12345678&code=12345678`)).status, 400);
  assert.equal((await fetch(`${base}/scan?code=22345678&format=UPC_E`)).status, 400);
  const response = await fetch(`${base}/scan?code=12345678`);
  assert.equal(response.status, 200);
  assert.equal((await response.json()).name, '12345678');
  const upce = await fetch(`${base}/scan?code=01234565&format=UPC_E`);
  assert.equal((await upce.json()).name, '012345000065');
});
