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
