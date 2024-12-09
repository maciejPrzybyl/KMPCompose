import { sqlite3Worker1Promiser } from '@sqlite.org/sqlite-wasm';

const log = console.log;
const error = console.error;

const initializeSQLite = async () => {
  try {
    log('Loading and initializing SQLite3 module...');

    const promiser = await new Promise((resolve) => {
      const _promiser = sqlite3Worker1Promiser({
        onready: () => resolve(_promiser),
      });
    });

    log('Done initializing. Running demo...');

    const configResponse = await promiser('config-get', {});
    log('Running SQLite3 version', configResponse.result.version.libVersion);

    const openResponse = await promiser('open', {
      filename: 'file:mydb.sqlite3?vfs=opfs',
    });
    const { dbId } = openResponse;
    log(
      'OPFS is available, created persisted database at',
      openResponse.result.filename.replace(/^file:(.*?)\?vfs=opfs$/, '$1'),
    );

    await promiser('exec', { dbId, sql: 'CREATE TABLE IF NOT EXISTS DbNote(id INTEGER PRIMARY KEY AUTOINCREMENT, content TEXT NOT NULL)' });
        log('Creating a table...');

        log('Insert some data using exec()...');

          await promiser('exec', {
              dbId, sql: 'INSERT OR ABORT INTO `DbNote` (`id`,`content`) VALUES (nullif(?, 0),?)',
              bind: [99, 'tttttt'],
              });


        log('Query data with exec()...');
        await promiser('exec', {
            dbId, sql: 'SELECT * FROM DbNote ORDER BY id',
            callback: (row) => {
                log(row);
                },
            });
        await promiser('close', { dbId });
  } catch (err) {
    if (!(err instanceof Error)) {
      err = new Error(err.result.message);
    }
    error(err.name, err.message);
  }
};

initializeSQLite();

