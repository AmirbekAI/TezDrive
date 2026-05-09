# TezDrive — Mobile App Design Guide

**Base URL:** `https://tez-drive-198634486926.asia-south1.run.app`

---

## Design Philosophy

**Bright. Human. Fast.**

TezDrive is not another gray enterprise app. It uses bold primary colours on white backgrounds, generous spacing, rounded cards, and clear iconography. Every interaction answers within 200ms or shows a skeleton loader. The app has personality — illustrated empty states, playful micro-animations on booking confirmation, a subtle confetti burst when a ride is completed.

**Colour palette**
| Token | Hex | Usage |
|---|---|---|
| Primary | `#FF6B2C` | CTAs, active states, FAB |
| Primary Light | `#FFF0EA` | Card tints, chip backgrounds |
| Accent | `#1A1A2E` | Headlines, bold text |
| Surface | `#FFFFFF` | Cards, bottom sheets |
| Background | `#F5F6FA` | Page background |
| Success | `#27AE60` | Confirmed, finished |
| Warning | `#F2994A` | Pending states |
| Danger | `#EB5757` | Cancel, reject, errors |
| Muted | `#9B9BAD` | Secondary text, placeholders |

**Typography** — `Inter` (Latin) + `Noto Sans` (Cyrillic/Kyrgyz)
- H1 28sp Bold, H2 22sp SemiBold, Body 15sp Regular, Caption 12sp Regular

**Language** — Three-language toggle in Settings: 🇷🇺 Русский / 🇰🇬 Кыргызча / 🇬🇧 English. All labels, errors, and empty states are localised.

---

## Roles

The app detects the logged-in user's role (`DRIVER` or `PASSENGER`) after login and shows the appropriate tab bar and flows.

---

## Screen Map

```
Splash
  └─ Onboarding (first launch only)
       └─ Login / Register
            ├─ [PASSENGER] Home → Search → Ride Detail → Booking Confirm
            │                            My Bookings → Booking Detail
            │                            Driver Profile
            └─ [DRIVER]   Home → My Rides → Ride Detail → Bookings for Ride
                                  Create Ride
                                  My Cars → Add Car / Car Detail
            Both roles → Profile → Edit Profile / Change Photo
                          Settings (language, notifications, about)
```

---

## Screens — Passenger

### 1. Splash Screen
Full-screen white with the TezDrive wordmark in Primary orange centred. Auto-advances after 1.5s. Checks for saved token → goes to Home or Login.

---

### 2. Onboarding (3 slides, shown once)
Three illustrated slides (Lottie or static SVG):
1. **"Поехали! / Жүрөлү! / Let's go!"** — illustration of a car on a road
2. **"Найди поездку / Сапарды тап / Find a ride"** — map with pin
3. **"Безопасно и быстро / Коопсуз жана тез / Safe and fast"** — shield + lightning bolt

Bottom: skip button (Muted), dot pagination, and a large Primary "Далее / Кийинки / Next" button. Last slide shows "Начать / Баштоо / Get Started".

---

### 3. Login Screen
- Large headline: **"Добро пожаловать / Кош келиңиз / Welcome back"**
- Email field (icon: envelope)
- Password field (icon: lock, toggle visibility)
- Primary button: **"Войти / Кирүү / Sign in"**
- Link below: **"Нет аккаунта? Зарегистрируйтесь" / "Аккаунт жокпу? Катталуу" / "No account? Register"**

Error states appear as a red inline banner under the relevant field.

---

### 4. Register Screen
- Headline: **"Создать аккаунт / Аккаунт түзүү / Create account"**
- Name field
- Email field
- Phone field (with `+996` prefix chip)
- Password field
- Role selector: two large toggle chips — **Пассажир / Passenger** (person icon) and **Водитель / Driver** (steering wheel icon). Selected chip gets Primary background.
- Primary button: **"Зарегистрироваться / Катталуу / Register"**

---

### 5. Passenger Home (Search)
**Top area (white card with soft shadow):**
- Small greeting: "Привет, Айдан 👋 / Салам, Айдан 👋 / Hey, Aidan 👋"
- **From** field (location pin icon, orange) — text input
- **To** field (flag icon) — text input
- **Date** chip — opens date picker
- **Seats** stepper (+/-)
- Large Primary button: **"Найти / Табуу / Search"**

