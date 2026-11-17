# ArmyBazar — Bag Selling App

An Android e-commerce app for selling bags (کیف), built with modern Android tooling as a learning project by following an online course.

## Features

- Browse products by category (Backpack, Handbag, Tote, Clutch, Wallet, and more)
- Product details with images, price, material, and tags
- Comments on products (add / view)
- Shopping cart (add, remove, update quantity)
- Checkout flow with payment status handling
- User sign up / sign in with token-based authentication
- Offline support via local caching (Room database)
- User profile with saved address and postal code
- Light & Dark theme support

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Navigation:** Jetpack Navigation Compose
- **Dependency Injection:** Koin
- **Local Database:** Room
- **Networking:** Retrofit + OkHttp
- **Image Loading:** Coil
- **Animations:** Lottie
- **Architecture:** MVVM (Repository pattern)

## Project Structure

```
model/
  data/        -> data classes (Product, Ads, LoginResponse, ...)
  db/          -> Room database, DAO
  net/         -> Retrofit API service, authenticator
  Repository/  -> repository interfaces & implementations (Product, Cart, User, Comment)
ui/
  features/    -> screens & ViewModels (Main, Product, Category, Cart, Profile, SignIn, SignUp, Intro)
  theme/       -> Color, Type, Shapes, Theme (light/dark)
util/          -> constants, extensions, navigation routes, network checker
```

## Getting Started

1. Clone the repository
   ```bash
   git clone <repo-url>
   ```
2. Open the project in Android Studio
3. Let Gradle sync the dependencies
4. Run on an emulator or physical device (minSdk / Internet permission required)

## Notes

- Backend is a course-provided server; base URL is configured in `util/Const.kt`.

---

# اپلیکیشن فروش کیف (ArmyBazar)

یک اپلیکیشن اندرویدی فروشگاهی برای فروش کیف، که به‌عنوان پروژه‌ی یادگیری و با دنبال‌کردن یک دوره‌ی آموزشی آنلاین ساخته شده است.

## امکانات

- مرور محصولات بر اساس دسته‌بندی (کوله‌پشتی، دستی، توت، کیف پول و...)
- صفحه‌ی جزئیات محصول با تصویر، قیمت، جنس و برچسب‌ها
- ثبت و مشاهده‌ی کامنت روی محصولات
- سبد خرید (افزودن، حذف، تغییر تعداد)
- فرآیند پرداخت و پیگیری وضعیت پرداخت
- ثبت‌نام و ورود کاربر با احراز هویت مبتنی بر توکن
- پشتیبانی آفلاین با کش محلی (دیتابیس Room)
- پروفایل کاربر با آدرس و کد پستی ذخیره‌شده
- پشتیبانی از تم روشن و تیره (دارک مود)

## پشته فناوری (Tech Stack)

- **زبان:** Kotlin
- **رابط کاربری:** Jetpack Compose
- **ناوبری:** Jetpack Navigation Compose
- **تزریق وابستگی:** Koin
- **دیتابیس محلی:** Room
- **شبکه:** Retrofit + OkHttp
- **بارگذاری تصویر:** Coil
- **انیمیشن:** Lottie
- **معماری:** MVVM (الگوی Repository)

## ساختار پروژه

```
model/
  data/        -> کلاس‌های داده (Product، Ads، LoginResponse و...)
  db/          -> دیتابیس Room، DAO
  net/         -> سرویس API با Retrofit، Authenticator
  Repository/  -> اینترفیس‌ها و پیاده‌سازی Repositoryها (Product، Cart، User، Comment)
ui/
  features/    -> صفحات و ViewModelها (Main، Product، Category، Cart، Profile، SignIn، SignUp، Intro)
  theme/       -> رنگ‌ها، فونت‌ها، شکل‌ها، تم (روشن/تیره)
util/          -> ثابت‌ها، اکستنشن‌ها، مسیرهای ناوبری، بررسی اتصال شبکه
```

## شروع به کار

۱. کلون کردن ریپازیتوری:
```bash
git clone <repo-url>
```
۲. باز کردن پروژه در Android Studio
۳. صبر برای Sync شدن Gradle
۴. اجرا روی شبیه‌ساز یا دستگاه واقعی (نیاز به مجوز اینترنت)

## نکات.
- بک‌اند، سرور دوره‌ی آموزشی است؛ آدرس پایه در فایل `util/Const.kt` تنظیم شده است.
