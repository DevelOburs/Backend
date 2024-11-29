# Backend

Dataset:
https://www.kaggle.com/datasets/irkaal/foodcom-recipes-and-reviews/data

intellijide ultimate versiyonu okul emaili ile lisanslayıp kurmanız önerilir

to run database:
in root folder, run:
docker-compose up

to delete old db version adn install new:
#### docker compose down
// You should delete volume at this point, you can do it on docker desktop or temrinal
#### docker volume list 
// there should be: backend_fridge-db-data
#### docker volume rm backend_fridge-db-data

docker-compose up
berat deployment notları:

google da hesap açamadığım için şu anlık böyle bir çözüm yoluna gittim. google izin verirse bu konfigürasyonla çalıştırabileceğiz zaten sıkıntı yok, docker imajlarını çalıştırabiliriz.

aws

aws de çalışan kodlarımız direkt jar dosyaları ile çalışıyor, orada docker kullanmadım. (aylık 750 saat bedava. yani 7/24 çalışabilir. bu servisler sık güncellenmeyeceği için burada durması mantıklı.)
eureka ve gateway aws de çalışmakta. url: http://ec2-51-21-135-63.eu-north-1.compute.amazonaws.com:8080 veya 8761
render

diğer api lerimiz render da çalışmakta. docker imajlarını push ladıktan sonra render da deploy edebiliyoruz.
Şu anlık testlerimde çalıştı fakat dikkat etmemiz gereken iki nokta var:
render'da servislere birkaç dakika istek gelmediğinde pasif hale geliyor. bu yüzden siz denemeye çalıştığınızda request atıp beklemeniz gerekecek. render dashboard'dan da görebilirsiniz. api lerin url leri:
- https://recipe-api-yikp.onrender.com, https://auth-api-i2v8.onrender.com, https://user-api-ml66.onrender.com
şu an auth-api/register endpointi çalışıyor fakat 502 atıyor. detaylı bakacağım.
neden hepsi render da değil diye sorabilirsiniz. uzun uğraşlar ve hatalar sonucunda böyle bir çözümde karar kılmak durumunda kaldım .d bir toplantı yapıp konuşabiliriz.

hesap bilgileri wp den paylaşılacaktır.


Swagger --->
recipe: http://localhost:8081/swagger-ui/index.html#/fridge-controller
auth: http://localhost:8083/swagger-ui/index.html#/
user: http://localhost:8082/swagger-ui/index.html#/

