var http = require("http");
function start(){
	console.info("server starting...");
	function onRequest(request,response){
		console.info("receive the request"+request);
		console.info("url:"+request.url);
		response.writeHead(200,{"Content-Type":"text/plain"});
		response.write("Hello World");
		response.end();
	}
	http.createServer(onRequest).listen(1986);
	console.info("server has starting successfully.");
}
exports.start = start;
