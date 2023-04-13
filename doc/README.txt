Zamysł projektu: Stworzenie web api która posiada

POSTĘPY PROJEKTU OD PEWNEGO MOMENTU:

20.03
pobrać inputy gdy się logujesz/ sprawdzić w bazie i przekierować do api      ZROBIONE 20.03

21.03
pobrać inputy gdy się zarejestrujesz/  ZROBIONE 21.03
wprowadzić za pomocą hibernate ograniczenia znaków w danych min.  ZROBIONE 21.03
zwracanie komunikatu na ekranie zamiast w konsoli oraz nieprzekierowywanie jak user wprowadzi złe dane  ZROBIONE 21.03
BcryptNcoder zabezpieczenie hasła w bazie, gdy się rejestruje użytkownik  ZROBIONE 21.03

22.03
Nie przyjmie rejestracji użytkownika, jeśli jego mail już jest w bazie   ZROBIONE 22.03
dodanie remember me w login page:/ nie działa przez liścia  COŚ ZROBIONE 22.03
Tworzenie strony html i css API po zalogowaniu, główne mięsko aplikacji / notatnik  COŚ ZROBIONE 22.03

25.03
Strona profilu wizualnie ZROBIONE 25.03
Strona kreowania notatki wizualnie ZROBIONE 25.03
Strona Treningów wizualnie   ZROBIONE 25.03


02.04
Strona profilu zwraca dane zalogowanego użytkownika ZROBIONE 02.04
Poprawienie email'a na stronie z profilem  ZROBIONE 02.04
Schludniejsze security użycie lambd  ZROOBIONE 02.04
email przy rejestracji wymaga odpowiednich znaków  ZROBIONE 02.04

04.04
Dopracowanie strony z treningami, zwraca wszystkie treningi danego użytkownika, który jest zalogowany    ZROBIONE 04.04
Połączyłem hibernatem obie encje    ZROBIONE 04.04
Zrobiłem Service, repozytorium i zaimplementowałem to odpowiednio kontrolerze  ZROBIONE 04.04

05.04
Dodanmie na stronie z profilem użytkownika ilośc jego dodanych notatek za pomocą zapytania z bazy ZROBIONE 05.04

Aplikacja jest w stanie pobrać notatki od użytkownika do bazy oraz przypisać do zalogowanego użytkownika i użytkownik może przejrzeć pod odpowiednim url
prawie skończona funkcjonalność związana z zamysłem aplikacji "po co ona ma być".

Muszę dopracować kod, aby w kontrolerze nie zawierać tyle logiki dla serwisów i poprawić wstrzykiwane zależności
Chciciałbym też popracować, dodać coś ciekawszego z security owe remember me i temu podobne.

06.06.2023r
Poprawiłem error z logowaniem który powstał wczoraj ZROBIONE 06.04
Sortowanie treningu użytkownika według daty końcowej treningów ZROBIONE 06.04
Schowanie linków url przed potencjalnym wykradnięciem po zbadaniu strony   ZROBIONE 06.04
Zabezpieczenie plików css aby nie  było do nich wglądu po zalogowaniu, poprawienie pliku security ZROBIONE 06.04

12.04.2023r
Duży update
Użytkownicy  z rolą ADMIN mają dodatkową zakładkę, która zawiera użytkowników nowo zarejestrowanych z rolą ustawioną na NULL
Użytkownicy są poprawnie wypisywani, dostęp i widoczność zakładki zrealizowana poprawnie dla ADMINA
Natomiast po kliknięcia aktywuj nie zmienia się rola w bazie danych na USER wymaga to poprawienia natomiast wydaje się łatwą czynnością


13.04.2023r
W pełni sprawna aktywacja kont jako ADMIN
Dodanie możliwości wybrania czy aktywowane konto będzie ADMIN czy USER

////////////////
TESTY
DOCKER
HEROKU

