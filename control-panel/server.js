'use strict';

const http = require('http');
const fs = require('fs');
const path = require('path');
const { execFile } = require('child_process');

const PORT = 4545;
const PACKAGE = 'com.calmwrist.wear';
const PUBLIC_DIR = path.join(__dirname, 'public');

function findAdb() {
  const candidates = [
    process.env.ADB_PATH,
    path.join(process.env.LOCALAPPDATA || '', 'Android', 'Sdk', 'platform-tools', 'adb.exe'),
    'adb',
  ].filter(Boolean);
  for (const candidate of candidates) {
    if (candidate === 'adb' || fs.existsSync(candidate)) return candidate;
  }
  return 'adb';
}

const ADB = findAdb();

function runAdb(args) {
  return new Promise((resolve) => {
    execFile(ADB, args, { timeout: 10000 }, (error, stdout, stderr) => {
      resolve({ ok: !error, stdout: stdout || '', stderr: stderr || (error ? error.message : '') });
    });
  });
}

async function broadcast(action, extras = {}) {
  const args = ['shell', 'am', 'broadcast', '-a', `${PACKAGE}.debug.${action}`, '-p', PACKAGE];
  for (const [key, value] of Object.entries(extras)) {
    args.push('--es', key, String(value));
  }
  return runAdb(args);
}

function sleep(ms) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

async function fetchState() {
  await runAdb(['logcat', '-c']);
  const bcast = await broadcast('DUMP_STATE');
  if (!bcast.ok) return { ok: false, error: bcast.stderr || 'No se pudo contactar el reloj (adb broadcast falló).' };
  await sleep(600);
  const logs = await runAdb(['logcat', '-d', '-s', 'CalmWristDebug:I']);
  const lines = logs.stdout.split(/\r?\n/).filter((l) => l.includes('STATE:'));
  if (lines.length === 0) {
    return { ok: false, error: 'El reloj no respondió (¿la app está abierta? ¿sigue conectado el adb?).' };
  }
  const last = lines[lines.length - 1];
  const jsonText = last.slice(last.indexOf('STATE:') + 'STATE:'.length);
  try {
    return { ok: true, state: JSON.parse(jsonText) };
  } catch (e) {
    return { ok: false, error: 'Respuesta del reloj no se pudo interpretar: ' + e.message };
  }
}

async function deviceStatus() {
  const result = await runAdb(['devices']);
  if (!result.ok) return { connected: false, serial: null };
  const line = result.stdout
    .split(/\r?\n/)
    .slice(1)
    .map((l) => l.trim())
    .find((l) => l.endsWith('\tdevice') || l.endsWith(' device'));
  if (!line) return { connected: false, serial: null };
  const serial = line.split(/\s+/)[0];
  return { connected: true, serial };
}

const MIME = { '.html': 'text/html', '.js': 'text/javascript', '.css': 'text/css' };

function serveStatic(req, res) {
  let filePath = req.url === '/' ? '/index.html' : req.url;
  filePath = path.join(PUBLIC_DIR, filePath);
  if (!filePath.startsWith(PUBLIC_DIR)) {
    res.writeHead(403);
    res.end();
    return;
  }
  fs.readFile(filePath, (err, data) => {
    if (err) {
      res.writeHead(404);
      res.end('No encontrado');
      return;
    }
    const ext = path.extname(filePath);
    res.writeHead(200, { 'Content-Type': MIME[ext] || 'application/octet-stream' });
    res.end(data);
  });
}

function readBody(req) {
  return new Promise((resolve) => {
    let data = '';
    req.on('data', (chunk) => (data += chunk));
    req.on('end', () => {
      try {
        resolve(data ? JSON.parse(data) : {});
      } catch (e) {
        resolve({});
      }
    });
  });
}

function sendJson(res, obj, status = 200) {
  res.writeHead(status, { 'Content-Type': 'application/json' });
  res.end(JSON.stringify(obj));
}

const server = http.createServer(async (req, res) => {
  const url = req.url.split('?')[0];

  if (!url.startsWith('/api/')) return serveStatic(req, res);

  try {
    if (url === '/api/status' && req.method === 'GET') {
