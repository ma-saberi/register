   هنگامی که پروژه برای بار اول ران میشود یک کاربر با نقش مدیر سیستم در دیتابیس درج میشود. در ادامه کار با سرویس POST /api/user افراد میتوانند ثبت نام کنند که باید توجه داشت که username و email باید یکتا باشند. در ادامه تمامی کاربران برای استفاده از سرویس های دیگر نیاز به توکن دارند  که از سرویس /login قابل گرفتن است. توکن دریافتی در هدر Authorizations با فرمت زیر استفاده می‌شود: ...Bearer

برای تغییر نقش افراد از USER به ADMIN، مدیر سیستم (SYS_ADMIN) باید نقش افراد را از سرویس /changerole تغییر دهد. (امکان تغییر نقش به/از مدیر سیستم وجود ندارد)

هر کاربر می‌تواند اطلاعات خود را با استفاده از سرویس  /api/user/me ببیند

در ادامه کاربران می‌توانند با توجه به سطح دسترسی خود عملیات سرچ را با سرویس GET /api/user انجام دهند، کاربر با سطح دسترسی user فقط می‌تواند اطلاعات مربوط به کاربرانی که معرف آن هاست را ببیند و کاربران با سطح ADMIN و SYS_ADMIN اطلاعات همه ی کاربران را میتوانند ببینند.
همچنین سرویسی جداگانه که فقط اطلاعات کاربر را با توجه به معرف آن ها برمیگرداند با ادرس api/user/{introducer}/ قابل دسترس است

تمامی سرویس ها در سواگر لوکال با ادرس زیر در دسترس هستند:

http://localhost:8443/api/docs

دیتابیس استفاده شده از ادرس زیر قابل مشاهده است:


http://localhost:8443/h2


 لاگ های مربوط به request و response در کنسول قابل مشاهده است. 

همچنین بنابر شرح مساله از آیدی دیتابیسی موجودیت‌ها استفاده ای نشده است.