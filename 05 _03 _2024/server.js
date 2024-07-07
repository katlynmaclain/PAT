var express = require('express');
var app = express();
var fs = require('fs');

app.get('/', function(req, res){
    fs.readFile("test.htm", function(err,data){
        res.end(data);
    });
});

app.get('/book', function(req, res){});

app.get('/book/98782392', function(req, res){});

var server = app.listen(8000, function(){
    console.log("API Server running at port 8000");
});