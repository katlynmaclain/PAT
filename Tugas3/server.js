const express = require('express'); //import library buat app web
const bodyParser = require('body-parser'); //import library untuk parsing body dari request http
const mysql = require('mysql'); //import library untuk interaksi dengan database mysql
const bcrypt = require('bcrypt'); //import library untuk encrypt password

const app = express();
app.use(bodyParser.json()); //parsing body request sebagai json

//membuat konfigurasi untuk koneksi dengan database mysql
const conn = mysql.createConnection({
    host: 'localhost',
    user: 'katlyn',
    password : 'katlynmaclain',
    database : 'tugas3pat',
});

//melakukan koneksi dengan database mysql
conn.connect(function (err) {
    //jika ada kesalahan saat melakukan koneksi
    if (err) {
        console.error('Error connecting to MySQL: ', err);
        return;
    }
    //jika berhasil melakukan koneksi
    console.log('Connected to MySQL ....');
});

app.use((req, res, next) => {
    console.log("Received JSON: ", req.body);
    next();
});

// melakukan login !!!
app.post('/api/tugas3/users/login', function(req, res) {
    const { username, password } = req.body;

    // ambil password dari database berdasarkan username
    let sql = 'SELECT * FROM users WHERE username = ?';
    conn.query(sql, [username], (err, results) => {
        if (err) {
            res.status(500).json({ error: err.message });
            return;
        }
        if (results.length === 0) {
            res.status(404).json({ message: 'User not found' });
            return;
        }

        // mengecek apakah password yg diinput sudah sama dengan yg di database
        bcrypt.compare(password, results[0].password, function(err, result) {
            if (err || !result) {
                res.status(400).json({ message: 'Invalid username or password' });
            } else {
                res.status(200).json({ message: 'Login successful', user: results[0] });
            }
        });
    });
});