**Below:** Horizontal scrollable chips for popular destinations (if you want to pre-populate) OR a full-bleed illustrated empty state if no recent searches.

---

### 6. Search Results
List of ride cards. Each card:
```
┌──────────────────────────────────────────────────┐
│  09:00 ──────────────── 13:30            4 seats │
│  Бишкек            Ош                   500 сом  │
│  ● Avatar  Мирлан Д.  ★ 4.8   [Бронировать]     │
└──────────────────────────────────────────────────┘
```
- Departure time → arrival city
- Driver avatar (circle, 32dp) + name + star rating chip
- Available seats remaining
- Price in сом
- "Бронировать / Ирезервациялоо / Book" button (Primary, rounded)
- Tap anywhere else → Ride Detail

Sort options (horizontal chips at top): **По цене / Баа боюнча / By price** | **По рейтингу / Рейтинг боюнча / By rating** | **По времени / Убакыт боюнча / By time**

Empty state: illustrated car driving away — "Поездок не найдено / Сапарлар табылган жок / No rides found"

---

### 7. Ride Detail Screen
Full header card:
- From city → To city (large, bold)
- Date + departure time
- Price per seat
- Available seats (badge chip)
- Car info: make, model, color, plate (small grey text)
- Car photos: horizontal scroll of thumbnails (tap to open full-screen gallery)

Driver card:
- Large avatar
- Name + phone (visible after booking)
- Star rating (e.g. ★ 4.8 · 23 поездки / сапарлар / rides)
- "Профиль водителя / Айдоочунун профили / Driver profile" link

Comment section: list of passenger comments (max 5 shown, "Все отзывы / Баардык пикирлер / All reviews" link)

Bottom bar: fixed — seats stepper + **"Бронировать — 500 сом / Book — 500 som"** big Primary button.

---

### 8. Booking Confirm Sheet (Bottom sheet)
- Summary: route, date, seats, total = seats × price
- Note field (optional message to driver)
- **"Подтвердить / Ырастоо / Confirm"** button
- Status after tap: animated checkmark → "Заявка отправлена / Өтүнмө жөнөтүлдү / Request sent" — status is PENDING

---

### 9. My Bookings (Tab: Поездки / Сапарлар / Trips)
Tab bar at bottom — for passenger: 🔍 Search | 🎫 My Trips | 👤 Profile

List of booking cards filtered by status chips: **Все / Баары / All** | **Ожидание / Күтүү / Pending** | **Подтверждено / Тастыкталды / Confirmed** | **Завершено / Аяктады / Finished**

Each card shows:
- Route + date
- Status badge (colour coded: Warning=Pending, Primary=Accepted, Success=Finished, Danger=Cancelled)
- Driver name + rating (small)
- "Подробнее / Деталдар / Details" chevron

---

### 10. Booking Detail Screen
- Full booking info (route, date, seats, price, driver contact)
- Status timeline: Pending → Accepted → Ride Started → Finished
- If status = PENDING: red **"Отменить / Жокко чыгаруу / Cancel"** button
- If status = FINISHED and not rated: **"Оценить поездку / Бааны кой / Rate ride"** card (1–5 stars tap)
- If status = FINISHED and rated but no comment: **"Оставить отзыв / Пикир калтыруу / Leave a comment"** card (text input, 240 char max)

---

### 11. Driver Public Profile Screen
Reached from ride cards or search results.
- Large avatar + name
- Star rating + number of completed rides
- About / bio (if set)
- List of comments from passengers (cards with commenter name, date, text)

---

## Screens — Driver

### 12. Driver Home (My Rides)
Bottom tab bar: 🗺 My Rides | ➕ New Ride | 🚗 My Cars | 👤 Profile

List of ride cards (created by this driver):
```
┌──────────────────────────────────────────────────┐
│  Бишкек → Ош       15 мая · 09:00      3/4 seats │
│  [ACTIVE]  2 заявки / 2 өтүнмө / 2 pending      │
└──────────────────────────────────────────────────┘
```
Status badges: `SCHEDULED` (Warning) | `ACTIVE` (Primary) | `FINISHED` (Success)

Tap → Ride Management screen.

---

