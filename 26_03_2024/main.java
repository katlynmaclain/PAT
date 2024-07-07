// File: Main.java
// Kelas Main
public class main {
    public static void main(String[] args) {
        // Membuat objek dari kelas Mobil
        mobil mobilPertama = new mobil("Toyota", "Hitam", 2020);
        mobil mobilKedua = new mobil("Honda", "Putih", 2018);

        // Menggunakan method dari objek mobilPertama
        mobilPertama.hidupkanMesin();
        mobilPertama.kemudi();

        // Menggunakan method dari objek mobilKedua
        mobilKedua.hidupkanMesin();
        mobilKedua.kemudi();
    }
}
