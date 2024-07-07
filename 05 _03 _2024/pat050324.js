//console.log('hello world');

var http = require('http');
var url = require('url');
var fs = require('fs');

http.createServer(function(req, res){
    fs.readFile('test.htm',function(err,data){
        res.writeHead(200,{'Content-Type':'text/html'});
        res.write(data);
        return res.end();
    });
    //res.writeHead(200,{'Content-Type':'text/html'});
    //res.write('Hello World');
    //res.end('Hello World');
    //res.write(req.url);
    // var q = url.parse(req.url,true).query;
    // var t = q.data+"";
    // res.write(t);
    // res.end();
}).listen(8080);