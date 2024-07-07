const express = require('express');
const bodyParser = require('body-parser');
const mysql = require('mysql');

const app = express();
app.use(bodyParser.json());

const conn = mysql.createConnection({
    host: '172.22.42.69',
    user: 'katlyn',
    password : 'katlynmaclain',
    database : 'tugas3pat',
});

conn.connect(function(err){
    if (err) throw err;
    console.log("connected to MySQL ....");
});

// conn.connect((err)=>{
//     if (err) throw err;
//     console.log("connected to MySQL ....");
// });

//semua user
app.get('/api/users', function(req, res){
    let sql = "SELECT * FROM users";
    let query = conn.query(sql, function(err, result){
        if (err) throw err;
        res.send(JSON.stringify({
            "status" : 200,
            "error" : null,
            "response" : result
        }));
    });
});

//user dgn id tertentu
app.get('/api/users/:id', function(req, res){
    let sql = "SELECT * FROM users WHERE id="+req.params.id;
    let query = conn.query(sql, function(err, result){
        if (err) throw err;
        res.send(JSON.stringify({
            "status" : 200,
            "error" : null,
            "response" : result
        }));
    });
}); 

//nambah
app.post('/api/users/', function(req, res){
    let data = {username: req.body.nama, pass: req.body.pass};
    let sql = "INSERT INTO users SET ?";
    let query = conn.query(sql,data,function(err,result){
        if (err) throw err;
        res.send(JSON.stringify({
            "status" : 200, //standar http berhasil itu 200, tp kalau mau diganti jg boleh pke info lain cth string 100 sukses
            "error" : null,
            "response" : result
        }));
    });
}); 

//update hrs ada id mana yg di update
app.put('/api/users/:id', function(req, res){
    let sql = "UPDATE users SET pass='"+req.body.pass+
        "'WHERE id="+req.params.id;
    let query = conn.query(sql,function(err,result){
        if (err) throw err;
        res.send(JSON.stringify({
            "status" : 200,
            "error" : null,
            "response" : result
        }));
    });
}); 

//harus ada id agar yg dihapus id tertentu bkn semua
app.delete('/api/users/:id', function(req, res){}); 

var server = app.listen(8000, function(){
    console.log("API Server running at port 8000");
});