### 13. Ride Management Screen (Driver)
- Route + date + time
- Passengers list (accepted bookings): avatar, name, seats, status
- Pending bookings: each with **"Принять / Кабыл алуу / Accept"** (green) and **"Отклонить / Баш тартуу / Reject"** (red) buttons
- Bottom bar: if SCHEDULED → **"Начать поездку / Сапарды баштоо / Start Ride"** Primary button
- If ACTIVE → **"Завершить поездку / Сапарды аяктоо / Finish Ride"** Success button

---

### 14. Create Ride Screen
Large form:
- **Откуда / Кайдан / From** — text
- **Куда / Кайда / To** — text
- **Дата / Дата / Date** — date picker (calendar bottom sheet)
- **Время отправления / Чыгуу убактысы / Departure time** — time picker
- **Цена за место / Орун баасы / Price per seat** — numeric, сом suffix
- **Количество мест / Орундар саны / Seats** — stepper
- **Автомобиль / Автомобиль / Car** — dropdown from driver's registered cars
- Primary button: **"Создать / Түзүү / Create"**

After success: confetti micro-animation, then navigate to the new ride's management screen.

---

### 15. My Cars Screen
List of car cards:
```
┌──────────────────────────────────────────────────┐
│  📷  Toyota Camry 2019        Белый / White      │
│      А 123 ВС 01              [Управление]       │
└──────────────────────────────────────────────────┘
```
FAB (+) at bottom right → Add Car.

---

### 16. Car Detail Screen
- Car photos (horizontal scroll, tap to full-screen)
- Make, model, year, colour, plate
- **"Добавить фото / Сүрөт кошуу / Add photo"** button (camera icon)
- Each photo has a trash icon to delete
- **"Удалить автомобиль / Автомобилди өчүрүү / Delete car"** (red, at bottom, with confirm dialog)

---

### 17. Add Car Screen (Bottom sheet or full screen)
Fields:
- Make (Марка) — text
- Model (Модель) — text
- Year (Год) — numeric picker
- Color (Цвет) — text or colour chips
- Plate (Номер) — text, uppercase auto-format
- **"Добавить / Кошуу / Add"** Primary button

---

## Shared Screens

### 18. My Profile Screen (both roles)
- Large avatar (tap → photo picker → uploads, shows progress ring)
- Name (tap → edit inline)
- Phone
- Email (read-only)
- Role badge chip
- **"Редактировать / Түзөтүү / Edit"** button → edit name + phone
- Language switcher: 🇷🇺 RU | 🇰🇬 KG | 🇬🇧 EN chips
- **"Выйти / Чыгуу / Sign out"** (red text, at bottom)

---

### 19. Settings Screen
- Language
- Notifications toggle
- About TezDrive
- Privacy policy link
- App version

---

## Navigation Summary

**Passenger bottom tabs**
| Icon | Label RU | Label KG | Label EN |
|---|---|---|---|
| 🔍 | Поиск | Издөө | Search |
| 🎫 | Поездки | Сапарлар | Trips |
| 👤 | Профиль | Профиль | Profile |

**Driver bottom tabs**
| Icon | Label RU | Label KG | Label EN |
|---|---|---|---|
| 🗺 | Мои поездки | Сапарларым | My Rides |
| ➕ | Создать | Түзүү | Create |
| 🚗 | Мои авто | Авторум | My Cars |
| 👤 | Профиль | Профиль | Profile |

---

---

# API Reference

**Base URL:** `https://tez-drive-198634486926.asia-south1.run.app`

All endpoints that require authentication use a Bearer token in the `Authorization` header:
```
Authorization: Bearer <token>
```
Tokens are returned on login/register and are valid for 24 hours.

All request/response bodies are `application/json` unless marked as `multipart/form-data`.

---

## Auth

### `POST /api/auth/register`
**Public.** Create a new account.

**Request body:**
```json
{
  "name": "Айдан Бекова",
  "email": "aidan@example.com",
  "phone": "+996700123456",
  "password": "secret123",
  "role": "PASSENGER"
}
```
`role` must be `"PASSENGER"` or `"DRIVER"`.

**Response `200`:**
```json
{
  "token": "eyJhbGci...",
  "id": 1,
  "name": "Айдан Бекова",
  "email": "aidan@example.com",
  "role": "PASSENGER"
}
```

