var fs = require('fs');
fs.renameSync('helloSync','wordSync');
var stats = fs.statSync('wordSync');
console.log('stats sync:' + JSON.stringify(stats));
