import sqlite3InitModule from '@sqlite.org/sqlite-wasm';

const log = console.log;
const error = console.error;

const initializeSQLite = async () => {
  try {
    log('[SQLite3] Initializing SQLite3 module...');
    const sqlite3 = await sqlite3InitModule({
      print: log,
      printErr: error,
    });
    log('[SQLite3] Done initializing SQLite3 module.');
    start(sqlite3);
  } catch (err) {
    error('[SQLite3] SQLite3 module initialization error:', err.name, err.message);
  }
};

const start = (sqlite3) => {
  log('[SQLite3] Running SQLite3 version', sqlite3.version.libVersion);
  const db = new sqlite3.oo1.DB('/mydb.sqlite3', 'ct');
  try {
      log('[SQLite3] Creating SQLite3 DB table...');
      db.exec('CREATE TABLE IF NOT EXISTS DbNote(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, content TEXT NOT NULL)');
    } finally {
      db.close();
    }
};

function insertNote(noteContent) {
    console.log('[SQLite3] InsertNote called');
}

/* const insertNote = async (sqlite3) => {
    try {
        console.log('Insert some data using exec()...');

        db.exec({
          sql: 'INSERT OR ABORT INTO `DbNote` (`id`,`content`) VALUES (nullif(?, 0),?)',
          bind: [i, noteContent],
        });
        log('Query data with exec()...');
        db.exec({
        sql: 'SELECT content FROM DbNote ORDER BY id',
        callback: (row) => {
          log(row);
        },
        });
    } catch (err) {
      error('Initialization error:', err.name, err.message);
    }
}; */

initializeSQLite();