---

### `POST /api/auth/login`
**Public.** Log in with email + password.

**Request body:**
```json
{
  "email": "aidan@example.com",
  "password": "secret123"
}
```

**Response `200`:** Same shape as register response.

---

## Users / Profile

### `GET /api/users/me`
**Auth required.** Returns the logged-in user's full profile.

**Response `200`:**
```json
{
  "id": 1,
  "name": "Айдан Бекова",
  "email": "aidan@example.com",
  "phone": "+996700123456",
  "role": "PASSENGER",
  "photoUrl": "https://pub-1ea80174cb174415b3ee4c932d9283ab.r2.dev/avatars/uuid.jpg",
  "rating": null,
  "comments": []
}
```
For drivers, `rating` is a float (e.g. `4.8`) and `comments` is a list of passenger comments.

---

### `PATCH /api/users/me`
**Auth required.** Update name and/or phone.

**Request body (all fields optional):**
```json
{
  "name": "Айдан Б.",
  "phone": "+996700999888"
}
```

**Response `200`:** Updated profile (same shape as GET /me).

---

### `POST /api/users/me/photo`
**Auth required.** Upload or replace the profile photo.

**Request:** `multipart/form-data`, field name `file` (JPEG/PNG, max 10 MB).

**Response `200`:** Updated profile with new `photoUrl`.

The old photo is automatically deleted from storage.

---

### `GET /api/users/drivers/{driverId}`
**Public.** View a driver's public profile — name, photo, rating, comments.

**Response `200`:** Same profile shape (sensitive fields like email/phone hidden server-side for non-authenticated requests — implement on client by not displaying them for public view).

---

## Cars

### `POST /api/cars`
**Auth required (DRIVER only).** Register a new car.

**Request body:**
```json
{
  "make": "Toyota",
  "model": "Camry",
  "year": 2019,
  "color": "Белый",
  "plateNumber": "А123ВС01"
}
```

**Response `201`:**
```json
{
  "id": 5,
  "driverId": 2,
  "make": "Toyota",
  "model": "Camry",
  "year": 2019,
  "color": "Белый",
  "plateNumber": "А123ВС01",
  "photos": []
}
```

---

### `GET /api/cars/my`
**Auth required (DRIVER only).** List all cars belonging to the logged-in driver.

**Response `200`:** Array of car objects (same shape as above).

---

### `POST /api/cars/{carId}/photos`
**Auth required (DRIVER only).** Upload a photo for one of the driver's cars.

**Request:** `multipart/form-data`, field name `file` (JPEG/PNG, max 10 MB).

**Response `201`:** Updated car object with `photos` list containing the new URL.

---

### `DELETE /api/cars/photos/{photoId}`
**Auth required (DRIVER only).** Delete a specific car photo. Only the owner can delete.

**Response `204` No Content.**

---

### `DELETE /api/cars/{carId}`
**Auth required (DRIVER only).** Delete a car. Only the owner can delete.

**Response `204` No Content.**

---

## Rides

### `POST /api/rides`
**Auth required (DRIVER only).** Create a new ride offer.

**Request body:**
```json
{
  "from": "Бишкек",
  "to": "Ош",
  "departureTime": "2026-05-20T09:00:00",
  "pricePerSeat": 500,
  "totalSeats": 4,
  "carId": 5
}
```

**Response `201`:**
```json
{
  "id": 10,
  "driverId": 2,
  "driverName": "Мирлан Джумабеков",
  "driverRating": 4.8,
  "from": "Бишкек",
  "to": "Ош",
  "departureTime": "2026-05-20T09:00:00",
  "pricePerSeat": 500,
  "totalSeats": 4,
  "availableSeats": 4,
  "status": "SCHEDULED",
  "car": { "make": "Toyota", "model": "Camry", "year": 2019, "color": "Белый", "plateNumber": "А123ВС01", "photos": [] }
}
```

---

### `GET /api/rides/my`
**Auth required (DRIVER only).** List all rides created by the logged-in driver.

**Response `200`:** Array of ride objects.

---

### `GET /api/rides/search`
**Public.** Search available rides.