//mendapatkan data semua user
app.get('/api/tugas3/users', function(req, res){
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

//mendapatkan data user dgn id tertentu
app.get('/api/tugas3/users/:userID', function(req, res){
    let sql = "SELECT * FROM users WHERE userID="+req.params.userID;
    let query = conn.query(sql, function(err, result){
        if (err) throw err;
        res.send(JSON.stringify({
            "status" : 200,
            "error" : null,
            "response" : result
        }));
    });
}); 

// melakukan registrasi user baru !!!
app.post('/api/tugas3/users/register', function(req, res){
    let { username, password } = req.body;

    // password di enkripsi
    bcrypt.hash(password, 10, function(err, hash) {
        if (err) {
            console.error('Error hashing password: ', err);
            res.status(500).json({ error: 'Internal server error' });
            return;
        }

        // simpan username dan password yg sudah di enkripsi ke database
        let data = { username: username, password: hash };
        let sql = "INSERT INTO users SET ?";
        let query = conn.query(sql, data, function(err, result){
            if (err) {
                console.error('Error creating user: ', err);
                res.status(500).json({ error: 'Internal server error' });
                return;
            }
            res.status(200).json({
                status: 'User created successfully',
                userID: result.insertId,
                username: username
            });
        });
    });
});

//melakukan update data user harus ada id mana yang diupdate
app.put('/api/tugas3/users/:userID', function(req, res){
    // enkripsi password baru
    bcrypt.hash(req.body.password, 10, function(err, hash) {
        if (err) {
            console.error('Error hashing password: ', err);
            res.status(500).json({ error: 'Internal server error' });
            return;
        }

        // menyimpan password baru ke database 
        let sql = "UPDATE users SET password=? WHERE userID=?";
        let query = conn.query(sql, [hash, req.params.userID], function(err, result){
            if (err) {
                console.error('Error updating password: ', err);
                res.status(500).json({ error: 'Internal server error' });
                return;
            }
            res.send(JSON.stringify({
                "status" : 200,
                "error" : null,
                "response" : result
            }));
        });
    });
});

//menghapus data user harus ada id agar yg dihapus id tertentu bkn semua
app.delete('/api/tugas3/users/:userID', function(req, res){
    let userID = req.params.userID;
    let sql = "DELETE FROM users WHERE userID = ?";
    let query = conn.query(sql, userID, function(err, result){
        if (err) {
            console.error('Error deleting user: ', err);
            res.status(500).json({ error: 'Internal server error' });
            return;
        }
        if (result.affectedRows === 0) {
            // jika tidak ditemukan user dengan id tertentu
            res.status(404).json({ error: 'User not found' });
            return;
        }
        //jika ditemukan user dengan id tertentu
        res.status(200).json({ message: 'User deleted successfully' });
    });
});

//membuat data kamar baru ke tabel
app.post('/api/tugas3/rooms', function(req, res){
    let data = {
        roomNumber: req.body.roomNumber,
        roomType: req.body.roomType,
        price: req.body.price,
        facilities: req.body.facilities
    };
    let sql = "INSERT INTO rooms SET ?";
    let query = conn.query(sql, data, function(err, result){
        if (err) {
            console.error('Error creating room: ', err);
            res.status(500).json({ error: 'Internal server error' });
            return;
        }
        res.status(200).json({ 
            status: 'Room created successfully', 
            roomID: result.insertId,
            roomDetails: data 
        });
    });
});

//melakukan update data kamar berdasarkan ID kamar
app.put('/api/tugas3/rooms/:roomID', function(req, res){
    let data = {
        roomNumber: req.body.roomNumber,
        roomType: req.body.roomType,
        price: req.body.price,
        facilities: req.body.facilities
    };
    let sql = "UPDATE rooms SET ? WHERE roomID="+req.params.roomID;
    let query = conn.query(sql, data, function(err, result){
        if (err) {
            console.error('Error updating room: ', err);
            res.status(500).json({ error: 'Internal server error' });
            return;
        }
        res.status(200).json({ 
            status: 'Room updated successfully',
            roomID: req.params.roomID,
            roomDetails: data
        });
    });
});

//menghapus data kamar berdasarkan ID kamar
app.delete('/api/tugas3/rooms/:roomID', function(req, res){
    let roomID = req.params.roomID;
    let sql = "DELETE FROM rooms WHERE roomID = ?";
    let query = conn.query(sql, roomID, function(err, result){
        if (err) {
            console.error('Error deleting room: ', err);
            res.status(500).json({ error: 'Internal server error' });
            return;
        }
        if (result.affectedRows === 0) {
            // jika tidak ditemukan kamar dengan id tertentu
            res.status(404).json({ error: 'Room not found' });
            return; // Perbaikan disini
        }
        //jika ditemukan kamar dengan id tertentu
        res.status(200).json({ 
            status: 'Room deleted successfully', 
            roomID: roomID 
        });
    });
});

//booking kamar
app.post('/api/tugas3/bookings', function(req, res){
    let data = {
        userID: req.body.userID,
        checkInDate: req.body.checkInDate,
        checkOutDate: req.body.checkOutDate,
        roomCount: req.body.roomCount,
        roomType: req.body.roomType
    };
    let sql = "INSERT INTO bookings SET ?";
    let query = conn.query(sql, data, function(err, result){
        if (err) {
            console.error('Error making booking: ', err);
            res.status(500).json({ error: 'Internal server error' });
            return;
        }
        res.status(200).json({ bookingID: result.insertId, status: 'Booking made successfully' });
    });
});

//update status booking kamar
app.put('/api/tugas3/bookings/:bookingID', function(req, res){
    let sql = "UPDATE bookings SET status='"+req.body.status+"' WHERE bookingID="+req.params.bookingID;
    let query = conn.query(sql, function(err, result){
        if (err) {
            console.error('Error updating reservation: ', err);
            res.status(500).json({ error: 'Internal server error' });
            return;
        }
        res.status(200).json({ message: 'Reservation updated successfully' });
    });
});


//menghapus booking kamar
app.delete('/api/tugas3/bookings/:bookingID', function(req, res){
    let bookingID = req.params.bookingID;

    // Hapus reservasi dari database
    let sql = "DELETE FROM bookings WHERE bookingID=?";
    let query = conn.query(sql, [bookingID], function(err, result){
        if (err) {
            console.error('Error deleting reservation: ', err);
            res.status(500).json({ error: 'Internal server error' });
            return;
        }
        // Periksa apakah reservasi berhasil dihapus atau tidak
        if (result.affectedRows === 0) {
            res.status(404).json({ error: 'Reservation not found' });
            return;
        }
        res.status(200).json({ message: 'Reservation deleted successfully' });
    });
});


//cek status booking kamar
app.get('/api/tugas3/bookings/:bookingID', function(req, res){
    let sql = "SELECT bookingID, status FROM bookings WHERE bookingID="+req.params.bookingID;
    let query = conn.query(sql, function(err, result){
        if (err) {
            console.error('Error tracking order: ', err);
            res.status(500).json({ error: 'Internal server error' });
            return;
        }
        if (result.length === 0) {
            res.status(404).json({ error: 'Booking not found' });
            return;
        }
        res.status(200).json(result[0]);
    });
});

//mendapatkan semua data booking
app.get('/api/tugas3/bookings', function(req, res){
    let sql = "SELECT * FROM bookings";
    let query = conn.query(sql, function(err, result){
        if (err) throw err;
        res.send(JSON.stringify({
            "status" : 200,
            "error" : null,
            "response" : result
        }));
    });
});

//mendapatkan semua data kamar
app.get('/api/tugas3/rooms', function(req, res){
    let sql = "SELECT * FROM rooms";
    let query = conn.query(sql, function(err, result){
        if (err) throw err;
        res.send(JSON.stringify({
            "status" : 200,
            "error" : null,
            "response" : result
        }));
    });
});

//menambah review
app.post('/api/tugas3/review', function(req, res){
    let data = {
        bookingID: req.body.bookingID,
        review: req.body.review,
        rating: req.body.rating
    };
    let sql = "INSERT INTO reviews SET ?";
    let query = conn.query(sql, data, function(err, result){
        if (err) {
            console.error('Error adding review: ', err);
            res.status(500).json({ error: 'Internal server error' });
            return;
        }
        res.status(200).json({ status: 'Review added successfully' });
    });
});

// mendapatkan data booking berdasarkan userId !!!
app.get('/api/tugas3/bookings/users/:userID', function(req, res){
    let userID = req.params.userID;
    let sql = "SELECT * FROM bookings WHERE userID = ?";
    let query = conn.query(sql, [userID], function(err, result){
        if (err) throw err;

        if (result.length > 0) {
            res.send(JSON.stringify({
                "status" : 200,
                "error" : null,
                "data" : result
            }));
        } else {
            res.status(404).send(JSON.stringify({
                "status" : 404,
                "error" : "Booking not found",
                "data" : []
            }));
        }
    });
});

//buat server http yang akan mendengar permintaan pada port 8000
var server = app.listen(8000, function(){
    //menampilkan pesan bahwa server API sedang berjalan pada port 8000
    console.log("API Server running at port 8000");
});