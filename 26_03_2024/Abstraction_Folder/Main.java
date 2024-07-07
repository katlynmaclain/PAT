package Abstraction_Folder;

// File: Main.java
// Kelas utama untuk menjalankan program
public class Main {
    public static void main(String[] args) {
        // Buat objek dari kelas Lingkaran
        Bentuk lingkaran = new Lingkaran();
        lingkaran.gambar(); // Panggil metode gambar() dari kelas Lingkaran

        // Buat objek dari kelas Segitiga
        Bentuk segitiga = new Segitiga();
        segitiga.gambar(); // Panggil metode gambar() dari kelas Segitiga

        // Buat objek dari kelas Persegi
        Bentuk persegi = new Persegi();
        persegi.gambar(); // Panggil metode gambar() dari kelas Persegi
    }
}