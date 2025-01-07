# Профилирование приложения "Трекер заявок"
## Общие сведения
- **Цели:**
  - Отслеживание состояния памяти при создании/удалении заметок.
  - Используя настройки JVM по ограничению размера памяти, выйти за пределы доступной памяти и получить `OutOfMemoryError`.
- **Параметры JVM:** `-XX:+UseParallelGC -Xms128m -Xmx256m -Xlog:gc:data/gc.log  -Xlog:gc*`
- **Инструменты:** `jps`, `jmap`, `jstat`,`jconsole`

---

## Описание действий и состояний

### 1. Запуск приложения
- Запустил приложение с указанными параметрами JVM.
- Используя утилиту `jps` получен `pid`:`18804`.
- Запустил `jsonsole`, подключился к приложению.
  - Потоков используется: 14 - 16.
  - Всего загруженных классов: 2318.
  - Памяти использовано: 3М - 34М (рост происходит из-за факта наблюдения).
- За время наблюдения была произведена сборка `PS Scavenge` в молодом поколении:
  - 32M -> 4M.
---
- Вывод первых строк `jmap -histo 18804`:
```chatinput
 num     #instances         #bytes  class name (module)
-------------------------------------------------------
   1:        107363       10731832  [B (java.base@17.0.9)
   2:         21949        4992024  [I (java.base@17.0.9)
   3:         14661        4276320  [C (java.base@17.0.9)
   4:         91848        4023536  [Ljava.lang.Object; (java.base@17.0.9)
   5:         11112        1333440  java.io.ObjectStreamClass (java.base@17.0.9)
   6:         51349        1232376  java.lang.String (java.base@17.0.9)
```
---
- Сводная информация `jstat -gc 18804 1s 1`:
```chatinput
    S0C         S1C         S0U         S1U          EC           EU           OC           OU          MC         MU       CCSC      CCSU     YGC     YGCT     FGC    FGCT     CGC    CGCT       GCT   
     5120,0      5120,0         0,0         0,0      33280,0      20669,2      87552,0          0,0        0,0        0,0       0,0       0,0      0     0,000     0     0,000     -         -     0,000
```
---

### 2. Массовое создание заявок
- Создано 1_000_000 заявок.
- Использовано памяти в пике: 200M.
- Текущих классов: 2347.

---
- Так выглядит файл лога:
```chatinput
[370.362s][info][gc] GC(0) Pause Young (Allocation Failure) 32M->4M(123M) 3.633ms
[418.005s][info][gc] GC(1) Pause Young (Allocation Failure) 36M->19M(123M) 10.446ms
[418.029s][info][gc] GC(2) Pause Young (Allocation Failure) 51M->38M(123M) 13.186ms
[418.052s][info][gc] GC(3) Pause Young (Allocation Failure) 71M->62M(155M) 15.727ms
[418.114s][info][gc] GC(4) Pause Full (Ergonomics) 62M->61M(198M) 61.857ms
[418.152s][info][gc] GC(5) Pause Young (Allocation Failure) 126M->109M(162M) 19.376ms
[418.219s][info][gc] GC(6) Pause Full (Ergonomics) 109M->108M(228M) 66.949ms
[418.236s][info][gc] GC(7) Pause Young (Allocation Failure) 137M->130M(228M) 10.231ms
[418.254s][info][gc] GC(8) Pause Young (Allocation Failure) 159M->152M(228M) 12.724ms
[474.384s][info][gc] GC(9) Pause Young (Allocation Failure) 181M->171M(228M) 13.445ms
[474.462s][info][gc] GC(10) Pause Full (Ergonomics) 171M->168M(228M) 78.414ms
```
- 3 полные сборки `PS MarkSweep`.
- 8 сборок в молодом поколении `PS Scavenge`.
---
- Вывод первых строк `jmap -histo 18804`:
```chatinput
 num     #instances         #bytes  class name (module)
-------------------------------------------------------
   1:       1072053       55142640  [B (java.base@17.0.9)
   2:       1037123       24890952  java.lang.String (java.base@17.0.9)
   3:       1000132       24003168  java.time.LocalDateTime (java.base@17.0.9)
   4:       1000072       24001728  java.time.LocalDate (java.base@17.0.9)
   5:       1000027       24000648  java.time.LocalTime (java.base@17.0.9)
   6:       1000000       24000000  ru.job4j.tracker.Item
   7:         58100        7422184  [Ljava.lang.Object; (java.base@17.0.9)
   8:          9063        2648384  [C (java.base@17.0.9)
   9:         13660        2258272  [I (java.base@17.0.9)
```
- Видно обширное использование объектов, входящих в состав модели `Item`: `String`, `LocalDateTime`.
---
- Сводная информация `jstat -gc 18804 1s 1`:
```chatinput
    S0C         S1C         S0U         S1U          EC           EU           OC           OU          MC         MU       CCSC      CCSU     YGC     YGCT     FGC    FGCT     CGC    CGCT       GCT   
    28672,0     28672,0         0,0         0,0      29696,0      23295,5     175104,0     172569,1     7552,0     7119,0     960,0     761,9      8     0,099     3     0,207     -         -     0,306
```
---
### 3. Массовое удаление заявок
- Удалены все заявки.
- Через некоторое время случилась очередная сборка мусора:
```chatinput
[1234.448s][info][gc] GC(12) Pause Full (Ergonomics) 197M->8M(170M) 14.936ms
```
- Память освободилась до 8М.
---
### 4. Получаем `OutOfMemoryError`
- Массово создаю 2_000_000 заявок:
```chatinput
Exception in thread "main" java.lang.OutOfMemoryError: GC overhead limit exceeded
	at java.base/jdk.internal.misc.Unsafe.allocateUninitializedArray(Unsafe.java:1375)
	at java.base/java.lang.StringConcatHelper.newArray(StringConcatHelper.java:494)
	at java.base/java.lang.invoke.DirectMethodHandle$Holder.invokeStatic(DirectMethodHandle$Holder)
	at java.base/java.lang.invoke.LambdaForm$MH/0x000001d9b800c400.invoke(LambdaForm$MH)
	at java.base/java.lang.invoke.Invokers$Holder.linkToTargetMethod(Invokers$Holder)
	at ru.job4j.tracker.action.CreateManyItems.execute(CreateManyItems.java:25)
	at ru.job4j.tracker.StartUI.init(StartUI.java:30)
	at ru.job4j.tracker.StartUI.main(StartUI.java:55)
```