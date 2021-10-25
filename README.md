# restaurant-reservations-app

Poza funkcjonalnościami opisanymi w specyfikacji zaimplementowałem również system rejestracji i logowania. Żeby zarejestrować nowego użytkownika
należy wysłać POST request na /register z obiektem JSON w formacie:
```
{ "username":username, "password":password", "email":email}
```
Żeby się zalogować należy wysłać POST request na /login z obiektem JSON:
```
{ "username":username, "password":password"}
```
Podczas rejestracji użytkownik automatycznie dostaje rolę "ROLE_CLIENT". Żeby mieć możliwość do wysyłania requestów GET na endpoint /reservations 
należy mieć rolę "ROLE_EMPLOYEE" albo "ROLE_ADMIN". Reszta powinna być jak w specyfikacji.
