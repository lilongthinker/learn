console.log('HelloWorld running...\n');
var connect = require('connect');

var app = connect()
.use(function (req, res, next) {
  if (req.url === '/sync_error') {
    throw new Error('sync error');
  }
  if (req.url === '/async_error') {
    return process.nextTick(function () {
      mk2.haha();
    });
  }
  res.end('hello, ' + req.method + ' ' + req.url+'\n');
});

process.on('uncaughtException', function (err) {
  console.error(err);
});

app.listen(1986);
