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

3. **مقادیر ثابت و رشته‌های جادویی**  
   ارزها (`"USD"`, `"EUR"`, `"GBP"`) و کلیدهایی مثل `"card_number"`، `"wallet_id"` و ... در کد به صورت هاردکد قرار داده شده‌اند.

4. **متدهای چندمنظوره**  
   متدی مانند `processPayment` هم‌زمان ولیدیشن، پردازش و لاگینگ را انجام می‌دهد که اصل تک‌وظیفگی را زیر سؤال می‌برد.

---

## نقض اصول SOLID

### 1. اصل Single Responsibility (SRP)
کلاس `PaymentProcessor` علاوه بر مسئولیت اصلی پردازش پرداخت، وظایف دیگری نظیر لاگینگ و اعتبارسنجی را نیز بر عهده دارد. این مسئله باعث می‌شود هر تغییری در یکی از این وظایف بر کل کلاس اثر بگذارد و پیچیدگی و هزینه‌ی نگهداری را بالا ببرد.

### 2. اصل Open-Closed (OCP)
در متدهای `processPayment` و `validatePayment`، برای تشخیص نوع پرداخت از ساختار سوئیچ استفاده شده است. برای افزودن یک روش پرداخت جدید (مثلاً پرداخت با ارز دیجیتال)، باید این متدها را ویرایش کرد. این بدان معناست که کلاس برای افزودن ویژگی جدید **باز** است اما نیازمند **تغییر** در کد فعلی است که مغایر با اصل OCP می‌باشد.

### 3. اصل Interface Segregation (ISP)
در این کد Interface  تعریف نشده است. اگر بخواهیم یک اینترفیس واحد بسازیم که متدهای انواع پرداخت (کارت اعتباری، کیف پول دیجیتال و انتقال بانکی) را در بر داشته باشد، کلاس‌هایی که تنها بخشی از این متدها را نیاز دارند، بیهوده ملزم به پیاده‌سازی سایر متدها خواهند شد. این کار با اصل ISP در تضاد است. راهکار آن، تعریف اینترفیس‌های مجزا برای هر نوع پرداخت یا حداقل تفکیک رفتارهاست.

### 4. اصل Dependency Inversion (DIP)
کلاس `PaymentProcessor` به‌صورت مستقیم با جزییات ماژول‌های سطح پایین همچون APIهای کارت اعتباری، کیف پول دیجیتال و انتقال بانکی کار می‌کند (در متدهایی مانند `processCreditCard`، `processDigitalWallet` و `processBankTransfer`). 

### 5. اصل Liskov Substitution (LSP)
در نمونه کد داشده شده نقض این قانون مشاهده نمی شود.


# مستندات فاز دوم 

## تغییرات اعمال شده

### ایجاد کلاس انتزاعی `Payment`

- تعریف ویژگی‌های مشترک مانند **مبلغ**، **ارز** و **زمان**
- ایجاد متد انتزاعی `validatePayment()` برای اعتبارسنجی

### پیاده‌سازی زیرکلاس‌های مشخص

- `CreditCardPayment`: برای پرداخت‌های کارت اعتباری
- `DigitalWalletPayment`: برای پرداخت‌های کیف پول دیجیتال
- `BankTransferPayment`: برای پرداخت‌های انتقال بانکی

### الگوی طراحی فکتوری

- ایجاد کلاس `PaymentFactory` برای تولید انواع پرداخت
- جداسازی منطق ایجاد اشیاء از سیستم پردازش

### بازسازی کلاس `PaymentProcessor`

- استفاده از فکتوری برای ایجاد اشیاء پرداخت
- واگذاری منطق اعتبارسنجی به زیرکلاس‌های پرداخت
- جداسازی منطق پردازش از اعتبارسنجی


# مستندات فاز سوم: وراثت و Polymorphism

## مقدمه
در این فاز، با پیاده‌سازی رابط `PaymentGateway` و اعمال اصول وراثت و Polymorphism، سیستم پرداخت را توسعه دادیم.

---

## تغییرات اعمال شده

### ایجاد رابط PaymentGateway
- تعریف متودهای اصلی: `process`، `refundPayment` و `getTransactionStatus`
- ایجاد قرارداد مشخص برای درگاه‌های پرداخت

### پیاده‌سازی کلاس انتزاعی BaseGateway
- فراهم کردن عملکرد مشترک برای تمام درگاه‌ها
- مدیریت پیکربندی نقطه پایانی و لاگینگ
- پیاده‌سازی پیش‌فرض `getTransactionStatus`

### ایجاد درگاه‌های مشخص
- `StripeGateway`: پیاده‌سازی پردازش پرداخت از طریق Stripe
- `PayPalGateway`: پیاده‌سازی پردازش پرداخت از طریق PayPal

### بازسازی کلاس PaymentProcessor
- امکان انتخاب درگاه پرداخت در زمان اجرا
- پیاده‌سازی Polymorphism با استفاده از رابط `PaymentGateway`
- ارسال درخواست پرداخت به درگاه انتخاب شده

---

پیاده‌سازی وراثت و Polymorphism از طریق رابط `PaymentGateway` امکان انتخاب و تعویض درگاه‌های پرداخت در زمان اجرا را فراهم می‌کند. این طراحی، سیستم را برای تغییرات آینده آماده می‌سازد و انعطاف‌پذیری آن را هم افزایش می‌دهد.

# گزارش فاز چهارم پروژه - پیاده‌سازی Dependency Injection

### 1. اضافه شدن کلاس جدید `ConfigManager`
- خواندن مقادیر پیکربندی از فایل `app.properties`.
- بارگذاری مقادیر مورد نیاز برای ساخت درگاه‌های پرداخت (endpoint، کلیدها و شناسه‌ها).
### 2. افزودن فایل `app.properties`
- شامل مقادیر پیکربندی مانند:
  ```properties
  paypal.api.url = https://api.paypal.com
  paypal.client.secret = paypal_client_secret
  paypal.client.id = paypal_client_id
  stripe.api.url = https://api.stripe.com
  stripe.api.key = stripe_api_key

### 3.Dependency Injection

 با استفاده از Dependency Injection، لیست درگاه‌های پرداخت به‌صورت پارامتر به کلاس `PaymentProcessor` تزریق شد؛ این کار باعث حذف وابستگی مستقیم به کلاس‌های خاص، افزایش انعطاف‌پذیری سیستم و ساده‌تر شدن تست و نگهداری کد گردید.