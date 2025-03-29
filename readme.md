# فاز ۱ – گزارش کد اسمِل‌ها و نقض اصول SOLID

## مقدمه
## اصول SOLID در طراحی شی‌گرا

اصول SOLID پنج اصل کلیدی در طراحی نرم‌افزار شی‌گرا هستند که هدف آن‌ها ایجاد کدی قابل نگهداری، توسعه‌پذیر، و خوانا است. این اصول عبارت‌اند از:

- **S: اصل مسئولیت یگانه (Single Responsibility Principle)** – هر کلاس باید تنها یک مسئولیت داشته باشد و فقط به یک دلیل تغییر کند.
- **O: اصل باز-بسته بودن (Open/Closed Principle)** – کلاس‌ها باید برای توسعه باز و برای تغییر بسته باشند، یعنی بتوانیم بدون تغییر در کد موجود، ویژگی جدید اضافه کنیم.
- **L: اصل جایگزینی لیسکوف (Liskov Substitution Principle)** – کلاس‌های فرزند باید بتوانند جایگزین کلاس‌های والد خود شوند بدون اینکه رفتار سیستم را خراب کنند.
- **I: اصل تفکیک واسط‌ها (Interface Segregation Principle)** – نباید کلاس‌ها را مجبور کنیم متدهایی را پیاده‌سازی کنند که به آن‌ها نیازی ندارند؛ واسط‌ها باید کوچک و اختصاصی باشند.
- **D: اصل وارونگی وابستگی (Dependency Inversion Principle)** – ماژول‌های سطح بالا نباید مستقیماً به ماژول‌های سطح پایین وابسته باشند؛ هر دو باید به انتزاع‌ها وابسته باشند.

---

## مشکلات کد (Code Smells)

1. **کلاس بزرگ با وظایف متعدد**  
   کلاس `PaymentProcessor` هم‌زمان مسئول ولیدیشن، پردازش پرداخت و لاگینگ است.

2. **سوئیچ‌های بزرگ**  
   متدهای `processPayment` و `validatePayment` از سوئیچ بر اساس نوع پرداخت استفاده می‌کنند و افزودن روش جدید را دشوار می‌سازند.

3. **استفاده مکرر از Map**  
   کار با `Map<String, String>` موجب عدم ایمنی نوع (type-safety) و کد شکننده می‌شود، زیرا هر کی-ولیویی می‌تواند وارد شود.

4. **مقادیر ثابت و رشته‌های جادویی**  
   ارزها (`"USD"`, `"EUR"`, `"GBP"`) و کلیدهایی مثل `"card_number"`، `"wallet_id"` و ... در کد به صورت هاردکد قرار داده شده‌اند.

5. **متدهای چندمنظوره**  
   متدی مانند `processPayment` هم‌زمان ولیدیشن، پردازش و لاگینگ را انجام می‌دهد که اصل تک‌وظیفگی را زیر سؤال می‌برد.

---

## نقض اصول SOLID

### 1. اصل Single Responsibility (SRP)
  - کلاس `PaymentProcessor` علاوه بر مسئولیت پردازش پرداخت (Process Payment)، وظایف مختلف دیگری مانند لاگینگ (Log Transaction) و ولیدیشن (Validate Payment) را نیز بر عهده دارد. این باعث می‌شود تغییر در یکی از این وظایف روی کل کلاس اثر بگذارد و پیچیدگی را افزایش دهد.

### 2. اصل Open-Closed (OCP)
  - در متدهای `processPayment` و `validatePayment` از سوئیچ برای نوع پرداخت استفاده می‌شود. اگر قرار باشد روش پرداخت جدید (مثلاً پرداخت با کریپتو) اضافه کنیم، مجبور هستیم این متدها را تغییر دهیم. بنابراین، کلاس برای اضافه شدن ویژگی‌های جدید **باز** است اما باید **تغییر** داده شود که خلاف OCP است.

### 3. اصل Liskov Substitution (LSP)
  - از آنجا که در این نمونه، فعلاً مبحث ارث‌بری یا چندریختی (Inheritance/Polymorphism) به روش مستقیم وجود ندارد، نقض LSP خیلی واضح نیست. اما اگر در آینده بخواهید کلاس‌های فرزند برای انواع مختلف پرداخت ایجاد کنید، باید مطمئن باشید رفتار زیرکلاس‌ها با کلاس پدر هم‌خوانی دارد و کلاینت‌ها بدون دانستن تفاوت پیاده‌سازی، بتوانند با آنها کار کنند. ساختار سوئیچ‌های بزرگ نشانه‌ای از عدم تطابق با اصول پلی‌مورفیسم است که در نهایت می‌تواند به نقض LSP منجر شود.

### 4. اصل Interface Segregation (ISP)
  - در این کد، عملاً اینترفیس رسمی وجود ندارد، ولی اگر بخواهید اینترفیس واحدی برای تمام انواع پرداخت تعریف کنید که متدهای مخصوص به هر پرداخت (Credit Card, Wallet, Bank Transfer) را در بر داشته باشد، کلاس‌هایی که به برخی متدها نیازی ندارند، بی‌دلیل مجبور به پیاده‌سازی خواهند شد. این نقض ISP است. با تعریف اینترفیس‌های جداگانه برای هر نوع پرداخت (یا دست‌کم تفکیک رفتارها) می‌توان آن را رفع کرد.

### 5. اصل Dependency Inversion (DIP)
  - `PaymentProcessor` مستقیماً با جزئیات مختلف پرداخت مثل APIهای کارت اعتباری، والت و بانک ترنسفر کار می‌کند (`System.out.println` و مقادیر هاردکدشده). بهتر است این منطق را در ماژول یا اینترفیس‌های انتزاعی (مثلاً `PaymentHandler`) قرار دهیم و `PaymentProcessor` فقط به آن اینترفیس‌ها وابسته باشد. در نتیجه می‌توان پیاده‌سازی‌های مختلف را به سادگی جایگزین کرد، بی‌آنکه کلاس اصلی دست‌کاری شود.

