var express = require('express');
var app = express();
var fs = require('fs');

app.use(express.static('public'));



app.get('/list_user', function(req, res){
    console.log("got a GET request, route: /list_user");
    res.send('welcome to express framework --> GET (list_user)');
});

app.get('/abc*def', function(req, res){
    console.log("got a GET request, route: /abc*def");
    res.send('welcome to express framework --> GET (abc*def)');
});

// app.get('/bc?d', function(req, res){
//     console.log("got a GET request, route: /bc?d");
//     res.send('welcome to express framework --> GET (bc?d)');
// });

app.get('/', function(req, res){
    console.log("got a GET request, route: /");
    res.send('welcome to express framework --> GET (/)');
});

app.post('/', function(req, res){
    console.log("got a POST request, route :/");
    res.send('welcome to express framework --> POST(/)');
});

app.delete('/', function(req, res){
    console.log("got a DELETE request, route :/");
    res.send('welcome to express framework --> DELETE(/)');
});

app.put('/', function(req, res){
    console.log("got a PUT request, route :/");
    res.send('welcome to express framework --> PUT(/)');
});

var server = app.listen(8000, function(){
    console.log("API Server running at port 8000");
});