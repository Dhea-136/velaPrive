# Sistem Manajemen Rental Yacht (Vela Privé)
Sistem Manajemen Rental Yacht Vela Privé 
adalah aplikasi berbasis Java Swing yang dirancang untuk pengelolaan 
bisnis rental yacht dalam mengelola data armada yacht, pelanggan, 
transaksi penyewaan, dashboard statistik, serta informasi lengkap 
secara terstruktur dan efisien. Aplikasi ini menerapkan konsep 
Object-Oriented Programming (OOP), arsitektur modular, 
serta penyimpanan data berbasis file. 

## Executive Summary
Sistem Manajemen Rental Yacht menyediakan sebuah platform terpusat 
untuk mengelola informasi penting yang berkaitan dengan operasional 
bisnis rental yacht. Sistem ini mencakup pengelolaan data armada 
yacht, data pelanggan, pencatatan transaksi rental, serta penyajian 
statistik real-time. Dengan adanya sistem ini, pengelola dapat 
mengurangi kesalahan pencatatan manual, meningkatkan efisiensi kerja, 
serta memperoleh data yang lebih terorganisir dan mudah diakses. 
Dokumentasi ini disusun sebagai panduan utama bagi pengembang maupun 
pengguna dalam memahami struktur sistem, cara instalasi, konfigurasi, 
serta arsitektur kode program.

## Fitur Utama Sistem
Aplikasi ini memiliki beberapa fitur utama yang mendukung oprasional
dari rental yacht, antara lain:
1. Manajemen Armada Yacht: Mengelola data yacht yang tersedia, termasuk
ID yacht, nama, tipe, kapasitas penumpang, harga sewa perhari, dan status 
ketersediaan.
2. Manajemen Pelanggan: Menyimpan dan mengelola data pelanggan seperti 
identitas, nomor telepon, email, dan alamat.
3. Manajemen Rental: Mencatat transaksi penyewaan yacht dengan kalkulasi 
otomatis biaya berdasarkan durasi rental pelanggan dan memastikan data 
tersimpan dengan baik.
4. Dashboard: Menampilkan ringkasan data seprti total rental, jumlah pelanggan, 
dan total pendapatan secara real-time.
5. Galeri Yacht: Menyajikan tampilan visual armada yacht dengan informasi lengkap
dan tombol detail untuk melihat spesifikasi yacht.

## Login
- Halaman login dengan tampilan modern (custom UI dengan background image)
- Validasi input username & password
- Autentikasi pengguna dengan sistem file-based
- Redirect otomatis ke Dashboard setelah login berhasil
- Kredensial default: Username: admin | Password: admin123

## Dashboard
Ringkasan statistik utama:
- Total Rentals - Jumlah seluruh transaksi rental
- Total Customers - Jumlah pelanggan terdaftar
- Available Yachts - Jumlah yacht yang tersedia untuk disewa
- Total Revenue - Total pendapatan dari semua rental

Galeri Yacht:
- Tampilan visual 3 yacht unggulan dengan gambar
- Informasi singkat (nama, panjang, harga per hari)
- Tombol "View Details" untuk melihat spesifikasi lengkap

## Manajemen Yacht
Data yacht meliputi:
- ID Yacht
- Nama yacht
- Tipe (Motor Yacht, Sailing Yacht, Catamaran)
- Kapasitas penumpang
- Harga sewa per hari
- Status (Available / Rented)

## Manajemen Pelanggan
Data pelanggan:
- ID pelanggan (auto-generate)
- Nama lengkap
- Nomor telepon
- Email
- Alamat

Fitur:
- Tambah pelanggan baru
- Edit data pelanggan
- Hapus pelanggan
- Tabel data pelanggan

## Manajemen Rental
Modul Manajemen Rental pada sistem rental yacht 
berfungsi untuk mencatat dan mengelola transaksi 
penyewaan yacht secara terstruktur. Fitur ini 
memungkinkan pengelola memilih pelanggan dan yacht 
yang tersedia, menentukan tanggal mulai dan akhir 
rental, serta sistem akan otomatis menghitung total 
biaya berdasarkan durasi sewa. Setiap transaksi yang 
berhasil disimpan akan ditampilkan dalam tabel lengkap 
dengan status rental dan dapat dikelola dengan fitur 
complete atau cancel rental.

Fitur utama:
- Pembuatan rental baru dengan form lengkap
- Pilihan customer dari database
- Pilihan yacht yang tersedia (status Available)
- Input tanggal mulai dan akhir (format: YYYY-MM-DD)
- Kalkulasi otomatis total biaya
- Status rental: Active / Completed
- Complete rental (yacht kembali menjadi Available)
- Cancel rental dengan konfirmasi

## Detail Yacht
Window detail yacht menampilkan:
- Gambar yacht
- Nama yacht
- Panjang yacht
- Harga sewa per hari
- Status ketersediaan
- Kapasitas penumpang

## Sistem Penyimpanan Data
Aplikasi menggunakan file text (.txt) untuk penyimpanan 
data:

File yang digunakan:
- users.txt - Data pengguna sistem
- customers.txt - Data pelanggan
- yachts.txt - Data armada yacht
- rentals.txt - Data transaksi rental

## Project Structure
## Cara Menjalankan Program

