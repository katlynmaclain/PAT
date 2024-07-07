var express = require('express');
var app = express();
var fs = require('fs');
const { stringify } = require('querystring');

app.use(express.static('public'));



app.get('/list_user', function(req, res){
    fs.readFile("users.json", "utf-8", function(err,data){
        console.log(data);
        res.end(data);
    });
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

// app.post('/', function(req, res){
//     console.log("got a POST request, route :/");
//     res.send('welcome to express framework --> POST(/)');
// });

var user = {
    "user5" : {
        "nama" : "Edward",
        "password" : "password5",
        "role" : "student",
        "id" : 5
    }
}

app.post('/add_data', function(req, res){
    fs.readFile("users.json", "utf-8", function(err,data){
        data = JSON.parse(data);
        data["user5"] = user["user5"];
        console.log(data);
        res.end(JSON.stringify(data));
    });
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