**Query parameters:**
| Param | Type | Required | Description |
|---|---|---|---|
| `from` | string | ✅ | Departure city |
| `to` | string | ✅ | Destination city |
| `date` | string | ✅ | Date in `YYYY-MM-DD` format |
| `seats` | int | ✅ | Minimum seats needed |
| `sortBy` | string | ❌ | `price` or `rating` (default: departure time) |

**Example:** `GET /api/rides/search?from=Бишкек&to=Ош&date=2026-05-20&seats=1&sortBy=rating`

**Response `200`:** Array of ride objects with `status = SCHEDULED` and `availableSeats >= seats`.

---

### `GET /api/rides/{id}`
**Public.** Get details of a single ride by ID.

**Response `200`:** Single ride object.

---

### `PATCH /api/rides/{rideId}/start`
**Auth required (DRIVER only).** Mark a ride as started (`ACTIVE`). Only the ride's driver can do this. Ride must currently be `SCHEDULED`.

**Response `200`:** Updated ride with `status: "ACTIVE"`.

---

### `PATCH /api/rides/{rideId}/finish`
**Auth required (DRIVER only).** Mark a ride as finished (`FINISHED`). Ride must currently be `ACTIVE`.

**Response `200`:** Updated ride with `status: "FINISHED"`.

---

## Bookings

### `POST /api/rides/{rideId}/bookings`
**Auth required (PASSENGER only).** Request a booking on a ride.

**Request body:**
```json
{
  "seatsRequested": 2,
  "note": "Позвоните перед выездом"
}
```
`note` is optional.

**Response `201`:**
```json
{
  "id": 33,
  "rideId": 10,
  "passengerId": 1,
  "passengerName": "Айдан Бекова",
  "seatsRequested": 2,
  "note": "Позвоните перед выездом",
  "status": "PENDING",
  "rating": null
}
```

---

### `GET /api/bookings/my`
**Auth required (PASSENGER only).** List all bookings made by the logged-in passenger.

**Response `200`:** Array of booking objects.

---

### `PATCH /api/bookings/{bookingId}/cancel`
**Auth required (PASSENGER only).** Cancel a booking. Only allowed if status is `PENDING` or `ACCEPTED`.

**Response `200`:** Updated booking with `status: "CANCELLED"`.

---

### `GET /api/rides/{rideId}/bookings`
**Auth required (DRIVER only).** View all bookings for one of the driver's rides.

**Response `200`:** Array of booking objects showing which passengers requested seats.

---

### `PATCH /api/bookings/{bookingId}/accept`
**Auth required (DRIVER only).** Accept a passenger's booking request.

**Response `200`:** Updated booking with `status: "ACCEPTED"`. Available seats on the ride decrease accordingly.

---

### `PATCH /api/bookings/{bookingId}/reject`
**Auth required (DRIVER only).** Reject a passenger's booking request.

**Response `200`:** Updated booking with `status: "REJECTED"`.

---

### `PATCH /api/bookings/{bookingId}/rate`
**Auth required (PASSENGER only).** Rate the driver after a completed ride (`FINISHED`). Can only be done once per booking.

**Request body:**
```json
{
  "rating": 5
}
```
`rating` must be `1`–`5`.

**Response `200`:** Updated booking with `rating` set. Driver's average rating recalculates automatically.

---

### `POST /api/bookings/{bookingId}/comment`
**Auth required (PASSENGER only).** Leave a text comment for the driver after a completed ride. Can only be done once per booking.

**Request body:**
```json
{
  "text": "Отличный водитель, приехал вовремя!"
}
```

**Response `201`:**
```json
{
  "id": 7,
  "bookingId": 33,
  "passengerId": 1,
  "passengerName": "Айдан Бекова",
  "text": "Отличный водитель, приехал вовремя!",
  "createdAt": "2026-05-20T15:30:00"
}
```

---

## Error Responses

All errors follow this shape:
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Seats requested exceeds available seats"
}
```

Common HTTP status codes used:
| Code | Meaning |
|---|---|
| `400` | Validation error or business rule violation |
| `401` | Missing or invalid token |
| `403` | Authenticated but not allowed (wrong role or not owner) |
| `404` | Resource not found |
| `409` | Conflict (e.g. duplicate plate number) |
| `500` | Server error |
