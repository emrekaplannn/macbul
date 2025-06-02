[EN]
“MaçBul” is a mobile and web‐based platform that brings together players in Turkey who want to organize indoor soccer matches.

* **Purpose**: To allow individuals or groups who cannot find enough players to view nearby open matches and register with a single click.

* **Key Features**:

  * User registration (email/phone + password), profile information, and referral code system
  * Paid match listings (field name, date‐time, fee), sorted listings, and field location on a map
  * Wallet system: balance top‐up, payment, refund, and transaction history tracking
  * Dynamic overall scoring: a score between 0–100 that is updated based on user performance
  * Automatic team matching: teams are arranged so that their average scores are balanced
  * Post‐match feedback: player rating, comments, and reporting capability
  * Real‐time participation notifications (via WebSocket/SSE)
  * Match videos and goal highlights (video metadata stored as a URL)
  * Complaint management and admin notification module

* **Technology Stack**:

  * **Database**: MariaDB (UUID and epoch‐millis based time fields)
  * **Backend**: Java Spring Boot (REST API, security, data access layer)
  * **Frontend**: React Native (iOS/Android applications) and an optional web interface

Thus, “MaçBul” offers rapid prototyping and a scalable infrastructure while making it easy for users to socially participate in indoor soccer matches.


[TR]
“MaçBul”, Türkiye’de halı saha maçı organize etmek isteyen oyuncuları bir araya getiren mobil ve web tabanlı bir platformdur.

* **Amaç**: Yeterli oyuncu bulamayan bireylerin veya grupların, yakınlarındaki açık maçları görmesini ve tek tıkla kaydolmasını sağlamak.

* **Temel Özellikler**:

  * Kullanıcı kaydı (e-posta/telefon + şifre), profil bilgileri ve referans kodu sistemi
  * Ücretli maç ilanları (saha adı, tarih-saat, ücret), sıralı listeleme ve harita üzerinden saha konumu
  * Cüzdan sistemi: bakiye yükleme, ödeme, iade ve işlem geçmişi takibi
  * Dinamik overall puanlama: kullanıcı performansına göre 0–100 arası güncellenen skor
  * Otomatik takım eşleştirme: iki takımın ortalama puanları dengeli olacak şekilde dağıtım
  * Maç sonrası geri bildirim: oyuncu puanlama, yorum ve raporlama imkanı
  * Gerçek zamanlı katılım bildirimleri (WebSocket/SSE ile)
  * Maç videoları ve gol kesitleri (video meta verisi URL olarak saklanır)
  * Şikayet yönetimi ve admin bildirim modülü

* **Teknoloji Yığını**:

  * **Veritabanı**: MariaDB (UUID ve epoch-millis bazlı zaman alanları)
  * **Backend**: Java Spring Boot (REST API, güvenlik, veri erişim katmanı)
  * **Frontend**: React Native (iOS/Android uygulamaları) ve isteğe bağlı web arayüzü

Bu sayede “MaçBul”, hızlı prototipleme ve ölçeklenebilir bir altyapı sunarken, kullanıcıların sosyal olarak halı saha maçlarına katılmasını kolaylaştırır.
